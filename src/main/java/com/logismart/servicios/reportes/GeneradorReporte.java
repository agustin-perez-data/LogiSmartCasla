package com.logismart.servicios.reportes;

public interface GeneradorReporte {
    String formatear(String contenido);
    String obtenerExtension();
}
