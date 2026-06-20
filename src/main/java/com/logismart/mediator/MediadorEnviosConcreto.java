package com.logismart.mediator;

import com.logismart.dominio.Envio;

public class MediadorEnviosConcreto implements MediadorEnvios {

    private CentroDistribucion centro;
    private ComponenteValidador validador;
    private SistemaPago pago;
    private SistemaNotificacion notificador;
    private SistemaAuditoria auditoria;

    @Override
    public void registrarCentro(CentroDistribucion centro) {
        this.centro = centro;
    }

    @Override
    public void registrarValidador(ComponenteValidador validador) {
        this.validador = validador;
    }

    @Override
    public void registrarPago(SistemaPago pago) {
        this.pago = pago;
    }

    @Override
    public void registrarNotificador(SistemaNotificacion notificador) {
        this.notificador = notificador;
    }

    @Override
    public void registrarAuditoria(SistemaAuditoria auditoria) {
        this.auditoria = auditoria;
    }

    @Override
    public void notificar(String evento, Object datos) {
        switch (evento) {
            case "ENVIO_CREADO" -> manejarEnvioCreado((Envio) datos);
            case "VALIDACION_OK" -> manejarValidacionOk((Envio) datos);
            case "PAGO_CONFIRMADO" -> manejarPagoConfirmado((Envio) datos);
            case "NOTIFICACION_ENVIADA" -> manejarNotificacionEnviada((Envio) datos);
            case "ENVIO_REGISTRADO" -> registrarAuditoria("ENVIO_REGISTRADO", datos);
            default -> registrarAuditoria("EVENTO_DESCONOCIDO:" + evento, datos);
        }
    }

    private void manejarEnvioCreado(Envio envio) {
        System.out.println("[Mediator] Envio creado: " + envio.getId());
        registrarAuditoria("ENVIO_CREADO", envio);
        envio.cambiarEstado("EN_PREPARACION");
        if (validador != null) {
            validador.validar(envio);
        }
    }

    private void manejarValidacionOk(Envio envio) {
        System.out.println("[Mediator] Validacion OK");
        registrarAuditoria("VALIDACION_OK", envio);
        if (pago != null) {
            pago.procesarPago(envio);
        }
    }

    private void manejarPagoConfirmado(Envio envio) {
        System.out.println("[Mediator] Pago confirmado");
        registrarAuditoria("PAGO_CONFIRMADO", envio);
        envio.cambiarEstado("EN_TRANSITO");
        if (notificador != null) {
            notificador.enviarConfirmacion(envio);
        }
    }

    private void manejarNotificacionEnviada(Envio envio) {
        System.out.println("[Mediator] Notificacion enviada");
        registrarAuditoria("NOTIFICACION_ENVIADA", envio);
        if (centro != null) {
            centro.registrarEnvio(envio);
        }
    }

    private void registrarAuditoria(String evento, Object datos) {
        if (auditoria != null) {
            auditoria.registrar(evento, datos);
        }
    }
}
