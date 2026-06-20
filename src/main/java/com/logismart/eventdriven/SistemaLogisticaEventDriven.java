package com.logismart.eventdriven;

import com.logismart.dominio.Envio;
import com.logismart.iterator.ColeccionArray;
import com.logismart.iterator.ColeccionEnvios;
import com.logismart.iterator.IteradorEnvios;
import com.logismart.mediator.CentroDistribucion;
import com.logismart.mediator.ComponenteValidador;
import com.logismart.mediator.MediadorEnvios;
import com.logismart.mediator.MediadorEnviosConcreto;
import com.logismart.mediator.SistemaAuditoria;
import com.logismart.mediator.SistemaNotificacion;
import com.logismart.mediator.SistemaPago;
import com.logismart.memento.HistorialEnvios;
import com.logismart.observer.CentroDistribucionObservador;
import com.logismart.observer.DashboardObservador;
import com.logismart.observer.SistemaAuditoriaObservador;
import com.logismart.observer.SistemaNotificacionObservador;

public class SistemaLogisticaEventDriven {

    private final ColeccionEnvios coleccion = new ColeccionArray();

    private final MediadorEnvios mediador;
    private final CentroDistribucion centro;
    private final SistemaAuditoria auditoriaMediator;

    private final HistorialEnvios historial = new HistorialEnvios();

    private final CentroDistribucionObservador centroObservador;
    private final SistemaNotificacionObservador notificacionObservador;
    private final SistemaAuditoriaObservador auditoriaObservador;
    private final DashboardObservador dashboard;

    public SistemaLogisticaEventDriven() {
        mediador = new MediadorEnviosConcreto();
        centro = new CentroDistribucion(mediador);
        ComponenteValidador validador = new ComponenteValidador(mediador);
        SistemaPago pago = new SistemaPago(mediador);
        SistemaNotificacion notificador = new SistemaNotificacion(mediador);
        auditoriaMediator = new SistemaAuditoria();

        mediador.registrarCentro(centro);
        mediador.registrarValidador(validador);
        mediador.registrarPago(pago);
        mediador.registrarNotificador(notificador);
        mediador.registrarAuditoria(auditoriaMediator);

        centroObservador = new CentroDistribucionObservador();
        notificacionObservador = new SistemaNotificacionObservador();
        auditoriaObservador = new SistemaAuditoriaObservador();
        dashboard = new DashboardObservador();
    }

    public void encolar(Envio envio) {
        envio.adjuntarObservador(centroObservador);
        envio.adjuntarObservador(notificacionObservador);
        envio.adjuntarObservador(auditoriaObservador);
        envio.adjuntarObservador(dashboard);
        coleccion.agregar(envio);
        System.out.println("[Sistema] Envio " + envio.getId() + " encolado");
    }

    public void procesarCola() {
        System.out.println("\n=== Procesando cola de envios ===");
        IteradorEnvios iterador = coleccion.crearIterador();

        while (iterador.tieneSiguiente()) {
            Envio envio = iterador.obtenerSiguiente();
            System.out.println("\n--- Procesando " + envio.getId() + " ---");
            historial.guardarEstado(envio);
            centro.crearEnvio(envio);
            historial.guardarEstado(envio);
        }

        System.out.println("\n[Sistema] Cola procesada. Total envios: " + coleccion.obtenerTamano());
    }

    public void simularErrorYRestaurar(Envio envio) {
        System.out.println("\n--- Simulando error en " + envio.getId() + " ---");
        System.out.println("Estado antes del error: " + envio.obtenerEstado());
        envio.cambiarEstado("ERROR");
        historial.guardarEstado(envio);

        System.out.println("\n--- Restaurando estado anterior ---");
        historial.irAlEstadoAnterior(envio);
        System.out.println("Estado restaurado: " + envio.obtenerEstado());
    }

    public void mostrarHistorial() {
        historial.mostrarHistorial();
    }

    public void mostrarDashboard() {
        dashboard.mostrarDashboard();
    }

    public void mostrarAuditoria() {
        auditoriaObservador.mostrarRegistros();
        System.out.println("Total eventos Observer: " + auditoriaObservador.obtenerCantidadRegistros());
        auditoriaMediator.mostrarLogs();
        System.out.println("Total eventos Mediator: " + auditoriaMediator.obtenerCantidadLogs());
    }
}
