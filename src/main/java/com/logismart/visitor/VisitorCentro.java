package com.logismart.visitor;

public interface VisitorCentro {
    void visitar(PuntoEntrega punto);
    void visitar(CentroRegional centro);

    default void entrarCentro(CentroRegional centro) {
    }

    default void salirCentro(CentroRegional centro) {
    }
}
