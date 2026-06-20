# Hito 6 - Patrones Creacionales I (Singleton, Factory Method)


# Hito 6 del TPO: Patrones Creacionales I

# Singleton y Factory Method en LogiSmart

## Actividad 1: Identificar Candidatos a Singleton

Entregable: Lista de 3-5 candidatos a Singleton con justificación.

Lista de candidatos a Singleton:

1. ConfiguracionLogiSmart

Es el recurso más crítico para ser Singleton. Almacena los parámetros globales del sistema: URL de la base de datos, credenciales de APIs externas, límites operativos (máximo de envíos por ruta, peso máximo permitido), y la zona horaria de operación. Si existieran dos instancias, una parte del sistema podría operar con parámetros desactualizados después de un cambio de configuración. Además, la configuración se lee al inicio y se consulta constantemente desde el Controller, los servicios y las validaciones. Tener un único punto de acceso garantiza consistencia en todo el sistema.

2. LoggerLogiSmart

Registra todos los eventos operativos del sistema: sincronizaciones de pedidos, creación de rutas, cambios de estado de envíos, errores de integración y acciones de los usuarios. Si hubiera múltiples instancias, los logs se escribirían en archivos o destinos distintos, haciendo imposible reconstruir la secuencia de eventos ante un problema. Un Singleton garantiza que todo el sistema escribe en el mismo destino con el mismo formato y con timestamps consistentes. Además, el logger necesita manejar concurrencia (múltiples conductores registrando entregas a la vez), y tener una única instancia facilita la sincronización.

3. PoolDeConexiones

Gestiona las conexiones a la base de datos. Crear conexiones es costoso (handshake TCP, autenticación), por lo que se reutilizan a través de un pool. Si hubiera múltiples pools, cada uno abriría su propio conjunto de conexiones, duplicando el consumo de recursos y potencialmente excediendo el límite de conexiones simultáneas del servidor de base de datos. Un Singleton garantiza que todas las partes del sistema (Controller, servicios, repositorios) comparten el mismo pool y respetan el límite máximo de conexiones configurado.

4. CacheLogiSmart

Almacena en memoria datos que se consultan frecuentemente y cambian poco: la lista de vehículos disponibles, las rutas activas del día, las configuraciones de integraciones activas. Sin un Singleton, dos instancias del caché podrían tener datos desincronizados: una con un vehículo marcado como disponible y otra con el mismo vehículo ya asignado a una ruta. Esto provocaría asignaciones duplicadas. Un único caché también permite implementar una política de invalidación consistente (TTL, invalidación por evento).

5. RegistroDeProveedores

Es el mapa que asocia cada tipo de integración ("MERCADOLIBRE", "TIENDANUBE", "MANUAL") con su implementación concreta de ProveedorDeIntegracion. En el Hito 5, este mapa vive dentro de IntegracionService, pero si se crearan múltiples instancias del servicio, cada una podría tener un mapa distinto (con proveedores faltantes o versiones diferentes). Un Singleton garantiza que el registro es único, que todos los proveedores se registran en un solo lugar, y que cualquier parte del sistema que necesite resolver un proveedor obtiene la misma instancia.

## Actividad 2: Implementar Singleton

Entregable: Código Java de 2-3 Singletons implementados.

public class ConfiguracionLogiSmart {

    // Instancia única - volatile para visibilidad entre hilos

    private static volatile ConfiguracionLogiSmart instancia;

    // Parámetros de configuración

    private String urlBaseDatos;

    private int maxConexiones;

    private int maxEnviosPorRuta;

    private double pesoMaximoPermitido;

    private String zonaHoraria;

    private Map<String, String> credencialesAPIs;

    // Constructor PRIVADO: nadie puede hacer new ConfiguracionLogiSmart()

    private ConfiguracionLogiSmart() {

        cargarConfiguracion();

    }

    // Punto de acceso único - Double-Checked Locking

    public static ConfiguracionLogiSmart obtenerInstancia() {

        if (instancia == null) {                          // Primer chequeo (sin lock)

            synchronized (ConfiguracionLogiSmart.class) { // Solo un hilo entra

                if (instancia == null) {                  // Segundo chequeo (con lock)

                    instancia = new ConfiguracionLogiSmart();

                }

            }

        }

        return instancia;

    }

    // Carga la configuración desde archivo o variables de entorno

