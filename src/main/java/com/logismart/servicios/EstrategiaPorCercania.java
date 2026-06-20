package com.logismart.servicios;

import com.logismart.dominio.Envio;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EstrategiaPorCercania implements EstrategiaDeOptimizacion {

    @Override
    public List<Envio> optimizar(List<Envio> envios) {
        if (envios.isEmpty()) return envios;
        List<Envio> pendientes = new ArrayList<>(envios);
        List<Envio> resultado = new ArrayList<>();
        Envio actual = pendientes.remove(0);
        resultado.add(actual);
        while (!pendientes.isEmpty()) {
            Envio siguiente = encontrarMasCercano(actual, pendientes);
            pendientes.remove(siguiente);
            resultado.add(siguiente);
            actual = siguiente;
        }
        return resultado;
    }

    private Envio encontrarMasCercano(Envio desde, List<Envio> candidatos) {
        return candidatos.stream()
            .min(Comparator.comparingDouble(e -> distancia(desde, e)))
            .orElseThrow();
    }

    private double distancia(Envio a, Envio b) {
        double dLat = a.getLatitud() - b.getLatitud();
        double dLon = a.getLongitud() - b.getLongitud();
        return Math.sqrt(dLat * dLat + dLon * dLon);
    }
}
