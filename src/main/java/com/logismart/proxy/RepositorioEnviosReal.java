package com.logismart.proxy;

import java.util.ArrayList;
import java.util.List;

public class RepositorioEnviosReal implements RepositorioEnvios {
    private List<EnvioSimple> envios = new ArrayList<>();

    public RepositorioEnviosReal() {
        System.out.println("[BD Real] Conectando a servidor remoto...");
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
        System.out.println("[BD Real] Conexión establecida");
        inicializarDatos();
    }

    private void inicializarDatos() {
        envios.add(new EnvioSimple("ENV-001", "Buenos Aires", "Córdoba", 5.0, "EN TRÁNSITO"));
        envios.add(new EnvioSimple("ENV-002", "Buenos Aires", "Rosario", 10.0, "EN TRÁNSITO"));
        envios.add(new EnvioSimple("ENV-003", "Córdoba", "Mendoza", 15.0, "PENDIENTE"));
        envios.add(new EnvioSimple("ENV-004", "Rosario", "Tucumán", 20.0, "ENTREGADO"));
        envios.add(new EnvioSimple("ENV-005", "Buenos Aires", "Salta", 25.0, "PENDIENTE"));
        System.out.println("[BD Real] Datos inicializados: " + envios.size() + " envíos");
    }

    @Override
    public EnvioSimple obtenerEnvio(String id) {
        System.out.println("[BD Real] Consultando envío: " + id);
        try { Thread.sleep(500); } catch (InterruptedException e) {}
        return envios.stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public List<EnvioSimple> obtenerEnvios() {
        System.out.println("[BD Real] Consultando todos los envíos");
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
        return new ArrayList<>(envios);
    }

    @Override
    public void guardarEnvio(EnvioSimple envio) {
        System.out.println("[BD Real] Guardando envío: " + envio.getId());
        try { Thread.sleep(500); } catch (InterruptedException e) {}
        envios.add(envio);
    }

    @Override
    public void eliminarEnvio(String id) {
        System.out.println("[BD Real] Eliminando envío: " + id);
        try { Thread.sleep(500); } catch (InterruptedException e) {}
        envios.removeIf(e -> e.getId().equals(id));
    }
}
