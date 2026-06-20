package com.logismart.mediator;

import com.logismart.dominio.Envio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Patrón Mediator - Coordinación de colegas vía el mediador")
class MediadorEnviosTest {

    private MediadorEnviosConcreto mediador;
    private CentroDistribucion centro;
    private SistemaPago pago;
    private SistemaNotificacion notificador;
    private SistemaAuditoria auditoria;

    @BeforeEach
    void setUp() {
        mediador = new MediadorEnviosConcreto();
        centro = new CentroDistribucion(mediador);
        ComponenteValidador validador = new ComponenteValidador(mediador);
        pago = new SistemaPago(mediador);
        notificador = new SistemaNotificacion(mediador);
        auditoria = new SistemaAuditoria();

        mediador.registrarCentro(centro);
        mediador.registrarValidador(validador);
        mediador.registrarPago(pago);
        mediador.registrarNotificador(notificador);
        mediador.registrarAuditoria(auditoria);
    }

    private Envio envioValido() {
        return new Envio.EnvioBuilder("M1", "Buenos Aires", "Rosario")
                .peso(10.0)
                .costo(500.0)
                .metodoPago("TARJETA")
                .build();
    }

    @Test
    @DisplayName("El flujo completo desde ENVIO_CREADO deja el envío ENTREGADO coordinando a todos los colegas")
    void flujoCompletoCoordinaTodosLosColegas() {
        Envio envio = envioValido();

        // Dispara la cadena: creado -> validado -> pagado -> notificado -> registrado
        centro.crearEnvio(envio);

        // Efecto observable final: el centro lo registra y lo deja ENTREGADO.
        assertEquals("ENTREGADO", envio.obtenerEstado());
    }

    @Test
    @DisplayName("Cada paso del flujo genera un registro en la auditoría coordinada por el mediador")
    void cadaPasoSeAuditaPorElMediador() {
        Envio envio = envioValido();

        centro.crearEnvio(envio);

        // ENVIO_CREADO, VALIDACION_OK, PAGO_CONFIRMADO, NOTIFICACION_ENVIADA, ENVIO_REGISTRADO
        assertEquals(5, auditoria.obtenerCantidadLogs());
    }

    @Test
    @DisplayName("Un envío inválido detiene el flujo en la validación: no se completa la entrega")
    void envioInvalidoDetieneElFlujo() {
        // Sin costo ni peso => el validador corta el flujo.
        Envio invalido = new Envio.EnvioBuilder("M2", "A", "B").build();

        centro.crearEnvio(invalido);

        // No llega a EN_TRANSITO ni a ENTREGADO; queda en EN_PREPARACION.
        assertEquals("EN_PREPARACION", invalido.obtenerEstado());
        // Solo se auditaron ENVIO_CREADO (el resto del flujo no ocurrió).
        assertEquals(1, auditoria.obtenerCantidadLogs());
    }

    @Test
    @DisplayName("Un evento desconocido es manejado por el mediador delegando en la auditoría")
    void eventoDesconocidoSeAudita() {
        mediador.notificar("EVENTO_RARO", "datos");

        assertEquals(1, auditoria.obtenerCantidadLogs());
    }
}
