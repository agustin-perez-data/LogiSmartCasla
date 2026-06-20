# Hito 12: Patrones de Comportamiento III - LogiSmart

## Resumen

Este hito incorpora los patrones State, Strategy, Template Method y Visitor sobre el sistema LogiSmart. La implementacion se agrego al proyecto Java `tp/LogiSmartTPO_GrupoCASLA` y se documento en esta carpeta de entregables.

## Comparacion con Hito 11

El Hito 11 tenia documentacion extensa por actividad: descripcion del patron, diagrama, secuencia, implementacion por clase, casos de prueba, decisiones y ventajas/desventajas. Esta version del Hito 12 sigue el mismo criterio:

| Aspecto | Hito 11 | Hito 12 |
|---|---|---|
| Documentacion por actividad | Si | Si |
| Diagramas por patron | Si | Si, en `Hito12_Diagramas.md` y `diagramas/*.mmd` |
| Implementacion paso a paso | Si | Si, con snippets por clase clave |
| Casos de prueba por patron | 5 por actividad | 5+ por actividad |
| Integracion completa | Si | Si, en `com.logismart.avanzada` |
| Decisiones de diseno | Si | Si |
| Pros y contras | Si | Si |

## Entregables

| Actividad | Patron | Paquete Java | Estado |
|---|---|---|---|
| 1 | State | `com.logismart.state` + extensiones en `com.logismart.dominio.Envio` | Completo |
| 2 | Strategy | `com.logismart.strategy` + extensiones en `com.logismart.dominio.Envio` | Completo |
| 3 | Template Method | `com.logismart.template` | Completo |
| 4 | Visitor | `com.logismart.visitor` | Completo |
| 5 | Integracion | `com.logismart.avanzada` | Completo |

## Archivos de documentacion

| Archivo | Contenido |
|---|---|
| `Hito12_Actividad1_State.md` | Descripcion, implementacion, casos, decisiones y pros/contras de State |
| `Hito12_Actividad2_Strategy.md` | Descripcion, implementacion, casos, decisiones y pros/contras de Strategy |
| `Hito12_Actividad3_TemplateMethod.md` | Descripcion, implementacion, casos, decisiones y pros/contras de Template Method |
| `Hito12_Actividad4_Visitor.md` | Descripcion, implementacion, casos, decisiones y pros/contras de Visitor |
| `Hito12_Actividad5_Integracion.md` | Integracion de los cuatro patrones y casos combinados |
| `Hito12_Diagramas.md` | Diagramas UML en Mermaid |

## Estructura de codigo agregada

```text
com.logismart
├── state/
│   ├── EstadoEnvio.java
│   ├── EstadoConfirmado.java
│   ├── EstadoEnTransito.java
│   ├── EstadoEnReparto.java
│   ├── EstadoEntregado.java
│   ├── EstadoRetenido.java
│   ├── EstadoCancelado.java
│   └── StateDemo.java
├── strategy/
│   ├── EstrategiaCalculoCosto.java
│   ├── EstrategiaDistancia.java
│   ├── EstrategiaPeso.java
│   ├── EstrategiaUrgencia.java
│   ├── EstrategiaVolumen.java
│   ├── EstrategiaHibrida.java
│   └── StrategyDemo.java
├── template/
│   ├── ProcesoProcesosEnvio.java
│   ├── ProcesoNacional.java
│   ├── ProcesoInternacional.java
│   ├── ProcesoUrgente.java
│   └── TemplateMethodDemo.java
├── visitor/
│   ├── VisitorCentro.java
│   ├── CentroDistribucion.java
│   ├── PuntoEntrega.java
│   ├── CentroRegional.java
│   ├── VisitorCalculoOcupacion.java
│   ├── VisitorGeneradorReporte.java
│   ├── VisitorCalculoCostoOperativo.java
│   ├── VisitorBusquedaPuntosCriticos.java
│   └── VisitorDemo.java
└── avanzada/
    ├── SistemaLogisticaAvanzada.java
    └── IntegracionHito12Demo.java
```

## Modificaciones al dominio existente

Se extendio `com.logismart.dominio.Envio` para que funcione como contexto compartido:

- State: agrega metodos `validar`, `entregar`, `cancelar`, `retener`, `devolver`, `reclamar` y sobrecarga `cambiarEstado(com.logismart.state.EstadoEnvio)`.
- Strategy: agrega `tipo`, `establecerEstrategia(EstrategiaCalculoCosto)` y `calcularCosto()`.
- Compatibilidad: mantiene `getEstado()`, `actualizarEstado(...)`, `cambiarEstado(String)` y los cambios del Hito 11.

Tambien se extendio `com.logismart.dominio.EstadoEnvio` con `EN_REPARTO` y `RETENIDO`.

## Casos de prueba

| Bloque | Casos |
|---|---:|
| StateDemo | 5 |
| StrategyDemo | 5 |
| TemplateMethodDemo | 5 |
| VisitorDemo | 5 |
| IntegracionHito12Demo | 5 |
| Casos adicionales documentados por patron | 5+ |

Total cubierto: 30+ escenarios entre demos ejecutables y casos documentados.

## Clases Java

El PDF pide 26 clases Java para la implementacion base. Se agregaron esas clases y, ademas, demos ejecutables por patron e integracion para facilitar validacion.
