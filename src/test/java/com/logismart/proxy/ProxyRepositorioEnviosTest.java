package com.logismart.proxy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Patrón Proxy - Repositorio de envíos con caché")
class ProxyRepositorioEnviosTest {

    @Test
    @DisplayName("El proxy devuelve el mismo envío que el repositorio real para un id existente")
    void proxyDevuelveMismoEnvioQueElReal() {
        RepositorioEnvios real = new RepositorioEnviosReal();
        RepositorioEnvios proxy = new ProxyRepositorioEnvios();

        EnvioSimple esperado = real.obtenerEnvio("ENV-001");
        EnvioSimple obtenido = proxy.obtenerEnvio("ENV-001");

        assertNotNull(obtenido);
        assertEquals(esperado.getId(), obtenido.getId());
        assertEquals(esperado.getOrigen(), obtenido.getOrigen());
        assertEquals(esperado.getDestino(), obtenido.getDestino());
        assertEquals(esperado.getPeso(), obtenido.getPeso(), 0.0001);
        assertEquals(esperado.getEstado(), obtenido.getEstado());
    }

    @Test
    @DisplayName("El proxy cachea: la segunda consulta del mismo id devuelve la MISMA instancia")
    void proxyCacheaYDevuelveMismaInstancia() {
        RepositorioEnvios proxy = new ProxyRepositorioEnvios();

        EnvioSimple primera = proxy.obtenerEnvio("ENV-002");
        EnvioSimple segunda = proxy.obtenerEnvio("ENV-002");

        assertNotNull(primera);
        // Mismo objeto cacheado -> assertSame demuestra el cache hit
        assertSame(primera, segunda);
    }

    @Test
    @DisplayName("El proxy devuelve null para un id inexistente, igual que el real")
    void proxyDevuelveNullParaIdInexistente() {
        RepositorioEnvios proxy = new ProxyRepositorioEnvios();

        EnvioSimple inexistente = proxy.obtenerEnvio("NO-EXISTE");

        assertNull(inexistente);
    }

    @Test
    @DisplayName("obtenerEnvios delega al real y devuelve la lista completa de datos iniciales")
    void obtenerEnviosDevuelveListaCompleta() {
        RepositorioEnvios proxy = new ProxyRepositorioEnvios();

        List<EnvioSimple> envios = proxy.obtenerEnvios();

        assertNotNull(envios);
        assertEquals(5, envios.size());
    }

    @Test
    @DisplayName("guardarEnvio persiste a través del proxy y queda disponible para consulta posterior")
    void guardarEnvioPersisteYQuedaDisponible() {
        ProxyRepositorioEnvios proxy = new ProxyRepositorioEnvios();
        EnvioSimple nuevo = new EnvioSimple("ENV-NEW", "Salta", "Jujuy", 7.5, "PENDIENTE");

        proxy.guardarEnvio(nuevo);
        EnvioSimple recuperado = proxy.obtenerEnvio("ENV-NEW");

        assertNotNull(recuperado);
        assertSame(nuevo, recuperado);
        assertEquals("ENV-NEW", recuperado.getId());
    }

    @Test
    @DisplayName("eliminarEnvio remueve el envío del repositorio a través del proxy")
    void eliminarEnvioRemueveDelRepositorio() {
        ProxyRepositorioEnvios proxy = new ProxyRepositorioEnvios();

        // Aseguramos que existe antes de borrar
        assertNotNull(proxy.obtenerEnvio("ENV-003"));

        proxy.eliminarEnvio("ENV-003");
        // Tras eliminar, la lista cacheada se invalida y se reconsulta el real
        List<EnvioSimple> restantes = proxy.obtenerEnvios();

        boolean sigueExistiendo = restantes.stream()
                .anyMatch(e -> e.getId().equals("ENV-003"));
        assertFalse(sigueExistiendo);
        assertEquals(4, restantes.size());
    }

    @Test
    @DisplayName("La lista de envíos se cachea: dos llamadas consecutivas tienen el mismo contenido")
    void listaDeEnviosSeCachea() {
        ProxyRepositorioEnvios proxy = new ProxyRepositorioEnvios();

        List<EnvioSimple> primera = proxy.obtenerEnvios();
        List<EnvioSimple> segunda = proxy.obtenerEnvios();

        assertEquals(primera.size(), segunda.size());
        // El cache de lista almacena las mismas instancias de envío
        for (int i = 0; i < primera.size(); i++) {
            assertSame(primera.get(i), segunda.get(i));
        }
    }
}
