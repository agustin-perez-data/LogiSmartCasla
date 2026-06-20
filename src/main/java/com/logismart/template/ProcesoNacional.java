package com.logismart.template;

import com.logismart.dominio.Envio;

public class ProcesoNacional extends ProcesoProcesosEnvio {

    @Override
    protected void validar(Envio envio) {
        System.out.println("[Nacional] Validando envio nacional");
    }

    @Override
    protected void calcularCosto(Envio envio) {
        double costo = 100.0 + (envio.getPeso() * 5.0);
        envio.setCosto(costo);
        System.out.println("[Nacional] Costo: $" + String.format("%.2f", costo));
    }

    @Override
    protected void procesarPago(Envio envio) {
        System.out.println("[Nacional] Procesando pago local");
    }

    @Override
    protected void notificar(Envio envio) {
        System.out.println("[Nacional] Enviando notificacion al cliente");
    }
}
