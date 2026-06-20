package com.logismart.mediator;

import com.logismart.dominio.Envio;

public class MediadorDemo {

    public static void main(String[] args) {
        MediadorEnvios mediador = new MediadorEnviosConcreto();
        CentroDistribucion centro = new CentroDistribucion(mediador);
        ComponenteValidador validador = new ComponenteValidador(mediador);
        SistemaPago pago = new SistemaPago(mediador);
        SistemaNotificacion notificador = new SistemaNotificacion(mediador);
        SistemaAuditoria auditoria = new SistemaAuditoria();

        mediador.registrarCentro(centro);
        mediador.registrarValidador(validador);
        mediador.registrarPago(pago);
        mediador.registrarNotificador(notificador);
        mediador.registrarAuditoria(auditoria);

        System.out.println("=== Caso 1: flujo completo ===");
        centro.crearEnvio(crearEnvio("E001", "Buenos Aires", "Cordoba", 5.0, 150.0));

        System.out.println("\n=== Caso 2: segundo envio ===");
        centro.crearEnvio(crearEnvio("E002", "Rosario", "Mendoza", 8.0, 200.0));

        System.out.println("\n=== Caso 3: envio invalido ===");
        centro.crearEnvio(crearEnvio("E003", "La Plata", "Salta", 0.0, 0.0));

        System.out.println("\n=== Caso 4: desacoplamiento ===");
        System.out.println("Los componentes solo conocen a MediadorEnvios.");

        System.out.println("\n=== Caso 5: auditoria total ===");
        auditoria.mostrarLogs();
        System.out.println("Total de eventos auditados: " + auditoria.obtenerCantidadLogs());
    }

    private static Envio crearEnvio(String id, String origen, String destino, double peso, double costo) {
        return new Envio.EnvioBuilder(id, origen, destino)
                .peso(peso)
                .costo(costo)
                .metodoPago("TARJETA")
                .productoId("PROD-" + id)
                .build();
    }
}
