# Hito 11 — Actividad 6: Integración Completa

**Clase:** `SistemaLogisticaEventDriven`  
**Paquete:** `com.logismart.eventdriven`

---

## Descripción

Integra los cuatro patrones del Hito 11 en un único flujo de procesamiento y ciclo de vida de un envío:

```
Envio
  │
  ▼
┌─────────────────────────────────┐
│  Iterator                       │  recorrer colección de envíos pendientes
│  ColeccionArray / IteradorEnvios│
└────────────────┬────────────────┘
                 │ para cada envío
                 ▼
┌─────────────────────────────────┐
│  Mediator                       │  orquestar el flujo: validar → pagar → notificar → registrar
│  MediadorEnviosConcreto         │
└────────────────┬────────────────┘
                 │ en cada cambio de estado
                 ▼
┌─────────────────────────────────┐
│  Memento                        │  guardar snapshot antes de cada transición
│  HistorialEnvios                │
└────────────────┬────────────────┘
                 │ al cambiar estado
                 ▼
┌─────────────────────────────────┐
│  Observer                       │  notificar dashboard, auditoría y centro
│  CentroDistribucionObservador   │
│  SistemaAuditoriaObservador     │
│  DashboardObservador            │
└─────────────────────────────────┘
```

**Relación entre patrones:**

| Patrón | Rol en la integración |
|---|---|
| Iterator | Recorre la cola de envíos pendientes sin exponer la estructura de almacenamiento |
| Mediator | Orquesta el flujo de negocio (validar, pagar, notificar, registrar) sin acoplar los componentes |
| Memento | Persiste el estado antes de cada transición para permitir retroceder en caso de error |
| Observer | Propaga los cambios de estado a todos los interesados en tiempo real |

---

## Diagrama de Secuencia de Integración

```
Sistema      Coleccion   Mediador    Envio      Historial   Observadores
   │              │           │         │            │            │
   │ agregar(e1,e2,e3)        │         │            │            │
   │─────────────>│           │         │            │            │
   │              │           │         │            │            │
   │ iterador = crearIterador()│         │            │            │
   │─────────────>│           │         │            │            │
   │              │           │         │            │            │
   │ [mientras tieneSiguiente()]         │            │            │
   │ envio = obtenerSiguiente()│         │            │            │
   │─────────────>│           │         │            │            │
   │              │           │         │            │            │
   │ historial.guardarEstado(envio)      │            │            │
   │────────────────────────────────────────────────>│            │
   │              │           │         │crearMemento│            │
   │              │           │         │<───────────│            │
   │              │           │         │────────────>│           │
   │              │           │         │            │            │
   │ mediador.notificar("ENVIO_CREADO", envio)        │            │
   │──────────────────────────>│         │            │            │
   │              │           │cambiarEstado/notificar│            │
   │              │           │─────────>│            │            │
   │              │           │         │ notificarObservadores()  │
   │              │           │         │────────────────────────>│
   │              │           │         │            │            │
   │              │           │ [VALIDACION_OK → PAGO → NOTIF → REGISTRADO]
   │              │           │         │            │            │
   │ historial.guardarEstado(envio)      │            │            │
   │────────────────────────────────────────────────>│            │
   │              │           │         │            │            │
   │ [siguiente envío en colección]      │            │            │
   │─────────────>│           │         │            │            │
```

---

## `SistemaLogisticaEventDriven.java`

