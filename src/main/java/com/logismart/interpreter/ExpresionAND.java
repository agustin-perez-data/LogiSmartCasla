package com.logismart.interpreter;

import com.logismart.dominio.Envio;

public class ExpresionAND implements Expresion {

    private final Expresion izquierda;
    private final Expresion derecha;

    public ExpresionAND(Expresion izquierda, Expresion derecha) {
        this.izquierda = izquierda;
        this.derecha = derecha;
    }

    @Override
    public boolean evaluar(Envio envio) {
        return izquierda.evaluar(envio) && derecha.evaluar(envio);
    }
}
