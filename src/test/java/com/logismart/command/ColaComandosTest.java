package com.logismart.command;

import com.logismart.dominio.Envio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Command - ColaComandos con undo/redo y ServicioEnvios")
class ColaComandosTest {

    private ServicioEnvios servicio;
    private ColaComandos cola;

    @BeforeEach
    void setUp() {
        servicio = new ServicioEnvios();
        cola = new ColaComandos();
    }

    private Envio nuevoEnvio() {
        return new Envio.EnvioBuilder("E1", "Buenos Aires", "Cordoba")
                .peso(10.0).costo(500.0).metodoPago("TARJETA").build();
    }

    @Test
    @DisplayName("Ejecutar ComandoCrearEnvio crea el envio y la cola registra el comando")
    void ejecutarCrearEnvioTieneEfecto() {
        ComandoCrearEnvio comando = new ComandoCrearEnvio(servicio, nuevoEnvio());
        cola.ejecutar(comando);

        String numero = comando.getNumeroSeguimiento();
        assertEquals("ENV-001", numero, "El primer envio creado debe ser ENV-001");
        assertEquals("CONFIRMADO", servicio.obtenerEstado(numero),
                "Un envio recien creado queda CONFIRMADO");
        assertEquals(1, cola.obtenerTamaño(), "La cola debe tener un comando");
    }

    @Test
    @DisplayName("Deshacer ComandoCrearEnvio revierte el efecto (cancela el envio)")
    void deshacerRevierteElComando() {
        ComandoCrearEnvio comando = new ComandoCrearEnvio(servicio, nuevoEnvio());
        cola.ejecutar(comando);
        String numero = comando.getNumeroSeguimiento();

        cola.deshacer();
        assertEquals("CANCELADO", servicio.obtenerEstado(numero),
                "Deshacer la creacion debe cancelar el envio");
    }

    @Test
    @DisplayName("Rehacer reaplica el comando deshecho")
    void rehacerReaplicaElComando() {
        String numero = "ENV-001";
        // creamos manualmente para tener un numero conocido
        cola.ejecutar(new ComandoCrearEnvio(servicio, nuevoEnvio()));
        cola.ejecutar(new ComandoActualizarEstado(servicio, numero, "EN_TRANSITO"));
        assertEquals("EN_TRANSITO", servicio.obtenerEstado(numero));

        cola.deshacer(); // revierte a CONFIRMADO
        assertEquals("CONFIRMADO", servicio.obtenerEstado(numero),
                "Deshacer debe restaurar el estado anterior");

        cola.rehacer(); // vuelve a EN_TRANSITO
        assertEquals("EN_TRANSITO", servicio.obtenerEstado(numero),
                "Rehacer debe reaplicar la actualizacion de estado");
    }

    @Test
    @DisplayName("ComandoCambiarMetodoPago: ejecutar cambia y deshacer restaura el metodo previo")
    void cambiarMetodoPagoYDeshacer() {
        ComandoCrearEnvio crear = new ComandoCrearEnvio(servicio, nuevoEnvio());
        cola.ejecutar(crear);
        String numero = crear.getNumeroSeguimiento();
        assertEquals("TARJETA", servicio.obtenerMetodoPago(numero));

        cola.ejecutar(new ComandoCambiarMetodoPago(servicio, numero, "EFECTIVO"));
        assertEquals("EFECTIVO", servicio.obtenerMetodoPago(numero),
                "El comando debe cambiar el metodo de pago");

        cola.deshacer();
        assertEquals("TARJETA", servicio.obtenerMetodoPago(numero),
                "Deshacer debe restaurar el metodo de pago anterior");
    }

    @Test
    @DisplayName("Ejecutar un comando nuevo tras deshacer descarta la rama rehacer")
    void ejecutarNuevoDescartaRamaRehacer() {
        ComandoCrearEnvio crear = new ComandoCrearEnvio(servicio, nuevoEnvio());
        cola.ejecutar(crear);
        String numero = crear.getNumeroSeguimiento();

        cola.ejecutar(new ComandoActualizarEstado(servicio, numero, "EN_TRANSITO"));
        cola.deshacer(); // deshace la actualizacion; indice queda en crear

        // ejecutar uno nuevo descarta el comando deshecho del historial
        cola.ejecutar(new ComandoAgregarServicio(servicio, numero, "SEGURO"));
        assertEquals(2, cola.obtenerTamaño(),
                "Tras deshacer y ejecutar uno nuevo, el historial tiene crear + agregarServicio");
    }
}
