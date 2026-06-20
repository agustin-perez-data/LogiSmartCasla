package com.logismart.datos.repositorio;

import com.logismart.datos.dominio.Cliente;
import java.util.*;
import java.util.stream.Collectors;

public class RepositorioClienteMemoria implements RepositorioCliente {
    private final Map<Integer, Cliente> almacen = new HashMap<>();

    @Override
    public void guardar(Cliente cliente) {
        almacen.put(cliente.getId(), cliente);
        System.out.println("[RepositorioClienteMem] guardado: " + cliente);
    }

    @Override
    public void actualizar(Cliente cliente) {
        almacen.put(cliente.getId(), cliente);
        System.out.println("[RepositorioClienteMem] actualizado: " + cliente);
    }

    @Override
    public void eliminar(Cliente cliente) {
        almacen.remove(cliente.getId());
        System.out.println("[RepositorioClienteMem] eliminado id=" + cliente.getId());
    }

    @Override
    public Cliente obtener(int id) {
        return almacen.get(id);
    }

    @Override
    public List<Cliente> obtenerTodos() {
        return new ArrayList<>(almacen.values());
    }

    @Override
    public Cliente buscarPorEmail(String email) {
        return almacen.values().stream()
            .filter(c -> c.getEmail().equalsIgnoreCase(email))
            .findFirst().orElse(null);
    }

    @Override
    public List<Cliente> buscarPorNombre(String nombre) {
        return almacen.values().stream()
            .filter(c -> c.getNombre().toLowerCase().contains(nombre.toLowerCase()))
            .collect(Collectors.toList());
    }
}
