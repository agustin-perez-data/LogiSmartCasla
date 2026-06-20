package com.logismart.proxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProxyRepositorioEnvios implements RepositorioEnvios {
    private RepositorioEnviosReal repositorioReal;
    private Map<String, EnvioSimple> cache = new HashMap<>();
    private List<EnvioSimple> cacheEnvios;
    private boolean cacheEnviosValido = false;
    private int cacheHits = 0;
    private int cacheMisses = 0;

    private RepositorioEnviosReal obtenerRepositorio() {
        if (repositorioReal == null) {
            System.out.println("[Proxy] Inicializando repositorio real (lazy loading)...");
            repositorioReal = new RepositorioEnviosReal();
        }
        return repositorioReal;
    }

    @Override
    public EnvioSimple obtenerEnvio(String id) {
        System.out.println("[Proxy] Buscando envío: " + id);
        if (cache.containsKey(id)) {
            cacheHits++;
            System.out.println("[Proxy] ✓ Cache HIT");
            return cache.get(id);
        }
        cacheMisses++;
        System.out.println("[Proxy] ✗ Cache MISS — consultando BD real");
        EnvioSimple envio = obtenerRepositorio().obtenerEnvio(id);
        if (envio != null) {
            cache.put(id, envio);
        }
        return envio;
    }

    @Override
    public List<EnvioSimple> obtenerEnvios() {
        System.out.println("[Proxy] Obteniendo todos los envíos");
        if (cacheEnviosValido) {
            cacheHits++;
            System.out.println("[Proxy] ✓ Cache HIT — usando lista cacheada");
            return new ArrayList<>(cacheEnvios);
        }
        cacheMisses++;
        System.out.println("[Proxy] ✗ Cache MISS — consultando BD real");
        cacheEnvios = obtenerRepositorio().obtenerEnvios();
        cacheEnviosValido = true;
        for (EnvioSimple envio : cacheEnvios) {
            cache.put(envio.getId(), envio);
        }
        return new ArrayList<>(cacheEnvios);
    }

    @Override
    public void guardarEnvio(EnvioSimple envio) {
        System.out.println("[Proxy] Guardando envío: " + envio.getId());
        obtenerRepositorio().guardarEnvio(envio);
        cache.put(envio.getId(), envio);
        cacheEnviosValido = false;
        System.out.println("[Proxy] Caché de lista invalidado");
    }

    @Override
    public void eliminarEnvio(String id) {
        System.out.println("[Proxy] Eliminando envío: " + id);
        obtenerRepositorio().eliminarEnvio(id);
        cache.remove(id);
        cacheEnviosValido = false;
        System.out.println("[Proxy] Envío removido del caché");
    }

    public void mostrarEstadisticas() {
        int total = cacheHits + cacheMisses;
        double tasaHit = total > 0 ? (double) cacheHits / total * 100 : 0;
        System.out.println("\n=== Estadísticas del Proxy ===");
        System.out.println("Cache hits:   " + cacheHits);
        System.out.println("Cache misses: " + cacheMisses);
        System.out.println("Total:        " + total);
        System.out.println("Tasa de hit:  " + String.format("%.1f", tasaHit) + "%");
        System.out.println("Entradas en caché: " + cache.size());
        System.out.println("Caché lista válido: " + cacheEnviosValido);
        System.out.println("BD real inicializada: " + (repositorioReal != null));
    }
}
