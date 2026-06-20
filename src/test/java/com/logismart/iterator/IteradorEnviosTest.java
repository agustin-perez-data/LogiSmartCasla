package com.logismart.iterator;

import com.logismart.dominio.Envio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Patrón Iterator - Recorrido uniforme de colecciones de envíos")
class IteradorEnviosTest {

    private Envio envio(String id) {
        return new Envio.EnvioBuilder(id, "Buenos Aires", "Córdoba")
                .peso(5.0)
                .costo(100.0)
                .build();
    }

    private List<String> recorrerIds(IteradorEnvios iterador) {
        List<String> ids = new ArrayList<>();
        while (iterador.tieneSiguiente()) {
            ids.add(iterador.obtenerSiguiente().getId());
        }
        return ids;
    }

    @Test
    @DisplayName("ColeccionArray recorre todos los envíos en orden de inserción")
    void coleccionArrayRecorreEnOrden() {
        ColeccionArray coleccion = new ColeccionArray();
        coleccion.agregar(envio("E1"));
        coleccion.agregar(envio("E2"));
        coleccion.agregar(envio("E3"));

        assertEquals(3, coleccion.obtenerTamano());

        List<String> ids = recorrerIds(coleccion.crearIterador());

        assertEquals(3, ids.size());
        assertEquals(List.of("E1", "E2", "E3"), ids);
    }

    @Test
    @DisplayName("ColeccionLista recorre todos los envíos en orden de inserción")
    void coleccionListaRecorreEnOrden() {
        ColeccionLista coleccion = new ColeccionLista();
        coleccion.agregar(envio("L1"));
        coleccion.agregar(envio("L2"));

        IteradorEnvios it = coleccion.crearIterador();

        assertTrue(it.tieneSiguiente());
        assertEquals("L1", it.obtenerSiguiente().getId());
        assertTrue(it.tieneSiguiente());
        assertEquals("L2", it.obtenerSiguiente().getId());
        assertFalse(it.tieneSiguiente());
    }

    @Test
    @DisplayName("ColeccionHash recorre todos los envíos sin perder elementos")
    void coleccionHashRecorreTodos() {
        ColeccionHash coleccion = new ColeccionHash();
        coleccion.agregar(envio("H1"));
        coleccion.agregar(envio("H2"));
        coleccion.agregar(envio("H3"));

        assertEquals(3, coleccion.obtenerTamano());

        List<String> ids = recorrerIds(coleccion.crearIterador());

        assertEquals(3, ids.size());
        // HashMap no garantiza orden, por eso validamos el contenido completo.
        assertTrue(ids.containsAll(List.of("H1", "H2", "H3")));
    }

    @Test
    @DisplayName("reiniciar() permite volver a recorrer la colección desde el principio")
    void reiniciarPermiteSegundoRecorrido() {
        ColeccionArray coleccion = new ColeccionArray();
        coleccion.agregar(envio("R1"));
        coleccion.agregar(envio("R2"));

        IteradorEnvios it = coleccion.crearIterador();
        List<String> primer = recorrerIds(it);
        assertEquals(2, primer.size());
        assertFalse(it.tieneSiguiente());

        it.reiniciar();

        List<String> segundo = recorrerIds(it);
        assertEquals(primer, segundo);
    }

    @Test
    @DisplayName("remover() reduce el tamaño y el iterador ya no devuelve el envío eliminado")
    void removerEliminaDelRecorrido() {
        ColeccionLista coleccion = new ColeccionLista();
        Envio e1 = envio("D1");
        Envio e2 = envio("D2");
        coleccion.agregar(e1);
        coleccion.agregar(e2);

        coleccion.remover(e1);

        assertEquals(1, coleccion.obtenerTamano());
        List<String> ids = recorrerIds(coleccion.crearIterador());
        assertEquals(List.of("D2"), ids);
    }
}
