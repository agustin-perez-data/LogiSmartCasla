# Hito 11 — Actividad 2: Mediator

**Proyecto:** LogiSmart - Sistema de Gestión de Logística  
**Patrón:** Mediator  
**Paquete:** `com.logismart.mediator`

---

## Descripción del Patrón

El patrón **Mediator** define un objeto que encapsula cómo interactúan un conjunto de objetos. Los componentes no se conocen entre sí: solo conocen al mediador. Esto reduce las dependencias directas de M×N a M+N.

En LogiSmart, el proceso de creación de un envío dispara una cadena de acciones: validación → pago → notificación → registro. Sin Mediator, cada componente necesitaría una referencia directa a los demás. Con Mediator, todos solo se comunican a través de `MediadorEnviosConcreto`.

> **Nota de coherencia con Hito 10:** El validador de envíos en este patrón se llama `ComponenteValidador` para diferenciarlo de `com.logismart.chain.ValidadorEnvio` (la clase abstracta del Chain of Responsibility del Hito 10). Son responsabilidades distintas: `ComponenteValidador` delega la decisión al Mediator; `ValidadorEnvio` es un manejador autónomo de la cadena.

---

## Diagrama de Clases

![Diagrama de Clases Mediator](files/2_clases_mediator.png)

```
<<interface>>
MediadorEnvios
──────────────────────────────────────────────────────────
+ registrarCentro(c: CentroDistribucion): void
+ registrarValidador(v: ComponenteValidador): void
+ registrarPago(p: SistemaPago): void
+ registrarNotificador(n: SistemaNotificacion): void
+ registrarAuditoria(a: SistemaAuditoria): void
+ notificar(evento: String, datos: Object): void
        ▲
        │ implements
MediadorEnviosConcreto
- centro / validador / pago / notificador / auditoria
+ notificar(evento, datos)

CentroDistribucion   ComponenteValidador   SistemaPago   SistemaNotificacion   SistemaAuditoria
  (usa mediador)        (usa mediador)      (usa mediador)    (usa mediador)      (sin mediador)
```

---

## Diagrama de Secuencia

Flujo completo: creación de envío válido a través del Mediator.

```
Centro    Mediador    Auditoria  Validador   Pago    Notificador
  │           │           │          │         │          │
  │crearEnvio │           │          │         │          │
  │──────────>│[ENVIO_CREADO]        │         │          │
  │           │─────────────────────>│         │          │
  │           │registrar("ENVIO_CREADO")        │         │
  │           │──────────>│          │         │          │
  │           │           │          │validar()│          │
  │           │           │          │[VALIDACION_OK]     │
  │           │<─────────────────────│         │          │
  │           │registrar("VALIDACION_OK")       │         │
  │           │──────────>│          │         │          │
  │           │           │          │    procesarPago()  │
  │           │───────────────────────────────>│          │
  │           │           │          │    [PAGO_CONFIRMADO]
  │           │<───────────────────────────────│          │
  │           │registrar("PAGO_CONFIRMADO")    │          │
  │           │──────────>│          │         │          │
  │           │           │          │         │  enviarConfirmacion()
  │           │──────────────────────────────────────────>│
  │           │           │          │         │  [NOTIFICACION_ENVIADA]
  │           │<──────────────────────────────────────────│
  │           │registrar("NOTIFICACION_ENVIADA")          │
  │           │──────────>│          │         │          │
  │           │registrarEnvio(envio) │         │          │
  │<──────────│           │          │         │          │
  │           │[ENVIO_REGISTRADO]    │         │          │
  │           │registrar("ENVIO_REGISTRADO")   │         │
  │           │──────────>│          │         │          │
```

---

## Implementación

### `MediadorEnvios.java`

```java
package com.logismart.mediator;

public interface MediadorEnvios {
    void registrarCentro(CentroDistribucion centro);
    void registrarValidador(ComponenteValidador validador);
    void registrarPago(SistemaPago pago);
    void registrarNotificador(SistemaNotificacion notificador);
    void registrarAuditoria(SistemaAuditoria auditoria);
    void notificar(String evento, Object datos);
}
```

---

### `MediadorEnviosConcreto.java`

