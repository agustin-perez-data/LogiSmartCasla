package com.logismart.state;

import com.logismart.dominio.Envio;

public class EstadoRetenido implements EstadoEnvio {

    @Override
    public void validar(Envio envio) {
        System.out.println("[State] No se puede validar: envio retenido");
    }

    @Override
    public void entregar(Envio envio) {
        System.out.println("[State] Envio liberado y enviado a reparto");
        envio.cambiarEstado(new EstadoEnReparto());
    }

    @Override
    public void cancelar(Envio envio) {
        System.out.println("[State] Envio cancelado");
        envio.cambiarEstado(new EstadoCancelado());
    }

    @Override
    public void retener(Envio envio) {
        System.out.println("[State] El envio ya esta retenido");
    }

    @Override
    public void devolver(Envio envio) {
        System.out.println("[State] Envio devuelto a transito");
        envio.cambiarEstado(new EstadoEnTransito());
    }

    @Override
    public void reclamar(Envio envio) {
        System.out.println("[State] No se puede reclamar: envio retenido");
    }

    @Override
    public String obtenerNombre() {
        return "RETENIDO";
    }
}
