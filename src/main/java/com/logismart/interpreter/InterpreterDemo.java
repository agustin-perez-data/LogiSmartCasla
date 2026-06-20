package com.logismart.interpreter;

import com.logismart.dominio.Envio;

public class InterpreterDemo {

    public static void main(String[] args) {
        Envio envio1 = new Envio.EnvioBuilder("E001", "Buenos Aires", "Córdoba")
                .peso(5.0).costo(150.0).metodoPago("TARJETA").productoId("PROD-001")
                .build();

        Envio envio2 = new Envio.EnvioBuilder("E002", "Rosario", "Mendoza")
                .peso(15.0).costo(80.0).metodoPago("EFECTIVO").productoId("PROD-002")
                .build();

        Envio envio3 = new Envio.EnvioBuilder("E003", "Buenos Aires", "Zona Restringida")
                .peso(3.0).costo(200.0).metodoPago("TARJETA").productoId("PROD-003")
                .build();

        // ── Caso 1: Regla simple — ORIGEN = "Buenos Aires" ─────────────
        Expresion regla1 = new ExpresionOrigen("Buenos Aires");
        System.out.println("--- Caso 1: ORIGEN = 'Buenos Aires' ---");
        System.out.println("  Envío 1 (BA→Córdoba):          " + regla1.evaluar(envio1)); // true
        System.out.println("  Envío 2 (Rosario→Mendoza):     " + regla1.evaluar(envio2)); // false

        // ── Caso 2: AND — ORIGEN = "Buenos Aires" AND PESO < 10 ────────
        Expresion regla2 = new ExpresionAND(
                new ExpresionOrigen("Buenos Aires"),
                new ExpresionPeso(10, "<")
        );
        System.out.println("\n--- Caso 2: ORIGEN='Buenos Aires' AND PESO < 10 ---");
        System.out.println("  Envío 1 (peso 5):   " + regla2.evaluar(envio1)); // true
        System.out.println("  Envío 2 (peso 15):  " + regla2.evaluar(envio2)); // false

        // ── Caso 3: OR — DESTINO = "Córdoba" OR DESTINO = "Mendoza" ───
        Expresion regla3 = new ExpresionOR(
                new ExpresionDestino("Córdoba"),
                new ExpresionDestino("Mendoza")
        );
        System.out.println("\n--- Caso 3: DESTINO='Córdoba' OR DESTINO='Mendoza' ---");
        System.out.println("  Envío 1 (Córdoba):           " + regla3.evaluar(envio1)); // true
        System.out.println("  Envío 2 (Mendoza):           " + regla3.evaluar(envio2)); // true
        System.out.println("  Envío 3 (Zona Restringida):  " + regla3.evaluar(envio3)); // false

        // ── Caso 4: NOT — NOT RESTRINGIDO ──────────────────────────────
        Expresion regla4 = new ExpresionNOT(new ExpresionRestringido());
        System.out.println("\n--- Caso 4: NOT RESTRINGIDO ---");
        System.out.println("  Envío 1 (Córdoba):           " + regla4.evaluar(envio1)); // true
        System.out.println("  Envío 3 (Zona Restringida):  " + regla4.evaluar(envio3)); // false

        // ── Caso 5: Compleja — (ORIGEN='BA' AND COSTO>100) AND NOT RESTRINGIDO
        Expresion regla5 = new ExpresionAND(
                new ExpresionAND(
                        new ExpresionOrigen("Buenos Aires"),
                        new ExpresionCosto(100, ">")
                ),
                new ExpresionNOT(new ExpresionRestringido())
        );
        System.out.println("\n--- Caso 5: (ORIGEN='BA' AND COSTO>100) AND NOT RESTRINGIDO ---");
        System.out.println("  Envío 1 (BA, costo 150, Córdoba):         " + regla5.evaluar(envio1)); // true
        System.out.println("  Envío 2 (Rosario, costo 80, Mendoza):     " + regla5.evaluar(envio2)); // false
        System.out.println("  Envío 3 (BA, costo 200, Restringido):     " + regla5.evaluar(envio3)); // false
    }
}