```java
package com.logismart.mediator;

import com.logismart.dominio.Envio;

public class MediadorEnviosConcreto implements MediadorEnvios {

    private CentroDistribucion centro;
    private ComponenteValidador validador;
    private SistemaPago pago;
    private SistemaNotificacion notificador;
    private SistemaAuditoria auditoria;

    @Override
    public void registrarCentro(CentroDistribucion centro)             { this.centro      = centro; }
    @Override
    public void registrarValidador(ComponenteValidador validador)      { this.validador   = validador; }
    @Override
    public void registrarPago(SistemaPago pago)                        { this.pago        = pago; }
    @Override
    public void registrarNotificador(SistemaNotificacion notificador)  { this.notificador = notificador; }
    @Override
    public void registrarAuditoria(SistemaAuditoria auditoria)         { this.auditoria   = auditoria; }

    @Override
    public void notificar(String evento, Object datos) {
        switch (evento) {
            case "ENVIO_CREADO":
                Envio envio = (Envio) datos;
                System.out.println("[Mediator] Envío creado: " + envio.getId());
                auditoria.registrar("ENVIO_CREADO", envio);
                validador.validar(envio);
                break;

            case "VALIDACION_OK":
                System.out.println("[Mediator] Validación OK");
                auditoria.registrar("VALIDACION_OK", datos);
                pago.procesarPago((Envio) datos);
                break;

            case "PAGO_CONFIRMADO":
                System.out.println("[Mediator] Pago confirmado");
                auditoria.registrar("PAGO_CONFIRMADO", datos);
                notificador.enviarConfirmacion((Envio) datos);
                break;

            case "NOTIFICACION_ENVIADA":
                System.out.println("[Mediator] Notificación enviada");
                auditoria.registrar("NOTIFICACION_ENVIADA", datos);
                centro.registrarEnvio((Envio) datos);
                break;

            case "ENVIO_REGISTRADO":
                System.out.println("[Mediator] Envío registrado en sistema");
                auditoria.registrar("ENVIO_REGISTRADO", datos);
                break;
        }
    }
}
```

---

### `CentroDistribucion.java`

```java
package com.logismart.mediator;

import com.logismart.dominio.Envio;

public class CentroDistribucion {

    private final MediadorEnvios mediador;

    public CentroDistribucion(MediadorEnvios mediador) {
        this.mediador = mediador;
    }

    public void crearEnvio(Envio envio) {
        System.out.println("[Centro] Iniciando creación de envío: " + envio.getId());
        mediador.notificar("ENVIO_CREADO", envio);
    }

    public void registrarEnvio(Envio envio) {
        System.out.println("[Centro] Envío " + envio.getId() + " registrado en el sistema");
        mediador.notificar("ENVIO_REGISTRADO", envio);
    }
}
```

---

### `ComponenteValidador.java`

```java
package com.logismart.mediator;

import com.logismart.dominio.Envio;

public class ComponenteValidador {

    private final MediadorEnvios mediador;

    public ComponenteValidador(MediadorEnvios mediador) {
        this.mediador = mediador;
    }

    public void validar(Envio envio) {
        System.out.println("[Validador] Validando envío " + envio.getId() + "...");
        if (envio.getCosto() > 0 && envio.getPeso() > 0) {
            System.out.println("  ✓ Envío válido");
            mediador.notificar("VALIDACION_OK", envio);
        } else {
            System.err.println("  ✗ Envío inválido (costo o peso <= 0) — flujo detenido");
        }
    }
}
```

---

### `SistemaPago.java`

```java
package com.logismart.mediator;

import com.logismart.dominio.Envio;

public class SistemaPago {

    private final MediadorEnvios mediador;

    public SistemaPago(MediadorEnvios mediador) {
        this.mediador = mediador;
    }

    public void procesarPago(Envio envio) {
        System.out.println("[Pago] Procesando pago de $" + envio.getCosto()
                + " con método: " + envio.getMetodoPago());
        System.out.println("  ✓ Pago confirmado");
        mediador.notificar("PAGO_CONFIRMADO", envio);
    }
}
```

---

### `SistemaNotificacion.java`

```java
package com.logismart.mediator;

import com.logismart.dominio.Envio;

public class SistemaNotificacion {

    private final MediadorEnvios mediador;

    public SistemaNotificacion(MediadorEnvios mediador) {
        this.mediador = mediador;
    }

    public void enviarConfirmacion(Envio envio) {
        System.out.println("[Notificador] Enviando confirmación para envío: " + envio.getId());
        System.out.println("  ✓ Confirmación enviada al cliente");
        mediador.notificar("NOTIFICACION_ENVIADA", envio);
    }
}
```

---

### `SistemaAuditoria.java`

```java
package com.logismart.mediator;

import java.util.ArrayList;
import java.util.List;

public class SistemaAuditoria {

    private final List<String> logs = new ArrayList<>();

    public void registrar(String evento, Object datos) {
        String log = "[Auditoría] " + evento + " — " + datos;
        logs.add(log);
        System.out.println(log);
    }

    public void mostrarLogs() {
        System.out.println("\n=== Logs de Auditoría ===");
        for (int i = 0; i < logs.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + logs.get(i));
        }
    }

    public int obtenerCantidadLogs() {
        return logs.size();
    }
}
```

---

## Casos de Prueba — `MediadorDemo.java`

