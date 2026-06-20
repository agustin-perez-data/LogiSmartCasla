package com.logismart.servicios;

public interface ProveedorPago {
    boolean procesarPago(double monto, String referencia);
    String obtenerEstado(String idTransaccion);
    void reembolsar(String idTransaccion, double monto);
    String obtenerNombre();
}
