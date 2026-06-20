package com.logismart.proxy;

import java.util.List;

public class ProxyDemo {
    public static void main(String[] args) {

        System.out.println("========== CASO 1: Crear proxy (BD no conectada) ==========");
        ProxyRepositorioEnvios repo = new ProxyRepositorioEnvios();
        System.out.println("Proxy creado. La BD real NO se conectó aún.\n");

        System.out.println("========== CASO 2: Primera consulta (lazy loading) ==========");
        long inicio = System.currentTimeMillis();
        EnvioSimple envio1 = repo.obtenerEnvio("ENV-001");
        long duracion = System.currentTimeMillis() - inicio;
        System.out.println("Envío obtenido: " + envio1);
        System.out.println("Duración: " + duracion + "ms\n");

        System.out.println("========== CASO 3: Segunda consulta (caché) ==========");
        inicio = System.currentTimeMillis();
        EnvioSimple envio1bis = repo.obtenerEnvio("ENV-001");
        duracion = System.currentTimeMillis() - inicio;
        System.out.println("Envío obtenido: " + envio1bis);
        System.out.println("Duración: " + duracion + "ms (desde caché)\n");

        System.out.println("========== CASO 4: Obtener todos (primera vez) ==========");
        inicio = System.currentTimeMillis();
        List<EnvioSimple> envios = repo.obtenerEnvios();
        duracion = System.currentTimeMillis() - inicio;
        System.out.println("Envíos obtenidos: " + envios.size());
        System.out.println("Duración: " + duracion + "ms\n");

        System.out.println("========== CASO 5: Obtener todos (caché) ==========");
        inicio = System.currentTimeMillis();
        List<EnvioSimple> envios2 = repo.obtenerEnvios();
        duracion = System.currentTimeMillis() - inicio;
        System.out.println("Envíos obtenidos: " + envios2.size());
        System.out.println("Duración: " + duracion + "ms (desde caché)\n");

        System.out.println("========== CASO 6: Guardar envío (invalida caché) ==========");
        EnvioSimple nuevoEnvio = new EnvioSimple("ENV-006", "Rosario", "Mendoza", 3.0, "NUEVO");
        repo.guardarEnvio(nuevoEnvio);
        System.out.println("\nObteniendo todos después de guardar:");
        inicio = System.currentTimeMillis();
        List<EnvioSimple> envios3 = repo.obtenerEnvios();
        duracion = System.currentTimeMillis() - inicio;
        System.out.println("Envíos: " + envios3.size() + " | Duración: " + duracion + "ms");

        repo.mostrarEstadisticas();
    }
}
