package com.logismart.datos.dominio;

import java.time.LocalDateTime;

public class Pago {
    private int id;
    private int envioId;
    private double monto;
    private LocalDateTime fecha;
    private EstadoPago estado;

    public Pago(int id, int envioId, double monto) {
        this.id = id;
        this.envioId = envioId;
        this.monto = monto;
        this.fecha = LocalDateTime.now();
        this.estado = EstadoPago.PENDIENTE;
    }

    public int getId() { return id; }
    public int getEnvioId() { return envioId; }
    public double getMonto() { return monto; }
    public LocalDateTime getFecha() { return fecha; }
    public EstadoPago getEstado() { return estado; }
    public void setEstado(EstadoPago estado) { this.estado = estado; }

    @Override
    public String toString() {
        return "Pago[" + id + "] envio=" + envioId + " $" + monto + " (" + estado + ")";
    }
}
