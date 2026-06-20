package com.logismart.memento;

import com.logismart.dominio.Envio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Patrón Memento - Guardar y restaurar el estado de un envío")
class MementoEnvioTest {

    private static final double DELTA = 0.0001;

    private Envio envioBase() {
        return new Envio.EnvioBuilder("MEM1", "Buenos Aires", "Mendoza")
                .peso(8.0)
                .costo(300.0)
                .estado(com.logismart.dominio.EstadoEnvio.CONFIRMADO)
                .build();
    }

    @Test
    @DisplayName("El memento captura una instantánea fiel del estado actual del envío")
    void mementoCapturaEstadoActual() {
        Envio envio = envioBase();

        MementoEnvio memento = envio.crearMemento();

        assertEquals("CONFIRMADO", memento.obtenerEstado());
        assertEquals("Buenos Aires", memento.obtenerOrigen());
        assertEquals("Mendoza", memento.obtenerDestino());
        assertEquals(8.0, memento.obtenerPeso(), DELTA);
        assertEquals(300.0, memento.obtenerCosto(), DELTA);
    }

    @Test
    @DisplayName("Modificar el envío y restaurar desde el memento vuelve al estado previo")
    void restaurarDesdeMementoVuelveAlEstadoPrevio() {
        Envio envio = envioBase();
        MementoEnvio snapshot = envio.crearMemento();

        // Modificamos el envío después de tomar la instantánea.
        envio.cambiarEstado("EN_TRANSITO");
        envio.setCosto(999.0);

        assertEquals("EN_TRANSITO", envio.obtenerEstado());
        assertEquals(999.0, envio.getCosto(), DELTA);

        // Restauramos: debe volver exactamente al estado guardado.
        envio.restaurarDesdeMemento(snapshot);

        assertEquals("CONFIRMADO", envio.obtenerEstado());
        assertEquals(300.0, envio.getCosto(), DELTA);
    }

    @Test
    @DisplayName("El HistorialEnvios (caretaker) permite deshacer hacia el estado anterior")
    void historialPermiteDeshacer() {
        Envio envio = envioBase();
        HistorialEnvios historial = new HistorialEnvios();

        historial.guardarEstado(envio);          // #0 CONFIRMADO, costo 300
        envio.cambiarEstado("EN_TRANSITO");
        envio.setCosto(450.0);
        historial.guardarEstado(envio);          // #1 EN_TRANSITO, costo 450

        assertEquals(2, historial.obtenerTamano());

        boolean retrocedio = historial.irAlEstadoAnterior(envio);

        assertTrue(retrocedio);
        assertEquals("CONFIRMADO", envio.obtenerEstado());
        assertEquals(300.0, envio.getCosto(), DELTA);
    }

    @Test
    @DisplayName("No se puede retroceder más allá del primer estado guardado")
    void noSePuedeRetrocederMasAllaDelInicio() {
        Envio envio = envioBase();
        HistorialEnvios historial = new HistorialEnvios();
        historial.guardarEstado(envio); // único estado

        boolean retrocedio = historial.irAlEstadoAnterior(envio);

        assertFalse(retrocedio);
        assertEquals("CONFIRMADO", envio.obtenerEstado());
    }
}
