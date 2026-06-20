package com.logismart.datos.repositorio;

import com.logismart.datos.dominio.CentroDistribucion;
import java.util.*;
import java.util.stream.Collectors;

public class RepositorioCentroMemoria implements RepositorioCentro {
    private final Map<Integer, CentroDistribucion> almacen = new HashMap<>();

    @Override
    public void guardar(CentroDistribucion centro) {
        almacen.put(centro.getId(), centro);
        System.out.println("[RepositorioCentroMem] guardado: " + centro);
    }

    @Override
    public void actualizar(CentroDistribucion centro) {
        almacen.put(centro.getId(), centro);
        System.out.println("[RepositorioCentroMem] actualizado: " + centro);
    }

    @Override
    public void eliminar(CentroDistribucion centro) {
        almacen.remove(centro.getId());
        System.out.println("[RepositorioCentroMem] eliminado id=" + centro.getId());
    }

    @Override
    public CentroDistribucion obtener(int id) {
        return almacen.get(id);
    }

    @Override
    public List<CentroDistribucion> obtenerTodos() {
        return new ArrayList<>(almacen.values());
    }

    @Override
    public List<CentroDistribucion> buscarPorCiudad(String ciudad) {
        return almacen.values().stream()
            .filter(c -> c.getCiudad().equalsIgnoreCase(ciudad))
            .collect(Collectors.toList());
    }

    @Override
    public CentroDistribucion buscarPorNombre(String nombre) {
        return almacen.values().stream()
            .filter(c -> c.getNombre().equalsIgnoreCase(nombre))
            .findFirst().orElse(null);
    }
}
