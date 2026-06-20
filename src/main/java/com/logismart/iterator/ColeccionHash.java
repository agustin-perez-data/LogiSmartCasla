package com.logismart.iterator;

import com.logismart.dominio.Envio;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ColeccionHash implements ColeccionEnvios {

    private final Map<String, Envio> envios = new HashMap<>();

    @Override
    public IteradorEnvios crearIterador() {
        return new IteradorHash();
    }

    @Override
    public void agregar(Envio envio) {
        envios.put(envio.getId(), envio);
    }

    @Override
    public void remover(Envio envio) {
        envios.remove(envio.getId());
    }

    @Override
    public int obtenerTamano() {
        return envios.size();
    }

    private class IteradorHash implements IteradorEnvios {
        private Iterator<Envio> iterador = envios.values().iterator();

        @Override
        public boolean tieneSiguiente() {
            return iterador.hasNext();
        }

        @Override
        public Envio obtenerSiguiente() {
            return iterador.next();
        }

        @Override
        public void reiniciar() {
            iterador = envios.values().iterator();
        }
    }
}
