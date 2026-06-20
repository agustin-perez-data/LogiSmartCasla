package com.logismart.state;

import com.logismart.dominio.Envio;

public class EstadoCancelado implements EstadoEnvio {

    @Override
    public void validar(Envio envio) {
        System.out.println("[State] Envio cancelado");
    }

    @Override
    public void entregar(Envio envio) {
        System.out.println("[State] Envio cancelado");
    }

    @Override
    public void cancelar(Envio envio) {
        System.out.println("[State] El envio ya esta cancelado");
    }

    @Override
    public void retener(Envio envio) {
        System.out.println("[State] Envio cancelado");
    }

    @Override
    public void devolver(Envio envio) {
        System.out.println("[State] Envio cancelado");
    }

    @Override
    public void reclamar(Envio envio) {
        System.out.println("[State] Envio cancelado");
    }

    @Override
    public String obtenerNombre() {
        return "CANCELADO";
    }
}
