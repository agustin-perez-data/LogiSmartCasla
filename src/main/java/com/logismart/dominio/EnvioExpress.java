package com.logismart.dominio;

public class EnvioExpress extends Envio {
    public static final int PRIORIDAD = 1;
    public static final int DIAS_ENTREGA = 1;
    public static final double MULTIPLICADOR_COSTO = 1.5;

    public EnvioExpress(Envio.EnvioBuilder builder) {
        super(builder);
    }

    public int getPrioridad() { return PRIORIDAD; }
    public int getDiasEntrega() { return DIAS_ENTREGA; }
    public double getMultiplicadorCosto() { return MULTIPLICADOR_COSTO; }
}
