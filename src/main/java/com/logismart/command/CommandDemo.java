package com.logismart.command;

import com.logismart.dominio.Envio;

public class CommandDemo {

    public static void main(String[] args) {
        ServicioEnvios servicio = new ServicioEnvios();
        ColaComandos cola = new ColaComandos();

        Envio envio = new Envio.EnvioBuilder("E001", "Buenos Aires", "Córdoba")
                .peso(5.0).costo(150.0).metodoPago("TARJETA").productoId("PROD-001")
                .build();

        // ── Caso 1: Crear envío ─────────────────────────────────────────
        System.out.println("--- Caso 1: Crear envío ---");
        ComandoCrearEnvio cmdCrear = new ComandoCrearEnvio(servicio, envio);
        cola.ejecutar(cmdCrear);

        String numero = cmdCrear.getNumeroSeguimiento();

        // ── Caso 2: Actualizar estado ───────────────────────────────────
        System.out.println("\n--- Caso 2: Actualizar estado ---");
        cola.ejecutar(new ComandoActualizarEstado(servicio, numero, "EN TRÁNSITO"));

        // ── Caso 3: Cambiar método de pago ─────────────────────────────
        System.out.println("\n--- Caso 3: Cambiar método de pago ---");
        cola.ejecutar(new ComandoCambiarMetodoPago(servicio, numero, "EFECTIVO"));

        // ── Caso 4: Agregar servicio adicional ─────────────────────────
        System.out.println("\n--- Caso 4: Agregar servicio 'Seguro' ---");
        cola.ejecutar(new ComandoAgregarServicio(servicio, numero, "Seguro"));

        // ── Caso 5: Mostrar historial ───────────────────────────────────
        System.out.println("--- Caso 5: Mostrar historial ---");
        cola.mostrarHistorial();

        // ── Caso 6: Deshacer (quita Seguro, luego vuelve a TARJETA) ────
        System.out.println("--- Caso 6: Deshacer x2 ---");
        cola.deshacer(); // quita Seguro
        cola.deshacer(); // restaura TARJETA
        cola.mostrarHistorial();

        // ── Caso 7: Rehacer (vuelve a EFECTIVO) ────────────────────────
        System.out.println("--- Caso 7: Rehacer ---");
        cola.rehacer(); // vuelve a EFECTIVO
        cola.mostrarHistorial();
    }
}
