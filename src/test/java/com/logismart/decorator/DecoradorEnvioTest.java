package com.logismart.decorator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Patrón Decorator - Envíos decorados")
class DecoradorEnvioTest {

    private static final double DELTA = 0.0001;

    @Test
    @DisplayName("El envío básico calcula costo = peso * 10 y servicios básicos")
    void envioBasicoCalculaCostoYServiciosBase() {
        Envio envio = new EnvioBasico("Buenos Aires", "Córdoba", 5.0);

        assertEquals(50.0, envio.obtenerCosto(), DELTA);
        assertEquals(3, envio.obtenerTiempoEntrega());
        assertEquals("Básico", envio.obtenerServicios());
        assertTrue(envio.obtenerDescripcion().contains("Buenos Aires"));
        assertTrue(envio.obtenerDescripcion().contains("Córdoba"));
    }

    @Test
    @DisplayName("DecoradorSeguro suma 15% al costo y marca la descripción como ASEGURADO")
    void decoradorSeguroSumaPorcentajeYDescripcion() {
        Envio base = new EnvioBasico("A", "B", 10.0); // costo base 100
        Envio asegurado = new DecoradorSeguro(base);

        assertEquals(115.0, asegurado.obtenerCosto(), DELTA);
        assertTrue(asegurado.obtenerDescripcion().contains("[ASEGURADO]"));
        assertTrue(asegurado.obtenerServicios().contains("Seguro"));
        // El tiempo de entrega se delega sin cambios
        assertEquals(base.obtenerTiempoEntrega(), asegurado.obtenerTiempoEntrega());
    }

    @Test
    @DisplayName("DecoradorPrioritario suma 100 al costo y reduce el tiempo de entrega en 2")
    void decoradorPrioritarioSumaCostoYReduceTiempo() {
        Envio base = new EnvioBasico("A", "B", 4.0); // costo base 40, tiempo 3
        Envio prioritario = new DecoradorPrioritario(base);

        assertEquals(140.0, prioritario.obtenerCosto(), DELTA);
        assertEquals(1, prioritario.obtenerTiempoEntrega()); // 3 - 2 = 1
        assertTrue(prioritario.obtenerServicios().contains("Prioritario"));
    }

    @Test
    @DisplayName("DecoradorRastreoGPS suma 50 al costo y reduce el tiempo en 1")
    void decoradorRastreoGPSSumaCostoYReduceTiempo() {
        Envio base = new EnvioBasico("A", "B", 2.0); // costo base 20, tiempo 3
        Envio gps = new DecoradorRastreoGPS(base);

        assertEquals(70.0, gps.obtenerCosto(), DELTA);
        assertEquals(2, gps.obtenerTiempoEntrega()); // 3 - 1 = 2
        assertTrue(gps.obtenerServicios().contains("Rastreo GPS"));
    }

    @Test
    @DisplayName("DecoradorNotificacionesSMS suma 25 al costo y no altera el tiempo")
    void decoradorSMSSumaCostoYNoAlteraTiempo() {
        Envio base = new EnvioBasico("A", "B", 1.0); // costo base 10, tiempo 3
        Envio sms = new DecoradorNotificacionesSMS(base);

        assertEquals(35.0, sms.obtenerCosto(), DELTA);
        assertEquals(3, sms.obtenerTiempoEntrega());
        assertTrue(sms.obtenerServicios().contains("Notificaciones SMS"));
    }

    @Test
    @DisplayName("Decoradores apilados acumulan costos y servicios en orden")
    void decoradoresApiladosAcumulanCostosYServicios() {
        // base: peso 10 -> costo 100
        Envio base = new EnvioBasico("Buenos Aires", "Mendoza", 10.0);
        // + GPS (+50) -> 150
        // + SMS (+25) -> 175
        // + Prioritario (+100) -> 275
        Envio decorado = new DecoradorPrioritario(
                new DecoradorNotificacionesSMS(
                        new DecoradorRastreoGPS(base)));

        assertEquals(275.0, decorado.obtenerCosto(), DELTA);

        String servicios = decorado.obtenerServicios();
        assertTrue(servicios.contains("Básico"));
        assertTrue(servicios.contains("Rastreo GPS"));
        assertTrue(servicios.contains("Notificaciones SMS"));
        assertTrue(servicios.contains("Prioritario"));

        // GPS resta 1 y Prioritario resta 2: 3 - 1 - 2 = 0 -> mínimo 1
        assertEquals(1, decorado.obtenerTiempoEntrega());
    }

    @Test
    @DisplayName("Seguro sobre Prioritario aplica el 15% sobre el costo ya incrementado")
    void seguroSobrePrioritarioAplicaPorcentajeSobreCostoIncrementado() {
        Envio base = new EnvioBasico("A", "B", 10.0); // 100
        Envio prioritario = new DecoradorPrioritario(base); // 200
        Envio seguroSobrePrioritario = new DecoradorSeguro(prioritario); // 200 * 1.15 = 230

        assertEquals(230.0, seguroSobrePrioritario.obtenerCosto(), DELTA);
        assertTrue(seguroSobrePrioritario.obtenerServicios().contains("Prioritario"));
        assertTrue(seguroSobrePrioritario.obtenerServicios().contains("Seguro"));
    }

    @Test
    @DisplayName("El orden de los decoradores de costo fijo no cambia el costo total")
    void ordenDecoradoresCostoFijoNoCambiaTotal() {
        Envio base1 = new EnvioBasico("A", "B", 5.0); // 50
        Envio base2 = new EnvioBasico("A", "B", 5.0); // 50

        Envio orden1 = new DecoradorNotificacionesSMS(new DecoradorRastreoGPS(base1)); // 50+50+25
        Envio orden2 = new DecoradorRastreoGPS(new DecoradorNotificacionesSMS(base2)); // 50+25+50

        assertEquals(125.0, orden1.obtenerCosto(), DELTA);
        assertEquals(orden1.obtenerCosto(), orden2.obtenerCosto(), DELTA);
    }
}
