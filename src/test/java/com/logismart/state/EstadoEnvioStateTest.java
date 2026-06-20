package com.logismart.state;

import com.logismart.dominio.Envio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Patron State: transiciones de estado del Envio")
class EstadoEnvioStateTest {

    private Envio nuevoEnvioConfirmado() {
        // Por defecto el builder deja el envio en estado CONFIRMADO.
        return new Envio.EnvioBuilder("ENV-1", "Buenos Aires", "Cordoba")
                .peso(10.0)
                .build();
    }

    @Test
    @DisplayName("Desde CONFIRMADO, validar() transiciona a EN_TRANSITO")
    void validarDesdeConfirmadoVaAEnTransito() {
        Envio envio = nuevoEnvioConfirmado();
        assertEquals("CONFIRMADO", envio.obtenerEstado(), "Debe arrancar CONFIRMADO");

        envio.validar();

        assertEquals("EN_TRANSITO", envio.obtenerEstado(),
                "Tras validar un envio confirmado debe quedar EN_TRANSITO");
    }

    @Test
    @DisplayName("Flujo feliz completo: CONFIRMADO -> EN_TRANSITO -> EN_REPARTO -> ENTREGADO")
    void flujoFelizHastaEntregado() {
        Envio envio = nuevoEnvioConfirmado();

        envio.validar();   // CONFIRMADO -> EN_TRANSITO
        assertEquals("EN_TRANSITO", envio.obtenerEstado());

        envio.entregar();  // EN_TRANSITO -> EN_REPARTO
        assertEquals("EN_REPARTO", envio.obtenerEstado());

        envio.entregar();  // EN_REPARTO -> ENTREGADO
        assertEquals("ENTREGADO", envio.obtenerEstado());
    }

    @Test
    @DisplayName("Transicion invalida: entregar() en CONFIRMADO no cambia el estado")
    void entregarEnConfirmadoNoCambiaEstado() {
        Envio envio = nuevoEnvioConfirmado();

        envio.entregar(); // no permitido desde CONFIRMADO

        assertEquals("CONFIRMADO", envio.obtenerEstado(),
                "Una transicion invalida debe dejar el estado intacto");
    }

    @Test
    @DisplayName("EN_TRANSITO puede retenerse y luego liberarse a EN_REPARTO")
    void retenerYLiberarDesdeEnTransito() {
        Envio envio = nuevoEnvioConfirmado();
        envio.validar(); // -> EN_TRANSITO

        envio.retener(); // EN_TRANSITO -> RETENIDO
        assertEquals("RETENIDO", envio.obtenerEstado());

        envio.entregar(); // RETENIDO libera -> EN_REPARTO
        assertEquals("EN_REPARTO", envio.obtenerEstado());
    }

    @Test
    @DisplayName("CANCELADO es terminal: ninguna accion lo modifica")
    void canceladoEsTerminal() {
        Envio envio = nuevoEnvioConfirmado();

        envio.cancelar(); // CONFIRMADO -> CANCELADO
        assertEquals("CANCELADO", envio.obtenerEstado());

        envio.validar();
        envio.entregar();
        envio.retener();
        envio.devolver();

        assertEquals("CANCELADO", envio.obtenerEstado(),
                "Un envio cancelado debe permanecer CANCELADO");
    }
}
