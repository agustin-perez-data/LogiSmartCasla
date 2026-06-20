package com.logismart.eventdriven;

import com.logismart.dominio.Envio;

public class IntegracionHito11Demo {

    public static void main(String[] args) {
        SistemaLogisticaEventDriven sistema = new SistemaLogisticaEventDriven();

        System.out.println("=== Caso 1: procesar cola valida ===");
        Envio envio1 = crearEnvio("E001", "Buenos Aires", "Cordoba", 5.0, 150.0);
        Envio envio2 = crearEnvio("E002", "Rosario", "Mendoza", 8.0, 200.0);
        sistema.encolar(envio1);
        sistema.encolar(envio2);
        sistema.procesarCola();

        System.out.println("\n=== Caso 2: dashboard y auditoria ===");
        sistema.mostrarDashboard();
        sistema.mostrarAuditoria();

        System.out.println("\n=== Caso 3: envio invalido ===");
        SistemaLogisticaEventDriven sistema2 = new SistemaLogisticaEventDriven();
        Envio envio3 = crearEnvio("E003", "La Plata", "Salta", 0.0, 0.0);
        sistema2.encolar(envio3);
        sistema2.procesarCola();
        sistema2.mostrarDashboard();

        System.out.println("\n=== Caso 4: error y restauracion con Memento ===");
        sistema.simularErrorYRestaurar(envio1);
        sistema.mostrarHistorial();

        System.out.println("\n=== Caso 5: patrones desacoplados ===");
        System.out.println("Iterator recorre la cola sin exponer la coleccion.");
        System.out.println("Mediator coordina validacion, pago y notificacion.");
        System.out.println("Memento restaura estados sin exponer internals de Envio.");
        System.out.println("Observer propaga cambios a los interesados.");
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
