package com.logismart.integracion;

import com.logismart.chain.CadenaValidadores;
import com.logismart.chain.SistemaCapacidad;
import com.logismart.chain.SistemaInventario;
import com.logismart.command.ColaComandos;
import com.logismart.command.ComandoActualizarEstado;
import com.logismart.command.ComandoCrearEnvio;
import com.logismart.command.ServicioEnvios;
import com.logismart.dominio.Envio;
import com.logismart.interpreter.Expresion;
import com.logismart.interpreter.ExpresionAND;
import com.logismart.interpreter.ExpresionCosto;
import com.logismart.interpreter.ExpresionDestino;
import com.logismart.interpreter.ExpresionNOT;
import com.logismart.interpreter.ExpresionPeso;
import com.logismart.interpreter.ExpresionRestringido;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Integra los tres patrones de comportamiento del Hito 10:
 *   1. Chain of Responsibility — validación antes de aceptar el envío
 *   2. Command               — operaciones reversibles con historial
 *   3. Interpreter           — evaluación de reglas de negocio
 */
public class SistemaLogisticaCompleto {

    private final CadenaValidadores validadores;
    private final ColaComandos cola;
    private final ServicioEnvios servicio;
    private final Map<String, Expresion> reglas;

    public SistemaLogisticaCompleto() {
        // ── 1. Chain of Responsibility ──────────────────────────────────
        SistemaInventario inventario = productoId -> true;       // stock siempre disponible
        SistemaCapacidad  capacidad  = peso -> peso <= 1000.0;   // límite 1000 kg

        this.validadores = new CadenaValidadores(inventario, capacidad);

        // ── 2. Command ───────────────────────────────────────────────────
        this.cola     = new ColaComandos();
        this.servicio = new ServicioEnvios();

        // ── 3. Interpreter ───────────────────────────────────────────────
        this.reglas = new LinkedHashMap<>();
        inicializarReglas();
    }

    private void inicializarReglas() {
        // Regla 1: envíos a Córdoba con peso menor a 10 kg
        reglas.put("REGLA_CORDOBA_LIVIANO", new ExpresionAND(
                new ExpresionDestino("Córdoba"),
                new ExpresionPeso(10, "<")
        ));

        // Regla 2: envíos de alto valor (costo > 100)
        reglas.put("REGLA_ALTO_VALOR", new ExpresionCosto(100, ">"));

        // Regla 3: destino no restringido
        reglas.put("REGLA_NO_RESTRINGIDO", new ExpresionNOT(new ExpresionRestringido()));
    }

    /**
     * Procesa un envío pasándolo por los tres patrones en secuencia.
     */
    public boolean procesarEnvio(Envio envio) {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║   PROCESANDO ENVÍO: " + envio.getId() + "          ║");
        System.out.println("╚══════════════════════════════════════╝");

        // Paso 1 — Chain of Responsibility: validar antes de aceptar
        System.out.println("\n[Paso 1] Chain of Responsibility — Validación");
        if (!validadores.validarEnvio(envio)) {
            System.out.println("✗ Envío rechazado en validación\n");
            return false;
        }
        System.out.println("→ Validación superada");

        // Paso 2 — Command: registrar la operación de forma reversible
        System.out.println("\n[Paso 2] Command — Registrar operación");
        ComandoCrearEnvio cmdCrear = new ComandoCrearEnvio(servicio, envio);
        cola.ejecutar(cmdCrear);
        String numero = cmdCrear.getNumeroSeguimiento();

        cola.ejecutar(new ComandoActualizarEstado(servicio, numero, "EN TRÁNSITO"));
        System.out.println("→ Operación registrada en historial");

        // Paso 3 — Interpreter: evaluar reglas de negocio
        System.out.println("\n[Paso 3] Interpreter — Evaluación de reglas");
        for (Map.Entry<String, Expresion> entry : reglas.entrySet()) {
            String cumple = entry.getValue().evaluar(envio) ? "✓ Cumple" : "  No cumple";
            System.out.println("  " + cumple + " " + entry.getKey());
        }

        System.out.println("\n→ Envío " + numero + " procesado correctamente\n");
        return true;
    }

    public void deshacerUltimaOperacion() {
        System.out.println("\n[Command] Deshaciendo última operación...");
        cola.deshacer();
    }

    public void mostrarHistorial() {
        cola.mostrarHistorial();
    }
}
