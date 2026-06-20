package com.logismart.visitor;

public class PuntoEntrega implements CentroDistribucion {

    private final String nombre;
    private final double ocupacion;

    public PuntoEntrega(String nombre, double ocupacion) {
        this.nombre = nombre;
        this.ocupacion = ocupacion;
    }

    @Override
    public void aceptar(VisitorCentro visitor) {
        visitor.visitar(this);
    }

    @Override
    public String obtenerNombre() {
        return nombre;
    }

    public double obtenerOcupacion() {
        return ocupacion;
    }
}
