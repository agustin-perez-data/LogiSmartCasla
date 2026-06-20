package com.logismart.dominio;

public class EnvioEconomico extends Envio {
    public static final int PRIORIDAD = 3;
    public static final int DIAS_ENTREGA = 7;
    public static final double MULTIPLICADOR_COSTO = 0.7;

    public EnvioEconomico(Envio.EnvioBuilder builder) {
        super(builder);
    }

    public int getPrioridad() { return PRIORIDAD; }
    public int getDiasEntrega() { return DIAS_ENTREGA; }
    public double getMultiplicadorCosto() { return MULTIPLICADOR_COSTO; }
}
