package com.logismart.strategy;

import com.logismart.dominio.Envio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Patron Strategy: estrategias de calculo de costo")
class EstrategiaCalculoCostoTest {

    private static final double DELTA = 0.0001;

    private Envio envioUrgente() {
        return new Envio.EnvioBuilder("ENV-S", "Rosario", "Mendoza")
                .peso(20.0)
                .tipo("URGENTE")
                .build();
    }

    // Replica de la formula de EstrategiaDistancia (hash determinista).
    private double distanciaEsperada(String origen, String destino) {
        int hash = Math.floorMod((origen + "->" + destino).hashCode(), 450);
        return (50.0 + hash) * 10.0;
    }

    @Test
    @DisplayName("EstrategiaPeso = peso * 5")
    void costoPorPeso() {
        Envio envio = envioUrgente(); // peso 20
        envio.establecerEstrategia(new EstrategiaPeso());

        assertEquals(20.0 * 5.0, envio.calcularCosto(), DELTA);
    }

    @Test
    @DisplayName("EstrategiaVolumen = (peso*2)*2")
    void costoPorVolumen() {
        Envio envio = envioUrgente(); // peso 20
        envio.establecerEstrategia(new EstrategiaVolumen());

        assertEquals((20.0 * 2.0) * 2.0, envio.calcularCosto(), DELTA);
    }

    @Test
    @DisplayName("EstrategiaUrgencia devuelve 500 para tipo URGENTE y 100 para NORMAL")
    void costoPorUrgenciaSegunTipo() {
        Envio urgente = envioUrgente();
        urgente.establecerEstrategia(new EstrategiaUrgencia());
        assertEquals(500.0, urgente.calcularCosto(), DELTA);

        Envio normal = new Envio.EnvioBuilder("ENV-N", "A", "B")
                .peso(20.0)
                .tipo("NORMAL")
                .build();
        normal.establecerEstrategia(new EstrategiaUrgencia());
        assertEquals(100.0, normal.calcularCosto(), DELTA);
    }

    @Test
    @DisplayName("EstrategiaDistancia coincide con la formula hash * costo por km")
    void costoPorDistancia() {
        Envio envio = envioUrgente();
        envio.establecerEstrategia(new EstrategiaDistancia());

        assertEquals(distanciaEsperada("Rosario", "Mendoza"), envio.calcularCosto(), DELTA);
    }

    @Test
    @DisplayName("EstrategiaHibrida = 0.4*dist + 0.3*peso + 0.3*urgencia")
    void costoHibridoEsCombinacionPonderada() {
        Envio envio = envioUrgente(); // peso 20, URGENTE

        double dist = distanciaEsperada("Rosario", "Mendoza");
        double peso = 20.0 * 5.0;
        double urg = 500.0;
        double esperado = (dist * 0.4) + (peso * 0.3) + (urg * 0.3);

        envio.establecerEstrategia(new EstrategiaHibrida());
        assertEquals(esperado, envio.calcularCosto(), DELTA);
    }

    @Test
    @DisplayName("Para un mismo envio, distintas estrategias dan costos distintos")
    void estrategiasDistintasDanCostosDistintos() {
        Envio envio = envioUrgente();

        envio.establecerEstrategia(new EstrategiaPeso());
        double porPeso = envio.calcularCosto();

        envio.establecerEstrategia(new EstrategiaUrgencia());
        double porUrgencia = envio.calcularCosto();

        assertNotEquals(porPeso, porUrgencia,
                "Cambiar la estrategia debe cambiar el costo calculado");
        assertTrue(porUrgencia > porPeso,
                "Para un envio URGENTE de 20kg, la urgencia (500) supera al peso (100)");
    }
}