    private void cargarConfiguracion() {

        try {

            Properties props = new Properties();

            props.load(new FileInputStream("logismart.config"));

            this.urlBaseDatos = props.getProperty("db.url", "jdbc:mysql://localhost:3306/logismart");

            this.maxConexiones = Integer.parseInt(props.getProperty("db.max_conexiones", "10"));

            this.maxEnviosPorRuta = Integer.parseInt(props.getProperty("ruta.max_envios", "50"));

            this.pesoMaximoPermitido = Double.parseDouble(props.getProperty("envio.peso_maximo", "5000"));

            this.zonaHoraria = props.getProperty("sistema.zona_horaria", "America/Argentina/Buenos_Aires");

            this.credencialesAPIs = new HashMap<>();

            this.credencialesAPIs.put("MERCADOLIBRE", props.getProperty("api.mercadolibre.key", ""));

            this.credencialesAPIs.put("TIENDANUBE", props.getProperty("api.tiendanube.key", ""));

        } catch (IOException e) {

            // Si no encuentra archivo, usa valores por defecto

            this.urlBaseDatos = "jdbc:mysql://localhost:3306/logismart";

            this.maxConexiones = 10;

            this.maxEnviosPorRuta = 50;

            this.pesoMaximoPermitido = 5000;

            this.zonaHoraria = "America/Argentina/Buenos_Aires";

            this.credencialesAPIs = new HashMap<>();

        }

    }

    // Getters públicos

    public String getUrlBaseDatos() {

        return this.urlBaseDatos;

    }

    public int getMaxConexiones() {

        return this.maxConexiones;

    }

    public int getMaxEnviosPorRuta() {

        return this.maxEnviosPorRuta;

    }

    public double getPesoMaximoPermitido() {

        return this.pesoMaximoPermitido;

    }

    public String getZonaHoraria() {

        return this.zonaHoraria;

    }

    public String getCredencialAPI(String proveedor) {

        return this.credencialesAPIs.get(proveedor);

    }

    // Recargar configuración en caliente (sin crear nueva instancia)

    public void recargarConfiguracion() {

        synchronized (ConfiguracionLogiSmart.class) {

            cargarConfiguracion();

        }

    }

}

public class LoggerLogiSmart {

    // Instancia única

    private static volatile LoggerLogiSmart instancia;

    // Recursos internos

    private PrintWriter writer;

    private SimpleDateFormat formatoFecha;

    // Niveles de log

    public enum Nivel {

        INFO, WARNING, ERROR, DEBUG

    }

    // Constructor PRIVADO

    private LoggerLogiSmart() {

        try {

            this.writer = new PrintWriter(new FileWriter("logismart.log", true));

            this.formatoFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        } catch (IOException e) {

            System.err.println("Error al inicializar logger: " + e.getMessage());

        }

    }

    // Punto de acceso único - Double-Checked Locking

    public static LoggerLogiSmart obtenerInstancia() {

        if (instancia == null) {

            synchronized (LoggerLogiSmart.class) {

                if (instancia == null) {

                    instancia = new LoggerLogiSmart();

                }

            }

        }

        return instancia;

    }

    // Método principal de logging (sincronizado para concurrencia)

    public synchronized void log(Nivel nivel, String clase, String mensaje) {

        String timestamp = formatoFecha.format(new Date());

        String entrada = "[" + timestamp + "] [" + nivel + "] [" + clase + "] " + mensaje;

        writer.println(entrada);

        writer.flush();

    }

    // Métodos de conveniencia por nivel

    public void info(String clase, String mensaje) {

        log(Nivel.INFO, clase, mensaje);

    }

    public void warning(String clase, String mensaje) {

        log(Nivel.WARNING, clase, mensaje);

    }

    public void error(String clase, String mensaje) {

        log(Nivel.ERROR, clase, mensaje);

    }

    public void debug(String clase, String mensaje) {

        log(Nivel.DEBUG, clase, mensaje);

    }

    // Cerrar el writer al finalizar la aplicación

    public void cerrar() {

        if (writer != null) {

            writer.close();

        }

    }

}

## Actividad 3: Identificar Candidatos a Factory Method

Entregable: Lista de 3-5 candidatos a Factory Method con justificación.

Actividad 3: Identificar Candidatos a Factory Method

Pregunta 10: ¿Dónde hay múltiples subclases?

En el Hito 5 creamos varias jerarquías de herencia e implementaciones de interfaces: Vehiculo tiene 3 subclases (Moto, Camioneta, Camion), Notificador tiene 3 implementaciones (Email, SMS, Push), ProveedorDeIntegracion tiene 3 implementaciones (MercadoLibre, Tiendanube, Manual), EstrategiaDeCostos tiene 3 implementaciones (PorKilometro, PorPeso, TarifaFija), y EstrategiaDeOptimizacion tiene 3 implementaciones (PorCercania, PorVentanaHoraria, PorZona). Además, el dominio logístico tiene un candidato natural que aún no modelamos: tipos de envío (Express, Standard, Económico) con comportamientos distintos.

Pregunta 11: ¿El código cliente conoce todas las subclases?

Sí, y ese es el problema. Actualmente, cuando la PyME registra un vehículo, el LogiSmartController recibe tipo: String y alguien tiene que decidir si hace new Moto(...), new Camioneta(...) o new Camion(...). Esa decisión hoy requiere un condicional, y quien crea el objeto necesita importar y conocer todas las subclases concretas. Lo mismo pasa cuando IntegracionService necesita resolver qué proveedor instanciar, o cuando se configura la estrategia de costos.

