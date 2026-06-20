package com.logismart.datos.repositorio;

import com.logismart.datos.dominio.Envio;
import com.logismart.datos.dominio.EstadoEnvio;
import com.logismart.datos.mapper.EnvioMapper;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class RepositorioEnvioSQL implements RepositorioEnvio {
    private final EnvioMapper mapper;

    public RepositorioEnvioSQL(Connection conexion) {
        this.mapper = new EnvioMapper(conexion);
    }

    @Override
    public void guardar(Envio envio) { mapper.insert(envio); }

    @Override
    public void actualizar(Envio envio) { mapper.update(envio); }

    @Override
    public void eliminar(Envio envio) { mapper.delete(envio); }

    @Override
    public Envio obtener(int id) { return mapper.findById(id); }

    @Override
    public List<Envio> obtenerTodos() { return new ArrayList<>(); }

    @Override
    public List<Envio> buscarPorEstado(EstadoEnvio estado) { return new ArrayList<>(); }

    @Override
    public List<Envio> buscarPorOrigen(String origen) { return new ArrayList<>(); }

    @Override
    public List<Envio> buscarPorDestino(String destino) { return new ArrayList<>(); }
}
