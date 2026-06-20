package com.logismart.command;

import com.logismart.dominio.Envio;
import java.util.HashMap;
import java.util.Map;

/**
 * Receptor del patrón Command.
 * Simula la lógica de negocio sobre envíos en memoria.
 */
public class ServicioEnvios {

    private final Map<String, String> estados       = new HashMap<>();
    private final Map<String, String> metodosPago   = new HashMap<>();
    private final Map<String, String> servicios     = new HashMap<>();
    private int contador = 0;

    public String crearEnvio(Envio envio) {
        String numero = "ENV-" + String.format("%03d", ++contador);
        estados.put(numero, "CONFIRMADO");
        metodosPago.put(numero, envio.getMetodoPago() != null ? envio.getMetodoPago() : "");
        System.out.println("  [Servicio] Envío creado: " + numero);
        return numero;
    }

    public void cancelarEnvio(String numero) {
        estados.put(numero, "CANCELADO");
        System.out.println("  [Servicio] Envío cancelado: " + numero);
    }

    public void reactivarEnvio(String numero) {
        estados.put(numero, "CONFIRMADO");
        System.out.println("  [Servicio] Envío reactivado: " + numero);
    }

    public String obtenerEstado(String numero) {
        return estados.getOrDefault(numero, "DESCONOCIDO");
    }

    public void actualizarEstado(String numero, String nuevoEstado) {
        estados.put(numero, nuevoEstado);
        System.out.println("  [Servicio] Estado de " + numero + " → " + nuevoEstado);
    }

    public String obtenerMetodoPago(String numero) {
        return metodosPago.getOrDefault(numero, "");
    }

    public void cambiarMetodoPago(String numero, String nuevoMetodo) {
        metodosPago.put(numero, nuevoMetodo);
        System.out.println("  [Servicio] Método de pago de " + numero + " → " + nuevoMetodo);
    }

    public void agregarServicio(String numero, String nombreServicio) {
        servicios.put(numero + "_" + nombreServicio, nombreServicio);
        System.out.println("  [Servicio] Servicio agregado a " + numero + ": " + nombreServicio);
    }

    public void removerServicio(String numero, String nombreServicio) {
        servicios.remove(numero + "_" + nombreServicio);
        System.out.println("  [Servicio] Servicio removido de " + numero + ": " + nombreServicio);
    }
}
