package com.logismart.datos.uow;

import com.logismart.datos.dominio.*;
import com.logismart.datos.repositorio.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UnitOfWork {
    private final Connection conexion;
    private final RepositorioEnvio repositorioEnvio;
    private final RepositorioCliente repositorioCliente;
    private final RepositorioCentro repositorioCentro;
    private final RepositorioPago repositorioPago;

    private final List<Envio> enviosNuevos = new ArrayList<>();
    private final List<Envio> enviosModificados = new ArrayList<>();
    private final List<Envio> enviosEliminados = new ArrayList<>();

    private final List<Cliente> clientesNuevos = new ArrayList<>();
    private final List<Cliente> clientesModificados = new ArrayList<>();
    private final List<Cliente> clientesEliminados = new ArrayList<>();

    private final List<CentroDistribucion> centrosNuevos = new ArrayList<>();
    private final List<CentroDistribucion> centrosModificados = new ArrayList<>();
    private final List<CentroDistribucion> centrosEliminados = new ArrayList<>();

    private final List<Pago> pagosNuevos = new ArrayList<>();
    private final List<Pago> pagosModificados = new ArrayList<>();
    private final List<Pago> pagosEliminados = new ArrayList<>();

    public UnitOfWork(Connection conexion,
                      RepositorioEnvio repositorioEnvio,
                      RepositorioCliente repositorioCliente,
                      RepositorioCentro repositorioCentro,
                      RepositorioPago repositorioPago) {
        this.conexion = conexion;
        this.repositorioEnvio = repositorioEnvio;
        this.repositorioCliente = repositorioCliente;
        this.repositorioCentro = repositorioCentro;
        this.repositorioPago = repositorioPago;
    }

    public void registrarNuevoEnvio(Envio envio) {
        enviosNuevos.add(envio);
        System.out.println("[UoW] Envio registrado como nuevo: " + envio.getId());
    }

    public void registrarModificadoEnvio(Envio envio) {
        enviosModificados.add(envio);
        System.out.println("[UoW] Envio registrado como modificado: " + envio.getId());
    }

    public void registrarEliminadoEnvio(Envio envio) {
        enviosEliminados.add(envio);
        System.out.println("[UoW] Envio registrado como eliminado: " + envio.getId());
    }

    public void registrarNuevoCliente(Cliente cliente) {
        clientesNuevos.add(cliente);
        System.out.println("[UoW] Cliente registrado como nuevo: " + cliente.getId());
    }

    public void registrarModificadoCliente(Cliente cliente) {
        clientesModificados.add(cliente);
        System.out.println("[UoW] Cliente registrado como modificado: " + cliente.getId());
    }

    public void registrarEliminadoCliente(Cliente cliente) {
        clientesEliminados.add(cliente);
        System.out.println("[UoW] Cliente registrado como eliminado: " + cliente.getId());
    }

    public void registrarNuevoCentro(CentroDistribucion centro) {
        centrosNuevos.add(centro);
        System.out.println("[UoW] Centro registrado como nuevo: " + centro.getId());
    }

    public void registrarModificadoCentro(CentroDistribucion centro) {
        centrosModificados.add(centro);
        System.out.println("[UoW] Centro registrado como modificado: " + centro.getId());
    }

    public void registrarNuevoPago(Pago pago) {
        pagosNuevos.add(pago);
        System.out.println("[UoW] Pago registrado como nuevo: " + pago.getId());
    }

    public void registrarModificadoPago(Pago pago) {
        pagosModificados.add(pago);
        System.out.println("[UoW] Pago registrado como modificado: " + pago.getId());
    }

    public void commit() {
        try {
            if (conexion != null) conexion.setAutoCommit(false);

            for (Envio e : enviosNuevos) repositorioEnvio.guardar(e);
            for (Envio e : enviosModificados) repositorioEnvio.actualizar(e);
            for (Envio e : enviosEliminados) repositorioEnvio.eliminar(e);

            for (Cliente c : clientesNuevos) repositorioCliente.guardar(c);
            for (Cliente c : clientesModificados) repositorioCliente.actualizar(c);
            for (Cliente c : clientesEliminados) repositorioCliente.eliminar(c);

            for (CentroDistribucion cd : centrosNuevos) repositorioCentro.guardar(cd);
            for (CentroDistribucion cd : centrosModificados) repositorioCentro.actualizar(cd);
            for (CentroDistribucion cd : centrosEliminados) repositorioCentro.eliminar(cd);

            for (Pago p : pagosNuevos) repositorioPago.guardar(p);
            for (Pago p : pagosModificados) repositorioPago.actualizar(p);
            for (Pago p : pagosEliminados) repositorioPago.eliminar(p);

            if (conexion != null) conexion.commit();
            System.out.println("[UoW] Transaccion completada con exito");
            limpiar();
        } catch (SQLException e) {
            System.out.println("[UoW] Error en commit, intentando rollback: " + e.getMessage());
            rollback();
        }
    }

    public void rollback() {
        try {
            if (conexion != null) conexion.rollback();
            System.out.println("[UoW] Cambios revertidos (rollback)");
            limpiar();
        } catch (SQLException e) {
            System.out.println("[UoW] Error en rollback: " + e.getMessage());
        }
    }

    private void limpiar() {
        enviosNuevos.clear(); enviosModificados.clear(); enviosEliminados.clear();
        clientesNuevos.clear(); clientesModificados.clear(); clientesEliminados.clear();
        centrosNuevos.clear(); centrosModificados.clear(); centrosEliminados.clear();
        pagosNuevos.clear(); pagosModificados.clear(); pagosEliminados.clear();
    }
}
