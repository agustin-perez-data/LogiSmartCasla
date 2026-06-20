package com.logismart.servicios.adapters;

import com.logismart.servicios.ProveedorPago;
import com.logismart.servicios.externas.StripeAPI;
import com.logismart.singleton.LoggerLogiSmart;

public class AdapterStripe implements ProveedorPago {
    private final StripeAPI stripeAPI;
    private final LoggerLogiSmart logger = LoggerLogiSmart.obtenerInstancia();

    public AdapterStripe() {
        this.stripeAPI = new StripeAPI();
    }

    @Override
    public boolean procesarPago(double monto, String referencia) {
        double montoEnCentavos = monto * 100;
        boolean exito = stripeAPI.charge(montoEnCentavos, referencia);
        logger.info("AdapterStripe: pago " + (exito ? "exitoso" : "fallido")
                   + " | monto=$" + monto + " (" + montoEnCentavos + " cents)");
        return exito;
    }

    @Override
    public String obtenerEstado(String idTransaccion) {
        String estadoStripe = stripeAPI.getChargeStatus(idTransaccion);
        return switch (estadoStripe) {
            case "succeeded" -> "COMPLETADA";
            case "pending"   -> "PENDIENTE";
            case "failed"    -> "FALLIDA";
            default          -> estadoStripe;
        };
    }

    @Override
    public void reembolsar(String idTransaccion, double monto) {
        double montoEnCentavos = monto * 100;
        stripeAPI.refundCharge(idTransaccion, montoEnCentavos);
        logger.info("AdapterStripe: reembolso $" + monto + " para " + idTransaccion);
    }

    @Override
    public String obtenerNombre() {
        return "Stripe";
    }
}
