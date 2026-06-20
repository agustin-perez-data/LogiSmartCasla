package com.logismart.facade;

import com.logismart.facade.subsistemas.SistemaInventario;
import com.logismart.facade.subsistemas.SistemaPagos;
import com.logismart.facade.subsistemas.SistemaRastreo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests del patron Facade.
 *
 * Verifican que {@link ServicioLogisticaFacade} ofrece una interfaz de alto
 * nivel que coordina los subsistemas (inventario, pagos, rastreo,
 * notificaciones y reportes) y devuelve resultados coherentes, sin que el
 * cliente tenga que conocer la complejidad interna.
 */
@DisplayName("Patron Facade - ServicioLogisticaFacade y subsistemas")
class ServicioLogisticaFacadeTest {

    @Test
    @DisplayName("crearEnvio coordina los subsistemas y devuelve un numero de seguimiento valido")
    void crearEnvioDevuelveNumeroSeguimientoCuandoStockYPagoSonExitosos() {
        ServicioLogisticaFacade facade = new ServicioLogisticaFacade();

        String numeroSeguimiento = facade.crearEnvio("PROD-1", 5000.0,
                "cliente@correo.com", "1122334455");

        assertNotNull(numeroSeguimiento,
                "Con stock disponible y pago aprobado debe devolver un numero de seguimiento");
        assertTrue(numeroSeguimiento.startsWith("LS-"),
                "El numero de seguimiento generado por el subsistema de rastreo debe iniciar con LS-");
    }

    @Test
    @DisplayName("crearEnvio devuelve null cuando no hay stock (falla el subsistema de inventario)")
    void crearEnvioDevuelveNullSiNoHayStock() {
        ServicioLogisticaFacade facade = new ServicioLogisticaFacade();

        // El SistemaInventario considera sin stock todo producto que empieza con "PROD-NO".
        String resultado = facade.crearEnvio("PROD-NO-99", 5000.0,
                "cliente@correo.com", "1122334455");

        assertNull(resultado,
                "Si el inventario reporta stock insuficiente el facade debe abortar y devolver null");
    }

    @Test
    @DisplayName("crearEnvio devuelve null cuando el pago es rechazado (falla el subsistema de pagos)")
    void crearEnvioDevuelveNullSiElPagoEsRechazado() {
        ServicioLogisticaFacade facade = new ServicioLogisticaFacade();

        // El SistemaPagos rechaza montos mayores a 10000.
        String resultado = facade.crearEnvio("PROD-1", 25000.0,
                "cliente@correo.com", "1122334455");

        assertNull(resultado,
                "Si el pago es rechazado el facade debe abortar la creacion del envio");
    }

    @Test
    @DisplayName("obtenerEstadoEnvio delega en el subsistema de rastreo y devuelve un estado coherente")
    void obtenerEstadoEnvioDevuelveEstadoDelSubsistemaRastreo() {
        ServicioLogisticaFacade facade = new ServicioLogisticaFacade();

        String estado = facade.obtenerEstadoEnvio("LS-123");

        assertNotNull(estado, "El estado consultado no debe ser nulo");
        assertEquals("EN TRÁNSITO", estado,
                "El facade debe devolver el estado que entrega el SistemaRastreo");
    }

    @Test
    @DisplayName("cancelarEnvio coordina rastreo, pagos y notificaciones sin lanzar excepciones")
    void cancelarEnvioNoLanzaExcepciones() {
        ServicioLogisticaFacade facade = new ServicioLogisticaFacade();

        assertDoesNotThrow(() -> facade.cancelarEnvio("LS-123", "cliente@correo.com"),
                "La cancelacion debe coordinar los subsistemas sin propagar errores");
    }

    @Test
    @DisplayName("Los subsistemas aplican su logica de negocio de forma independiente")
    void subsistemasAplicanSuLogicaDeNegocio() {
        SistemaInventario inventario = new SistemaInventario();
        SistemaPagos pagos = new SistemaPagos();
        SistemaRastreo rastreo = new SistemaRastreo();

        assertTrue(inventario.verificarStock("PROD-OK"),
                "Debe haber stock para un producto comun");
        assertFalse(inventario.verificarStock("PROD-NO-1"),
                "No debe haber stock para productos PROD-NO");

        assertTrue(pagos.procesarPago("TARJETA", 9999.0),
                "Un monto dentro del limite debe ser aprobado");
        assertFalse(pagos.procesarPago("TARJETA", 10001.0),
                "Un monto por encima del limite debe ser rechazado");

        String numero = rastreo.crearNumeroSeguimiento();
        assertNotNull(numero, "El rastreo debe generar un numero de seguimiento");
        assertTrue(numero.startsWith("LS-"),
                "El numero generado por el rastreo debe iniciar con LS-");
    }
}
