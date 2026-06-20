package com.logismart.dominio.distribucion;

import java.util.ArrayList;
import java.util.List;

public class CentroRegional extends CentroDistribucion {
    private final List<CentroDistribucion> subcentros;

    public CentroRegional(String nombre, String ubicacion, String codigo) {
        super(nombre, ubicacion, codigo);
        this.subcentros = new ArrayList<>();
    }

    public void agregar(CentroDistribucion centro) {
        subcentros.add(centro);
    }

    public void remover(CentroDistribucion centro) {
        subcentros.remove(centro);
    }

    public List<CentroDistribucion> obtenerSubcentros() {
        return new ArrayList<>(subcentros);
    }

    @Override
    public int obtenerCapacidad() {
        int total = 0;
        for (CentroDistribucion centro : subcentros) {
            total += centro.obtenerCapacidad();
        }
        return total;
    }

    @Override
    public int obtenerOcupacion() {
        int total = 0;
        for (CentroDistribucion centro : subcentros) {
            total += centro.obtenerOcupacion();
        }
        return total;
    }

    @Override
    public int contarPuntosDeEntrega() {
        int total = 0;
        for (CentroDistribucion centro : subcentros) {
            total += centro.contarPuntosDeEntrega();
        }
        return total;
    }

    @Override
    public void mostrar(int profundidad) {
        String indent = "  ".repeat(profundidad);
        String icono = profundidad == 0 ? "🏭" : "🏢";
        System.out.println(indent + icono + " " + nombre +
            " (" + obtenerOcupacion() + "/" + obtenerCapacidad() + ")" +
            " [" + codigo + "]" +
            " — " + String.format("%.1f", obtenerPorcentajeOcupacion()) + "% ocupado");

        for (CentroDistribucion centro : subcentros) {
            centro.mostrar(profundidad + 1);
        }
    }

    public PuntoEntrega buscarPuntoDisponible() {
        for (CentroDistribucion centro : subcentros) {
            if (centro instanceof PuntoEntrega punto) {
                if (punto.tieneCapacidadDisponible()) {
                    return punto;
                }
            } else if (centro instanceof CentroRegional regional) {
                PuntoEntrega encontrado = regional.buscarPuntoDisponible();
                if (encontrado != null) {
                    return encontrado;
                }
            }
        }
        return null;
    }
}
