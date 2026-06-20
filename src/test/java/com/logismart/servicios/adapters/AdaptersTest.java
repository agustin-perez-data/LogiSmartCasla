package com.logismart.servicios.adapters;

import com.logismart.dominio.Envio;
import com.logismart.servicios.ProveedorEnvio;
import com.logismart.servicios.ProveedorPago;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests del patron Adapter.
 *
 * Verifican que cada adapter (DHL, FedEx, UPS para envios; PayPal y Stripe
 * para pagos) implementa la interfaz destino comun ({@link ProveedorEnvio} /
 * {@link ProveedorPago}) y traduce la llamada hacia su API externa propietaria,
 * devolviendo un resultado valido. Tambien se comprueba el polimorfismo:
 * tratar varios adapters distintos a traves de la misma interfaz.
 */
@DisplayName("Patron Adapter - ProveedorEnvio y ProveedorPago")
class AdaptersTest {

    private Envio nuevoEnvio() {
        return new Envio.EnvioBuilder("ENV-1", "Buenos Aires", "Cordoba")
                .peso(10.0)
                .build();
    }

    @Test
    @DisplayName("AdapterDHL implementa ProveedorEnvio y traduce crearEnvio/calcularCosto a la API de DHL")
    void adapterDhlTraduceLlamadasALaApiDhl() {
        ProveedorEnvio dhl = new AdapterDHL();
        Envio envio = nuevoEnvio();

        boolean creado = dhl.crearEnvio(envio);

        assertTrue(creado, "DHL debe poder crear el envio");
        assertNotNull(envio.getNumeroSeguimiento(),
                "El adapter debe setear el numero de seguimiento devuelto por la API externa");
        assertTrue(envio.getNumeroSeguimiento().startsWith("DHL-"),
                "El codigo de DHL debe comenzar con DHL-");
        assertEquals("DHL", dhl.obtenerNombre());
        assertEquals(10.0 * 15.0, dhl.calcularCosto(envio), 0.001,
                "El costo debe surgir de la tarifa de la API de DHL (peso * 15)");
        assertNotNull(dhl.obtenerEstado(envio.getNumeroSeguimiento()));
    }

    @Test
    @DisplayName("AdapterFedEx adapta el shipmentId numerico y traduce el estado al espanol")
    void adapterFedExAdaptaShipmentIdYEstado() {
        ProveedorEnvio fedex = new AdapterFedEx();
        Envio envio = nuevoEnvio();

        boolean creado = fedex.crearEnvio(envio);

        assertTrue(creado, "FedEx debe crear el envio con un shipmentId positivo");
        assertNotNull(envio.getNumeroSeguimiento());
        assertTrue(envio.getNumeroSeguimiento().startsWith("FDX-"),
                "El adapter debe componer el numero con prefijo FDX-");
        assertEquals("FedEx", fedex.obtenerNombre());
        // La API externa devuelve DELIVERED; el adapter lo traduce a "Entregado".
        assertEquals("Entregado", fedex.obtenerEstado(envio.getNumeroSeguimiento()),
                "El adapter debe traducir el estado de la API externa al dominio en espanol");
        assertEquals(10.0 * 12.0, fedex.calcularCosto(envio), 0.001);
    }

    @Test
    @DisplayName("AdapterUPS implementa ProveedorEnvio y traduce el tracking a estados del dominio")
    void adapterUpsTraduceLlamadasALaApiUps() {
        ProveedorEnvio ups = new AdapterUPS();
        Envio envio = nuevoEnvio();

        boolean creado = ups.crearEnvio(envio);

        assertTrue(creado, "UPS debe confirmar el envio del paquete");
        assertNotNull(envio.getNumeroSeguimiento());
        assertTrue(envio.getNumeroSeguimiento().startsWith("UPS-"));
        assertEquals("UPS", ups.obtenerNombre());
        // La API externa devuelve "Out for delivery"; el adapter lo traduce a "En camino".
        assertEquals("En camino", ups.obtenerEstado(envio.getNumeroSeguimiento()),
                "El adapter debe traducir el estado propietario de UPS al dominio");
        assertEquals(10.0 * 10.0, ups.calcularCosto(envio), 0.001);
    }

    @Test
    @DisplayName("AdapterPayPal implementa ProveedorPago y traduce procesarPago a la API de PayPal")
    void adapterPayPalTraduceLlamadasALaApiPayPal() {
        ProveedorPago paypal = new AdapterPayPal();

        boolean exito = paypal.procesarPago(1500.0, "ORD-100");

        assertTrue(exito, "PayPal debe procesar el pago correctamente");
        assertEquals("PayPal", paypal.obtenerNombre());
        assertNotNull(paypal.obtenerEstado("PP-123"),
                "El adapter debe devolver el estado consultado en la API externa");
        assertDoesNotThrow(() -> paypal.reembolsar("PP-123", 500.0),
                "El reembolso debe delegarse a la API sin errores");
    }

    @Test
    @DisplayName("AdapterStripe convierte el monto a centavos y traduce el estado de la API de Stripe")
    void adapterStripeConvierteMontoYTraduceEstado() {
        ProveedorPago stripe = new AdapterStripe();

        boolean exito = stripe.procesarPago(20.0, "ORD-200");

        assertTrue(exito, "Stripe debe procesar el cobro en centavos");
        assertEquals("Stripe", stripe.obtenerNombre());
        // La API externa devuelve "succeeded"; el adapter lo traduce a "COMPLETADA".
        assertEquals("COMPLETADA", stripe.obtenerEstado("ch_123"),
                "El adapter debe traducir el estado de Stripe al dominio en espanol");
        assertDoesNotThrow(() -> stripe.reembolsar("ch_123", 5.0));
    }

    @Test
    @DisplayName("Polimorfismo de envios: todos los adapters se usan a traves de ProveedorEnvio")
    void polimorfismoProveedoresDeEnvio() {
        List<ProveedorEnvio> proveedores = List.of(
                new AdapterDHL(), new AdapterFedEx(), new AdapterUPS());

        for (ProveedorEnvio proveedor : proveedores) {
            Envio envio = nuevoEnvio();
            assertTrue(proveedor.crearEnvio(envio),
                    "Cada proveedor debe poder crear un envio via la interfaz comun");
            assertNotNull(proveedor.obtenerNombre(),
                    "Cada proveedor debe exponer su nombre");
            assertTrue(proveedor.calcularCosto(envio) > 0,
                    "Cada proveedor debe calcular un costo positivo");
        }

        assertEquals(3, proveedores.size());
        assertTrue(proveedores.stream().allMatch(p -> p instanceof ProveedorEnvio),
                "Todos los adapters de envio deben ser instancias de ProveedorEnvio");
    }

    @Test
    @DisplayName("Polimorfismo de pagos: todos los adapters se usan a traves de ProveedorPago")
    void polimorfismoProveedoresDePago() {
        List<ProveedorPago> proveedores = List.of(new AdapterPayPal(), new AdapterStripe());

        for (ProveedorPago proveedor : proveedores) {
            assertTrue(proveedor.procesarPago(100.0, "REF-" + proveedor.obtenerNombre()),
                    "Cada proveedor debe procesar el pago via la interfaz comun");
            assertNotNull(proveedor.obtenerNombre());
        }

        assertEquals(2, proveedores.size());
        assertTrue(proveedores.stream().allMatch(p -> p instanceof ProveedorPago),
                "Todos los adapters de pago deben ser instancias de ProveedorPago");
    }
}
