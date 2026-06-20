package com.logismart.visitor;

public class VisitorCalculoCostoOperativo implements VisitorCentro {

    private double costoTotal = 0.0;

    @Override
    public void visitar(PuntoEntrega punto) {
        costoTotal += punto.obtenerOcupacion() * 10.0;
    }

    @Override
    public void visitar(CentroRegional centro) {
        System.out.println("[Costo] Centro: " + centro.obtenerNombre());
    }

    public double obtenerCostoTotal() {
        return costoTotal;
    }
}
