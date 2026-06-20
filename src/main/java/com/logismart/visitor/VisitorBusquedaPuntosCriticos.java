package com.logismart.visitor;

import java.util.ArrayList;
import java.util.List;

public class VisitorBusquedaPuntosCriticos implements VisitorCentro {

    private final List<String> puntosCriticos = new ArrayList<>();
    private final double umbral;

    public VisitorBusquedaPuntosCriticos() {
        this(80.0);
    }

    public VisitorBusquedaPuntosCriticos(double umbral) {
        this.umbral = umbral;
    }

    @Override
    public void visitar(PuntoEntrega punto) {
        if (punto.obtenerOcupacion() > umbral) {
            puntosCriticos.add(punto.obtenerNombre() + " ("
                    + String.format("%.1f", punto.obtenerOcupacion()) + "%)");
        }
    }

    @Override
    public void visitar(CentroRegional centro) {
        // Los centros regionales no se consideran puntos criticos directos.
    }

    public List<String> obtenerPuntosCriticos() {
        return new ArrayList<>(puntosCriticos);
    }
}
