package com.logismart.iterator;

import com.logismart.dominio.Envio;

public class ColeccionArray implements ColeccionEnvios {

    private final Envio[] envios = new Envio[1000];
    private int tamano = 0;

    @Override
    public IteradorEnvios crearIterador() {
        return new IteradorArray();
    }

    @Override
    public void agregar(Envio envio) {
        if (tamano >= envios.length) {
            throw new IllegalStateException("La coleccion alcanzo su capacidad maxima");
        }
        envios[tamano++] = envio;
    }

    @Override
    public void remover(Envio envio) {
        for (int i = 0; i < tamano; i++) {
            if (envios[i].getId().equals(envio.getId())) {
                System.arraycopy(envios, i + 1, envios, i, tamano - i - 1);
                envios[--tamano] = null;
                return;
            }
        }
    }

    @Override
    public int obtenerTamano() {
        return tamano;
    }

    private class IteradorArray implements IteradorEnvios {
        private int indice = 0;

        @Override
        public boolean tieneSiguiente() {
            return indice < tamano;
        }

        @Override
        public Envio obtenerSiguiente() {
            return envios[indice++];
        }

        @Override
        public void reiniciar() {
            indice = 0;
        }
    }
}
