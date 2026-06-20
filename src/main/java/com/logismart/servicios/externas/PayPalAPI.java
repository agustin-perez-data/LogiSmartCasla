package com.logismart.servicios.externas;

public class PayPalAPI {
    public String crearTransaccion(double cantidad, String descripcion) {
        System.out.println("[PayPal API] Creando transacción: $" + cantidad);
        return "PP-" + System.currentTimeMillis();
    }

    public String consultarTransaccion(String id) {
        return "COMPLETADA";
    }

    public void refund(String transactionId, double amount) {
        System.out.println("[PayPal API] Refund $" + amount + " para " + transactionId);
    }
}
