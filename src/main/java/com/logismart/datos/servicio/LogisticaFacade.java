package com.logismart.datos.servicio;

import com.logismart.datos.dominio.CentroDistribucion;
import com.logismart.datos.dominio.Cliente;
import com.logismart.datos.dominio.Envio;
import com.logismart.datos.dominio.Pago;

public class LogisticaFacade {
    private final ServicioEnvios servicioEnvios;
    private final ServicioClientes servicioClientes;
    private final ServicioCentros servicioCentros;
    private final ServicioPagos servicioPagos;

    public LogisticaFacade(ServicioEnvios servicioEnvios,
                           ServicioClientes servicioClientes,
                           ServicioCentros servicioCentros,
                           ServicioPagos servicioPagos) {
        this.servicioEnvios = servicioEnvios;
        this.servicioClientes = servicioClientes;
        this.servicioCentros = servicioCentros;
        this.servicioPagos = servicioPagos;
    }

    public void procesarEnvioCompleto(int envioId, String origen, String destino, double peso,
                                      int clienteId, String nombre, String email, String telefono,
                                      int pagoId, double monto) {
        System.out.println("\n[Facade] === Procesando envio completo ===");
        Cliente cliente = servicioClientes.crearCliente(clienteId, nombre, email, telefono);
        Envio envio = servicioEnvios.crearEnvio(envioId, origen, destino, peso);
        Pago pago = servicioPagos.procesarPago(pagoId, envioId, monto);
        System.out.println("[Facade] Proceso completo: " + envio + " | " + cliente + " | " + pago);
    }

    public Envio consultarEnvio(int id) {
        return servicioEnvios.obtenerEnvio(id);
    }

    public Cliente consultarCliente(int id) {
        return servicioClientes.obtenerCliente(id);
    }

    public CentroDistribucion consultarCentro(int id) {
        return servicioCentros.obtenerCentro(id);
    }
}