Pregunta 12: ¿Hay condicionales para elegir qué crear?

Sí. En PyME.registrarNuevoVehiculo(patente, capacidad, tipo) tendría que haber un switch(tipo) para instanciar Moto, Camioneta o Camion. En la configuración del sistema, al elegir la estrategia de costos según el plan de la PyME, habría un if sobre el tipo de plan. Cada punto donde se crea un objeto de una jerarquía polimórfica sin factory tiene un condicional escondido.

Pregunta 13: ¿Sería beneficioso centralizar la creación?

Absolutamente. Centralizar la creación en factories permite que el código cliente (Controller, PyME, servicios) trabaje contra la clase abstracta o interfaz sin conocer las subclases concretas. Si mañana se agrega un nuevo tipo de vehículo (Bicicleta) o un nuevo tipo de envío (SameDay), solo se modifica el factory correspondiente, sin tocar el Controller ni ningún servicio.

Lista de candidatos a Factory Method:

1. VehiculoFactory

Actualmente PyME.registrarNuevoVehiculo(patente, capacidad, tipo) recibe tipo: String pero no hay un mecanismo centralizado para decidir qué subclase instanciar. Sin un factory, la PyME tendría un switch interno (if tipo == "MOTO" → new Moto(...)) que la acoplaría a todas las subclases concretas. Un VehiculoFactory centraliza esa decisión: recibe el tipo como String y retorna la subclase correcta de Vehiculo. Si se agrega Bicicleta como nuevo tipo, solo se modifica el factory. Además, cada tipo tiene validaciones de creación distintas (la Moto necesita pesoMaximoPaquete, el Camión necesita tieneRefrigeracion), y el factory puede encapsular esa lógica de inicialización específica.

2. EnvioFactory

Hoy todos los envíos se crean iguales, pero en un sistema logístico real hay tipos con comportamientos distintos: Express (prioridad alta, ventana horaria corta, costo mayor), Standard (entrega en 3-5 días, costo normal), y Económico (sin ventana horaria, se agrupa con otros envíos para optimizar, costo mínimo). Cada tipo tiene reglas de validación diferentes (Express exige coordenadas exactas, Económico no requiere ventana horaria), tiempos estimados distintos, y prioridades diferentes al ser asignados a una ruta. Sin un factory, la creación de envíos tendría condicionales dispersos en IntegracionService y en el Controller para inicializar cada tipo correctamente.

3. NotificadorFactory

En el Hito 5, el ServicioDeNotificaciones recibe una Lista<Notificador> ya creada, pero alguien tiene que instanciar esos notificadores. Actualmente eso se haría con condicionales sobre la configuración de la PyME (si tiene email habilitado → new NotificadorEmail(), si tiene SMS → new NotificadorSMS()). Un factory centraliza esa creación según la configuración, y además permite que cada notificador se inicialice con sus propios parámetros (NotificadorEmail necesita servidor SMTP, NotificadorSMS necesita credenciales de Twilio, NotificadorPush necesita token de Firebase).

4. EstrategiaDeCostosFactory

La PyME elige su política tarifaria (por kilómetro, por peso, tarifa fija), y esa elección se traduce en instanciar la implementación correcta de EstrategiaDeCostos. Sin un factory, el código que configura la CalculadoraDeCostos tendría un switch sobre el tipo de plan o preferencia de la PyME. Un factory recibe el tipo de estrategia y los parámetros tarifarios, y retorna la implementación correcta ya configurada. Si mañana se agrega una estrategia combinada (por km + por peso), solo se modifica el factory.

5. ProveedorDeIntegracionFactory

Cuando una PyME configura una nueva integración con un e-commerce, el sistema necesita instanciar el ProveedorDeIntegracion correcto según el tipo ("MERCADOLIBRE", "TIENDANUBE", "MANUAL"). Actualmente eso lo resuelve el mapa dentro de IntegracionService, pero ese mapa se llena manualmente. Un factory centraliza la creación de proveedores y encapsula la lógica de inicialización de cada uno (cada proveedor necesita configuración diferente: URL base de la API, formato de autenticación, headers requeridos).

## Actividad 4: Implementar Factory Method

Entregable: Código Java de 3-4 Factory Methods implementados

public enum TipoVehiculo {

    MOTO, CAMIONETA, CAMION

}

public class VehiculoFactory {

