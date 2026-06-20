package com.logismart.datos.servicio;

import com.logismart.datos.dominio.Cliente;
import com.logismart.datos.repositorio.RepositorioCliente;
import com.logismart.datos.uow.UnitOfWork;

public class ServicioClientes {
    private final RepositorioCliente repositorio;
    private final UnitOfWork unitOfWork;

    public ServicioClientes(RepositorioCliente repositorio, UnitOfWork unitOfWork) {
        this.repositorio = repositorio;
        this.unitOfWork = unitOfWork;
    }

    public Cliente crearCliente(int id, String nombre, String email, String telefono) {
        Cliente cliente = new Cliente(id, nombre, email, telefono);
        unitOfWork.registrarNuevoCliente(cliente);
        unitOfWork.commit();
        System.out.println("[ServicioClientes] Cliente creado: " + cliente);
        return cliente;
    }

    public Cliente obtenerCliente(int id) {
        return repositorio.obtener(id);
    }

    public Cliente buscarPorEmail(String email) {
        return repositorio.buscarPorEmail(email);
    }
}
