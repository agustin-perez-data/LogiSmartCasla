package com.logismart.dominio;

import com.logismart.memento.MementoEnvio;
import com.logismart.observer.ObservadorEnvio;
import com.logismart.strategy.EstrategiaCalculoCosto;
import com.logismart.strategy.EstrategiaDistancia;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Envio implements Cloneable {

    // --- Campos requeridos ---
    private String id;                // shipmentId externo (no-final para soportar Prototype)
    private String origen;
    private String destino;

    // --- Campos opcionales (actividad Builder) ---
    private final String descripcion;
    private double peso;
    private final boolean fragil;
    private final boolean requiereSignatura;
    private final boolean requiereRefrigeracion;
    private final boolean requiereAseguranza;
    private final String instruccionesEspeciales;
    private final String contactoEmergencia;
    private final LocalTime horaEntregaPreferida;

    // --- Campos del dominio logístico ---
    private EstadoEnvio estado;
    private com.logismart.state.EstadoEnvio estadoComportamiento;
    private final LocalDate fechaEstimadaEntrega;
    private final String ventanaHorariaInicio;
    private final String ventanaHorariaFin;
    private final String direccionEntrega;
    private final double latitud;
    private final double longitud;
    private final int destinatarioId;
    private int rutaId;
    private int userDriverId;
    private int numericId;            // clave interna de secuencia
    private String numeroSeguimiento; // codigo del proveedor externo (Adapter - Hito 8)

    // --- Campos para validación (Chain of Responsibility - Hito 10) ---
    private double costo;
    private String metodoPago;
    private String productoId;
    private String tipo;
    private EstrategiaCalculoCosto estrategiaCalculoCosto;

    // --- Observer (Hito 11) ---
    private List<ObservadorEnvio> observadores = new ArrayList<>();

    // Constructor privado — solo accesible via Builder o subclases
    protected Envio(EnvioBuilder builder) {
        this.id                    = builder.id;
        this.origen                = builder.origen;
        this.destino               = builder.destino;
        this.descripcion           = builder.descripcion;
        this.peso                  = builder.peso;
        this.fragil                = builder.fragil;
        this.requiereSignatura     = builder.requiereSignatura;
        this.requiereRefrigeracion = builder.requiereRefrigeracion;
        this.requiereAseguranza    = builder.requiereAseguranza;
        this.instruccionesEspeciales = builder.instruccionesEspeciales;
        this.contactoEmergencia    = builder.contactoEmergencia;
        this.horaEntregaPreferida  = builder.horaEntregaPreferida;
        this.estado                = builder.estado;
        this.estadoComportamiento  = crearEstadoComportamiento(builder.estado);
        this.fechaEstimadaEntrega  = builder.fechaEstimadaEntrega;
        this.ventanaHorariaInicio  = builder.ventanaHorariaInicio;
        this.ventanaHorariaFin     = builder.ventanaHorariaFin;
        this.direccionEntrega      = builder.direccionEntrega;
        this.latitud               = builder.latitud;
        this.longitud              = builder.longitud;
        this.destinatarioId        = builder.destinatarioId;
        this.rutaId                = builder.rutaId;
        this.userDriverId          = builder.userDriverId;
        this.numericId             = builder.numericId;
        this.costo                 = builder.costo;
        this.metodoPago            = builder.metodoPago;
        this.productoId            = builder.productoId;
        this.tipo                  = builder.tipo;
        this.estrategiaCalculoCosto = builder.estrategiaCalculoCosto != null
                ? builder.estrategiaCalculoCosto
                : new EstrategiaDistancia();
    }

    public void actualizarEstado(EstadoEnvio nuevoEstado) {
        if (!estado.puedeTransicionarA(nuevoEstado)) {
            throw new IllegalStateException(
                "Transicion invalida: " + estado + " -> " + nuevoEstado);
        }
        this.estado = nuevoEstado;
        this.estadoComportamiento = crearEstadoComportamiento(nuevoEstado);
        notificarObservadores(nuevoEstado.name());
    }

    // --- Getters ---
    public String getId()                    { return id; }
    public String getShipmentId()            { return id; }
    public String getOrigen()               { return origen; }
    public String getDestino()              { return destino; }
    public String getDescripcion()          { return descripcion; }
    public double getPeso()                 { return peso; }
    public boolean isFragil()               { return fragil; }
    public boolean isRequiereSignatura()    { return requiereSignatura; }
    public boolean isRequiereRefrigeracion(){ return requiereRefrigeracion; }
    public boolean isRequiereAseguranza()   { return requiereAseguranza; }
    public String getInstruccionesEspeciales() { return instruccionesEspeciales; }
    public String getContactoEmergencia()   { return contactoEmergencia; }
    public LocalTime getHoraEntregaPreferida() { return horaEntregaPreferida; }
    public EstadoEnvio getEstado()          { return estado; }
    public LocalDate getFechaEstimadaEntrega() { return fechaEstimadaEntrega; }
    public String getVentanaHorariaInicio() { return ventanaHorariaInicio; }
    public String getVentanaHorariaFin()    { return ventanaHorariaFin; }
    public String getDireccionEntrega()     { return direccionEntrega; }
    public double getLatitud()              { return latitud; }
    public double getLongitud()             { return longitud; }
    public int getDestinatarioId()          { return destinatarioId; }
    public int getRutaId()                  { return rutaId; }
    public int getUserDriverId()            { return userDriverId; }
    public int getNumericId()               { return numericId; }

    public void setRutaId(int rutaId)       { this.rutaId = rutaId; }
    public void setUserDriverId(int id)     { this.userDriverId = id; }
    public void setId(int id)               { this.numericId = id; }
    public void setId(String id)            { this.id = id; }

    // --- Adapter (Hito 8): codigo de seguimiento del proveedor externo ---
    public String getNumeroSeguimiento()                  { return numeroSeguimiento; }
    public void setNumeroSeguimiento(String numero)       { this.numeroSeguimiento = numero; }

    // --- Getters Chain of Responsibility (Hito 10) ---
    public double getCosto()       { return costo; }
    public String getMetodoPago()  { return metodoPago; }
    public String getProductoId()  { return productoId; }
    public String getTipo()        { return tipo; }

    public void setCosto(double costo)           { this.costo = costo; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    // =========================================================
    // State (Hito 12)
    // =========================================================
    public void cambiarEstado(com.logismart.state.EstadoEnvio nuevoEstado) {
        EstadoEnvio estadoAnterior = this.estado;
        this.estadoComportamiento = nuevoEstado;
        this.estado = EstadoEnvio.valueOf(nuevoEstado.obtenerNombre());
        System.out.println("[Envio " + id + "] Estado: " + estadoAnterior + " -> " + this.estado);
        notificarObservadores(this.estado.name());
    }

    public void validar() {
        estadoComportamiento.validar(this);
    }

    public void entregar() {
        estadoComportamiento.entregar(this);
    }

    public void cancelar() {
        estadoComportamiento.cancelar(this);
    }

    public void retener() {
        estadoComportamiento.retener(this);
    }

    public void devolver() {
        estadoComportamiento.devolver(this);
    }

    public void reclamar() {
        estadoComportamiento.reclamar(this);
    }

    // =========================================================
    // Strategy (Hito 12)
    // =========================================================
    public void establecerEstrategia(EstrategiaCalculoCosto estrategia) {
        this.estrategiaCalculoCosto = estrategia;
        System.out.println("[Envio " + id + "] Estrategia cambiada a: "
                + estrategia.obtenerNombre());
    }

    public double calcularCosto() {
        return estrategiaCalculoCosto.calcular(this);
    }

    // =========================================================
    // Memento + Observer (Hito 11)
    // =========================================================
    public MementoEnvio crearMemento() {
        return new MementoEnvio(estado.name(), origen, destino, peso, costo);
    }

    public void restaurarDesdeMemento(MementoEnvio memento) {
        this.estado = EstadoEnvio.valueOf(memento.obtenerEstado());
        this.estadoComportamiento = crearEstadoComportamiento(this.estado);
        this.origen = memento.obtenerOrigen();
        this.destino = memento.obtenerDestino();
        this.peso = memento.obtenerPeso();
        this.costo = memento.obtenerCosto();
        notificarObservadores(this.estado.name());
    }

    public void cambiarEstado(String nuevoEstado) {
        EstadoEnvio estadoAnterior = this.estado;
        this.estado = EstadoEnvio.valueOf(nuevoEstado.trim().toUpperCase());
        this.estadoComportamiento = crearEstadoComportamiento(this.estado);
        System.out.println("[Envio " + id + "] Estado: " + estadoAnterior + " -> " + this.estado);
        notificarObservadores(this.estado.name());
    }

    public String obtenerEstado() {
        return estado.name();
    }

    public void adjuntarObservador(ObservadorEnvio observador) {
        if (observador != null && !observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    public void desadjuntarObservador(ObservadorEnvio observador) {
        observadores.remove(observador);
    }

    private void notificarObservadores(String evento) {
        for (ObservadorEnvio observador : new ArrayList<>(observadores)) {
            observador.actualizar(this, evento);
        }
    }

    private static com.logismart.state.EstadoEnvio crearEstadoComportamiento(EstadoEnvio estado) {
        return switch (estado) {
            case EN_TRANSITO, EN_CAMINO -> new com.logismart.state.EstadoEnTransito();
            case EN_REPARTO -> new com.logismart.state.EstadoEnReparto();
            case ENTREGADO -> new com.logismart.state.EstadoEntregado();
            case RETENIDO -> new com.logismart.state.EstadoRetenido();
            case CANCELADO -> new com.logismart.state.EstadoCancelado();
            default -> new com.logismart.state.EstadoConfirmado();
        };
    }

    // =========================================================
    // Prototype — Cloneable
    // =========================================================
    @Override
    public Envio clone() {
        try {
            Envio clonado = (Envio) super.clone();
            // La copia no debe compartir observadores con el prototipo.
            clonado.observadores = new ArrayList<>();
            return clonado;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Error al clonar Envio", e);
        }
    }

    // =========================================================
    // Builder anidado
    // =========================================================
    public static class EnvioBuilder {

        // Requeridos
        private final String id;
        private final String origen;
        private final String destino;

        // Opcionales — valores por defecto
        private String descripcion           = "";
        private double peso                  = 0.0;
        private boolean fragil               = false;
        private boolean requiereSignatura    = false;
        private boolean requiereRefrigeracion = false;
        private boolean requiereAseguranza   = false;
        private String instruccionesEspeciales = "";
        private String contactoEmergencia    = "";
        private LocalTime horaEntregaPreferida = null;

        // Opcionales del dominio logístico
        private EstadoEnvio estado           = EstadoEnvio.CONFIRMADO;
        private LocalDate fechaEstimadaEntrega = null;
        private String ventanaHorariaInicio  = "";
        private String ventanaHorariaFin     = "";
        private String direccionEntrega      = "";
        private double latitud               = 0.0;
        private double longitud              = 0.0;
        private int destinatarioId           = 0;
        private int rutaId                   = 0;
        private int userDriverId             = 0;
        private int numericId                = 0;

        // Chain of Responsibility (Hito 10)
        private double costo                 = 0.0;
        private String metodoPago            = "";
        private String productoId            = "";
        private String tipo                  = "NORMAL";
        private EstrategiaCalculoCosto estrategiaCalculoCosto = null;

        public EnvioBuilder(String id, String origen, String destino) {
            this.id      = id;
            this.origen  = origen;
            this.destino = destino;
        }

        public EnvioBuilder descripcion(String descripcion) {
            this.descripcion = descripcion; return this;
        }
        public EnvioBuilder peso(double peso) {
            this.peso = peso; return this;
        }
        public EnvioBuilder fragil(boolean fragil) {
            this.fragil = fragil; return this;
        }
        public EnvioBuilder requiereSignatura(boolean requiereSignatura) {
            this.requiereSignatura = requiereSignatura; return this;
        }
        public EnvioBuilder requiereRefrigeracion(boolean requiereRefrigeracion) {
            this.requiereRefrigeracion = requiereRefrigeracion; return this;
        }
        public EnvioBuilder requiereAseguranza(boolean requiereAseguranza) {
            this.requiereAseguranza = requiereAseguranza; return this;
        }
        public EnvioBuilder instruccionesEspeciales(String instrucciones) {
            this.instruccionesEspeciales = instrucciones; return this;
        }
        public EnvioBuilder contactoEmergencia(String contacto) {
            this.contactoEmergencia = contacto; return this;
        }
        public EnvioBuilder horaEntregaPreferida(LocalTime hora) {
            this.horaEntregaPreferida = hora; return this;
        }
        public EnvioBuilder estado(EstadoEnvio estado) {
            this.estado = estado; return this;
        }
        public EnvioBuilder fechaEstimadaEntrega(LocalDate fecha) {
            this.fechaEstimadaEntrega = fecha; return this;
        }
        public EnvioBuilder ventanaHoraria(String inicio, String fin) {
            this.ventanaHorariaInicio = inicio;
            this.ventanaHorariaFin   = fin;
            return this;
        }
        public EnvioBuilder direccionEntrega(String direccion) {
            this.direccionEntrega = direccion; return this;
        }
        public EnvioBuilder coordenadas(double latitud, double longitud) {
            this.latitud  = latitud;
            this.longitud = longitud;
            return this;
        }
        public EnvioBuilder destinatarioId(int destinatarioId) {
            this.destinatarioId = destinatarioId; return this;
        }
        public EnvioBuilder numericId(int id) {
            this.numericId = id; return this;
        }
        public EnvioBuilder costo(double costo) {
            this.costo = costo; return this;
        }
        public EnvioBuilder metodoPago(String metodoPago) {
            this.metodoPago = metodoPago; return this;
        }
        public EnvioBuilder productoId(String productoId) {
            this.productoId = productoId; return this;
        }
        public EnvioBuilder tipo(String tipo) {
            this.tipo = tipo; return this;
        }
        public EnvioBuilder estrategiaCalculoCosto(EstrategiaCalculoCosto estrategia) {
            this.estrategiaCalculoCosto = estrategia; return this;
        }

        public Envio build() {
            return new Envio(this);
        }
    }
}
