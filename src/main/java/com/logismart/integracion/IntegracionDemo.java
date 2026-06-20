package com.logismart.integracion;

import com.logismart.flyweight.FabricaUbicaciones;
import com.logismart.proxy.EnvioSimple;

public class IntegracionDemo {
    public static void main(String[] args) {

        ServicioLogisticaCompleto servicio = new ServicioLogisticaCompleto();

        // ENVÍO 1: Buenos Aires → Córdoba, con seguro + rastreo
        System.out.println("████████████████████████████████████████");
        System.out.println("█         ENVÍO 1                      █");
        System.out.println("████████████████████████████████████████");
        String numero1 = servicio.crearEnvioConServicios(
            "PROD-001", 150.0, "juan@email.com",
            "Buenos Aires", "Buenos Aires", "1425",
            "Córdoba", "Córdoba", "5000",
            5.0, true, true, false, false
        );

        // ENVÍO 2: Buenos Aires → Córdoba (MISMAS ubicaciones), con todo
        System.out.println("\n████████████████████████████████████████");
        System.out.println("█         ENVÍO 2                      █");
        System.out.println("████████████████████████████████████████");
        String numero2 = servicio.crearEnvioConServicios(
            "PROD-002", 200.0, "maria@email.com",
            "Buenos Aires", "Buenos Aires", "1425",
            "Córdoba", "Córdoba", "5000",
            8.0, true, true, true, true
        );

        // ENVÍO 3: Buenos Aires → Rosario (origen reutilizado, destino nuevo)
        System.out.println("\n████████████████████████████████████████");
        System.out.println("█         ENVÍO 3                      █");
        System.out.println("████████████████████████████████████████");
        String numero3 = servicio.crearEnvioConServicios(
            "PROD-003", 100.0, "pedro@email.com",
            "Buenos Aires", "Buenos Aires", "1425",
            "Rosario", "Santa Fe", "2000",
            3.0, false, false, false, true
        );

        // CONSULTAS con Proxy caché
        System.out.println("\n████████████████████████████████████████");
        System.out.println("█    CONSULTAS (Proxy con caché)        █");
        System.out.println("████████████████████████████████████████");

        if (numero1 != null) {
            EnvioSimple env1 = servicio.consultarEnvio(numero1);
            System.out.println("Resultado: " + env1);
            EnvioSimple env1bis = servicio.consultarEnvio(numero1);
            System.out.println("Resultado (segunda vez): " + env1bis);
        }

        EnvioSimple envBD = servicio.consultarEnvio("ENV-003");
        System.out.println("Resultado desde BD: " + envBD);
        EnvioSimple envBD2 = servicio.consultarEnvio("ENV-003");
        System.out.println("Resultado desde caché: " + envBD2);

        // CANCELACIÓN
        System.out.println("\n████████████████████████████████████████");
        System.out.println("█    CANCELACIÓN (Facade + Proxy)       █");
        System.out.println("████████████████████████████████████████");
        if (numero3 != null) {
            servicio.cancelarEnvio(numero3, "pedro@email.com");
        }

        // ESTADÍSTICAS
        servicio.mostrarEstadisticas();

        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║          RESUMEN DE PATRONES         ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║ Decorator:  7 clases, ∞ combinaciones║");
        System.out.println("║ Facade:     1 llamada = 5 subsistemas║");
        System.out.println("║ Flyweight:  " + FabricaUbicaciones.obtenerCantidadUnicas() + " ubicaciones en memoria   ║");
        System.out.println("║ Proxy:      Lazy loading + caché     ║");
        System.out.println("╚══════════════════════════════════════╝");
    }
}
