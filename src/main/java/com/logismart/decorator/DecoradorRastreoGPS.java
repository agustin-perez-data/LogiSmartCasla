package com.logismart.decorator;

public class DecoradorRastreoGPS extends DecoradorEnvio {

    public DecoradorRastreoGPS(Envio envio) {
        super(envio);
    }

    @Override
    public double obtenerCosto() {
        return envioDecorado.obtenerCosto() + 50.0;
    }

    @Override
    public int obtenerTiempoEntrega() {
        return Math.max(1, envioDecorado.obtenerTiempoEntrega() - 1);
    }

    @Override
    public String obtenerServicios() {
        return envioDecorado.obtenerServicios() + " + Rastreo GPS";
    }
}