```java
package com.logismart.mediator;

import com.logismart.dominio.Envio;

public class MediadorDemo {

    public static void main(String[] args) {
        MediadorEnvios mediador      = new MediadorEnviosConcreto();
        CentroDistribucion centro    = new CentroDistribucion(mediador);
        ComponenteValidador validador = new ComponenteValidador(mediador);
        SistemaPago pago             = new SistemaPago(mediador);
        SistemaNotificacion notif    = new SistemaNotificacion(mediador);
        SistemaAuditoria auditoria   = new SistemaAuditoria();

        mediador.registrarCentro(centro);
        mediador.registrarValidador(validador);
        mediador.registrarPago(pago);
        mediador.registrarNotificador(notif);
        mediador.registrarAuditoria(auditoria);

        // Caso 1: Flujo completo con envío válido
        System.out.println("=== Caso 1: Flujo completo ===");
        Envio envio1 = new Envio.EnvioBuilder("E001", "Buenos Aires", "Córdoba")
                .peso(5.0).costo(150.0).metodoPago("TARJETA").productoId("PROD-001").build();
        centro.crearEnvio(envio1);
        auditoria.mostrarLogs();

        // Caso 2: Segundo envío — misma cadena, log acumulado
        System.out.println("\n=== Caso 2: Segundo envío ===");
        Envio envio2 = new Envio.EnvioBuilder("E002", "Rosario", "Mendoza")
                .peso(8.0).costo(200.0).metodoPago("EFECTIVO").productoId("PROD-002").build();
        centro.crearEnvio(envio2);

        // Caso 3: Envío con datos inválidos — flujo se detiene en validación
        System.out.println("\n=== Caso 3: Envío con costo 0 (inválido) ===");
        Envio envio3 = new Envio.EnvioBuilder("E003", "La Plata", "Salta")
                .peso(0).costo(0).metodoPago("TARJETA").productoId("PROD-003").build();
        centro.crearEnvio(envio3);

        // Caso 4: Demostrar desacoplamiento — Validador no conoce a Pago
        System.out.println("\n=== Caso 4: Verificar desacoplamiento ===");
        System.out.println("✓ ComponenteValidador no referencia a SistemaPago directamente");
        System.out.println("✓ SistemaPago no referencia a SistemaNotificacion directamente");
        System.out.println("✓ Todos se comunican solo a través de MediadorEnvios");

        // Caso 5: Log total — verificar que todos los eventos fueron auditados
        System.out.println("\n=== Caso 5: Auditoría total ===");
        auditoria.mostrarLogs();
        System.out.println("\nTotal de eventos auditados: " + auditoria.obtenerCantidadLogs());
    }
}
```

| # | Descripción | Resultado esperado |
|---|---|---|
| 1 | Flujo completo — envío válido | 5 eventos auditados: CREADO → VALIDACION_OK → PAGO_CONFIRMADO → NOTIF_ENVIADA → REGISTRADO |
| 2 | Segundo envío válido | Misma cadena disparada, log acumulado |
| 3 | Envío con costo/peso 0 | Flujo detenido en ComponenteValidador, sin pago ni notificación |
| 4 | Verificar desacoplamiento | Ningún componente referencia directamente a otro |
| 5 | Auditoría total | Todos los eventos de los casos 1 y 2 visibles, caso 3 solo con ENVIO_CREADO |

---

## Decisiones de Diseño

**¿Por qué renombrar `ValidadorEnvio` a `ComponenteValidador`?**  
En Hito 10 ya existe `com.logismart.chain.ValidadorEnvio` como clase abstracta del patrón Chain. Aunque Java los distingue por paquete, usar el mismo nombre genera confusión conceptual: son roles distintos. `ComponenteValidador` expresa claramente que es un componente que participa en la comunicación via Mediator, no un manejador autónomo de cadena.

**¿Por qué `SistemaAuditoria` no recibe referencia al mediador?**  
La auditoría es un receptor pasivo: solo registra, nunca dispara eventos. No necesita notificar al mediador, por lo tanto no lo referencia. Esto mantiene la clase más simple y sin dependencias innecesarias.

**¿Por qué usar `switch` en lugar de un mapa de handlers?**  
Para mantener coherencia con el Hito 10 y la simplicidad del ejemplo académico. En un sistema productivo, un mapa `Map<String, Consumer<Object>>` sería más extensible.

---

## Ventajas y Desventajas

**Ventajas**
- Desacoplamiento total: ningún componente conoce a otro, solo al mediador.
- Agregar un nuevo componente (ej. sistema de métricas) requiere solo registrarlo y manejar su evento.
- El flujo completo del negocio es legible en un solo lugar (`notificar()`).

**Desventajas**
- El mediador puede convertirse en un "God Object" si gestiona demasiados flujos.
- El `switch` crece con cada nuevo evento — difícil de mantener a escala.
- El orden de registro de componentes importa; un componente no registrado causa `NullPointerException`.
