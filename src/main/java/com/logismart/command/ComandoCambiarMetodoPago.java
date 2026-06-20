package com.logismart.command;

public class ComandoCambiarMetodoPago implements Comando {

    private final ServicioEnvios servicio;
    private final String numeroSeguimiento;
    private final String nuevoMetodo;
    private String metodoAnterior;

    public ComandoCambiarMetodoPago(ServicioEnvios servicio, String numeroSeguimiento, String nuevoMetodo) {
        this.servicio = servicio;
        this.numeroSeguimiento = numeroSeguimiento;
        this.nuevoMetodo = nuevoMetodo;
    }

    @Override
    public void ejecutar() {
        System.out.println("[Comando] Cambiando método de pago a: " + nuevoMetodo);
        metodoAnterior = servicio.obtenerMetodoPago(numeroSeguimiento);
        servicio.cambiarMetodoPago(numeroSeguimiento, nuevoMetodo);
        System.out.println("✓ Método de pago cambiado");
    }

    @Override
    public void deshacer() {
        System.out.println("[Comando] Deshaciendo: Restaurando método anterior...");
        servicio.cambiarMetodoPago(numeroSeguimiento, metodoAnterior);
        System.out.println("✓ Método de pago restaurado: " + metodoAnterior);
    }

    @Override
    public String obtenerDescripcion() {
        return "Cambiar método de pago a: " + nuevoMetodo;
    }
}
