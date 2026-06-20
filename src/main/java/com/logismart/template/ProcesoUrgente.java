package com.logismart.template;

import com.logismart.dominio.Envio;

public class ProcesoUrgente extends ProcesoProcesosEnvio {

    @Override
    protected void validar(Envio envio) {
        System.out.println("[Urgente] Validacion acelerada");
    }

    @Override
    protected void calcularCosto(Envio envio) {
        double costo = 500.0 + (envio.getPeso() * 15.0);
        envio.setCosto(costo);
        System.out.println("[Urgente] Costo prioritario: $" + String.format("%.2f", costo));
    }

    @Override
    protected void procesarPago(Envio envio) {
        System.out.println("[Urgente] Procesando pago inmediato");
    }

    @Override
    protected void notificar(Envio envio) {
        System.out.println("[Urgente] Enviando SMS urgente");
    }
}
