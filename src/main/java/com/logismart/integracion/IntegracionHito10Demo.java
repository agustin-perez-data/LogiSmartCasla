package com.logismart.integracion;

import com.logismart.dominio.Envio;

public class IntegracionHito10Demo {

    public static void main(String[] args) {
        SistemaLogisticaCompleto sistema = new SistemaLogisticaCompleto();

        // ── Caso 1: Envío válido a Córdoba, peso liviano, costo alto ────
        // Debe pasar validación, registrarse y cumplir REGLA_1 y REGLA_2
        Envio envio1 = new Envio.EnvioBuilder("E001", "Buenos Aires", "Córdoba")
                .peso(5.0).costo(150.0).metodoPago("TARJETA").productoId("PROD-001")
                .build();
        sistema.procesarEnvio(envio1);

        // ── Caso 2: Envío válido a Mendoza, peso pesado, costo bajo ─────
        // Pasa validación; cumple solo REGLA_NO_RESTRINGIDO
        Envio envio2 = new Envio.EnvioBuilder("E002", "Rosario", "Mendoza")
                .peso(50.0).costo(80.0).metodoPago("EFECTIVO").productoId("PROD-002")
                .build();
        sistema.procesarEnvio(envio2);

        // ── Caso 3: Envío a destino restringido ─────────────────────────
        // Falla en ValidadorSeguridad; no llega a Command ni Interpreter
        Envio envio3 = new Envio.EnvioBuilder("E003", "Buenos Aires", "Zona Restringida")
                .peso(3.0).costo(200.0).metodoPago("TARJETA").productoId("PROD-003")
                .build();
        sistema.procesarEnvio(envio3);

        // ── Historial y undo ─────────────────────────────────────────────
        System.out.println("=== Estado del historial ===");
        sistema.mostrarHistorial();

        System.out.println("=== Deshacer última operación ===");
        sistema.deshacerUltimaOperacion();
        sistema.mostrarHistorial();
    }
}
