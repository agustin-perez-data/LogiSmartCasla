package com.logismart.datos.lazy;

import com.logismart.datos.dominio.CentroDistribucion;
import com.logismart.datos.repositorio.RepositorioCentro;

public class CentroDistribucionLazyProxy {
    private final int id;
    private CentroDistribucion centro;
    private final RepositorioCentro repositorio;
    private boolean cargado = false;

    public CentroDistribucionLazyProxy(int id, RepositorioCentro repositorio) {
        this.id = id;
        this.repositorio = repositorio;
    }

    private void cargar() {
        if (!cargado) {
            this.centro = repositorio.obtener(id);
            this.cargado = true;
            System.out.println("[LazyLoad] Centro cargado desde repositorio: id=" + id);
        }
    }

    public String getNombre() { cargar(); return centro != null ? centro.getNombre() : null; }
    public String getCiudad() { cargar(); return centro != null ? centro.getCiudad() : null; }
    public double getCapacidad() { cargar(); return centro != null ? centro.getCapacidad() : 0; }
    public double getOcupacion() { cargar(); return centro != null ? centro.getOcupacion() : 0; }
    public CentroDistribucion getCentro() { cargar(); return centro; }
    public boolean isCargado() { return cargado; }
}
