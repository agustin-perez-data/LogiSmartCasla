package com.logismart.singleton;

import java.util.ArrayDeque;
import java.util.Queue;

public class PoolDeConexiones {

    private static volatile PoolDeConexiones instancia;

    private final Queue<String> conexionesDisponibles = new ArrayDeque<>();
    private final int maxConexiones;

    private PoolDeConexiones() {
        maxConexiones = ConfiguracionLogiSmart.obtenerInstancia().getMaxEnviosPorRuta();
        for (int i = 1; i <= maxConexiones; i++) {
            conexionesDisponibles.add("conexion-" + i);
        }
    }

    public static PoolDeConexiones obtenerInstancia() {
        if (instancia == null) {
            synchronized (PoolDeConexiones.class) {
                if (instancia == null) {
                    instancia = new PoolDeConexiones();
                }
            }
        }
        return instancia;
    }

    public synchronized String obtenerConexion() {
        if (conexionesDisponibles.isEmpty()) {
            throw new IllegalStateException("No hay conexiones disponibles en el pool");
        }
        return conexionesDisponibles.poll();
    }

    public synchronized void liberarConexion(String conexion) {
        if (conexion != null) {
            conexionesDisponibles.offer(conexion);
        }
    }

    public int getConexionesDisponibles() {
        return conexionesDisponibles.size();
    }

    public int getMaxConexiones() {
        return maxConexiones;
    }
}
