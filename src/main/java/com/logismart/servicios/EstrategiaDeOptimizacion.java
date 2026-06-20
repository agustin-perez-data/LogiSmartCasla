package com.logismart.servicios;

import com.logismart.dominio.Envio;
import java.util.List;

public interface EstrategiaDeOptimizacion {
    List<Envio> optimizar(List<Envio> envios);
}