    public static Vehiculo crearVehiculo(TipoVehiculo tipo, String patente, Double capacidad) {

        switch (tipo) {

            case MOTO:

                // La moto tiene restricción de peso por paquete individual

                Moto moto = new Moto(patente, capacidad);

                moto.setPesoMaximoPaquete(20.0);

                // Registrar en logger (Singleton)

                LoggerLogiSmart.obtenerInstancia().info(

                    "VehiculoFactory",

                    "Moto creada: " + patente + " (capacidad: " + capacidad + " kg)"

                );

                return moto;

            case CAMIONETA:

                Camioneta camioneta = new Camioneta(patente, capacidad);

                LoggerLogiSmart.obtenerInstancia().info(

                    "VehiculoFactory",

                    "Camioneta creada: " + patente + " (capacidad: " + capacidad + " kg)"

                );

                return camioneta;

            case CAMION:

                Camion camion = new Camion(patente, capacidad);

                camion.setTieneRefrigeracion(false); // por defecto sin refrigeración

                LoggerLogiSmart.obtenerInstancia().info(

                    "VehiculoFactory",

                    "Camión creado: " + patente + " (capacidad: " + capacidad + " kg)"

                );

                return camion;

            default:

                throw new IllegalArgumentException("Tipo de vehículo no soportado: " + tipo);

        }

    }

    // Sobrecarga para Camión con refrigeración

    public static Vehiculo crearCamionRefrigerado(String patente, Double capacidad) {

        Camion camion = new Camion(patente, capacidad);

        camion.setTieneRefrigeracion(true);

        LoggerLogiSmart.obtenerInstancia().info(

            "VehiculoFactory",

            "Camión refrigerado creado: " + patente + " (capacidad: " + capacidad + " kg)"

        );

        return camion;

    }

}

public enum TipoEnvio {

    EXPRESS, STANDARD, ECONOMICO

}

// Subclases de Envio (nuevas en Hito 6)

public class EnvioExpress extends Envio {

    private static final int PRIORIDAD = 1;          // máxima prioridad

    private static final int DIAS_ENTREGA = 1;

    private static final double RECARGO = 1.5;        // 50% más caro

    public EnvioExpress(String shipmentId, Destinatario destinatario,

                        String direccion, Double peso) {

        super(shipmentId, destinatario, direccion, peso);

        this.setPrioridad(PRIORIDAD);

        // Express exige ventana horaria ajustada (4 horas)

        this.setVentanaHorariaFin(

            sumarHoras(this.getVentanaHorariaInicio(), 4)

        );

    }

    @Override

    public int getDiasEstimadosEntrega() {

        return DIAS_ENTREGA;

    }

    @Override

    public double getMultiplicadorCosto() {

        return RECARGO;

    }

}

public class EnvioStandard extends Envio {

    private static final int PRIORIDAD = 2;

    private static final int DIAS_ENTREGA = 3;

    private static final double RECARGO = 1.0;        // sin recargo

    public EnvioStandard(String shipmentId, Destinatario destinatario,

                         String direccion, Double peso) {

        super(shipmentId, destinatario, direccion, peso);

        this.setPrioridad(PRIORIDAD);

    }

    @Override

    public int getDiasEstimadosEntrega() {

        return DIAS_ENTREGA;

    }

    @Override

    public double getMultiplicadorCosto() {

        return RECARGO;

    }

}

public class EnvioEconomico extends Envio {

    private static final int PRIORIDAD = 3;           // menor prioridad

    private static final int DIAS_ENTREGA = 7;

    private static final double RECARGO = 0.7;        // 30% descuento

    public EnvioEconomico(String shipmentId, Destinatario destinatario,

                          String direccion, Double peso) {

        super(shipmentId, destinatario, direccion, peso);

        this.setPrioridad(PRIORIDAD);

        // Económico no requiere ventana horaria

        this.setVentanaHorariaInicio(null);

        this.setVentanaHorariaFin(null);

    }

    @Override

    public int getDiasEstimadosEntrega() {

        return DIAS_ENTREGA;

    }

    @Override

    public double getMultiplicadorCosto() {

        return RECARGO;

    }

}

// Factory

public class EnvioFactory {

