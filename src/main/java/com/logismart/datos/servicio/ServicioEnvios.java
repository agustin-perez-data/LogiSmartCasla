package com.logismart.datos.servicio;

import com.logismart.datos.dominio.Envio;
import com.logismart.datos.dominio.EstadoEnvio;
import com.logismart.datos.repositorio.RepositorioEnvio;
import com.logismart.datos.uow.UnitOfWork;
import java.util.List;

public class ServicioEnvios {
    private final RepositorioEnvio repositorio;
    private final UnitOfWork unitOfWork;

    public ServicioEnvios(RepositorioEnvio repositorio, UnitOfWork unitOfWork) {
        this.repositorio = repositorio;
        this.unitOfWork = unitOfWork;
    }

    public Envio crearEnvio(int id, String origen, String destino, double peso) {
        Envio envio = new Envio(id, origen, destino, peso);
        unitOfWork.registrarNuevoEnvio(envio);
        unitOfWork.commit();
        System.out.println("[ServicioEnvios] Envio creado: " + envio);
        return envio;
    }

    public Envio obtenerEnvio(int id) {
        return repositorio.obtener(id);
    }

    public List<Envio> obtenerPorEstado(EstadoEnvio estado) {
        return repositorio.buscarPorEstado(estado);
    }

    public void actualizarEstado(int id, EstadoEnvio nuevoEstado) {
        Envio envio = repositorio.obtener(id);
        if (envio != null) {
            envio.setEstado(nuevoEstado);
            unitOfWork.registrarModificadoEnvio(envio);
            unitOfWork.commit();
            System.out.println("[ServicioEnvios] Estado actualizado: " + envio);
        }
    }
}
