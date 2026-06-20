package com.logismart.servicios;

import com.logismart.dominio.Envio;
import com.logismart.dominio.EnvioEconomico;
import com.logismart.dominio.EnvioExpress;
import com.logismart.dominio.EnvioStandard;
import com.logismart.dominio.Ruta;
import com.logismart.dominio.Vehiculo;

public class CalculadoraDeCostos {

    public double calcular(Ruta ruta, Vehiculo vehiculo, EstrategiaDeCostos estrategia) {
        double costoBase = estrategia.calcularCosto(ruta, vehiculo);
        double multiplicadorPromedio = ruta.getEnvios().stream()
            .mapToDouble(e -> getMultiplicador(e))
            .average()
            .orElse(1.0);
        return costoBase * multiplicadorPromedio;
    }

    private double getMultiplicador(Envio envio) {
        if (envio instanceof EnvioExpress e)   return e.getMultiplicadorCosto();
        if (envio instanceof EnvioStandard e)  return e.getMultiplicadorCosto();
        if (envio instanceof EnvioEconomico e) return e.getMultiplicadorCosto();
        return 1.0;
    }
}
