package com.logismart.visitor;

import java.util.ArrayList;
import java.util.List;

public class CentroRegional implements CentroDistribucion {

    private final String nombre;
    private final List<CentroDistribucion> subcentros = new ArrayList<>();

    public CentroRegional(String nombre) {
        this.nombre = nombre;
    }

    public void agregarSubcentro(CentroDistribucion centro) {
        subcentros.add(centro);
    }

    @Override
    public void aceptar(VisitorCentro visitor) {
        visitor.visitar(this);
        visitor.entrarCentro(this);
        for (CentroDistribucion centro : subcentros) {
            centro.aceptar(visitor);
        }
        visitor.salirCentro(this);
    }

    @Override
    public String obtenerNombre() {
        return nombre;
    }

    public List<CentroDistribucion> obtenerSubcentros() {
        return new ArrayList<>(subcentros);
    }
}
