package com.logismart.servicios.adapters;

import com.logismart.servicios.ProveedorPago;
import com.logismart.servicios.externas.PayPalAPI;
import com.logismart.singleton.LoggerLogiSmart;

public class AdapterPayPal implements ProveedorPago {
    private final PayPalAPI paypalAPI;
    private final LoggerLogiSmart logger = LoggerLogiSmart.obtenerInstancia();

    public AdapterPayPal() {
        this.paypalAPI = new PayPalAPI();
    }

    @Override
    public boolean procesarPago(double monto, String referencia) {
        String idTransaccion = paypalAPI.crearTransaccion(monto, referencia);
        boolean exito = idTransaccion != null;
        logger.info("AdapterPayPal: pago " + (exito ? "exitoso" : "fallido")
                   + " | monto=$" + monto + " | ref=" + referencia);
        return exito;
    }

    @Override
    public String obtenerEstado(String idTransaccion) {
        return paypalAPI.consultarTransaccion(idTransaccion);
    }

    @Override
    public void reembolsar(String idTransaccion, double monto) {
        paypalAPI.refund(idTransaccion, monto);
        logger.info("AdapterPayPal: reembolso $" + monto + " para " + idTransaccion);
    }

    @Override
    public String obtenerNombre() {
        return "PayPal";
    }
}
