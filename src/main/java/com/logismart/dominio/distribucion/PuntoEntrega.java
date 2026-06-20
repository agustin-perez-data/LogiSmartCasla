package com.logismart.dominio.distribucion;

import com.logismart.dominio.Envio;
import java.util.ArrayList;
import java.util.List;

public class PuntoEntrega extends CentroDistribucion {
    private final int capacidad;
    private final List<Envio> enviosAlmacenados;

    public PuntoEntrega(String nombre, String ubicacion, String codigo, int capacidad) {
        super(nombre, ubicacion, codigo);
        this.capacidad = capacidad;
        this.enviosAlmacenados = new ArrayList<>();
    }

    public void agregarEnvio(Envio envio) {
        if (enviosAlmacenados.size() >= capacidad) {
            throw new IllegalStateException(
                "Capacidad excedida en " + nombre + " (" + codigo + "): "
                + enviosAlmacenados.size() + "/" + capacidad);
        }
        enviosAlmacenados.add(envio);
    }

    public void removerEnvio(Envio envio) {
        enviosAlmacenados.remove(envio);
    }

    public List<Envio> getEnviosAlmacenados() {
        return new ArrayList<>(enviosAlmacenados);
    }

    @Override
    public int obtenerCapacidad() {
        return capacidad;
    }

    @Override
    public int obtenerOcupacion() {
        return enviosAlmacenados.size();
    }

    @Override
    public int contarPuntosDeEntrega() {
        return 1;
    }

    @Override
    public void mostrar(int profundidad) {
        String indent = "  ".repeat(profundidad);
        System.out.println(indent + "📍 " + nombre +
            " (" + obtenerOcupacion() + "/" + capacidad + ")" +
            " [" + codigo + "]");
    }
}
