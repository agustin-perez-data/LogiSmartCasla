package com.logismart.command;

import com.logismart.dominio.Envio;

public class ComandoCrearEnvio implements Comando {

    private final ServicioEnvios servicio;
    private final Envio envio;
    private String numeroSeguimiento;

    public ComandoCrearEnvio(ServicioEnvios servicio, Envio envio) {
        this.servicio = servicio;
        this.envio = envio;
    }

    @Override
    public void ejecutar() {
        System.out.println("[Comando] Creando envío...");
        numeroSeguimiento = servicio.crearEnvio(envio);
        System.out.println("✓ Envío creado: " + numeroSeguimiento);
    }

    @Override
    public void deshacer() {
        System.out.println("[Comando] Deshaciendo: Cancelando envío...");
        servicio.cancelarEnvio(numeroSeguimiento);
        System.out.println("✓ Envío cancelado");
    }

    @Override
    public String obtenerDescripcion() {
        return "Crear envío de " + envio.getOrigen() + " a " + envio.getDestino();
    }

    public String getNumeroSeguimiento() {
        return numeroSeguimiento;
    }
}
