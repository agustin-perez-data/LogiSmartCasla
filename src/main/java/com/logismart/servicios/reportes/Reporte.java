package com.logismart.servicios.reportes;

public abstract class Reporte {
    protected GeneradorReporte generador;

    public Reporte(GeneradorReporte generador) {
        this.generador = generador;
    }

    public abstract String generarContenido();

    public String generar() {
        String contenido = generarContenido();
        return generador.formatear(contenido);
    }

    public void setGenerador(GeneradorReporte generador) {
        this.generador = generador;
    }

    public String obtenerNombreArchivo(String nombre) {
        return nombre + "." + generador.obtenerExtension();
    }
}
