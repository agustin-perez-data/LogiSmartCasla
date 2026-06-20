package com.logismart.servicios;

import com.logismart.dominio.Envio;
import com.logismart.dominio.distribucion.CentroDistribucion;
import com.logismart.servicios.adapters.*;
import com.logismart.servicios.reportes.*;
import com.logismart.singleton.LoggerLogiSmart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServicioLogisticaUnificado {

    // Adapter: proveedores externos con interfaces incompatibles
    private final Map<String, ProveedorEnvio> proveedoresEnvio;
    private final Map<String, ProveedorPago> proveedoresPago;

    // Composite: jerarquía de centros de distribución
    private final CentroDistribucion centroDistribucion;

    // Datos internos
    private final List<Envio> enviosRegistrados;
    private final Map<String, Integer> contadorPorProveedor;

    private final LoggerLogiSmart logger = LoggerLogiSmart.obtenerInstancia();

    public ServicioLogisticaUnificado(CentroDistribucion centroDistribucion) {
        this.centroDistribucion = centroDistribucion;
        this.enviosRegistrados = new ArrayList<>();
        this.proveedoresEnvio = new HashMap<>();
        this.proveedoresPago = new HashMap<>();
        this.contadorPorProveedor = new HashMap<>();

        // Registrar adaptadores de envío (Adapter)
        proveedoresEnvio.put("DHL", new AdapterDHL());
        proveedoresEnvio.put("FedEx", new AdapterFedEx());
        proveedoresEnvio.put("UPS", new AdapterUPS());

        // Registrar adaptadores de pago (Adapter)
        proveedoresPago.put("PayPal", new AdapterPayPal());
        proveedoresPago.put("Stripe", new AdapterStripe());

        logger.info("ServicioLogisticaUnificado inicializado con " +
                    proveedoresEnvio.size() + " proveedores de envío y " +
                    proveedoresPago.size() + " proveedores de pago");
    }

    // ==================================================================
    // ADAPTER: Envíos a través de proveedores externos
    // ==================================================================

    public boolean crearEnvio(String nombreProveedor, Envio envio) {
        ProveedorEnvio proveedor = proveedoresEnvio.get(nombreProveedor);
        if (proveedor == null) {
            throw new IllegalArgumentException("Proveedor no soportado: " + nombreProveedor);
        }

        if (proveedor.crearEnvio(envio)) {
            enviosRegistrados.add(envio);
            contadorPorProveedor.merge(nombreProveedor, 1, Integer::sum);
            logger.info("Envío creado via " + nombreProveedor +
                       " | seguimiento: " + envio.getNumeroSeguimiento());
            return true;
        }

        logger.warning("Falló creación de envío via " + nombreProveedor);
        return false;
    }

    public String obtenerEstadoEnvio(String nombreProveedor, String numeroSeguimiento) {
        ProveedorEnvio proveedor = proveedoresEnvio.get(nombreProveedor);
        if (proveedor == null) {
            throw new IllegalArgumentException("Proveedor no soportado: " + nombreProveedor);
        }
        return proveedor.obtenerEstado(numeroSeguimiento);
    }

    public double calcularCostoEnvio(String nombreProveedor, Envio envio) {
        ProveedorEnvio proveedor = proveedoresEnvio.get(nombreProveedor);
        if (proveedor == null) {
            throw new IllegalArgumentException("Proveedor no soportado: " + nombreProveedor);
        }
        return proveedor.calcularCosto(envio);
    }

    // ==================================================================
    // ADAPTER: Pagos a través de proveedores externos
    // ==================================================================

    public boolean procesarPago(String nombreProveedor, double monto, String referencia) {
        ProveedorPago proveedor = proveedoresPago.get(nombreProveedor);
        if (proveedor == null) {
            throw new IllegalArgumentException(
                "Proveedor de pago no soportado: " + nombreProveedor);
        }

        boolean exito = proveedor.procesarPago(monto, referencia);
        logger.info("Pago " + (exito ? "exitoso" : "fallido") +
                   " via " + nombreProveedor + " | $" + monto);
        return exito;
    }

    public String obtenerEstadoPago(String nombreProveedor, String idTransaccion) {
        ProveedorPago proveedor = proveedoresPago.get(nombreProveedor);
        if (proveedor == null) {
            throw new IllegalArgumentException(
                "Proveedor de pago no soportado: " + nombreProveedor);
        }
        return proveedor.obtenerEstado(idTransaccion);
    }

    // ==================================================================
    // BRIDGE: Reportes en múltiples formatos
    // ==================================================================

    public Reporte generarReporte(String tipoReporte, String formato) {
        GeneradorReporte generador = obtenerGenerador(formato);

        return switch (tipoReporte.toLowerCase()) {
            case "envios" ->
                new ReporteEnvios(generador, enviosRegistrados);
            case "ingresos" ->
                new ReporteIngresos(generador, enviosRegistrados);
            case "desempeno" -> {
                Map<String, Integer> desempeno = new HashMap<>(contadorPorProveedor);
                // Asegurar que todos los proveedores aparezcan
                for (String prov : proveedoresEnvio.keySet()) {
                    desempeno.putIfAbsent(prov, 0);
                }
                yield new ReporteDesempenoProveedores(generador, desempeno);
            }
            default ->
                throw new IllegalArgumentException(
                    "Tipo de reporte no soportado: " + tipoReporte);
        };
    }

    private GeneradorReporte obtenerGenerador(String formato) {
        return switch (formato.toLowerCase()) {
            case "pdf"   -> new GeneradorPDF();
            case "excel" -> new GeneradorExcel();
            case "json"  -> new GeneradorJSON();
            case "csv"   -> new GeneradorCSV();
            default -> throw new IllegalArgumentException(
                "Formato no soportado: " + formato);
        };
    }

    // ==================================================================
    // COMPOSITE: Operaciones sobre la jerarquía de centros
    // ==================================================================

    public CentroDistribucion obtenerCentroDistribucion() {
        return centroDistribucion;
    }

    public int obtenerCapacidadTotal() {
        return centroDistribucion.obtenerCapacidad();
    }

    public int obtenerOcupacionTotal() {
        return centroDistribucion.obtenerOcupacion();
    }

    public double obtenerPorcentajeOcupacionTotal() {
        return centroDistribucion.obtenerPorcentajeOcupacion();
    }

    public void mostrarEstructuraDistribucion() {
        centroDistribucion.mostrar(0);
    }

    // ==================================================================
    // Consultas generales
    // ==================================================================

    public List<Envio> getEnviosRegistrados() {
        return new ArrayList<>(enviosRegistrados);
    }

    public List<String> getProveedoresEnvioDisponibles() {
        return new ArrayList<>(proveedoresEnvio.keySet());
    }

    public List<String> getProveedoresPagoDisponibles() {
        return new ArrayList<>(proveedoresPago.keySet());
    }
}
