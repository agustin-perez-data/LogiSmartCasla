package com.logismart.interpreter;

import com.logismart.dominio.Envio;

public class ExpresionNOT implements Expresion {

    private final Expresion expresion;

    public ExpresionNOT(Expresion expresion) {
        this.expresion = expresion;
    }

    @Override
    public boolean evaluar(Envio envio) {
        return !expresion.evaluar(envio);
    }
}
