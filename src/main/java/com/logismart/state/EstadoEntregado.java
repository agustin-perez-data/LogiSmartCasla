package com.logismart.state;

import com.logismart.dominio.Envio;

public class EstadoEntregado implements EstadoEnvio {

    @Override
    public void validar(Envio envio) {
        System.out.println("[State] El envio ya fue entregado");
    }

    @Override
    public void entregar(Envio envio) {
        System.out.println("[State] El envio ya esta entregado");
    }

    @Override
    public void cancelar(Envio envio) {
        System.out.println("[State] No se puede cancelar: ya fue entregado");
    }

    @Override
    public void retener(Envio envio) {
        System.out.println("[State] No se puede retener: ya fue entregado");
    }

    @Override
    public void devolver(Envio envio) {
        System.out.println("[State] No se puede devolver: ya fue entregado");
    }

    @Override
    public void reclamar(Envio envio) {
        System.out.println("[State] Reclamo registrado para " + envio.getId());
    }

    @Override
    public String obtenerNombre() {
        return "ENTREGADO";
    }
}
