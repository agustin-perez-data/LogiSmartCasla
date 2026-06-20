package com.logismart.interpreter;

import com.logismart.dominio.Envio;

public interface Expresion {
    boolean evaluar(Envio envio);
}
