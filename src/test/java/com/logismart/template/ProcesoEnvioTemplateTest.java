package com.logismart.template;

import com.logismart.dominio.Envio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Patron Template Method: procesos de envio")
class ProcesoEnvioTemplateTest {

    private static final double DELTA = 0.0001;

    private Envio nuevoEnvio() {
        return new Envio.EnvioBuilder("ENV-T", "A", "B")
                .peso(10.0)
                .build();
    }

    @Test
    @DisplayName("ProcesoNacional aplica costo = 100 + peso*5")
    void procesoNacionalCalculaCosto() {
        Envio envio = nuevoEnvio();
        new ProcesoNacional().procesarEnvio(envio);

        assertEquals(100.0 + (10.0 * 5.0), envio.getCosto(), DELTA);
    }

    @Test
    @DisplayName("ProcesoInternacional aplica base + 15% de aduanas")
    void procesoInternacionalCalculaCosto() {
        Envio envio = nuevoEnvio();
        new ProcesoInternacional().procesarEnvio(envio);

        double base = 200.0 + (10.0 * 10.0);
        double esperado = base + (base * 0.15);
        assertEquals(esperado, envio.getCosto(), DELTA);
    }

    @Test
    @DisplayName("ProcesoUrgente aplica costo prioritario = 500 + peso*15")
    void procesoUrgenteCalculaCosto() {
        Envio envio = nuevoEnvio();
        new ProcesoUrgente().procesarEnvio(envio);

        assertEquals(500.0 + (10.0 * 15.0), envio.getCosto(), DELTA);
    }

    @Test
    @DisplayName("El mismo template produce costos distintos segun el proceso concreto")
    void procesosConcretosDifierenEnResultado() {
        Envio nacional = nuevoEnvio();
        Envio internacional = nuevoEnvio();
        Envio urgente = nuevoEnvio();

        new ProcesoNacional().procesarEnvio(nacional);
        new ProcesoInternacional().procesarEnvio(internacional);
        new ProcesoUrgente().procesarEnvio(urgente);

        double cn = nacional.getCosto();
        double ci = internacional.getCosto();
        double cu = urgente.getCosto();

        // Para 10kg: nacional=150, internacional=345, urgente=650
        assertTrue(cn < ci, "Nacional debe ser mas barato que internacional");
        assertTrue(ci < cu, "Internacional debe ser mas barato que urgente");
        assertEquals(150.0, cn, DELTA);
        assertEquals(650.0, cu, DELTA);
    }
}