```java
package com.logismart.eventdriven;

import com.logismart.dominio.Envio;
import com.logismart.iterator.ColeccionArray;
import com.logismart.iterator.ColeccionEnvios;
import com.logismart.iterator.IteradorEnvios;
import com.logismart.mediator.ComponenteValidador;
import com.logismart.mediator.MediadorEnvios;
import com.logismart.mediator.MediadorEnviosConcreto;
import com.logismart.mediator.SistemaAuditoria;
import com.logismart.mediator.SistemaNotificacion;
import com.logismart.mediator.SistemaPago;
import com.logismart.mediator.CentroDistribucion;
import com.logismart.memento.HistorialEnvios;
import com.logismart.observer.CentroDistribucionObservador;
import com.logismart.observer.DashboardObservador;
import com.logismart.observer.SistemaAuditoriaObservador;

public class SistemaLogisticaEventDriven {

    // Iterator — colección de envíos pendientes
    private final ColeccionEnvios coleccion = new ColeccionArray();

    // Mediator — orquestador del flujo de negocio
    private final MediadorEnvios         mediador;
    private final CentroDistribucion     centro;
    private final ComponenteValidador    validador;
    private final SistemaPago            pago;
    private final SistemaNotificacion    notificador;
    private final SistemaAuditoria       auditoriaMediator;

    // Memento — historial de estados
    private final HistorialEnvios historial = new HistorialEnvios();

    // Observer — observadores globales del sistema
    private final CentroDistribucionObservador centroObservador;
    private final SistemaAuditoriaObservador   auditoriaObservador;
    private final DashboardObservador          dashboard;

    public SistemaLogisticaEventDriven() {
        // Configurar Mediator
        mediador        = new MediadorEnviosConcreto();
        centro          = new CentroDistribucion(mediador);
        validador       = new ComponenteValidador(mediador);
        pago            = new SistemaPago(mediador);
        notificador     = new SistemaNotificacion(mediador);
        auditoriaMediator = new SistemaAuditoria();

        mediador.registrarCentro(centro);
        mediador.registrarValidador(validador);
        mediador.registrarPago(pago);
        mediador.registrarNotificador(notificador);
        mediador.registrarAuditoria(auditoriaMediator);

        // Configurar Observer
        centroObservador    = new CentroDistribucionObservador();
        auditoriaObservador = new SistemaAuditoriaObservador();
        dashboard           = new DashboardObservador();
    }

    /**
     * Agrega un envío a la cola de procesamiento.
     */
    public void encolar(Envio envio) {
        // Registrar observadores en el envío (Observer)
        envio.adjuntarObservador(centroObservador);
        envio.adjuntarObservador(auditoriaObservador);
        envio.adjuntarObservador(dashboard);
        coleccion.agregar(envio);
        System.out.println("[Sistema] Envío " + envio.getId() + " encolado");
    }

    /**
     * Procesa todos los envíos en cola usando Iterator para recorrerlos
     * y Mediator para ejecutar el flujo de negocio de cada uno.
     * Antes de cada cambio de estado, Memento guarda un snapshot.
     */
    public void procesarCola() {
        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.println("║     PROCESANDO COLA DE ENVÍOS            ║");
        System.out.println("╚══════════════════════════════════════════╝");

        IteradorEnvios iterador = coleccion.crearIterador();

        while (iterador.tieneSiguiente()) {
            Envio envio = iterador.obtenerSiguiente();

            System.out.println("\n── Procesando " + envio.getId() + " ──────────────────");

            // Memento: guardar estado inicial antes del flujo
            historial.guardarEstado(envio);

            // Mediator: ejecutar flujo completo de negocio
            centro.crearEnvio(envio);

            // Memento: guardar estado final tras el flujo del Mediator
            historial.guardarEstado(envio);
        }

        System.out.println("\n[Sistema] Cola procesada. Total envíos: " + coleccion.obtenerTamaño());
    }

    /**
     * Simula un error en un envío y restaura su estado anterior usando Memento.
     */
    public void simularErrorYRestaurar(Envio envio) {
        System.out.println("\n── Simulando error en " + envio.getId() + " ──");
        System.out.println("Estado antes del error: " + envio.obtenerEstado());
        envio.cambiarEstado("ERROR");
        historial.guardarEstado(envio);

        System.out.println("\n── Restaurando estado anterior... ──");
        historial.irAlEstadoAnterior(envio);
        System.out.println("Estado restaurado: " + envio.obtenerEstado());
    }

    public void mostrarHistorial() {
        historial.mostrarHistorial();
    }

    public void mostrarDashboard() {
        dashboard.mostrarDashboard();
    }

    public void mostrarAuditoria() {
        auditoriaObservador.mostrarRegistros();
        System.out.println("Total eventos Observer: " + auditoriaObservador.obtenerCantidadRegistros());
        auditoriaMediator.mostrarLogs();
        System.out.println("Total eventos Mediator: " + auditoriaMediator.obtenerCantidadLogs());
    }
}
```

---

## `IntegracionHito11Demo.java`

