package com.logismart.datos.repositorio;

import com.logismart.datos.dominio.EstadoPago;
import com.logismart.datos.dominio.Pago;
import com.logismart.datos.mapper.PagoMapper;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class RepositorioPagoSQL implements RepositorioPago {
    private final PagoMapper mapper;

    public RepositorioPagoSQL(Connection conexion) {
        this.mapper = new PagoMapper(conexion);
    }

    @Override
    public void guardar(Pago pago) { mapper.insert(pago); }

    @Override
    public void actualizar(Pago pago) { mapper.update(pago); }

    @Override
    public void eliminar(Pago pago) { mapper.delete(pago); }

    @Override
    public Pago obtener(int id) { return mapper.findById(id); }

    @Override
    public List<Pago> obtenerTodos() { return new ArrayList<>(); }

    @Override
    public List<Pago> buscarPorEnvio(int envioId) { return new ArrayList<>(); }

    @Override
    public List<Pago> buscarPorEstado(EstadoPago estado) { return new ArrayList<>(); }
}
