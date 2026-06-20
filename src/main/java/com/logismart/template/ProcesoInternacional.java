package com.logismart.template;

import com.logismart.dominio.Envio;

public class ProcesoInternacional extends ProcesoProcesosEnvio {

    @Override
    protected void validar(Envio envio) {
        System.out.println("[Internacional] Validando documentacion aduanera");
    }

    @Override
    protected void calcularCosto(Envio envio) {
        double costoBase = 200.0 + (envio.getPeso() * 10.0);
        double costoAduanas = costoBase * 0.15;
        double costo = costoBase + costoAduanas;
        envio.setCosto(costo);
        System.out.println("[Internacional] Costo: $" + String.format("%.2f", costo));
    }

    @Override
    protected void procesarPago(Envio envio) {
        System.out.println("[Internacional] Procesando pago internacional");
    }

    @Override
    protected void notificar(Envio envio) {
        System.out.println("[Internacional] Enviando informacion aduanera");
    }
}
