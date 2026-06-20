package com.logismart.command;

public class ComandoCancelarEnvio implements Comando {

    private final ServicioEnvios servicio;
    private final String numeroSeguimiento;

    public ComandoCancelarEnvio(ServicioEnvios servicio, String numeroSeguimiento) {
        this.servicio = servicio;
        this.numeroSeguimiento = numeroSeguimiento;
    }

    @Override
    public void ejecutar() {
        System.out.println("[Comando] Cancelando envío: " + numeroSeguimiento);
        servicio.cancelarEnvio(numeroSeguimiento);
        System.out.println("✓ Envío cancelado");
    }

    @Override
    public void deshacer() {
        System.out.println("[Comando] Deshaciendo: Reactivando envío...");
        servicio.reactivarEnvio(numeroSeguimiento);
        System.out.println("✓ Envío reactivado");
    }

    @Override
    public String obtenerDescripcion() {
        return "Cancelar envío: " + numeroSeguimiento;
    }
}
