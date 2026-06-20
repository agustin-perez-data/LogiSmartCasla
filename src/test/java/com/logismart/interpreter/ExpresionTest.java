package com.logismart.interpreter;

import com.logismart.dominio.Envio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Interpreter - Expresiones terminales y no-terminales")
class ExpresionTest {

    private Envio envio(String origen, String destino, double peso, double costo) {
        return new Envio.EnvioBuilder("E1", origen, destino)
                .peso(peso).costo(costo).build();
    }

    @Test
    @DisplayName("Expresiones terminales evaluan correctamente origen, destino, peso y costo")
    void terminalesEvaluan() {
        Envio e = envio("Buenos Aires", "Cordoba", 10.0, 500.0);

        assertTrue(new ExpresionOrigen("Buenos Aires").evaluar(e));
        assertFalse(new ExpresionOrigen("Rosario").evaluar(e));

        assertTrue(new ExpresionDestino("Cordoba").evaluar(e));
        assertFalse(new ExpresionDestino("Mendoza").evaluar(e));

        assertTrue(new ExpresionPeso(5.0, ">").evaluar(e), "10 > 5");
        assertFalse(new ExpresionPeso(20.0, ">").evaluar(e), "10 > 20 es falso");

        assertTrue(new ExpresionCosto(1000.0, "<").evaluar(e), "500 < 1000");
        assertFalse(new ExpresionCosto(100.0, "<").evaluar(e), "500 < 100 es falso");
    }

    @Test
    @DisplayName("ExpresionRestringido detecta destinos restringidos")
    void restringidoDetectaDestino() {
        assertTrue(new ExpresionRestringido()
                .evaluar(envio("Buenos Aires", "Zona Restringido", 1.0, 1.0)));
        assertFalse(new ExpresionRestringido()
                .evaluar(envio("Buenos Aires", "Cordoba", 1.0, 1.0)));
    }

    @Test
    @DisplayName("ExpresionAND: verdadera solo si ambas subexpresiones son verdaderas")
    void andCombina() {
        Envio e = envio("Buenos Aires", "Cordoba", 10.0, 500.0);
        Expresion ambasVerdaderas = new ExpresionAND(
                new ExpresionOrigen("Buenos Aires"),
                new ExpresionDestino("Cordoba"));
        Expresion unaFalsa = new ExpresionAND(
                new ExpresionOrigen("Buenos Aires"),
                new ExpresionDestino("Mendoza"));

        assertTrue(ambasVerdaderas.evaluar(e));
        assertFalse(unaFalsa.evaluar(e));
    }

    @Test
    @DisplayName("ExpresionOR: verdadera si al menos una subexpresion es verdadera")
    void orCombina() {
        Envio e = envio("Buenos Aires", "Cordoba", 10.0, 500.0);
        Expresion unaVerdadera = new ExpresionOR(
                new ExpresionOrigen("Rosario"),
                new ExpresionDestino("Cordoba"));
        Expresion ambasFalsas = new ExpresionOR(
                new ExpresionOrigen("Rosario"),
                new ExpresionDestino("Mendoza"));

        assertTrue(unaVerdadera.evaluar(e));
        assertFalse(ambasFalsas.evaluar(e));
    }

    @Test
    @DisplayName("ExpresionNOT: invierte el resultado de la subexpresion")
    void notInvierte() {
        Envio e = envio("Buenos Aires", "Cordoba", 10.0, 500.0);
        assertFalse(new ExpresionNOT(new ExpresionOrigen("Buenos Aires")).evaluar(e));
        assertTrue(new ExpresionNOT(new ExpresionOrigen("Rosario")).evaluar(e));
    }

    @Test
    @DisplayName("Expresion compuesta: (origen=BA AND peso>5) AND NOT restringido")
    void expresionCompuestaAnidada() {
        Envio valido = envio("Buenos Aires", "Cordoba", 10.0, 500.0);
        Envio restringido = envio("Buenos Aires", "Area Restringido", 10.0, 500.0);

        Expresion regla = new ExpresionAND(
                new ExpresionAND(
                        new ExpresionOrigen("Buenos Aires"),
                        new ExpresionPeso(5.0, ">")),
                new ExpresionNOT(new ExpresionRestringido()));

        assertTrue(regla.evaluar(valido),
                "Origen BA, peso>5 y destino no restringido => true");
        assertFalse(regla.evaluar(restringido),
                "Destino restringido invalida la regla compuesta");
    }
}
