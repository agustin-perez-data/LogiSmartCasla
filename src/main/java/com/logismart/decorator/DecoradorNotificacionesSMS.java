package com.logismart.decorator;

public class DecoradorNotificacionesSMS extends DecoradorEnvio {

    public DecoradorNotificacionesSMS(Envio envio) {
        super(envio);
    }

    @Override
    public double obtenerCosto() {
        return envioDecorado.obtenerCosto() + 25.0;
    }

    @Override
    public String obtenerServicios() {
        return envioDecorado.obtenerServicios() + " + Notificaciones SMS";
    }
}
