package com.logismart.singleton;

import java.util.HashMap;
import java.util.Map;

public class CacheLogiSmart {

    private static volatile CacheLogiSmart instancia;

    private final Map<String, Object> cache = new HashMap<>();

    private CacheLogiSmart() {}

    public static CacheLogiSmart obtenerInstancia() {
        if (instancia == null) {
            synchronized (CacheLogiSmart.class) {
                if (instancia == null) {
                    instancia = new CacheLogiSmart();
                }
            }
        }
        return instancia;
    }

    public synchronized void put(String clave, Object valor) {
        cache.put(clave, valor);
    }

    public synchronized Object get(String clave) {
        return cache.get(clave);
    }

    public synchronized void invalidar(String clave) {
        cache.remove(clave);
    }

    public synchronized void limpiar() {
        cache.clear();
    }

    public synchronized boolean contiene(String clave) {
        return cache.containsKey(clave);
    }
}
