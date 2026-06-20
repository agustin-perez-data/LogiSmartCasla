package com.logismart.datos.repositorio;

import com.logismart.datos.dominio.EstadoPago;
import com.logismart.datos.dominio.Pago;
import java.util.*;
import java.util.stream.Collectors;

public class RepositorioPagoMemoria implements RepositorioPago {
    private final Map<Integer, Pago> almacen = new HashMap<>();

    @Override
    public void guardar(Pago pago) {
        almacen.put(pago.getId(), pago);
        System.out.println("[RepositorioPagoMem] guardado: " + pago);
    }

    @Override
    public void actualizar(Pago pago) {
        almacen.put(pago.getId(), pago);
        System.out.println("[RepositorioPagoMem] actualizado: " + pago);
    }

    @Override
    public void eliminar(Pago pago) {
        almacen.remove(pago.getId());
        System.out.println("[RepositorioPagoMem] eliminado id=" + pago.getId());
    }

    @Override
    public Pago obtener(int id) {
        return almacen.get(id);
    }

    @Override
    public List<Pago> obtenerTodos() {
        return new ArrayList<>(almacen.values());
    }

    @Override
    public List<Pago> buscarPorEnvio(int envioId) {
        return almacen.values().stream()
            .filter(p -> p.getEnvioId() == envioId)
            .collect(Collectors.toList());
    }

    @Override
    public List<Pago> buscarPorEstado(EstadoPago estado) {
        return almacen.values().stream()
            .filter(p -> p.getEstado() == estado)
            .collect(Collectors.toList());
    }
}
