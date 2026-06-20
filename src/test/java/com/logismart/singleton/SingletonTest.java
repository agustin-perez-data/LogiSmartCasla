package com.logismart.singleton;

import com.logismart.servicios.ManualProvider;
import com.logismart.servicios.ProveedorDeIntegracion;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Tests del patron Singleton")
class SingletonTest {

    // -------------------- ConfiguracionLogiSmart --------------------

    @Test
    @DisplayName("ConfiguracionLogiSmart.obtenerInstancia() devuelve siempre la misma instancia")
    void configuracionLogiSmartEsSingleton() {
        ConfiguracionLogiSmart a = ConfiguracionLogiSmart.obtenerInstancia();
        ConfiguracionLogiSmart b = ConfiguracionLogiSmart.obtenerInstancia();

        assertNotNull(a);
        assertSame(a, b);
        // Valores por defecto cargados al construirse
        assertTrue(a.getMaxPesoTotal() > 0);
        assertNotNull(a.getDbUrl());
    }

    // -------------------- ConfiguracionSistema --------------------

    @Test
    @DisplayName("ConfiguracionSistema.getInstance() es singleton y delega en ConfiguracionLogiSmart")
    void configuracionSistemaEsSingletonYDelega() {
        ConfiguracionSistema a = ConfiguracionSistema.getInstance();
        ConfiguracionSistema b = ConfiguracionSistema.getInstance();

        assertSame(a, b);
        // Delega los valores en la configuracion subyacente
        assertEquals(ConfiguracionLogiSmart.obtenerInstancia().getMaxPesoTotal(), a.getMaxPesoTotal());
        assertEquals(ConfiguracionLogiSmart.obtenerInstancia().getSmtpServer(), a.getSmtpServer());
    }

    // -------------------- LoggerLogiSmart --------------------

    @Test
    @DisplayName("LoggerLogiSmart.obtenerInstancia() devuelve siempre la misma instancia")
    void loggerLogiSmartEsSingleton() {
        LoggerLogiSmart a = LoggerLogiSmart.obtenerInstancia();
        LoggerLogiSmart b = LoggerLogiSmart.obtenerInstancia();

        assertSame(a, b);
    }

    // -------------------- Logger --------------------

    @Test
    @DisplayName("Logger.getInstance() devuelve siempre la misma instancia")
    void loggerEsSingleton() {
        Logger a = Logger.getInstance();
        Logger b = Logger.getInstance();

        assertSame(a, b);
    }

    // -------------------- CacheLogiSmart --------------------

    @Test
    @DisplayName("CacheLogiSmart es singleton y mantiene el estado compartido entre referencias")
    void cacheLogiSmartEsSingletonYComparteEstado() {
        CacheLogiSmart a = CacheLogiSmart.obtenerInstancia();
        CacheLogiSmart b = CacheLogiSmart.obtenerInstancia();
        assertSame(a, b);

        String clave = "clave-test-" + System.nanoTime();
        a.put(clave, "valor");

        // El estado puesto via 'a' es visible via 'b' por ser la misma instancia
        assertTrue(b.contiene(clave));
        assertEquals("valor", b.get(clave));

        a.invalidar(clave);
        assertFalse(b.contiene(clave));
    }

    // -------------------- PoolDeConexiones --------------------

    @Test
    @DisplayName("PoolDeConexiones es singleton y administra el estado del pool de forma consistente")
    void poolDeConexionesEsSingletonYAdministraEstado() {
        PoolDeConexiones a = PoolDeConexiones.obtenerInstancia();
        PoolDeConexiones b = PoolDeConexiones.obtenerInstancia();
        assertSame(a, b);

        int disponiblesIniciales = a.getConexionesDisponibles();
        assertTrue(disponiblesIniciales > 0);

        String conexion = a.obtenerConexion();
        assertNotNull(conexion);
        // El cambio de estado es visible desde la otra referencia (misma instancia)
        assertEquals(disponiblesIniciales - 1, b.getConexionesDisponibles());

        b.liberarConexion(conexion);
        assertEquals(disponiblesIniciales, a.getConexionesDisponibles());
    }

    // -------------------- RegistroDeProveedores --------------------

    @Test
    @DisplayName("RegistroDeProveedores es singleton, resuelve proveedores y permite registrar nuevos")
    void registroDeProveedoresEsSingletonYRegistra() {
        RegistroDeProveedores a = RegistroDeProveedores.obtenerInstancia();
        RegistroDeProveedores b = RegistroDeProveedores.obtenerInstancia();
        assertSame(a, b);

        // Proveedores pre-registrados (insensible a mayusculas/minusculas)
        assertNotNull(a.obtenerProveedor("MERCADOLIBRE"));
        assertNotNull(a.obtenerProveedor("tiendanube"));

        // Tipo desconocido lanza excepcion
        assertThrows(IllegalArgumentException.class, () -> a.obtenerProveedor("DESCONOCIDO"));

        // El registro en 'a' es visible desde 'b' por ser la misma instancia
        String clave = "PROV_TEST_" + System.nanoTime();
        ProveedorDeIntegracion nuevo = new ManualProvider();
        a.registrar(clave, nuevo);
        assertSame(nuevo, b.obtenerProveedor(clave));
    }
}
