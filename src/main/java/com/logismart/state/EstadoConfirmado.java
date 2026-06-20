package com.logismart.state;

import com.logismart.dominio.Envio;

public class EstadoConfirmado implements EstadoEnvio {

    @Override
    public void validar(Envio envio) {
        System.out.println("[State] Envio validado");
        envio.cambiarEstado(new EstadoEnTransito());
    }

    @Override
    public void entregar(Envio envio) {
        System.out.println("[State] No se puede entregar: debe estar en transito");
    }

    @Override
    public void cancelar(Envio envio) {
        System.out.println("[State] Envio cancelado");
        envio.cambiarEstado(new EstadoCancelado());
    }

    @Override
    public void retener(Envio envio) {
        System.out.println("[State] No se puede retener: debe estar en transito");
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
        return "CONFIRMADO";
    }
}
