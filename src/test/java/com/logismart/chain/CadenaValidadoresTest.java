package com.logismart.chain;

import com.logismart.dominio.Envio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Chain of Responsibility - CadenaValidadores")
class CadenaValidadoresTest {

    // Sistemas simulados (stubs) para inventario y capacidad.
    private static final SistemaInventario STOCK_OK = productoId -> true;
    private static final SistemaInventario STOCK_NO = productoId -> false;
    private static final SistemaCapacidad CAPACIDAD_OK = peso -> true;
    private static final SistemaCapacidad CAPACIDAD_NO = peso -> false;

    private Envio.EnvioBuilder envioValido() {
        return new Envio.EnvioBuilder("E1", "Buenos Aires", "Cordoba")
                .peso(10.0)
                .costo(500.0)
                .metodoPago("TARJETA")
                .productoId("PROD-1");
    }

    @Test
    @DisplayName("Envio valido pasa toda la cadena de validadores")
    void envioValidoPasaTodaLaCadena() {
        CadenaValidadores cadena = new CadenaValidadores(STOCK_OK, CAPACIDAD_OK);
        assertTrue(cadena.validarEnvio(envioValido().build()),
                "Un envio completo y valido debe pasar todos los validadores");
    }

    @Test
    @DisplayName("Falla en ValidadorDatos cuando el peso es invalido")
    void fallaPorDatosInvalidos() {
        CadenaValidadores cadena = new CadenaValidadores(STOCK_OK, CAPACIDAD_OK);
        Envio envio = envioValido().peso(0.0).build();
        assertFalse(cadena.validarEnvio(envio),
                "Peso <= 0 debe hacer fallar la validacion de datos");
    }

    @Test
    @DisplayName("Falla en ValidadorInventario cuando no hay stock")
    void fallaPorInventarioSinStock() {
        CadenaValidadores cadena = new CadenaValidadores(STOCK_NO, CAPACIDAD_OK);
        assertFalse(cadena.validarEnvio(envioValido().build()),
                "Sin stock disponible la cadena debe rechazar el envio");
    }

    @Test
    @DisplayName("Falla en ValidadorPago cuando el costo es invalido")
    void fallaPorPagoInvalido() {
        CadenaValidadores cadena = new CadenaValidadores(STOCK_OK, CAPACIDAD_OK);
        Envio envio = envioValido().costo(0.0).build();
        assertFalse(cadena.validarEnvio(envio),
                "Costo <= 0 debe hacer fallar la validacion de pago");
    }

    @Test
    @DisplayName("Falla en ValidadorSeguridad cuando el destino esta restringido")
    void fallaPorSeguridadDestinoRestringido() {
        CadenaValidadores cadena = new CadenaValidadores(STOCK_OK, CAPACIDAD_OK);
        Envio envio = new Envio.EnvioBuilder("E2", "Buenos Aires", "Zona Restringido")
                .peso(10.0).costo(500.0).metodoPago("TARJETA").productoId("PROD-1")
                .build();
        assertFalse(cadena.validarEnvio(envio),
                "Un destino que contiene 'Restringido' debe ser rechazado");
    }

    @Test
    @DisplayName("Falla en ValidadorCapacidad cuando no hay espacio disponible")
    void fallaPorCapacidadInsuficiente() {
        CadenaValidadores cadena = new CadenaValidadores(STOCK_OK, CAPACIDAD_NO);
        assertFalse(cadena.validarEnvio(envioValido().build()),
                "Sin capacidad disponible la cadena debe rechazar el envio");
    }

    @Test
    @DisplayName("ValidadorDatos directo: encadena al siguiente validador cuando los datos son validos")
    void validadorDatosEncadenaAlSiguiente() {
        ValidadorEnvio datos = new ValidadorDatos();
        ValidadorEnvio pago = new ValidadorPago();
        datos.setSiguiente(pago);

        Envio envio = envioValido().build();
        assertTrue(datos.validar(envio),
                "Datos validos + pago valido => true");

        Envio sinPago = envioValido().metodoPago("").build();
        assertFalse(datos.validar(sinPago),
                "Datos validos pero metodo de pago vacio => el siguiente validador falla");
    }

    @Test
    @DisplayName("obtenerNombre devuelve el nombre de cada validador")
    void obtenerNombreDeValidadores() {
        org.junit.jupiter.api.Assertions.assertEquals("ValidadorDatos",
                new ValidadorDatos().obtenerNombre());
        org.junit.jupiter.api.Assertions.assertEquals("ValidadorPago",
                new ValidadorPago().obtenerNombre());
        org.junit.jupiter.api.Assertions.assertEquals("ValidadorSeguridad",
                new ValidadorSeguridad().obtenerNombre());
    }
}
