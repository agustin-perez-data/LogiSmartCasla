package com.logismart.dominio.distribucion;

public abstract class CentroDistribucion {
    protected String nombre;
    protected String ubicacion;
    protected String codigo;

    public CentroDistribucion(String nombre, String ubicacion, String codigo) {
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.codigo = codigo;
    }

    public abstract int obtenerCapacidad();
    public abstract int obtenerOcupacion();
    public abstract int contarPuntosDeEntrega();
    public abstract void mostrar(int profundidad);

    public double obtenerPorcentajeOcupacion() {
        int capacidad = obtenerCapacidad();
        if (capacidad == 0) return 0.0;
        return (double) obtenerOcupacion() / capacidad * 100;
    }

    public boolean tieneCapacidadDisponible() {
        return obtenerOcupacion() < obtenerCapacidad();
    }

    public String getNombre() { return nombre; }
    public String getUbicacion() { return ubicacion; }
    public String getCodigo() { return codigo; }

    @Override
    public String toString() {
        return nombre + " [" + codigo + "] (" +
               obtenerOcupacion() + "/" + obtenerCapacidad() + ")";
    }
}
