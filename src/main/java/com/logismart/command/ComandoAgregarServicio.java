package com.logismart.command;

public class ComandoAgregarServicio implements Comando {

    private final ServicioEnvios servicio;
    private final String numeroSeguimiento;
    private final String nombreServicio;

    public ComandoAgregarServicio(ServicioEnvios servicio, String numeroSeguimiento, String nombreServicio) {
        this.servicio = servicio;
        this.numeroSeguimiento = numeroSeguimiento;
        this.nombreServicio = nombreServicio;
    }

    @Override
    public void ejecutar() {
        System.out.println("[Comando] Agregando servicio: " + nombreServicio);
        servicio.agregarServicio(numeroSeguimiento, nombreServicio);
        System.out.println("✓ Servicio agregado");
    }

    @Override
    public void deshacer() {
        System.out.println("[Comando] Deshaciendo: Removiendo servicio...");
        servicio.removerServicio(numeroSeguimiento, nombreServicio);
        System.out.println("✓ Servicio removido");
    }

    @Override
    public String obtenerDescripcion() {
        return "Agregar servicio: " + nombreServicio;
    }
}
