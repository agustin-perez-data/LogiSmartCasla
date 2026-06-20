package com.logismart.observer;

import com.logismart.dominio.Envio;

import java.util.HashMap;
import java.util.Map;

public class DashboardObservador implements ObservadorEnvio {

    private final Map<String, String> estadosActuales = new HashMap<>();

    @Override
    public void actualizar(Envio envio, String evento) {
        estadosActuales.put(envio.getId(), evento);
        System.out.println("[Dashboard] " + envio.getId() + " = " + evento);
    }

    public void mostrarDashboard() {
        System.out.println("\n=== Dashboard en Tiempo Real ===");
        estadosActuales.forEach((id, estado) ->
                System.out.println("  " + id + " -> " + estado));
    }
}
