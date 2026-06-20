package com.logismart.datos.repositorio;

import com.logismart.datos.dominio.Cliente;
import com.logismart.datos.mapper.ClienteMapper;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class RepositorioClienteSQL implements RepositorioCliente {
    private final ClienteMapper mapper;

    public RepositorioClienteSQL(Connection conexion) {
        this.mapper = new ClienteMapper(conexion);
    }

    @Override
    public void guardar(Cliente cliente) { mapper.insert(cliente); }

    @Override
    public void actualizar(Cliente cliente) { mapper.update(cliente); }

    @Override
    public void eliminar(Cliente cliente) { mapper.delete(cliente); }

    @Override
    public Cliente obtener(int id) { return mapper.findById(id); }

    @Override
    public List<Cliente> obtenerTodos() { return new ArrayList<>(); }

    @Override
    public Cliente buscarPorEmail(String email) { return null; }

    @Override
    public List<Cliente> buscarPorNombre(String nombre) { return new ArrayList<>(); }
}
