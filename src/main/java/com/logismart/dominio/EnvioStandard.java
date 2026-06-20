package com.logismart.dominio;

public class EnvioStandard extends Envio {
    public static final int PRIORIDAD = 2;
    public static final int DIAS_ENTREGA = 5;
    public static final double MULTIPLICADOR_COSTO = 1.0;

    public EnvioStandard(Envio.EnvioBuilder builder) {
        super(builder);
    }

    public int getPrioridad() { return PRIORIDAD; }
    public int getDiasEntrega() { return DIAS_ENTREGA; }
    public double getMultiplicadorCosto() { return MULTIPLICADOR_COSTO; }
}
