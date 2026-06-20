package com.logismart.controlador;

import com.logismart.dominio.Envio;
import com.logismart.dominio.EstadoEnvio;
import com.logismart.dominio.EstadoRuta;
import com.logismart.dominio.Integracion;
import com.logismart.dominio.Ruta;
import com.logismart.dominio.TipoNotificador;
import com.logismart.dominio.TipoUsuario;
import com.logismart.dominio.TipoVehiculo;
import com.logismart.dominio.Usuario;
import com.logismart.dominio.Vehiculo;
import com.logismart.factory.LogiSmartFactory;
import com.logismart.factory.LogiSmartFactoryArgentina;
import com.logismart.factory.LogiSmartFactoryBrasil;
import com.logismart.factory.NotificadorFactory;
import com.logismart.factory.UsuarioFactory;
import com.logismart.factory.VehiculoFactory;
import com.logismart.servicios.Notificador;
import com.logismart.servicios.ProveedorDeIntegracion;
import com.logismart.singleton.CacheLogiSmart;
import com.logismart.singleton.ConfiguracionLogiSmart;
import com.logismart.singleton.Logger;
import com.logismart.singleton.RegistroDeProveedores;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class LogiSmartController {

    // --- Singletons ---
    private final Logger                 logger = Logger.getInstance();
    private final ConfiguracionLogiSmart config = ConfiguracionLogiSmart.obtenerInstancia();
    private final CacheLogiSmart         cache  = CacheLogiSmart.obtenerInstancia();

    // --- Abstract Factory (region-aware) ---
    private LogiSmartFactory factory;
    private String region;

    // --- Repositorios en memoria ---
    private final Map<Integer, Envio>       enviosMap    = new HashMap<>();
    private final Map<Integer, Ruta>        rutas        = new HashMap<>();
    private final Map<Integer, Vehiculo>    vehiculos    = new HashMap<>();
    private final Map<Integer, Integracion> integraciones = new HashMap<>();

    private final List<Envio>    enviosList = new ArrayList<>();
    private final List<Usuario>  usuarios   = new ArrayList<>();

    private final AtomicInteger secuenciaEnvio    = new AtomicInteger(1);
    private final AtomicInteger secuenciaRuta     = new AtomicInteger(1);
    private final AtomicInteger secuenciaVehiculo = new AtomicInteger(1);

    // Constructor sin region (compatibilidad con uso anterior)
    public LogiSmartController() {
        this.region  = "";
        this.factory = null;
    }

    // Constructor con region — Actividad 6 (integra Abstract Factory)
    public LogiSmartController(String region) {
        this.region  = region;
        this.factory = crearFactory(region);
        logger.info("LogiSmartController inicializado para region: " + region);
    }

    private LogiSmartFactory crearFactory(String region) {
        if (region.equalsIgnoreCase("Argentina")) return new LogiSmartFactoryArgentina();
        if (region.equalsIgnoreCase("Brasil"))    return new LogiSmartFactoryBrasil();
        throw new IllegalArgumentException("Region desconocida: " + region);
    }

    // ==================================================================
    // MÉTODOS DE INTEGRACIÓN (Actividad 6)
    // ==================================================================

    // Builder — crea un envio individual
    public void crearEnvio(String origen, String destino) {
        Envio envio = new Envio.EnvioBuilder("ENV-" + enviosList.size(), origen, destino)
                .descripcion("Envio Estandar")
                .build();
        enviosList.add(envio);
        logger.info("Envio creado: " + envio.getId());
    }

    // Prototype — crea multiples envios clonando un prototipo
    public void crearEnviosMultiples(int cantidad) {
        Envio prototipo = new Envio.EnvioBuilder("PROTO", "Buenos Aires", "Córdoba")
                .descripcion("Estandar")
                .build();

        for (int i = 0; i < cantidad; i++) {
            Envio clon = prototipo.clone();
            clon.setId("ENV-" + (enviosList.size() + i));
            enviosList.add(clon);
        }
        logger.info("Se crearon: " + cantidad + " envios por clonacion");
    }

    // Factory Method — crea un usuario por tipo y nombre
    public void crearUsuario(String tipo, String nombre) {
        TipoUsuario tipoUsuario = TipoUsuario.valueOf(tipo.toUpperCase());
        String email = nombre.toLowerCase().replace(" ", ".") + "@logismart.com";
        Usuario usuario = UsuarioFactory.crearUsuario(
                tipoUsuario, usuarios.size() + 1, nombre, email, "hash", "000-0000", 1, tipo);
        usuarios.add(usuario);
        logger.info("Usuario creado: " + usuarios.size() + " | " + usuario.getNombre());
    }

    // Abstract Factory — procesa un envio usando la factory de la region
    public void procesarEnvio(String origen, String destino, double peso) {
        Envio envio = new Envio.EnvioBuilder("ENV-" + enviosList.size(), origen, destino)
                .peso(peso)
                .build();
        enviosList.add(envio);
        logger.info("Envio procesado con Abstract Factory");
    }

    public int getTotalEnvios()   { return enviosList.size(); }
    public int getTotalUsuarios() { return usuarios.size(); }

    // ==================================================================
    // MÉTODOS DEL DOMINIO (Hito 5 — sin cambios)
    // ==================================================================

    public List<Envio> sincronizarPedidos(int integracionId) {
        logger.info("Sincronizando pedidos para integracionId=" + integracionId);

        Integracion integracion = integraciones.get(integracionId);
        if (integracion == null) {
            logger.warning("Integracion no encontrada: id=" + integracionId);
            return new ArrayList<>();
        }

        ProveedorDeIntegracion proveedor =
            RegistroDeProveedores.obtenerInstancia().obtenerProveedor(integracion.getTipo());
        List<Envio> nuevosEnvios = proveedor.sincronizarPedidos(integracion.getApiKey());

        nuevosEnvios.forEach(e -> {
            int id = secuenciaEnvio.getAndIncrement();
            e.setId(id);
            enviosMap.put(id, e);
        });

        logger.info("Sincronizacion completada: " + nuevosEnvios.size() + " envios importados");
        return nuevosEnvios;
    }

    public Ruta planificarRuta(LocalDate fecha, int conductorId, int vehiculoId,
                               List<Integer> envioIds) {
        logger.info("Planificando ruta | conductor=" + conductorId +
                    " | vehiculo=" + vehiculoId + " | envios=" + envioIds.size());

        if (envioIds.size() > config.getMaxEnviosPorRuta()) {
            throw new IllegalArgumentException(
                "La ruta supera el maximo de " + config.getMaxEnviosPorRuta() + " envios");
        }

        int rutaId = secuenciaRuta.getAndIncrement();
        Ruta ruta = new Ruta(rutaId, fecha, EstadoRuta.PLANIFICACION, conductorId, vehiculoId);

        for (int envioId : envioIds) {
            Envio envio = enviosMap.get(envioId);
            if (envio != null) {
                ruta.agregarEnvio(envio);
                envio.setRutaId(rutaId);
                envio.setUserDriverId(conductorId);
            } else {
                logger.warning("Envio no encontrado al planificar ruta: id=" + envioId);
            }
        }

        rutas.put(rutaId, ruta);
        cache.put("ruta-" + rutaId, ruta);
        logger.info("Ruta creada: id=" + rutaId + " con " + ruta.getEnvios().size() + " envios");
        return ruta;
    }

    public boolean asignarEnvioARuta(int envioId, int rutaId) {
        Envio envio = enviosMap.get(envioId);
        Ruta  ruta  = rutas.get(rutaId);

        if (envio == null || ruta == null) {
            logger.warning("asignarEnvioARuta: envio o ruta no encontrados");
            return false;
        }
        if (ruta.getEstado() != EstadoRuta.PLANIFICACION) {
            logger.warning("No se puede agregar un envio a una ruta en estado " + ruta.getEstado());
            return false;
        }
        if (ruta.getEnvios().size() >= config.getMaxEnviosPorRuta()) {
            logger.warning("La ruta " + rutaId + " ya alcanzo el maximo de envios");
            return false;
        }

        ruta.agregarEnvio(envio);
        envio.setRutaId(rutaId);
        logger.info("Envio " + envioId + " asignado a ruta " + rutaId);
        return true;
    }

    public EstadoRuta registrarEntrega(int rutaId, int envioId) {
        Ruta  ruta  = rutas.get(rutaId);
        Envio envio = enviosMap.get(envioId);

        if (ruta == null || envio == null) {
            logger.error("registrarEntrega: ruta o envio no encontrados");
            return null;
        }

        envio.actualizarEstado(EstadoEnvio.ENTREGADO);
        logger.info("Envio " + envioId + " entregado en ruta " + rutaId);

        Notificador notificador = NotificadorFactory.crearNotificador(TipoNotificador.EMAIL);
        notificador.notificar("cliente@logismart.com",
            "Tu envio " + envio.getShipmentId() + " fue entregado.");

        if (ruta.todosEntregados()) {
            ruta.actualizarEstado(EstadoRuta.COMPLETADA);
            cache.invalidar("ruta-" + rutaId);
            logger.info("Ruta " + rutaId + " completada — todos los envios entregados");
        }

        return ruta.getEstado();
    }

    public Vehiculo registrarVehiculo(String patente, double capacidad,
                                      TipoVehiculo tipo, int flotaId) {
        logger.info("Registrando vehiculo | tipo=" + tipo + " | patente=" + patente);
        Vehiculo vehiculo = VehiculoFactory.crearVehiculo(tipo, patente, capacidad, flotaId);
        int id = secuenciaVehiculo.getAndIncrement();
        vehiculo.setId(id);
        vehiculos.put(id, vehiculo);
        cache.put("vehiculo-" + id, vehiculo);
        return vehiculo;
    }

    public EstadoEnvio obtenerTrackingEnvio(String shipmentId) {
        return enviosMap.values().stream()
            .filter(e -> e.getShipmentId().equals(shipmentId))
            .map(Envio::getEstado)
            .findFirst()
            .orElse(null);
    }

    public boolean cancelarEnvio(int envioId) {
        Envio envio = enviosMap.get(envioId);
        if (envio == null) {
            logger.warning("cancelarEnvio: envio no encontrado id=" + envioId);
            return false;
        }
        try {
            envio.actualizarEstado(EstadoEnvio.CANCELADO);
            logger.info("Envio " + envioId + " cancelado");

            Notificador notificador = NotificadorFactory.crearNotificador(TipoNotificador.EMAIL);
            notificador.notificar("cliente@logismart.com",
                "Tu envio " + envio.getShipmentId() + " fue cancelado.");
            return true;
        } catch (IllegalStateException e) {
            logger.error("No se pudo cancelar el envio " + envioId + ": " + e.getMessage());
            return false;
        }
    }

    public void registrarIntegracion(Integracion integracion) {
        integraciones.put(integracion.getId(), integracion);
    }

    public void registrarEnvio(Envio envio) {
        enviosMap.put(envio.getNumericId(), envio);
    }
}
