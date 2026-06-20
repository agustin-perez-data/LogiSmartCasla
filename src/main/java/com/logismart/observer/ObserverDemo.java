package com.logismart.observer;

import com.logismart.dominio.Envio;

public class ObserverDemo {

    public static void main(String[] args) {
        CentroDistribucionObservador centro = new CentroDistribucionObservador();
        SistemaNotificacionObservador notificador = new SistemaNotificacionObservador();
        SistemaAuditoriaObservador auditoria = new SistemaAuditoriaObservador();
        DashboardObservador dashboard = new DashboardObservador();

        System.out.println("=== Caso 1: registrar observadores ===");
        Envio envio1 = crearEnvio("E001", "Buenos Aires", "Cordoba", 5.0, 150.0);
        envio1.adjuntarObservador(centro);
        envio1.adjuntarObservador(notificador);
        envio1.adjuntarObservador(auditoria);
        envio1.adjuntarObservador(dashboard);
        envio1.cambiarEstado("EN_PREPARACION");

        System.out.println("\n=== Caso 2: ciclo completo ===");
        envio1.cambiarEstado("EN_TRANSITO");
        envio1.cambiarEstado("ENTREGADO");
        dashboard.mostrarDashboard();

        System.out.println("\n=== Caso 3: desregistrar observador ===");
        envio1.desadjuntarObservador(notificador);
        Envio envio2 = crearEnvio("E002", "Rosario", "Mendoza", 8.0, 200.0);
        envio2.adjuntarObservador(centro);
        envio2.adjuntarObservador(auditoria);
        envio2.adjuntarObservador(dashboard);
        envio2.cambiarEstado("EN_TRANSITO");

        System.out.println("\n=== Caso 4: multiples envios ===");
        Envio envio3 = crearEnvio("E003", "Cordoba", "Salta", 3.0, 90.0);
        envio3.adjuntarObservador(auditoria);
        envio3.adjuntarObservador(dashboard);
        envio3.cambiarEstado("EN_TRANSITO");
        envio3.cambiarEstado("DEVUELTO");
        dashboard.mostrarDashboard();

        System.out.println("\n=== Caso 5: auditoria total ===");
        auditoria.mostrarRegistros();
        System.out.println("Total de eventos auditados: " + auditoria.obtenerCantidadRegistros());
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
