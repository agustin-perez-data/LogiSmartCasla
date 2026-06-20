package com.logismart.servicios;

public interface ProveedorMapas {
    double calcularDistancia(double lat1, double lon1, double lat2, double lon2);
    String obtenerRuta(String origen, String destino);
    String getNombreProveedor();
}