```java
package com.logismart.eventdriven;

import com.logismart.dominio.Envio;

public class IntegracionHito11Demo {

    public static void main(String[] args) {
        SistemaLogisticaEventDriven sistema = new SistemaLogisticaEventDriven();

        // Caso 1: Dos envíos válidos — flujo completo a través de los 4 patrones
        System.out.println("=== Caso 1: Procesar cola con dos envíos válidos ===");
        Envio envio1 = new Envio.EnvioBuilder("E001", "Buenos Aires", "Córdoba")
                .peso(5.0).costo(150.0).metodoPago("TARJETA").productoId("PROD-001").build();
        Envio envio2 = new Envio.EnvioBuilder("E002", "Rosario", "Mendoza")
                .peso(8.0).costo(200.0).metodoPago("EFECTIVO").productoId("PROD-002").build();

        sistema.encolar(envio1);
        sistema.encolar(envio2);
        sistema.procesarCola();

        // Caso 2: Dashboard y auditoría después del procesamiento
        System.out.println("\n=== Caso 2: Estado del sistema tras el procesamiento ===");
        sistema.mostrarDashboard();
        sistema.mostrarAuditoria();

        // Caso 3: Envío con datos inválidos — Mediator detiene el flujo en validación
        System.out.println("\n=== Caso 3: Envío inválido (peso 0) ===");
        SistemaLogisticaEventDriven sistema2 = new SistemaLogisticaEventDriven();
        Envio envio3 = new Envio.EnvioBuilder("E003", "La Plata", "Salta")
                .peso(0).costo(0).metodoPago("TARJETA").productoId("PROD-003").build();
        sistema2.encolar(envio3);
        sistema2.procesarCola();
        // Observer recibe ENVIO_CREADO pero Mediator no continúa → sin más notificaciones
        sistema2.mostrarDashboard();

        // Caso 4: Simular error y restaurar con Memento
        System.out.println("\n=== Caso 4: Error en envío y restauración con Memento ===");
        sistema.simularErrorYRestaurar(envio1);
        sistema.mostrarHistorial();

        // Caso 5: Demostrar desacoplamiento — Iterator recorre sin importar la colección
        System.out.println("\n=== Caso 5: Desacoplamiento por Iterator ===");
        System.out.println("✓ procesarCola() usó IteradorEnvios — no conoce la estructura interna");
        System.out.println("✓ Mediator orquestó el flujo — los componentes no se conocen entre sí");
        System.out.println("✓ Memento preservó estados — Envio no expuso su estructura interna");
        System.out.println("✓ Observer propagó cambios — Envio no conoce a sus observadores");
    }
}
```

---

## Casos de Integración

| # | Envío | Iterator | Mediator | Memento | Observer |
|---|---|---|---|---|---|
| 1a | E001 BA→Córdoba, 5kg, $150 | recorrido en cola | flujo completo: VALIDACION_OK → PAGO → NOTIF → REGISTRADO | 2 snapshots guardados (inicial + final) | 3 observadores notificados por cada cambio de estado |
| 1b | E002 Rosario→Mendoza, 8kg, $200 | recorrido en cola | flujo completo | 2 snapshots guardados | 3 observadores notificados |
| 2 | E001 y E002 | — | — | historial con 4 entradas | dashboard muestra estado final de ambos |
| 3 | E003 La Plata→Salta, 0kg, $0 | recorrido en cola | flujo detenido en ComponenteValidador | snapshot guardado del estado inicial | Observer recibe solo el evento inicial; sin más notificaciones |
| 4 | E001 (post-procesamiento) | — | — | restaurado al estado anterior desde ERROR | Observer notificado del cambio a ERROR y del estado restaurado |
| 5 | — | desacoplamiento verificado | desacoplamiento verificado | encapsulamiento verificado | desacoplamiento verificado |

---

## Relación entre Patrones

| Aspecto | Iterator | Mediator | Memento | Observer |
|---|---|---|---|---|
| Cuándo actúa | Al iniciar el procesamiento de la cola | Durante el flujo de negocio de cada envío | Antes y después de cada transición de estado | En cada cambio de estado del envío |
| Pregunta | ¿Qué envíos hay pendientes? | ¿Cómo coordino validación, pago y notificación? | ¿Puedo revertir si algo falla? | ¿Quién necesita saber que el estado cambió? |
| Conoce a | Solo la interfaz `ColeccionEnvios` | Solo la interfaz `MediadorEnvios` | Solo `Envio` y `MementoEnvio` | Solo la interfaz `ObservadorEnvio` |
| Sin el patrón | El sistema tendría que conocer la estructura interna del array | Cada componente tendría referencias cruzadas | No habría forma de revertir estados | Habría llamadas directas hard-codeadas a cada sistema interesado |
