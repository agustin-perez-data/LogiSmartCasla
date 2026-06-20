package com.logismart.factory;

import com.logismart.dominio.Admin;
import com.logismart.dominio.Auto;
import com.logismart.dominio.Camion;
import com.logismart.dominio.Camioneta;
import com.logismart.dominio.Cliente;
import com.logismart.dominio.Conductor;
import com.logismart.dominio.Envio;
import com.logismart.dominio.EnvioEconomico;
import com.logismart.dominio.EnvioExpress;
import com.logismart.dominio.EnvioStandard;
import com.logismart.dominio.Moto;
import com.logismart.dominio.Operador;
import com.logismart.dominio.TipoEnvio;
import com.logismart.dominio.TipoEstrategia;
import com.logismart.dominio.TipoNotificador;
import com.logismart.dominio.TipoUsuario;
import com.logismart.dominio.TipoVehiculo;
import com.logismart.dominio.Usuario;
import com.logismart.dominio.Vehiculo;
import com.logismart.servicios.CalculadorCostosArgentina;
import com.logismart.servicios.CalculadorCostosBrasil;
import com.logismart.servicios.EstrategiaDeCostos;
import com.logismart.servicios.EstrategiaPorKilometro;
import com.logismart.servicios.EstrategiaPorPeso;
import com.logismart.servicios.EstrategiaTarifaFija;
import com.logismart.servicios.GoogleMapsArgentina;
import com.logismart.servicios.HereMaps;
import com.logismart.servicios.ManualProvider;
import com.logismart.servicios.MercadoLibreProvider;
import com.logismart.servicios.Notificador;
import com.logismart.servicios.ProveedorDeIntegracion;
import com.logismart.servicios.TiendanubeProvider;
import com.logismart.singleton.ConfiguracionLogiSmart;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Tests de los patrones creacionales Factory / Abstract Factory")
class FactoryTest {

    // -------------------- EnvioFactory (Factory Method) --------------------

    @Test
    @DisplayName("EnvioFactory crea el subtipo concreto correcto segun el TipoEnvio")
    void envioFactoryCreaSubtipoConcretoSegunTipo() {
        Envio express = EnvioFactory.crearEnvio(TipoEnvio.EXPRESS, "S-1", "Origen", "Destino",
                10.0, LocalDate.now(), "08:00", "12:00", "Calle 1", -34.6, -58.4, 1);
        Envio standard = EnvioFactory.crearEnvio(TipoEnvio.STANDARD, "S-2", "Origen", "Destino",
                10.0, LocalDate.now(), "08:00", "12:00", "Calle 1", -34.6, -58.4, 1);
        Envio economico = EnvioFactory.crearEnvio(TipoEnvio.ECONOMICO, "S-3", "Origen", "Destino",
                10.0, LocalDate.now(), "08:00", "12:00", "Calle 1", -34.6, -58.4, 1);

        assertInstanceOf(EnvioExpress.class, express);
        assertInstanceOf(EnvioStandard.class, standard);
        assertInstanceOf(EnvioEconomico.class, economico);
    }

    @Test
    @DisplayName("EnvioFactory transfiere correctamente los datos del builder al envio creado")
    void envioFactoryTransfiereDatosAlEnvio() {
        Envio envio = EnvioFactory.crearEnvio(TipoEnvio.STANDARD, "SHIP-99", "Buenos Aires",
                "Cordoba", 42.5, LocalDate.now(), "09:00", "18:00", "Av Siempreviva 742",
                -31.4, -64.2, 7);

        assertNotNull(envio);
        assertEquals("SHIP-99", envio.getShipmentId());
        assertEquals("Buenos Aires", envio.getOrigen());
        assertEquals("Cordoba", envio.getDestino());
        assertEquals(42.5, envio.getPeso());
    }

