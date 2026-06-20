package com.logismart.visitor;

public class VisitorCalculoOcupacion implements VisitorCentro {

    private double ocupacionTotal = 0.0;
    private int puntosContados = 0;

    @Override
    public void visitar(PuntoEntrega punto) {
        ocupacionTotal += punto.obtenerOcupacion();
        puntosContados++;
    }

    @Override
    public void visitar(CentroRegional centro) {
        System.out.println("[Ocupacion] Centro: " + centro.obtenerNombre());
    }

    public double obtenerOcupacionPromedio() {
        return puntosContados > 0 ? ocupacionTotal / puntosContados : 0.0;
    }
}
