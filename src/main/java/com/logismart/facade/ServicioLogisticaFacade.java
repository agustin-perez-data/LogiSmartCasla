package com.logismart.facade;

import com.logismart.facade.subsistemas.*;

public class ServicioLogisticaFacade {
    private SistemaInventario inventario;
    private SistemaPagos pagos;
    private SistemaNotificaciones notificaciones;
    private SistemaRastreo rastreo;
    private SistemaReportes reportes;

    public ServicioLogisticaFacade() {
        this.inventario = new SistemaInventario();
        this.pagos = new SistemaPagos();
        this.notificaciones = new SistemaNotificaciones();
        this.rastreo = new SistemaRastreo();
        this.reportes = new SistemaReportes();
    }

    public String crearEnvio(String productoId, double monto, String email, String telefono) {
        System.out.println("\n=== Creando Envío ===");
        try {
            if (!inventario.verificarStock(productoId)) {
                throw new Exception("Stock insuficiente para: " + productoId);
            }
            if (!pagos.procesarPago("TARJETA", monto)) {
                throw new Exception("Pago rechazado por $" + monto);
            }
            inventario.restarStock(productoId, 1);
            String numeroSeguimiento = rastreo.crearNumeroSeguimiento();
            notificaciones.enviarEmail(email, "Tu envío ha sido confirmado. Número: " + numeroSeguimiento);
            notificaciones.enviarSMS(telefono, "Envío confirmado: " + numeroSeguimiento);
            reportes.generarReporte("ENVIO", numeroSeguimiento, "PDF");
            System.out.println("✓ Envío creado exitosamente: " + numeroSeguimiento);
            return numeroSeguimiento;
        } catch (Exception e) {
            System.err.println("✗ Error: " + e.getMessage());
            return null;
        }
    }

    public void cancelarEnvio(String numeroSeguimiento, String email) {
        System.out.println("\n=== Cancelando Envío ===");
        rastreo.cancelarRastreo(numeroSeguimiento);
        pagos.reembolsar(numeroSeguimiento);
        notificaciones.enviarEmail(email, "Tu envío " + numeroSeguimiento + " fue cancelado. Reembolso en proceso.");
        System.out.println("✓ Envío cancelado: " + numeroSeguimiento);
    }

    public String obtenerEstadoEnvio(String numeroSeguimiento) {
        return rastreo.obtenerEstado(numeroSeguimiento);
    }

    public void generarReporte(String tipo, String referencia, String formato) {
        reportes.generarReporte(tipo, referencia, formato);
    }
}
