package com.logismart.servicios;

public class GoogleMapsArgentina implements ProveedorMapas {
    private static final double RADIO_TIERRA_KM = 6371.0;

    @Override
    public double calcularDistancia(double lat1, double lon1, double lat2, double lon2) {
        // Haversine con factor de correccion por rutas urbanas argentinas (1.3)
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                 + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                 * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double distanciaLineal = RADIO_TIERRA_KM * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return distanciaLineal * 1.3;
    }

    @Override
    public String obtenerRuta(String origen, String destino) {
        String ruta = "[Google Maps AR] Ruta: " + origen + " → " + destino;
        System.out.println(ruta);
        return ruta;
    }

    @Override
    public String getNombreProveedor() {
        return "Google Maps Argentina";
    }
}
