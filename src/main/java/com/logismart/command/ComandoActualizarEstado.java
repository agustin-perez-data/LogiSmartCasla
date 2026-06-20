package com.logismart.command;

public class ComandoActualizarEstado implements Comando {

    private final ServicioEnvios servicio;
    private final String numeroSeguimiento;
    private final String nuevoEstado;
    private String estadoAnterior;

    public ComandoActualizarEstado(ServicioEnvios servicio, String numeroSeguimiento, String nuevoEstado) {
        this.servicio = servicio;
        this.numeroSeguimiento = numeroSeguimiento;
        this.nuevoEstado = nuevoEstado;
    }

    @Override
    public void ejecutar() {
        System.out.println("[Comando] Actualizando estado a: " + nuevoEstado);
        estadoAnterior = servicio.obtenerEstado(numeroSeguimiento);
        servicio.actualizarEstado(numeroSeguimiento, nuevoEstado);
        System.out.println("✓ Estado actualizado");
    }

    @Override
    public void deshacer() {
        System.out.println("[Comando] Deshaciendo: Restaurando estado anterior...");
        servicio.actualizarEstado(numeroSeguimiento, estadoAnterior);
        System.out.println("✓ Estado restaurado: " + estadoAnterior);
    }

    @Override
    public String obtenerDescripcion() {
        return "Actualizar estado a: " + nuevoEstado;
    }
}
