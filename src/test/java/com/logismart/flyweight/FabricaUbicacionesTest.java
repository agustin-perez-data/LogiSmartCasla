package com.logismart.flyweight;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Patrón Flyweight - Fábrica de ubicaciones compartidas")
class FabricaUbicacionesTest {

    @BeforeEach
    void limpiarCache() {
        // Cada test parte de una caché vacía para resultados deterministas
        FabricaUbicaciones.limpiar();
    }

    @Test
    @DisplayName("La fábrica devuelve la MISMA instancia para la misma clave")
    void mismaClaveDevuelveMismaInstancia() {
        Ubicacion u1 = FabricaUbicaciones.obtener("Córdoba", "Córdoba", "5000");
        Ubicacion u2 = FabricaUbicaciones.obtener("Córdoba", "Córdoba", "5000");

        assertSame(u1, u2);
        assertEquals(1, FabricaUbicaciones.obtenerCantidadUnicas());
    }

    @Test
    @DisplayName("La fábrica devuelve instancias DISTINTAS para claves distintas")
    void clavesDistintasDevuelvenInstanciasDistintas() {
        Ubicacion ba = FabricaUbicaciones.obtener("Buenos Aires", "Buenos Aires", "1000");
        Ubicacion ros = FabricaUbicaciones.obtener("Rosario", "Santa Fe", "2000");

        assertNotSame(ba, ros);
        assertNotEquals(ba, ros);
        assertEquals(2, FabricaUbicaciones.obtenerCantidadUnicas());
    }

    @Test
    @DisplayName("La fábrica reutiliza instancias: muchas solicitudes, pocas instancias únicas")
    void reutilizaInstanciasEntreMultiplesSolicitudes() {
        for (int i = 0; i < 100; i++) {
            FabricaUbicaciones.obtener("Mendoza", "Mendoza", "5500");
            FabricaUbicaciones.obtener("Salta", "Salta", "4400");
        }

        // 200 solicitudes pero solo 2 ubicaciones únicas creadas
        assertEquals(2, FabricaUbicaciones.obtenerCantidadUnicas());
    }

    @Test
    @DisplayName("La ubicación obtenida conserva los atributos solicitados")
    void ubicacionConservaSusAtributos() {
        Ubicacion u = FabricaUbicaciones.obtener("Tucumán", "Tucumán", "4000");

        assertEquals("Tucumán", u.getCiudad());
        assertEquals("Tucumán", u.getProvincia());
        assertEquals("4000", u.getCodigoPostal());
    }

    @Test
    @DisplayName("Una sola diferencia en la clave genera una nueva instancia (mismo nombre, distinto CP)")
    void diferenciaEnCodigoPostalGeneraNuevaInstancia() {
        Ubicacion a = FabricaUbicaciones.obtener("La Plata", "Buenos Aires", "1900");
        Ubicacion b = FabricaUbicaciones.obtener("La Plata", "Buenos Aires", "1901");

        assertNotSame(a, b);
        assertEquals(2, FabricaUbicaciones.obtenerCantidadUnicas());
    }

    @Test
    @DisplayName("limpiar() vacía la caché de ubicaciones")
    void limpiarVaciaLaCache() {
        FabricaUbicaciones.obtener("Neuquén", "Neuquén", "8300");
        assertEquals(1, FabricaUbicaciones.obtenerCantidadUnicas());

        FabricaUbicaciones.limpiar();
        assertEquals(0, FabricaUbicaciones.obtenerCantidadUnicas());
    }
}
