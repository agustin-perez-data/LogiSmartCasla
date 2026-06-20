package com.logismart.datos.repositorio;

import com.logismart.datos.dominio.Envio;
import com.logismart.datos.dominio.EstadoEnvio;
import java.util.*;
import java.util.stream.Collectors;

public class RepositorioEnvioMemoria implements RepositorioEnvio {
    private final Map<Integer, Envio> almacen = new HashMap<>();

    @Override
    public void guardar(Envio envio) {
        almacen.put(envio.getId(), envio);
        System.out.println("[RepositorioEnvioMem] guardado: " + envio);
    }

    @Override
    public void actualizar(Envio envio) {
        almacen.put(envio.getId(), envio);
        System.out.println("[RepositorioEnvioMem] actualizado: " + envio);
    }

    @Override
    public void eliminar(Envio envio) {
        almacen.remove(envio.getId());
        System.out.println("[RepositorioEnvioMem] eliminado id=" + envio.getId());
    }

    @Override
    public Envio obtener(int id) {
        return almacen.get(id);
    }

    @Override
    public List<Envio> obtenerTodos() {
        return new ArrayList<>(almacen.values());
    }

    @Override
    public List<Envio> buscarPorEstado(EstadoEnvio estado) {
        return almacen.values().stream()
            .filter(e -> e.getEstado() == estado)
            .collect(Collectors.toList());
    }

    @Override
    public List<Envio> buscarPorOrigen(String origen) {
        return almacen.values().stream()
            .filter(e -> e.getOrigen().equalsIgnoreCase(origen))
            .collect(Collectors.toList());
    }

    @Override
    public List<Envio> buscarPorDestino(String destino) {
        return almacen.values().stream()
            .filter(e -> e.getDestino().equalsIgnoreCase(destino))
            .collect(Collectors.toList());
    }
}
