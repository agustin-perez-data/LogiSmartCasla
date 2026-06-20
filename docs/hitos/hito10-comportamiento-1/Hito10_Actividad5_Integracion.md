# Hito 10 — Actividad 5: Integración Completa

**Clase:** `SistemaLogisticaCompleto`  
**Paquete:** `com.logismart.integracion`

---

## Descripción

Integra los tres patrones del Hito 10 en un único flujo de procesamiento:

```
Envio
  │
  ▼
┌─────────────────────────────────┐
│  Chain of Responsibility        │  ¿el envío es válido?
│  CadenaValidadores (5 niveles)  │
└────────────────┬────────────────┘
                 │ OK
                 ▼
┌─────────────────────────────────┐
│  Command                        │  registrar de forma reversible
│  ColaComandos + ServicioEnvios  │
└────────────────┬────────────────┘
                 │
                 ▼
┌─────────────────────────────────┐
│  Interpreter                    │  ¿qué reglas cumple?
│  Map<String, Expresion>         │
└─────────────────────────────────┘
```

Si el Chain falla, Command e Interpreter **no se ejecutan**.

---

## `SistemaLogisticaCompleto.java`

```java
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

public class SistemaLogisticaCompleto {

    private final CadenaValidadores validadores;
    private final ColaComandos cola;
    private final ServicioEnvios servicio;
    private final Map<String, Expresion> reglas;

    public SistemaLogisticaCompleto() {
        SistemaInventario inventario = productoId -> true;
        SistemaCapacidad  capacidad  = peso -> peso <= 1000.0;

        this.validadores = new CadenaValidadores(inventario, capacidad);
        this.cola        = new ColaComandos();
        this.servicio    = new ServicioEnvios();
        this.reglas      = new LinkedHashMap<>();
        inicializarReglas();
    }

    private void inicializarReglas() {
        reglas.put("REGLA_CORDOBA_LIVIANO", new ExpresionAND(
                new ExpresionDestino("Córdoba"),
                new ExpresionPeso(10, "<")
        ));
        reglas.put("REGLA_ALTO_VALOR", new ExpresionCosto(100, ">"));
        reglas.put("REGLA_NO_RESTRINGIDO", new ExpresionNOT(new ExpresionRestringido()));
    }

    public boolean procesarEnvio(Envio envio) {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║   PROCESANDO ENVÍO: " + envio.getId() + "          ║");
        System.out.println("╚══════════════════════════════════════╝");

        // Paso 1 — Chain of Responsibility
        System.out.println("\n[Paso 1] Chain of Responsibility — Validación");
        if (!validadores.validarEnvio(envio)) {
            System.out.println("✗ Envío rechazado en validación\n");
            return false;
        }
        System.out.println("→ Validación superada");

        // Paso 2 — Command
        System.out.println("\n[Paso 2] Command — Registrar operación");
        ComandoCrearEnvio cmdCrear = new ComandoCrearEnvio(servicio, envio);
        cola.ejecutar(cmdCrear);
        String numero = cmdCrear.getNumeroSeguimiento();
        cola.ejecutar(new ComandoActualizarEstado(servicio, numero, "EN TRÁNSITO"));
        System.out.println("→ Operación registrada en historial");

        // Paso 3 — Interpreter
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
```

---

## `IntegracionHito10Demo.java`

```java
package com.logismart.integracion;

import com.logismart.dominio.Envio;

public class IntegracionHito10Demo {

    public static void main(String[] args) {
        SistemaLogisticaCompleto sistema = new SistemaLogisticaCompleto();

        // Caso 1: Envío válido a Córdoba, peso liviano, costo alto
        // Pasa validación, se registra, cumple REGLA_CORDOBA_LIVIANO y REGLA_ALTO_VALOR
        Envio envio1 = new Envio.EnvioBuilder("E001", "Buenos Aires", "Córdoba")
                .peso(5.0).costo(150.0).metodoPago("TARJETA").productoId("PROD-001")
                .build();
        sistema.procesarEnvio(envio1);

        // Caso 2: Envío válido a Mendoza, peso pesado, costo bajo
        // Pasa validación, se registra, solo cumple REGLA_NO_RESTRINGIDO
        Envio envio2 = new Envio.EnvioBuilder("E002", "Rosario", "Mendoza")
                .peso(50.0).costo(80.0).metodoPago("EFECTIVO").productoId("PROD-002")
                .build();
        sistema.procesarEnvio(envio2);

        // Caso 3: Destino restringido — falla en ValidadorSeguridad
        // No llega a Command ni a Interpreter
        Envio envio3 = new Envio.EnvioBuilder("E003", "Buenos Aires", "Zona Restringida")
                .peso(3.0).costo(200.0).metodoPago("TARJETA").productoId("PROD-003")
                .build();
        sistema.procesarEnvio(envio3);

        // Historial y undo
        System.out.println("=== Estado del historial ===");
        sistema.mostrarHistorial();

        System.out.println("=== Deshacer última operación ===");
        sistema.deshacerUltimaOperacion();
        sistema.mostrarHistorial();
    }
}
```

---

## Casos de integración

| # | Envío | Chain | Command | Interpreter |
|---|---|---|---|---|
| 1 | BA→Córdoba, 5kg, $150 | ✓ pasa | ENV-001 creado | CORDOBA_LIVIANO ✓, ALTO_VALOR ✓, NO_RESTRINGIDO ✓ |
| 2 | Rosario→Mendoza, 50kg, $80 | ✓ pasa | ENV-002 creado | ALTO_VALOR ✗, NO_RESTRINGIDO ✓ |
| 3 | BA→Zona Restringida, 3kg, $200 | ✗ falla en ValidadorSeguridad | no se ejecuta | no se ejecuta |

---

## Relación entre patrones

| Aspecto | Chain | Command | Interpreter |
|---|---|---|---|
| Cuándo actúa | Antes de aceptar | Al aceptar | Después de aceptar |
| Pregunta | ¿Puedo procesar esto? | ¿Cómo lo registro y revierto? | ¿Qué significa para el negocio? |
| Reversible | No aplica | Sí (undo/redo) | No aplica |
