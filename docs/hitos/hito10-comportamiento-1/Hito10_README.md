# Hito 10: Patrones de Comportamiento I — Entregables

## Checklist general

### Código Java

| Actividad | Clases | Estado |
|---|---|---|
| **Chain** | `ValidadorEnvio` (abstracta) + 5 validadores + `CadenaValidadores` + 2 interfaces + `ChainDemo` | ✅ |
| **Command** | `Comando` (interfaz) + 5 comandos + `ColaComandos` + `ServicioEnvios` + `CommandDemo` | ✅ |
| **Interpreter** | `Expresion` (interfaz) + 5 terminales + 3 no-terminales + `InterpreterDemo` | ✅ |
| **Integración** | `SistemaLogisticaCompleto` + `IntegracionHito10Demo` | ✅ |

**Total: 30 clases/interfaces Java**

### Documentación Markdown

| Archivo | Contenido |
|---|---|
| `Hito10_Actividad1_Chain.md` | Descripción, diagramas, implementación, casos, decisiones, pros/cons |
| `Hito10_Actividad2_Command.md` | Descripción, diagramas, implementación, casos, decisiones, pros/cons |
| `Hito10_Actividad3_Interpreter.md` | Descripción, árbol AST, diagramas, implementación, casos, decisiones, pros/cons |
| `Hito10_Actividad4_Diagramas.md` | 3 diagramas de secuencia ASCII detallados |
| `Hito10_Actividad5_Integracion.md` | Flujo integrado, tabla de casos, relación entre patrones |

### Casos de prueba

| Actividad | Casos | Estado |
|---|---|---|
| Chain | 5 (válido, origen vacío, peso negativo, costo 0, destino restringido) | ✅ |
| Command | 7 (crear, actualizar, cambiar pago, agregar servicio, historial, undo x2, redo) | ✅ |
| Interpreter | 5 reglas con 11 evaluaciones | ✅ |
| Integración | 3 (envío válido, envío parcial, envío rechazado) | ✅ |

**Total: 26+ casos de prueba**

---

## Estructura de paquetes

```
com.logismart
├── chain/
│   ├── ValidadorEnvio.java          ← abstract handler
│   ├── ValidadorDatos.java
│   ├── ValidadorInventario.java
│   ├── ValidadorPago.java
│   ├── ValidadorSeguridad.java
│   ├── ValidadorCapacidad.java
│   ├── CadenaValidadores.java       ← ensambla la cadena
│   ├── SistemaInventario.java       ← interface
│   ├── SistemaCapacidad.java        ← interface
│   └── ChainDemo.java               ← 5 casos de prueba
│
├── command/
│   ├── Comando.java                 ← interface
│   ├── ServicioEnvios.java          ← receptor
│   ├── ComandoCrearEnvio.java
│   ├── ComandoCancelarEnvio.java
│   ├── ComandoActualizarEstado.java
│   ├── ComandoCambiarMetodoPago.java
│   ├── ComandoAgregarServicio.java
│   ├── ColaComandos.java            ← invoker con undo/redo
│   └── CommandDemo.java             ← 7 casos de prueba
│
├── interpreter/
│   ├── Expresion.java               ← interface
│   ├── ExpresionOrigen.java         ← terminal
│   ├── ExpresionDestino.java        ← terminal
│   ├── ExpresionPeso.java           ← terminal
│   ├── ExpresionCosto.java          ← terminal
│   ├── ExpresionRestringido.java    ← terminal
│   ├── ExpresionAND.java            ← no-terminal
│   ├── ExpresionOR.java             ← no-terminal
│   ├── ExpresionNOT.java            ← no-terminal
│   └── InterpreterDemo.java         ← 5 casos de prueba
│
└── integracion/
    ├── SistemaLogisticaCompleto.java ← 3 patrones unificados
    └── IntegracionHito10Demo.java    ← demo de integración
```

---

## Modificación a clase existente

`com.logismart.dominio.Envio` — se agregaron los campos `costo`, `metodoPago` y `productoId` con sus getters y builder methods para soportar `ValidadorPago` e `InterpreterDemo`.
