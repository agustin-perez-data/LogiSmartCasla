package com.logismart.facade.subsistemas;

public class SistemaPagos {
    public boolean procesarPago(String metodoPago, double monto) {
        System.out.println("  [Pagos] Procesando pago de $" + monto + " via " + metodoPago);
        boolean exito = monto <= 10000;
        System.out.println("  [Pagos] Pago " + (exito ? "APROBADO" : "RECHAZADO"));
        return exito;
    }

    public void reembolsar(String numeroSeguimiento) {
        System.out.println("  [Pagos] Reembolso procesado para: " + numeroSeguimiento);
    }
}
