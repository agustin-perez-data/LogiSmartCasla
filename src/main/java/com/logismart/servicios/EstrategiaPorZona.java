package com.logismart.servicios;

import com.logismart.dominio.Envio;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EstrategiaPorZona implements EstrategiaDeOptimizacion {

    @Override
    public List<Envio> optimizar(List<Envio> envios) {
        // Agrupa por zona geografica aproximada (grado de latitud) y ordena dentro de cada zona por longitud
        List<Envio> resultado = new ArrayList<>(envios);
        resultado.sort(Comparator
            .comparingInt((Envio e) -> (int) e.getLatitud())
            .thenComparingDouble(Envio::getLongitud));
        return resultado;
    }
}
