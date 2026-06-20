package com.logismart.datos.servicio;

import com.logismart.datos.dominio.CentroDistribucion;
import com.logismart.datos.repositorio.RepositorioCentro;
import com.logismart.datos.uow.UnitOfWork;
import java.util.List;

public class ServicioCentros {
    private final RepositorioCentro repositorio;
    private final UnitOfWork unitOfWork;

    public ServicioCentros(RepositorioCentro repositorio, UnitOfWork unitOfWork) {
        this.repositorio = repositorio;
        this.unitOfWork = unitOfWork;
    }

    public CentroDistribucion crearCentro(int id, String nombre, String ciudad, double capacidad) {
        CentroDistribucion centro = new CentroDistribucion(id, nombre, ciudad, capacidad);
        unitOfWork.registrarNuevoCentro(centro);
        unitOfWork.commit();
        System.out.println("[ServicioCentros] Centro creado: " + centro);
        return centro;
    }

    public CentroDistribucion obtenerCentro(int id) {
        return repositorio.obtener(id);
    }

    public List<CentroDistribucion> buscarPorCiudad(String ciudad) {
        return repositorio.buscarPorCiudad(ciudad);
    }

    public void actualizarOcupacion(int id, double ocupacion) {
        CentroDistribucion centro = repositorio.obtener(id);
        if (centro != null) {
            centro.setOcupacion(ocupacion);
            unitOfWork.registrarModificadoCentro(centro);
            unitOfWork.commit();
        }
    }
}
