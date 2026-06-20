package com.logismart.mediator;

public interface MediadorEnvios {
    void registrarCentro(CentroDistribucion centro);
    void registrarValidador(ComponenteValidador validador);
    void registrarPago(SistemaPago pago);
    void registrarNotificador(SistemaNotificacion notificador);
    void registrarAuditoria(SistemaAuditoria auditoria);
    void notificar(String evento, Object datos);
}
