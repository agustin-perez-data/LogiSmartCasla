package com.logismart.state;

import com.logismart.dominio.Envio;

public class EstadoEnReparto implements EstadoEnvio {

    @Override
    public void validar(Envio envio) {
        System.out.println("[State] El envio ya esta validado");
    }

    @Override
    public void entregar(Envio envio) {
        System.out.println("[State] Envio entregado");
        envio.cambiarEstado(new EstadoEntregado());
    }

    @Override
    public void cancelar(Envio envio) {
        System.out.println("[State] No se puede cancelar: esta en reparto");
    }

    @Override
    public void retener(Envio envio) {
        System.out.println("[State] Ya esta en reparto");
    }

    @Override
    public void devolver(Envio envio) {
        System.out.println("[State] Envio devuelto a transito");
        envio.cambiarEstado(new EstadoEnTransito());
    }

    @Override
    public void reclamar(Envio envio) {
        System.out.println("[State] No se puede reclamar: debe estar entregado");
    }

    @Override
    public String obtenerNombre() {
        return "EN_REPARTO";
    }
}