    public static Envio crearEnvio(TipoEnvio tipo, String shipmentId,

                                    Destinatario destinatario, String direccion,

                                    Double peso) {

        // Validar peso contra configuración global (Singleton)

        double pesoMaximo = ConfiguracionLogiSmart.obtenerInstancia().getPesoMaximoPermitido();

        if (peso <= 0 || peso > pesoMaximo) {

            throw new IllegalArgumentException(

                "El peso debe estar entre 0 y " + pesoMaximo + " kg"

            );

        }

        Envio envio;

        switch (tipo) {

            case EXPRESS:

                envio = new EnvioExpress(shipmentId, destinatario, direccion, peso);

                break;

            case STANDARD:

                envio = new EnvioStandard(shipmentId, destinatario, direccion, peso);

                break;

            case ECONOMICO:

                envio = new EnvioEconomico(shipmentId, destinat

public enum TipoNotificador {

    EMAIL, SMS, PUSH

}

public class NotificadorFactory {

    public static Notificador crearNotificador(TipoNotificador tipo) {

        ConfiguracionLogiSmart config = ConfiguracionLogiSmart.obtenerInstancia();

        switch (tipo) {

            case EMAIL:

                // Cada notificador se inicializa con su configuración específica

                NotificadorEmail notifEmail = new NotificadorEmail();

                notifEmail.setServidorSMTP(config.getParametro("notificacion.smtp.servidor"));

                notifEmail.setPuerto(Integer.parseInt(

                    config.getParametro("notificacion.smtp.puerto")

                ));

                notifEmail.setRemitente(config.getParametro("notificacion.email.remitente"));

## Actividad 5: Integración en LogiSmartController

Entregable: Código Java del LogiSmartController actualizado con Singletons y Factories

public class LogiSmartController {

    // =============================================

    // SINGLETONS (acceso único a recursos globales)

    // =============================================

    private ConfiguracionLogiSmart config = ConfiguracionLogiSmart.obtenerInstancia();

    private LoggerLogiSmart logger = LoggerLogiSmart.obtenerInstancia();

    private CacheLogiSmart cache = CacheLogiSmart.obtenerInstancia();

    // =============================================

    // DEPENDENCIAS INYECTADAS (Hito 5)

    // =============================================

    private PyME pyme;

    private IntegracionService integracionService;

    private CalculadoraDeCostos calculadoraDeCostos;

    private OptimizadorDeRutas optimizadorDeRutas;

    private ServicioDeNotificaciones servicioDeNotificaciones;

    private ValidadorDeEnvios validadorDeEnvios;

    private AsignadorDeVehiculos asignadorDeVehiculos;

    // Constructor con inyección de dependencias

    public LogiSmartController(PyME pyme,

                                IntegracionService integracionService,

                                CalculadoraDeCostos calculadoraDeCostos,

                                OptimizadorDeRutas optimizadorDeRutas,

                                ServicioDeNotificaciones servicioDeNotificaciones,

                                ValidadorDeEnvios validadorDeEnvios,

                                AsignadorDeVehiculos asignadorDeVehiculos) {

        this.pyme = pyme;

        this.integracionService = integracionService;

        this.calculadoraDeCostos = calculadoraDeCostos;

        this.optimizadorDeRutas = optimizadorDeRutas;

        this.servicioDeNotificaciones = servicioDeNotificaciones;

        this.validadorDeEnvios = validadorDeEnvios;

        this.asignadorDeVehiculos = asignadorDeVehiculos;

        logger.info("LogiSmartController", "Controller inicializado para PyME: " + pyme.getNombre());

    }

    // ---------------------------------------------------------------

    // 1. Sincronizar pedidos externos y crear envíos (usa EnvioFactory)

    // ---------------------------------------------------------------

    public List<Envio> sincronizarPedidos(String integracionId) {

        try {

            logger.info("LogiSmartController",

                "Inicio sincronización para integración: " + integracionId);

            // Validar entrada

            if (integracionId == null) {

                throw new IllegalArgumentException("ID de integración no puede ser nulo");

            }

            // Buscar la integración en la PyME

            Integracion integracion = pyme.buscarIntegracion(integracionId);

            if (integracion == null) {

                throw new EntidadNoEncontradaException(

                    "Integración no encontrada: " + integracionId

                );

            }

            // Validar que esté activa

            if (!integracion.getEstado().equals(EstadoIntegracion.ACTIVA)) {

                throw new EstadoInvalidoException("La integración no está activa");

            }

            // Delegar al servicio (internamente usa EnvioFactory para crear envíos)

            List<Envio> enviosCreados = integracionService.sincronizarPedidos(integracion);

            // Notificar cada envío creado

            for (Envio envio : enviosCreados) {

                servicioDeNotificaciones.notificarEnvioCreado(envio);

            }

            // Invalidar caché de envíos pendientes

            cache.invalidarPorPrefijo("envios_pendientes");

            logger.info("LogiSmartController",

                "Sincronización completada: " + enviosCreados.size() + " envíos creados");

            return enviosCreados;

        } catch (Exception e) {

            logger.error("LogiSmartController",

                "Error en sincronización: " + e.getMessage());

            throw e;

        }

    }

    // ---------------------------------------------------------------

    // 2. Planificar ruta (usa VehiculoFactory + AsignadorDeVehiculos)

    // ---------------------------------------------------------------

    public Ruta planificarRuta(Date fecha, String conductorId,

                               TipoVehiculo tipoVehiculo, List<String> envioIds) {

        try {

            logger.info("LogiSmartController",

                "Planificando ruta para fecha: " + fecha

                + " con " + envioIds.size() + " envíos");

            // Validar fecha

            if (fecha.before(new Date())) {

                throw new IllegalArgumentException("La fecha no puede ser pasada");

            }

            // Validar límite de envíos por ruta (Singleton de configuración)

            int maxEnvios = config.getMaxEnviosPorRuta();

            if (envioIds.size() > maxEnvios) {

                throw new IllegalArgumentException(

                    "Máximo " + maxEnvios + " envíos por ruta"

                );

            }

            // Buscar conductor

            Conductor conductor = pyme.buscarConductor(conductorId);

            if (conductor == null) {

                throw new EntidadNoEncontradaException(

                    "Conductor no encontrado: " + conductorId

                );

            }

            // Obtener envíos pendientes

            List<Envio> envios = new ArrayList<>();

            for (String envioId : envioIds) {

                envios.add(buscarEnvioPendiente(envioId));

            }

            // ============================================

            // FACTORY METHOD: crear vehículo si se indica tipo

            // ============================================

            Vehiculo vehiculo;

            if (tipoVehiculo != null) {

                // Buscar vehículos disponibles del tipo solicitado

                vehiculo = buscarVehiculoDisponiblePorTipo(tipoVehiculo);

            } else {

                // Protected Variations: asignación automática con estrategia

                List<Vehiculo> disponibles = obtenerVehiculosDisponiblesConCache();

                vehiculo = asignadorDeVehiculos.asignar(envios, disponibles);

            }

            if (vehiculo == null) {

                throw new EstadoInvalidoException("No hay vehículo disponible");

            }

            // Validar licencia especial (Polymorphism del Hito 5)

            if (vehiculo.requiereLicenciaEspecial()) {

                logger.info("LogiSmartController",

                    "Vehículo requiere licencia especial - validando conductor");

                // Validación adicional del conductor

            }

            // Crear la ruta

            Ruta ruta = new Ruta(fecha);

            ruta.asignarConductor(conductor);

            ruta.asignarVehiculo(vehiculo);

            // Asignar envíos validando capacidad

            List<String> rechazados = new ArrayList<>();

            for (Envio envio : envios) {

                // Polymorphism: cada tipo de vehículo valida distinto

                if (vehiculo.puedeTransportar(envio.getPeso())) {

                    ruta.agregarEnvio(envio);

                } else {

                    rechazados.add(envio.getShipmentId());

                    logger.warning("LogiSmartController",

                        "Envío rechazado por capacidad: " + envio.getShipmentId());

                }

            }

            // Pure Fabrication: optimizar orden de paradas

            optimizadorDeRutas.optimizarRuta(ruta);

            // Expert: Ruta calcula su distancia

            ruta.calcularDistanciaTotal();

            // Calcular costo con estrategia (Factory del Hito 6)

            double costoTotal = calculadoraDeCostos.calcularCostoTotalRuta(ruta);

            // Invalidar caché

            cache.invalidarPorPrefijo("vehiculos_disponibles");

            cache.invalidarPorPrefijo("envios_pendientes");

            logger.info("LogiSmartController",

                "Ruta planificada: " + ruta.getEnvios().size() + " envíos, "

                + rechazados.size() + " rechazados, costo: $" + costoTotal);

            return ruta;

        } catch (Exception e) {

            logger.error("LogiSmartController",

                "Error al planificar ruta: " + e.getMessage());

            throw e;

        }

    }

    // ---------------------------------------------------------------

    // 3. Registrar entrega (usa Singleton Logger + Notificaciones)

    // ---------------------------------------------------------------

    public EstadoRuta registrarEntrega(String rutaId, String envioId) {

        try {

            logger.info("LogiSmartController",

                "Registrando entrega - Ruta: " + rutaId + ", Envío: " + envioId);

            // Buscar entidades

            Ruta ruta = buscarRuta(rutaId);

            Envio envio = ruta.buscarEnvio(envioId);

            if (envio == null) {

                throw new EntidadNoEncontradaException(

                    "Envío " + envioId + " no pertenece a la ruta " + rutaId

                );

            }

            // Validar precondición

            if (!ruta.getEstado().equals(EstadoRuta.EN_CURSO)) {

                throw new EstadoInvalidoException("La ruta no está en curso");

            }

            // Expert: Envío actualiza su propio estado

            envio.actualizarEstado(EstadoEnvio.ENTREGADO);

            // Pure Fabrication: notificar al destinatario

            servicioDeNotificaciones.notificarCambioEstado(envio);

            logger.info("LogiSmartController",

                "Envío " + envioId + " entregado exitosamente");

            // Expert: Ruta evalúa si todos fueron entregados

            if (ruta.todosEntregados()) {

                ruta.actualizarEstado(EstadoRuta.COMPLETADA);

                ruta.getVehiculoAsignado().actualizarDisponibilidad(true);

                // Notificar ruta completada

                servicioDeNotificaciones.notificarRutaCompletada(ruta);

                // Invalidar caché de vehículos (cambió disponibilidad)

                cache.invalidarPorPrefijo("vehiculos_disponibles");

                logger.info("LogiSmartController",

                    "Ruta " + rutaId + " COMPLETADA - vehículo liberado");

            }

            return ruta.getEstado();

        } catch (Exception e) {

            logger.error("LogiSmartController",

                "Error al registrar entrega: " + e.getMessage());

            throw e;

        }

    }

    // ---------------------------------------------------------------

    // 4. Registrar vehículo (usa VehiculoFactory)

    // ---------------------------------------------------------------

    public Vehiculo registrarVehiculo(String patente, Double capacidad,

                                      TipoVehiculo tipo, String flotaId) {

        try {

            logger.info("LogiSmartController",

                "Registrando vehículo: " + patente + " tipo: " + tipo);

            // Validar entrada

            if (capacidad <= 0) {

                throw new IllegalArgumentException("La capacidad debe ser positiva");

            }

            // ============================================

            // FACTORY METHOD: PyME delega creación al Factory

            // ============================================

            Vehiculo vehiculo = pyme.registrarNuevoVehiculo(patente, capacidad, tipo);

            // Si se especificó flota, asignar

            if (flotaId != null) {

                Flota flota = pyme.buscarFlota(flotaId);

                if (flota != null) {

                    flota.agregarVehiculo(vehiculo);

                    logger.info("LogiSmartController",

                        "Vehículo " + patente + " asignado a flota: " + flotaId);

                }

            }

            // Invalidar caché de vehículos

            cache.invalidarPorPrefijo("vehiculos_disponibles");

            return vehiculo;

        } catch (Exception e) {

            logger.error("LogiSmartController",

                "Error al registrar vehículo: " + e.getMessage());

            throw e;

        }

    }

    // ---------------------------------------------------------------

    // 5. Obtener tracking de envío

    // ---------------------------------------------------------------

    public EstadoEnvio obtenerTrackingEnvio(String shipmentId) {

        try {

            if (shipmentId == null) {

                throw new IllegalArgumentException("El ID de envío no puede ser nulo");

            }

            // Intentar obtener del caché primero

            String cacheKey = "tracking_" + shipmentId;

            EstadoEnvio estadoCacheado = (EstadoEnvio) cache.obtener(cacheKey);

            if (estadoCacheado != null) {

                logger.debug("LogiSmartController",

                    "Tracking obtenido de caché: " + shipmentId);

                return estadoCacheado;

            }

            // Cache miss: consultar al dominio

            Envio envio = buscarEnvio(shipmentId);

            EstadoEnvio estado = envio.getEstado();

            // Cachear el resultado (30 segundos)

            cache.guardar(cacheKey, estado, 30000);

            return estado;

        } catch (Exception e) {

            logger.error("LogiSmartController",

                "Error al obtener tracking: " + e.getMessage());

            throw e;

        }

    }

    // ---------------------------------------------------------------

    // 6. Cancelar envío

    // ---------------------------------------------------------------

    public boolean cancelarEnvio(String envioId) {

        try {

            logger.info("LogiSmartController",

                "Cancelando envío: " + envioId);

            Envio envio = buscarEnvio(envioId);

            if (!envio.getEstado().equals(EstadoEnvio.PENDIENTE)) {

                throw new EstadoInvalidoException(

                    "Solo se pueden cancelar envíos en estado PENDIENTE"

                );

            }

            envio.actualizarEstado(EstadoEnvio.CANCELADO);

            // Notificar cancelación

            servicioDeNotificaciones.notificarCancelacion(envio);

            // Invalidar cachés relacionados

            cache.invalidar("tracking_" + envioId);

            cache.invalidarPorPrefijo("envios_pendientes");

            logger.info("LogiSmartController",

                "Envío " + envioId + " cancelado exitosamente");

            return true;

        } catch (Exception e) {

            logger.error("LogiSmartController",

                "Error al cancelar envío: " + e.getMessage());

            throw e;

        }

    }

    // ---------------------------------------------------------------

    // 7. Cambiar estrategia de costos (usa EstrategiaDeCostosFactory)

    // ---------------------------------------------------------------

    public void cambiarEstrategiaCostos(TipoEstrategiaCostos tipo) {

        try {

            logger.info("LogiSmartController",

                "Cambiando estrategia de costos a: " + tipo);

            // ============================================

            // FACTORY METHOD: crear nueva estrategia

            // ============================================

            EstrategiaDeCostos nuevaEstrategia =

                EstrategiaDeCostosFactory.crearEstrategia(tipo, pyme);

            calculadoraDeCostos.cambiarEstrategia(nuevaEstrategia);

            logger.info("LogiSmartController",

                "Estrategia de costos actualizada a: " + tipo);

        } catch (Exception e) {

            logger.error("LogiSmartController",

                "Error al cambiar estrategia: " + e.getMessage());

            throw e;

        }

    }

    // ---------------------------------------------------------------

    // Métodos privados auxiliares

    // ---------------------------------------------------------------

    private Envio buscarEnvio(String envioId) {

        Envio envio = pyme.buscarEnvio(envioId);

        if (envio == null) {

            throw new EntidadNoEncontradaException("Envío no encontrado: " + envioId);

        }

        return envio;

    }

    private Envio buscarEnvioPendiente(String envioId) {

        Envio envio = buscarEnvio(envioId);

        if (!envio.getEstado().equals(EstadoEnvio.PENDIENTE)) {

            throw new EstadoInvalidoException("El envío no está en estado PENDIENTE");

        }

        return envio;

    }

    private Ruta buscarRuta(String rutaId) {

        Ruta ruta = pyme.buscarRuta(rutaId);

        if (ruta == null) {

            throw new EntidadNoEncontradaException("Ruta no encontrada: " + rutaId);

        }

        return ruta;

    }

    private List<Vehiculo> obtenerVehiculosDisponiblesConCache() {

        List<Vehiculo> disponibles = (List<Vehiculo>) cache.obtener("vehiculos_disponibles");

        if (disponibles == null) {

            disponibles = pyme.obtenerVehiculosDisponibles();

            cache.guardar("vehiculos_disponibles", disponibles, 60000);

        }

        return disponibles;

    }

    private Vehiculo buscarVehiculoDisponiblePorTipo(TipoVehiculo tipo) {

        List<Vehiculo> disponibles = obtenerVehiculosDisponiblesConCache();

        for (Vehiculo v : disponibles) {

            if (v.estaDisponible() && coincideTipo(v, tipo)) {

                return v;

            }

        }

        return null;

    }

    private boolean coincideTipo(Vehiculo vehiculo, TipoVehiculo tipo) {

        switch (tipo) {

            case MOTO:      return vehiculo instanceof Moto;

            case CAMIONETA: return vehiculo instanceof Camioneta;

            case CAMION:    return vehiculo instanceof Camion;

            default:        return false;

        }

    }

}

## Actividad 6: Documentación

Documenta las decisiones de diseño.

El patrón Singleton se aplicó en tres recursos que deben ser únicos en toda la aplicación: ConfiguracionLogiSmart (almacena parámetros globales como peso máximo, límite de envíos por ruta y credenciales de APIs), LoggerLogiSmart (registra todos los eventos operativos del sistema con niveles INFO, WARNING, ERROR y DEBUG), y CacheLogiSmart (almacena en memoria datos frecuentes como vehículos disponibles y tracking de envíos con TTL configurable). El problema que resolvió es que sin Singleton, múltiples instancias de estos recursos generarían inconsistencias graves: configuraciones contradictorias entre distintas partes del sistema, logs dispersos en archivos diferentes que imposibilitan la trazabilidad, y cachés desincronizados que provocarían asignaciones duplicadas de vehículos. Los tres Singletons usan constructor privado (impide instanciación externa), variable estática volatile (visibilidad entre hilos) y double-checked locking en obtenerInstancia() (thread-safe sin penalizar rendimiento). Se integran transversalmente en todo el sistema: el Controller los usa en cada método, los Factories los usan para validar y registrar creaciones, y los servicios Pure Fabrication los usan para logging de errores.

El patrón Factory Method se aplicó en cuatro puntos donde existían o existirían condicionales para instanciar subclases concretas: VehiculoFactory (crea Moto, Camioneta o Camion según el tipo, eliminando el switch que tendría PyME.registrarNuevoVehiculo()), EnvioFactory (crea EnvioExpress, EnvioStandard o EnvioEconomico, tres subclases nuevas del Hito 6 con prioridades, tiempos de entrega y multiplicadores de costo distintos), NotificadorFactory (crea NotificadorEmail, NotificadorSMS o NotificadorPush inicializando cada uno con sus credenciales específicas desde el Singleton de configuración), y EstrategiaDeCostosFactory (crea CostoPorKilometro, CostoPorPeso o CostoTarifaFija con las tarifas de la PyME). El problema que resolvieron es que el código cliente (Controller, PyME, IntegracionService) necesitaba conocer e importar todas las subclases concretas para instanciarlas, generando acoplamiento alto y violando el principio Open/Closed. Ahora el cliente solo conoce la clase abstracta o interfaz y el enum de tipos, y los Factories encapsulan toda la lógica de creación, validación de parámetros e inicialización específica de cada subclase.

Ambos patrones creacionales se integran entre sí y con los patrones de hitos anteriores: los Factories usan internamente los Singletons (EnvioFactory valida peso máximo contra ConfiguracionLogiSmart, NotificadorFactory lee credenciales del mismo Singleton, todos registran creaciones en LoggerLogiSmart), y el LogiSmartController del Hito 5 se actualizó para usar ambos patrones sin perder su rol de Facade Controller. El resultado es que la creación de objetos polimórficos está centralizada y desacoplada (Factory Method), los recursos globales tienen acceso controlado y consistente (Singleton), y el Controller coordina todo sin conocer implementaciones concretas, manteniendo los principios de Low Coupling, High Cohesion y Protected Variations establecidos en los hitos anteriores.

## Diagramas
