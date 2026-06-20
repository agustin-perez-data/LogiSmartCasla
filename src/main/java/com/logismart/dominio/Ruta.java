package com.logismart.dominio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Ruta {
    private static final double RADIO_TIERRA_KM = 6371.0;

    private int id;
    private LocalDate fecha;
    private EstadoRuta estado;
    private int usuarioConductorId;
    private int vehiculoId;
    private List<Envio> envios;

    public Ruta(int id, LocalDate fecha, EstadoRuta estado, int usuarioConductorId, int vehiculoId) {
        this.id = id;
        this.fecha = fecha;
        this.estado = estado;
        this.usuarioConductorId = usuarioConductorId;
        this.vehiculoId = vehiculoId;
        this.envios = new ArrayList<>();
    }

    public void agregarEnvio(Envio envio) {
        envios.add(envio);
    }

    public void removerEnvio(int envioId) {
        envios.removeIf(e -> e.getNumericId() == envioId);
    }

    public boolean todosEntregados() {
        return !envios.isEmpty() &&
               envios.stream().allMatch(e -> e.getEstado() == EstadoEnvio.ENTREGADO);
    }

    public void actualizarEstado(EstadoRuta nuevoEstado) {
        if (!estado.puedeTransicionarA(nuevoEstado)) {
            throw new IllegalStateException(
                "Transicion invalida: " + estado + " -> " + nuevoEstado);
        }
        this.estado = nuevoEstado;
    }

    // Haversine formula — returns total distance in km between consecutive envio stops
    public double calcularDistanciaTotal() {
        double total = 0.0;
        for (int i = 0; i < envios.size() - 1; i++) {
            total += haversine(
                envios.get(i).getLatitud(),     envios.get(i).getLongitud(),
                envios.get(i + 1).getLatitud(), envios.get(i + 1).getLongitud()
            );
        }
        return total;
    }

    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                 + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                 * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return RADIO_TIERRA_KM * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public EstadoRuta getEstado() { return estado; }
    public void setEstado(EstadoRuta estado) { this.estado = estado; }

    public int getUsuarioConductorId() { return usuarioConductorId; }
    public void setUsuarioConductorId(int usuarioConductorId) { this.usuarioConductorId = usuarioConductorId; }

    public int getVehiculoId() { return vehiculoId; }
    public void setVehiculoId(int vehiculoId) { this.vehiculoId = vehiculoId; }

    public List<Envio> getEnvios() { return envios; }
    public void setEnvios(List<Envio> envios) {
        this.envios = envios != null ? envios : new ArrayList<>();
    }
}
