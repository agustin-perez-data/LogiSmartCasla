package com.logismart.dominio;

public enum EstadoEnvio {
    CONFIRMADO,
    EN_PREPARACION,
    EN_TRANSITO,
    EN_REPARTO,
    PENDIENTE,
    EN_CAMINO,
    ENTREGADO,
    CANCELADO,
    RETENIDO,
    DEVUELTO,
    ERROR;

    public boolean puedeTransicionarA(EstadoEnvio nuevo) {
        return switch (this) {
            case PENDIENTE -> nuevo == CONFIRMADO || nuevo == EN_CAMINO
                    || nuevo == ENTREGADO || nuevo == CANCELADO;
            case CONFIRMADO -> nuevo == EN_PREPARACION || nuevo == EN_TRANSITO
                    || nuevo == EN_REPARTO || nuevo == EN_CAMINO || nuevo == ENTREGADO
                    || nuevo == CANCELADO || nuevo == ERROR;
            case EN_PREPARACION -> nuevo == EN_TRANSITO || nuevo == EN_CAMINO
                    || nuevo == CANCELADO || nuevo == DEVUELTO || nuevo == ERROR;
            case EN_TRANSITO, EN_CAMINO -> nuevo == EN_REPARTO || nuevo == ENTREGADO
                    || nuevo == CANCELADO || nuevo == RETENIDO || nuevo == DEVUELTO || nuevo == ERROR;
            case EN_REPARTO -> nuevo == ENTREGADO || nuevo == EN_TRANSITO
                    || nuevo == CANCELADO || nuevo == DEVUELTO || nuevo == ERROR;
            case RETENIDO -> nuevo == EN_REPARTO || nuevo == EN_TRANSITO
                    || nuevo == CANCELADO || nuevo == DEVUELTO || nuevo == ERROR;
            case ERROR -> nuevo == EN_PREPARACION || nuevo == EN_TRANSITO || nuevo == EN_REPARTO
                    || nuevo == EN_CAMINO || nuevo == CANCELADO || nuevo == DEVUELTO;
            default -> false;
        };
    }
}
