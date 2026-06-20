package com.logismart.visitor;

public interface CentroDistribucion {
    void aceptar(VisitorCentro visitor);
    String obtenerNombre();
}
