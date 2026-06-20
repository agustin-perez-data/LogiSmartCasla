package com.logismart.servicios;

import com.logismart.dominio.Envio;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EstrategiaPorVentanaHoraria implements EstrategiaDeOptimizacion {

    @Override
    public List<Envio> optimizar(List<Envio> envios) {
        List<Envio> resultado = new ArrayList<>(envios);
        resultado.sort(Comparator.comparing(Envio::getVentanaHorariaInicio));
        return resultado;
    }
}
