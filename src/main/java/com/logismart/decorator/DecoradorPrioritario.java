package com.logismart.decorator;

public class DecoradorPrioritario extends DecoradorEnvio {

    public DecoradorPrioritario(Envio envio) {
        super(envio);
    }

    @Override
    public double obtenerCosto() {
        return envioDecorado.obtenerCosto() + 100.0;
    }

    @Override
    public int obtenerTiempoEntrega() {
        return Math.max(1, envioDecorado.obtenerTiempoEntrega() - 2);
    }

    @Override
    public String obtenerServicios() {
        return envioDecorado.obtenerServicios() + " + Prioritario";
    }
}
