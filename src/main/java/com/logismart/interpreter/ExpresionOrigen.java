package com.logismart.interpreter;

import com.logismart.dominio.Envio;

public class ExpresionOrigen implements Expresion {

    private final String valor;

    public ExpresionOrigen(String valor) {
        this.valor = valor;
    }

    @Override
    public boolean evaluar(Envio envio) {
        return valor.equals(envio.getOrigen());
    }
}
