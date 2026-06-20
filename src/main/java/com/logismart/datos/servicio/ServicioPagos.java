package com.logismart.datos.servicio;

import com.logismart.datos.dominio.EstadoPago;
import com.logismart.datos.dominio.Pago;
import com.logismart.datos.repositorio.RepositorioPago;
import com.logismart.datos.uow.UnitOfWork;
import java.util.List;

public class ServicioPagos {
    private final RepositorioPago repositorio;
    private final UnitOfWork unitOfWork;

    public ServicioPagos(RepositorioPago repositorio, UnitOfWork unitOfWork) {
        this.repositorio = repositorio;
        this.unitOfWork = unitOfWork;
    }

    public Pago procesarPago(int id, int envioId, double monto) {
        Pago pago = new Pago(id, envioId, monto);
        unitOfWork.registrarNuevoPago(pago);
        unitOfWork.commit();
        System.out.println("[ServicioPagos] Pago procesado: " + pago);
        return pago;
    }

    public Pago obtenerPago(int id) {
        return repositorio.obtener(id);
    }

    public List<Pago> obtenerPorEnvio(int envioId) {
        return repositorio.buscarPorEnvio(envioId);
    }

    public void actualizarEstado(int id, EstadoPago nuevoEstado) {
        Pago pago = repositorio.obtener(id);
        if (pago != null) {
            pago.setEstado(nuevoEstado);
            unitOfWork.registrarModificadoPago(pago);
            unitOfWork.commit();
        }
    }
}