    @Test
    @DisplayName("EnvioFactory lanza IllegalArgumentException si el peso supera el maximo permitido")
    void envioFactoryRechazaPesoExcesivo() {
        double maxPeso = ConfiguracionLogiSmart.obtenerInstancia().getMaxPesoTotal();
        double pesoInvalido = maxPeso + 1.0;

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                EnvioFactory.crearEnvio(TipoEnvio.EXPRESS, "S-X", "A", "B", pesoInvalido,
                        LocalDate.now(), "08:00", "12:00", "Calle", 0.0, 0.0, 1));
        assertTrue(ex.getMessage().contains("supera el maximo"));
    }

    // -------------------- VehiculoFactory --------------------

    @Test
    @DisplayName("VehiculoFactory crea el tipo concreto de vehiculo segun el TipoVehiculo")
    void vehiculoFactoryCreaTipoConcreto() {
        Vehiculo moto = VehiculoFactory.crearVehiculo(TipoVehiculo.MOTO, "AAA111", 20.0, 1);
        Vehiculo camioneta = VehiculoFactory.crearVehiculo(TipoVehiculo.CAMIONETA, "BBB222", 500.0, 1);
        Vehiculo camion = VehiculoFactory.crearVehiculo(TipoVehiculo.CAMION, "CCC333", 3000.0, 1);

        assertInstanceOf(Moto.class, moto);
        assertInstanceOf(Camioneta.class, camioneta);
        assertInstanceOf(Camion.class, camion);
        assertEquals("AAA111", moto.getPatente());
        assertEquals(500.0, camioneta.getCapacidad());
    }

    // -------------------- UsuarioFactory --------------------

    @Test
    @DisplayName("UsuarioFactory crea el subtipo de usuario correcto y conserva los datos basicos")
    void usuarioFactoryCreaSubtipoCorrecto() {
        Usuario cliente = UsuarioFactory.crearUsuario(TipoUsuario.CLIENTE, 1, "Ana", "ana@x.com",
                "hash", "111", 10, "EmpresaX");
        Usuario operador = UsuarioFactory.crearUsuario(TipoUsuario.OPERADOR, 2, "Beto", "beto@x.com",
                "hash", "222", 10, "");
        Usuario conductor = UsuarioFactory.crearUsuario(TipoUsuario.CONDUCTOR, 3, "Carla", "carla@x.com",
                "hash", "333", 10, "");
        Usuario admin = UsuarioFactory.crearUsuario(TipoUsuario.ADMIN, 4, "Dario", "dario@x.com",
                "hash", "444", 10, "");

        assertInstanceOf(Cliente.class, cliente);
        assertInstanceOf(Operador.class, operador);
        assertInstanceOf(Conductor.class, conductor);
        assertInstanceOf(Admin.class, admin);
        assertEquals("Ana", cliente.getNombre());
        assertEquals("dario@x.com", admin.getEmail());
    }

    // -------------------- NotificadorFactory --------------------

    @Test
    @DisplayName("NotificadorFactory devuelve un Notificador no nulo para cada tipo soportado")
    void notificadorFactoryDevuelveInstanciaPorTipo() {
        Notificador email = NotificadorFactory.crearNotificador(TipoNotificador.EMAIL);
        Notificador sms = NotificadorFactory.crearNotificador(TipoNotificador.SMS);
        Notificador push = NotificadorFactory.crearNotificador(TipoNotificador.PUSH);

        assertNotNull(email);
        assertNotNull(sms);
        assertNotNull(push);
        assertNotSame(email, sms);
    }

    // -------------------- EstrategiaDeCostosFactory --------------------

    @Test
    @DisplayName("EstrategiaDeCostosFactory crea la estrategia concreta segun el TipoEstrategia")
    void estrategiaFactoryCreaEstrategiaConcreta() {
        EstrategiaDeCostos porKm = EstrategiaDeCostosFactory.crearEstrategia(TipoEstrategia.POR_KILOMETRO);
        EstrategiaDeCostos porPeso = EstrategiaDeCostosFactory.crearEstrategia(TipoEstrategia.POR_PESO);
        EstrategiaDeCostos tarifaFija = EstrategiaDeCostosFactory.crearEstrategia(TipoEstrategia.TARIFA_FIJA);

        assertInstanceOf(EstrategiaPorKilometro.class, porKm);
        assertInstanceOf(EstrategiaPorPeso.class, porPeso);
        assertInstanceOf(EstrategiaTarifaFija.class, tarifaFija);
    }

    // -------------------- ProveedorDeIntegracionFactory --------------------

    @Test
    @DisplayName("ProveedorDeIntegracionFactory resuelve el proveedor registrado y falla con uno desconocido")
    void proveedorFactoryResuelveProveedorRegistrado() {
        ProveedorDeIntegracion meli = ProveedorDeIntegracionFactory.crearProveedor("MERCADOLIBRE");
        ProveedorDeIntegracion tn = ProveedorDeIntegracionFactory.crearProveedor("TIENDANUBE");
        ProveedorDeIntegracion manual = ProveedorDeIntegracionFactory.crearProveedor("MANUAL");

        assertInstanceOf(MercadoLibreProvider.class, meli);
        assertInstanceOf(TiendanubeProvider.class, tn);
        assertInstanceOf(ManualProvider.class, manual);

        assertThrows(IllegalArgumentException.class,
                () -> ProveedorDeIntegracionFactory.crearProveedor("INEXISTENTE"));
    }

    // -------------------- Abstract Factory (LogiSmartFactory) --------------------

    @Test
    @DisplayName("LogiSmartFactoryArgentina produce la familia de objetos de Argentina")
    void abstractFactoryArgentinaProduceFamiliaArgentina() {
        LogiSmartFactory factory = new LogiSmartFactoryArgentina();

        Vehiculo vehiculo = factory.crearVehiculo();
        assertInstanceOf(Auto.class, vehiculo);
        assertInstanceOf(CalculadorCostosArgentina.class, factory.crearCalculadorCostos());
        assertInstanceOf(GoogleMapsArgentina.class, factory.crearProveedorMapas());
    }

    @Test
    @DisplayName("LogiSmartFactoryBrasil produce la familia de objetos de Brasil")
    void abstractFactoryBrasilProduceFamiliaBrasil() {
        LogiSmartFactory factory = new LogiSmartFactoryBrasil();

        Vehiculo vehiculo = factory.crearVehiculo();
        assertInstanceOf(Moto.class, vehiculo);
        assertInstanceOf(CalculadorCostosBrasil.class, factory.crearCalculadorCostos());
        assertInstanceOf(HereMaps.class, factory.crearProveedorMapas());
    }
}
