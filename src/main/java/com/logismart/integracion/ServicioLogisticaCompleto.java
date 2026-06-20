package com.logismart.integracion;

import com.logismart.decorator.*;
import com.logismart.facade.ServicioLogisticaFacade;
import com.logismart.flyweight.FabricaUbicaciones;
import com.logismart.flyweight.Ubicacion;
import com.logismart.proxy.ProxyRepositorioEnvios;
import com.logismart.proxy.EnvioSimple;

public class ServicioLogisticaCompleto {
    private ServicioLogisticaFacade facade;
    private ProxyRepositorioEnvios repositorio;

    public ServicioLogisticaCompleto() {
        this.facade = new ServicioLogisticaFacade();
        this.repositorio = new ProxyRepositorioEnvios();
    }

    public String crearEnvioConServicios(
            String productoId, double monto, String email,
            String ciudadOrigen, String provinciaOrigen, String cpOrigen,
            String ciudadDestino, String provinciaDestino, String cpDestino,
            double peso,
            boolean conSeguro, boolean conRastreo,
            boolean conNotificaciones, boolean prioritario
    ) {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║   CREANDO ENVÍO CON 4 PATRONES      ║");
        System.out.println("╚══════════════════════════════════════╝");

        // PASO 1 — FLYWEIGHT
        System.out.println("\n--- Paso 1: Flyweight (ubicaciones compartidas) ---");
        Ubicacion origen = FabricaUbicaciones.obtener(ciudadOrigen, provinciaOrigen, cpOrigen);
        Ubicacion destino = FabricaUbicaciones.obtener(ciudadDestino, provinciaDestino, cpDestino);
        System.out.println("  Origen:  " + origen);
        System.out.println("  Destino: " + destino);

        // PASO 2 — DECORATOR
        System.out.println("\n--- Paso 2: Decorator (servicios opcionales) ---");
        Envio envio = new EnvioBasico(origen.toString(), destino.toString(), peso);
        System.out.println("  Base: " + envio.obtenerServicios() + " | $" + envio.obtenerCosto());

        if (conSeguro) {
            envio = new DecoradorSeguro(envio);
            System.out.println("  + Seguro: $" + envio.obtenerCosto());
        }
        if (conRastreo) {
            envio = new DecoradorRastreoGPS(envio);
            System.out.println("  + Rastreo GPS: $" + envio.obtenerCosto());
        }
        if (conNotificaciones) {
            envio = new DecoradorNotificacionesSMS(envio);
            System.out.println("  + Notificaciones SMS: $" + envio.obtenerCosto());
        }
        if (prioritario) {
            envio = new DecoradorPrioritario(envio);
            System.out.println("  + Prioritario: $" + envio.obtenerCosto());
        }

        System.out.println("  Resultado: " + envio.obtenerServicios());
        System.out.println("  Costo final: $" + envio.obtenerCosto());
        System.out.println("  Tiempo entrega: " + envio.obtenerTiempoEntrega() + " días");

        // PASO 3 — FACADE
        System.out.println("\n--- Paso 3: Facade (coordinar subsistemas) ---");
        String numeroSeguimiento = facade.crearEnvio(
            productoId, envio.obtenerCosto(), email, "+54911234567"
        );

        // PASO 4 — PROXY
        if (numeroSeguimiento != null) {
            System.out.println("\n--- Paso 4: Proxy (guardar con lazy loading + caché) ---");
            EnvioSimple envioParaBD = new EnvioSimple(
                numeroSeguimiento, origen.toString(), destino.toString(), peso, "CONFIRMADO"
            );
            repositorio.guardarEnvio(envioParaBD);
            System.out.println("  Envío guardado en BD via Proxy");
        }

        return numeroSeguimiento;
    }

    public EnvioSimple consultarEnvio(String id) {
        System.out.println("\n--- Consultando envío (Proxy con caché) ---");
        return repositorio.obtenerEnvio(id);
    }

    public void cancelarEnvio(String numeroSeguimiento, String email) {
        facade.cancelarEnvio(numeroSeguimiento, email);
        repositorio.eliminarEnvio(numeroSeguimiento);
    }

    public void mostrarEstadisticas() {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║       ESTADÍSTICAS DE PATRONES       ║");
        System.out.println("╚══════════════════════════════════════╝");
        FabricaUbicaciones.mostrarEstadisticas();
        repositorio.mostrarEstadisticas();
    }

    public ProxyRepositorioEnvios getRepositorio() {
        return repositorio;
    }
}
