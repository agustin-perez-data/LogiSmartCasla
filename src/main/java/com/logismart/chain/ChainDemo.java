package com.logismart.chain;

import com.logismart.dominio.Envio;

public class ChainDemo {

    public static void main(String[] args) {
        // Implementaciones simples de las interfaces de soporte
        SistemaInventario inventario = productoId -> true; // siempre hay stock
        SistemaCapacidad capacidad   = peso -> peso <= 1000; // límite de 1000 kg

        CadenaValidadores cadena = new CadenaValidadores(inventario, capacidad);

        // ── Caso 1: Envío completamente válido ──────────────────────────
        Envio envio1 = new Envio.EnvioBuilder("E001", "Buenos Aires", "Córdoba")
                .peso(5.0).costo(150.0).metodoPago("TARJETA").productoId("PROD-001")
                .build();
        if (cadena.validarEnvio(envio1)) {
            System.out.println("→ ✓ Envío aprobado\n");
        }

        // ── Caso 2: Origen vacío → rechazado en ValidadorDatos ──────────
        Envio envio2 = new Envio.EnvioBuilder("E002", "", "Córdoba")
                .peso(5.0).costo(150.0).metodoPago("TARJETA").productoId("PROD-001")
                .build();
        if (!cadena.validarEnvio(envio2)) {
            System.out.println("→ ✗ Envío rechazado\n");
        }

        // ── Caso 3: Peso negativo → rechazado en ValidadorDatos ─────────
        Envio envio3 = new Envio.EnvioBuilder("E003", "Buenos Aires", "Córdoba")
                .peso(-5.0).costo(150.0).metodoPago("TARJETA").productoId("PROD-001")
                .build();
        if (!cadena.validarEnvio(envio3)) {
            System.out.println("→ ✗ Envío rechazado\n");
        }

        // ── Caso 4: Costo 0 → rechazado en ValidadorPago ────────────────
        Envio envio4 = new Envio.EnvioBuilder("E004", "Buenos Aires", "Córdoba")
                .peso(5.0).costo(0).metodoPago("TARJETA").productoId("PROD-001")
                .build();
        if (!cadena.validarEnvio(envio4)) {
            System.out.println("→ ✗ Envío rechazado\n");
        }

        // ── Caso 5: Destino restringido → rechazado en ValidadorSeguridad
        Envio envio5 = new Envio.EnvioBuilder("E005", "Buenos Aires", "Zona Restringida")
                .peso(5.0).costo(150.0).metodoPago("TARJETA").productoId("PROD-001")
                .build();
        if (!cadena.validarEnvio(envio5)) {
            System.out.println("→ ✗ Envío rechazado\n");
        }
    }
}
