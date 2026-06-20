package com.logismart.datos.repositorio;

import com.logismart.datos.dominio.CentroDistribucion;
import com.logismart.datos.mapper.CentroDistribucionMapper;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class RepositorioCentroSQL implements RepositorioCentro {
    private final CentroDistribucionMapper mapper;

    public RepositorioCentroSQL(Connection conexion) {
        this.mapper = new CentroDistribucionMapper(conexion);
    }

    @Override
    public void guardar(CentroDistribucion centro) { mapper.insert(centro); }

    @Override
    public void actualizar(CentroDistribucion centro) { mapper.update(centro); }

    @Override
    public void eliminar(CentroDistribucion centro) { mapper.delete(centro); }

    @Override
    public CentroDistribucion obtener(int id) { return mapper.findById(id); }

    @Override
    public List<CentroDistribucion> obtenerTodos() { return new ArrayList<>(); }

    @Override
    public List<CentroDistribucion> buscarPorCiudad(String ciudad) { return new ArrayList<>(); }

    @Override
    public CentroDistribucion buscarPorNombre(String nombre) { return null; }
}
