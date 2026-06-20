package com.logismart.observer;

import com.logismart.dominio.Envio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Patrón Observer - Notificación de observadores ante cambios de estado")
class ObservadorEnvioTest {

    private Envio envioBase() {
        return new Envio.EnvioBuilder("OBS1", "Buenos Aires", "Salta")
                .peso(4.0)
                .costo(120.0)
                .build();
    }

    @Test
    @DisplayName("El SistemaAuditoriaObservador registra una entrada al notificarse un cambio")
    void auditoriaRegistraAlNotificarse() {
        Envio envio = envioBase();
        SistemaAuditoriaObservador auditoria = new SistemaAuditoriaObservador();
        envio.adjuntarObservador(auditoria);

        assertEquals(0, auditoria.obtenerCantidadRegistros());

        envio.cambiarEstado("EN_TRANSITO");

        assertEquals(1, auditoria.obtenerCantidadRegistros());
    }

    @Test
    @DisplayName("El SistemaAuditoriaObservador acumula un registro por cada cambio")
    void auditoriaAcumulaUnRegistroPorCambio() {
        Envio envio = envioBase();
        SistemaAuditoriaObservador auditoria = new SistemaAuditoriaObservador();
        envio.adjuntarObservador(auditoria);

        envio.cambiarEstado("EN_TRANSITO");
        envio.cambiarEstado("ENTREGADO");

        assertEquals(2, auditoria.obtenerCantidadRegistros());
    }

    @Test
    @DisplayName("Varios observadores registrados reciben todos la misma notificación")
    void variosObservadoresReciben() {
        Envio envio = envioBase();
        // Estos observadores solo imprimen; verificamos que su presencia no rompe
        // la notificación y que el observador con estado observable sí la recibe.
        envio.adjuntarObservador(new DashboardObservador());
        envio.adjuntarObservador(new CentroDistribucionObservador());
        envio.adjuntarObservador(new SistemaNotificacionObservador());
        SistemaAuditoriaObservador auditoria = new SistemaAuditoriaObservador();
        envio.adjuntarObservador(auditoria);

        envio.cambiarEstado("EN_TRANSITO");

        assertEquals(1, auditoria.obtenerCantidadRegistros());
    }

    @Test
    @DisplayName("Un observador desadjuntado deja de recibir notificaciones")
    void observadorDesadjuntadoNoRecibe() {
        Envio envio = envioBase();
        SistemaAuditoriaObservador auditoria = new SistemaAuditoriaObservador();
        envio.adjuntarObservador(auditoria);

        envio.cambiarEstado("EN_TRANSITO"); // 1 registro
        envio.desadjuntarObservador(auditoria);
        envio.cambiarEstado("ENTREGADO");   // ya no se registra

        assertEquals(1, auditoria.obtenerCantidadRegistros());
    }

    @Test
    @DisplayName("El mismo observador no se registra dos veces (notificación única)")
    void observadorNoSeDuplica() {
        Envio envio = envioBase();
        SistemaAuditoriaObservador auditoria = new SistemaAuditoriaObservador();
        envio.adjuntarObservador(auditoria);
        envio.adjuntarObservador(auditoria); // intento duplicado

        envio.cambiarEstado("EN_TRANSITO");

        // Si estuviera duplicado habría 2 registros para un solo cambio.
        assertEquals(1, auditoria.obtenerCantidadRegistros());
    }
}
