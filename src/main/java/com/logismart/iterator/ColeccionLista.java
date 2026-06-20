package com.logismart.iterator;

import com.logismart.dominio.Envio;

public class ColeccionLista implements ColeccionEnvios {

    private Nodo cabeza;
    private int tamano = 0;

    @Override
    public IteradorEnvios crearIterador() {
        return new IteradorLista();
    }

    @Override
    public void agregar(Envio envio) {
        Nodo nuevoNodo = new Nodo(envio);
        if (cabeza == null) {
            cabeza = nuevoNodo;
        } else {
            Nodo actual = cabeza;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = nuevoNodo;
        }
        tamano++;
    }

    @Override
    public void remover(Envio envio) {
        if (cabeza != null && cabeza.envio.getId().equals(envio.getId())) {
            cabeza = cabeza.siguiente;
            tamano--;
            return;
        }

        Nodo actual = cabeza;
        while (actual != null && actual.siguiente != null) {
            if (actual.siguiente.envio.getId().equals(envio.getId())) {
                actual.siguiente = actual.siguiente.siguiente;
                tamano--;
                return;
            }
            actual = actual.siguiente;
        }
    }

    @Override
    public int obtenerTamano() {
        return tamano;
    }

    private class IteradorLista implements IteradorEnvios {
        private Nodo actual = cabeza;

        @Override
        public boolean tieneSiguiente() {
            return actual != null;
        }

        @Override
        public Envio obtenerSiguiente() {
            Envio envio = actual.envio;
            actual = actual.siguiente;
            return envio;
        }

        @Override
        public void reiniciar() {
            actual = cabeza;
        }
    }

    private static class Nodo {
        private final Envio envio;
        private Nodo siguiente;

        private Nodo(Envio envio) {
            this.envio = envio;
        }
    }
}
