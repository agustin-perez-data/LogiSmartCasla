package com.logismart.mediator;

import com.logismart.dominio.Envio;

public class CentroDistribucion {

    private final MediadorEnvios mediador;

    public CentroDistribucion(MediadorEnvios mediador) {
        this.mediador = mediador;
    }

    public void crearEnvio(Envio envio) {
        System.out.println("[Centro] Iniciando creacion de envio: " + envio.getId());
        mediador.notificar("ENVIO_CREADO", envio);
    }

    public void registrarEnvio(Envio envio) {
        System.out.println("[Centro] Envio " + envio.getId() + " registrado en el sistema");
        envio.cambiarEstado("ENTREGADO");
        mediador.notificar("ENVIO_REGISTRADO", envio);
    }
}
