package com.logismart.datos;

import com.logismart.datos.dominio.*;
import com.logismart.datos.lazy.*;
import com.logismart.datos.repositorio.*;
import com.logismart.datos.servicio.*;
import com.logismart.datos.uow.UnitOfWork;
import java.util.List;

public class DatosDemo {

    public static void main(String[] args) {
        System.out.println("=== Hito 13: Patrones de Acceso a Datos ===\n");

        RepositorioEnvioMemoria repoEnvio = new RepositorioEnvioMemoria();
        RepositorioClienteMemoria repoCliente = new RepositorioClienteMemoria();
        RepositorioCentroMemoria repoCentro = new RepositorioCentroMemoria();
        RepositorioPagoMemoria repoPago = new RepositorioPagoMemoria();

        UnitOfWork uow = new UnitOfWork(null, repoEnvio, repoCliente, repoCentro, repoPago);

        ServicioEnvios svcEnvios = new ServicioEnvios(repoEnvio, uow);
        ServicioClientes svcClientes = new ServicioClientes(repoCliente, uow);
        ServicioCentros svcCentros = new ServicioCentros(repoCentro, uow);
        ServicioPagos svcPagos = new ServicioPagos(repoPago, uow);
        LogisticaFacade facade = new LogisticaFacade(svcEnvios, svcClientes, svcCentros, svcPagos);

        // --- Actividad 1: Data Mapper (simulado con repositorios en memoria) ---
        System.out.println("--- Actividad 1: Data Mapper ---");
        Envio e1 = new Envio(1, "Buenos Aires", "Cordoba", 5.0);
        repoEnvio.guardar(e1);
        Envio e1r = repoEnvio.obtener(1);
        System.out.println("Recuperado: " + e1r);

        Cliente c1 = new Cliente(1, "Juan Perez", "juan@mail.com", "1234567890");
        repoCliente.guardar(c1);

        CentroDistribucion cd1 = new CentroDistribucion(1, "Centro BA", "Buenos Aires", 1000);
        repoCentro.guardar(cd1);

        Pago p1 = new Pago(1, 1, 500.0);
        repoPago.guardar(p1);

        // --- Actividad 2: Repository ---
        System.out.println("\n--- Actividad 2: Repository ---");
        Envio e2 = new Envio(2, "Mendoza", "Buenos Aires", 3.0);
        repoEnvio.guardar(e2);
        List<Envio> todos = repoEnvio.obtenerTodos();
        System.out.println("Total envios: " + todos.size());
        List<Envio> confirmados = repoEnvio.buscarPorEstado(EstadoEnvio.CONFIRMADO);
        System.out.println("Envios CONFIRMADO: " + confirmados.size());
        Cliente porEmail = repoCliente.buscarPorEmail("juan@mail.com");
        System.out.println("Cliente por email: " + porEmail);

        // --- Actividad 3: Unit of Work ---
        System.out.println("\n--- Actividad 3: Unit of Work ---");
        Envio e3 = new Envio(3, "Rosario", "Santa Fe", 2.0);
        Cliente c3 = new Cliente(3, "Maria Lopez", "maria@mail.com", "9876543210");
        Pago p3 = new Pago(3, 3, 200.0);
        uow.registrarNuevoEnvio(e3);
        uow.registrarNuevoCliente(c3);
        uow.registrarNuevoPago(p3);
        uow.commit();

        e1.setEstado(EstadoEnvio.EN_TRANSITO);
        uow.registrarModificadoEnvio(e1);
        uow.commit();

        uow.registrarNuevoEnvio(new Envio(99, "test", "test", 1.0));
        uow.rollback();

        Envio e4 = new Envio(4, "Tucuman", "Salta", 8.0);
        Cliente c4 = new Cliente(4, "Carlos Silva", "carlos@mail.com", "5555555555");
        CentroDistribucion cd4 = new CentroDistribucion(4, "Centro NOA", "Tucuman", 500);
        Pago p4 = new Pago(4, 4, 350.0);
        uow.registrarNuevoEnvio(e4);
        uow.registrarNuevoCliente(c4);
        uow.registrarNuevoCentro(cd4);
        uow.registrarNuevoPago(p4);
        uow.commit();

        // --- Actividad 4: Lazy Load ---
        System.out.println("\n--- Actividad 4: Lazy Load ---");
        ClienteLazyProxy lazyCliente = new ClienteLazyProxy(1, repoCliente);
        System.out.println("Proxy creado, cargado=" + lazyCliente.isCargado());
        String nombre = lazyCliente.getNombre();
        System.out.println("Nombre: " + nombre + ", cargado=" + lazyCliente.isCargado());
        String email = lazyCliente.getEmail();
        System.out.println("Email: " + email);

        CentroDistribucionLazyProxy lazyCentro = new CentroDistribucionLazyProxy(1, repoCentro);
        System.out.println("Centro: " + lazyCentro.getNombre() + " en " + lazyCentro.getCiudad());

        HistorialEnviosLazyProxy historial = new HistorialEnviosLazyProxy(1, repoEnvio);
        System.out.println("Historial cargado=" + historial.isCargado());
        System.out.println("Total envios en historial: " + historial.contar());

        // --- Actividad 5: Arquitectura Completa (Facade) ---
        System.out.println("\n--- Actividad 5: Arquitectura Completa ---");
        svcEnvios.crearEnvio(5, "Cordoba", "Mendoza", 4.0);
        svcClientes.crearCliente(5, "Ana Torres", "ana@mail.com", "4444444444");
        svcPagos.procesarPago(5, 5, 600.0);

        facade.procesarEnvioCompleto(
            6, "Buenos Aires", "Bariloche", 10.0,
            6, "Pedro Garcia", "pedro@mail.com", "3333333333",
            6, 800.0
        );

        svcEnvios.actualizarEstado(5, EstadoEnvio.EN_TRANSITO);

        System.out.println("\nEnvio consultado por facade: " + facade.consultarEnvio(5));
        System.out.println("Cliente consultado por facade: " + facade.consultarCliente(5));

        System.out.println("\n=== Demo completado ===");
    }
}
