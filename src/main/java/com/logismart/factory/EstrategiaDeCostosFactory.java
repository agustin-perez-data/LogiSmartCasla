package com.logismart.factory;

import com.logismart.dominio.TipoEstrategia;
import com.logismart.servicios.EstrategiaDeCostos;
import com.logismart.servicios.EstrategiaPorKilometro;
import com.logismart.servicios.EstrategiaPorPeso;
import com.logismart.servicios.EstrategiaTarifaFija;

public class EstrategiaDeCostosFactory {

    public static EstrategiaDeCostos crearEstrategia(TipoEstrategia tipo) {
        return switch (tipo) {
            case POR_KILOMETRO -> new EstrategiaPorKilometro();
            case POR_PESO      -> new EstrategiaPorPeso();
            case TARIFA_FIJA   -> new EstrategiaTarifaFija();
        };
    }
}
