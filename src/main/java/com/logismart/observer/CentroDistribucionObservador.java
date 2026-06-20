package com.logismart.observer;

import com.logismart.dominio.Envio;

public class CentroDistribucionObservador implements ObservadorEnvio {

    @Override
    public void actualizar(Envio envio, String evento) {
        System.out.println("[CentroObs] Envio " + envio.getId()
                + " cambio a " + evento
                + " | Ruta: " + envio.getOrigen() + " -> " + envio.getDestino());

        if ("EN_TRANSITO".equals(evento)) {
            System.out.println("  Accion: actualizar posicion en ruta");
        } else if ("ENTREGADO".equals(evento)) {
            System.out.println("  Accion: liberar bahia de carga");
        }
    }
}
