package com.logismart.datos.repositorio;

import com.logismart.datos.dominio.CentroDistribucion;
import java.util.List;

public interface RepositorioCentro extends Repositorio<CentroDistribucion> {
    List<CentroDistribucion> buscarPorCiudad(String ciudad);
    CentroDistribucion buscarPorNombre(String nombre);
}
