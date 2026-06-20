package com.logismart.interpreter;

import com.logismart.dominio.Envio;

public class ExpresionDestino implements Expresion {

    private final String valor;

    public ExpresionDestino(String valor) {
        this.valor = valor;
    }

    @Override
    public boolean evaluar(Envio envio) {
        return valor.equals(envio.getDestino());
    }
}
