package com.logismart.dominio;

public enum EstadoRuta {
    PLANIFICACION,
    EN_CURSO,
    COMPLETADA,
    CANCELADA;

    public boolean puedeTransicionarA(EstadoRuta nuevo) {
        return switch (this) {
            case PLANIFICACION -> nuevo == EN_CURSO    || nuevo == CANCELADA;
            case EN_CURSO      -> nuevo == COMPLETADA  || nuevo == CANCELADA;
            default            -> false;
        };
    }
}
