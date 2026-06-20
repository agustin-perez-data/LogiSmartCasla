package com.logismart.state;

import com.logismart.dominio.Envio;

public class EstadoEnTransito implements EstadoEnvio {

    @Override
    public void validar(Envio envio) {
        System.out.println("[State] El envio ya esta validado");
    }

    @Override
    public void entregar(Envio envio) {
        System.out.println("[State] Envio en reparto");
        envio.cambiarEstado(new EstadoEnReparto());
    }

    @Override
    public void cancelar(Envio envio) {
        System.out.println("[State] No se puede cancelar: esta en transito");
    }

    @Override
    public void retener(Envio envio) {
        System.out.println("[State] Envio retenido");
        envio.cambiarEstado(new EstadoRetenido());
    }

    @Override
    public void devolver(Envio envio) {
        System.out.println("[State] No se puede devolver: debe estar en reparto");
    }

    @Override
    public void reclamar(Envio envio) {
        System.out.println("[State] No se puede reclamar: debe estar entregado");
    }

    @Override
    public String obtenerNombre() {
        return "EN_TRANSITO";
    }
}
