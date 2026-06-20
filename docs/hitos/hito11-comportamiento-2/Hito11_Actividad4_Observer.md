# Hito 11 — Actividad 4: Observer

**Proyecto:** LogiSmart - Sistema de Gestión de Logística  
**Patrón:** Observer  
**Paquete:** `com.logismart.observer`

---

## Descripción del Patrón

El patrón **Observer** define una dependencia de uno-a-muchos entre objetos, de modo que cuando un objeto cambia de estado, todos sus dependientes son notificados y actualizados automáticamente.

En LogiSmart, `com.logismart.dominio.Envio` actúa como sujeto observable. Cada vez que su estado cambia (CONFIRMADO → EN_TRANSITO → ENTREGADO, etc.), notifica automáticamente a todos sus observadores registrados: el centro de distribución, el sistema de notificaciones al cliente, el dashboard en tiempo real y el sistema de auditoría.

Los participantes son:

- **Sujeto:** `com.logismart.dominio.Envio` — mantiene la lista de observadores y los notifica.
- **Interfaz Observador:** `ObservadorEnvio` — contrato que todos los observadores deben implementar.
- **Observadores concretos:** `CentroDistribucionObservador`, `SistemaNotificacionObservador`, `SistemaAuditoriaObservador`, `DashboardObservador`.

> **Nota de coherencia:** `CentroDistribucionObservador` y `SistemaNotificacionObservador` son clases del paquete `com.logismart.observer`, distintas de `CentroDistribucion` y `SistemaNotificacion` del paquete `com.logismart.mediator`. Cumplen roles similares pero en contextos de patrones distintos.

---

## Diagrama de Clases

![Diagrama de Clases Observer](files/4_clases_observer.png)

```
<<interface>>
ObservadorEnvio
─────────────────────────────────────────────────
+ actualizar(envio: Envio, evento: String): void
        ▲
        │ implements
        ├──────────────────────────────┐
        │                              │
CentroDistribucionObservador   SistemaNotificacionObservador
+ actualizar(envio, evento)    + actualizar(envio, evento)
        ▲                              ▲
        │ implements                   │ implements
SistemaAuditoriaObservador     DashboardObservador
+ actualizar(envio, evento)    + actualizar(envio, evento)

com.logismart.dominio.Envio  (Sujeto)
- observadores: List<ObservadorEnvio>
─────────────────────────────────────────────────
+ adjuntarObservador(o: ObservadorEnvio): void
+ desadjuntarObservador(o: ObservadorEnvio): void
+ notificarObservadores(evento: String): void
+ cambiarEstado(nuevoEstado: String): void   [ya existe — se extiende]
```

---

## Diagrama de Secuencia

Flujo de notificación al cambiar el estado de un envío.

```
Cliente    Envio        CentroObs.   NotifObs.   AuditoriaObs.  DashboardObs.
   │          │               │           │             │               │
   │adjuntar  │               │           │             │               │
   │Observador│               │           │             │               │
   │(centro)  │               │           │             │               │
   │─────────>│               │           │             │               │
   │adjuntar  │               │           │             │               │
   │Observador│               │           │             │               │
   │(notif)   │               │           │             │               │
   │─────────>│               │           │             │               │
   │adjuntar  │               │           │             │               │
   │Observador│               │           │             │               │
   │(audit)   │               │           │             │               │
   │─────────>│               │           │             │               │
   │adjuntar  │               │           │             │               │
   │Observador│               │           │             │               │
   │(dash)    │               │           │             │               │
   │─────────>│               │           │             │               │
   │          │               │           │             │               │
   │cambiarEstado("EN_TRANSITO")           │             │               │
   │─────────>│               │           │             │               │
   │          │notificarObservadores()     │             │               │
   │          │───────────────>│           │             │               │
   │          │actualizar(envio, "EN_TRANSITO")          │               │
   │          │               │           │             │               │
   │          │──────────────────────────>│             │               │
   │          │actualizar(envio, "EN_TRANSITO")          │               │
   │          │               │           │             │               │
   │          │───────────────────────────────────────>│               │
   │          │actualizar(envio, "EN_TRANSITO")                         │
   │          │────────────────────────────────────────────────────────>│
   │<─────────│               │           │             │               │
```

---

## Modificación a `com.logismart.dominio.Envio`

Se agregan los siguientes elementos a `Envio` para soportar el patrón Observer:

```java
import com.logismart.observer.ObservadorEnvio;
import java.util.ArrayList;
import java.util.List;

// Campo nuevo
private final List<ObservadorEnvio> observadores = new ArrayList<>();

// Métodos nuevos
public void adjuntarObservador(ObservadorEnvio observador) {
    observadores.add(observador);
}

public void desadjuntarObservador(ObservadorEnvio observador) {
    observadores.remove(observador);
}

private void notificarObservadores(String evento) {
    for (ObservadorEnvio obs : observadores) {
        obs.actualizar(this, evento);
    }
}
```

El método `cambiarEstado` ya existe desde Hito 11 Actividad 3 (Memento). Se extiende para disparar la notificación:

```java
public void cambiarEstado(String nuevoEstado) {
    System.out.println("[Envio " + id + "] Estado: " + this.estado + " → " + nuevoEstado);
    this.estado = nuevoEstado;
    notificarObservadores(nuevoEstado);   // ← línea nueva
}
```

> **Nota:** La llamada a `notificarObservadores` se agrega al final de `cambiarEstado`, asegurando que el estado ya esté actualizado cuando los observadores lo lean.

---

## Implementación

### `ObservadorEnvio.java`

```java
package com.logismart.observer;

import com.logismart.dominio.Envio;

public interface ObservadorEnvio {
    void actualizar(Envio envio, String evento);
}
```

---

### `CentroDistribucionObservador.java`

```java
package com.logismart.observer;

import com.logismart.dominio.Envio;

public class CentroDistribucionObservador implements ObservadorEnvio {

    @Override
    public void actualizar(Envio envio, String evento) {
        System.out.println("[Centro] Envío " + envio.getId()
                + " cambió a: " + evento
                + " | Ruta: " + envio.getOrigen() + " → " + envio.getDestino());
        if ("EN_TRANSITO".equals(evento)) {
            System.out.println("  → Actualizando posición en ruta");
        } else if ("ENTREGADO".equals(evento)) {
            System.out.println("  → Marcando bahía de carga como disponible");
        }
    }
}
```

---

### `SistemaNotificacionObservador.java`

```java
package com.logismart.observer;

import com.logismart.dominio.Envio;

public class SistemaNotificacionObservador implements ObservadorEnvio {

    @Override
    public void actualizar(Envio envio, String evento) {
        System.out.println("[Notificacion] Enviando alerta al cliente — Envío "
                + envio.getId() + ": " + evento);
        switch (evento) {
            case "EN_PREPARACION":
                System.out.println("  → SMS: 'Su envío está siendo preparado'");
                break;
            case "EN_TRANSITO":
                System.out.println("  → SMS: 'Su envío está en camino'");
                break;
            case "ENTREGADO":
                System.out.println("  → SMS: 'Su envío fue entregado'");
                break;
            case "DEVUELTO":
                System.out.println("  → SMS: 'Su envío fue devuelto — contáctenos'");
                break;
            default:
                System.out.println("  → Email: 'Estado actualizado: " + evento + "'");
        }
    }
}
```

---

### `SistemaAuditoriaObservador.java`

```java
package com.logismart.observer;

import com.logismart.dominio.Envio;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SistemaAuditoriaObservador implements ObservadorEnvio {

    private final List<String> registros = new ArrayList<>();

    @Override
    public void actualizar(Envio envio, String evento) {
        String entrada = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                + " | " + envio.getId() + " | " + evento
                + " | $" + envio.getCosto();
        registros.add(entrada);
        System.out.println("[Auditoria] " + entrada);
    }

    public void mostrarRegistros() {
        System.out.println("\n=== Registros de Auditoría ===");
        for (int i = 0; i < registros.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + registros.get(i));
        }
    }

    public int obtenerCantidadRegistros() {
        return registros.size();
    }
}
```

---

### `DashboardObservador.java`

```java
package com.logismart.observer;

import com.logismart.dominio.Envio;

import java.util.HashMap;
import java.util.Map;

public class DashboardObservador implements ObservadorEnvio {

    private final Map<String, String> estadosActuales = new HashMap<>();

    @Override
    public void actualizar(Envio envio, String evento) {
        estadosActuales.put(envio.getId(), evento);
        System.out.println("[Dashboard] Actualizado: " + envio.getId() + " = " + evento);
    }

    public void mostrarDashboard() {
        System.out.println("\n=== Dashboard en Tiempo Real ===");
        estadosActuales.forEach((id, estado) ->
                System.out.println("  " + id + " → " + estado));
    }
}
```

---

## Casos de Prueba — `ObserverDemo.java`

```java
package com.logismart.observer;

import com.logismart.dominio.Envio;

public class ObserverDemo {

    public static void main(String[] args) {
        // Caso 1: Registrar observadores y disparar primer cambio de estado
        System.out.println("=== Caso 1: Registrar observadores y primer cambio ===");
        CentroDistribucionObservador centro     = new CentroDistribucionObservador();
        SistemaNotificacionObservador notif     = new SistemaNotificacionObservador();
        SistemaAuditoriaObservador    auditoria = new SistemaAuditoriaObservador();
        DashboardObservador           dashboard = new DashboardObservador();

        Envio envio1 = new Envio.EnvioBuilder("E001", "Buenos Aires", "Córdoba")
                .peso(5.0).costo(150.0).metodoPago("TARJETA").productoId("PROD-001").build();

        envio1.adjuntarObservador(centro);
        envio1.adjuntarObservador(notif);
        envio1.adjuntarObservador(auditoria);
        envio1.adjuntarObservador(dashboard);

        envio1.cambiarEstado("EN_PREPARACION");

        // Caso 2: Ciclo completo hasta entrega
        System.out.println("\n=== Caso 2: Ciclo completo hasta ENTREGADO ===");
        envio1.cambiarEstado("EN_TRANSITO");
        envio1.cambiarEstado("ENTREGADO");
        dashboard.mostrarDashboard();

        // Caso 3: Desregistrar un observador y verificar que no recibe notificación
        System.out.println("\n=== Caso 3: Desregistrar SistemaNotificacion ===");
        envio1.desadjuntarObservador(notif);

        Envio envio2 = new Envio.EnvioBuilder("E002", "Rosario", "Mendoza")
                .peso(8.0).costo(200.0).metodoPago("EFECTIVO").productoId("PROD-002").build();
        envio2.adjuntarObservador(centro);
        envio2.adjuntarObservador(auditoria);
        envio2.adjuntarObservador(dashboard);
        // SistemaNotificacion NO está registrado en envio2 ni en envio1 (fue desregistrado)
        envio2.cambiarEstado("EN_TRANSITO");

        // Caso 4: Múltiples envíos independientes — observadores compartidos
        System.out.println("\n=== Caso 4: Múltiples envíos con observadores compartidos ===");
        Envio envio3 = new Envio.EnvioBuilder("E003", "Córdoba", "Salta")
                .peso(3.0).costo(90.0).metodoPago("TARJETA").productoId("PROD-003").build();
        envio3.adjuntarObservador(auditoria);
        envio3.adjuntarObservador(dashboard);
        envio3.cambiarEstado("EN_TRANSITO");
        envio3.cambiarEstado("DEVUELTO");
        dashboard.mostrarDashboard();

        // Caso 5: Verificar totales de auditoría
        System.out.println("\n=== Caso 5: Registro total de auditoría ===");
        auditoria.mostrarRegistros();
        System.out.println("Total de eventos auditados: " + auditoria.obtenerCantidadRegistros());
    }
}
```

| # | Descripción | Resultado esperado |
|---|---|---|
| 1 | Registrar 4 observadores, primer cambio | Los 4 observadores reciben EN_PREPARACION |
| 2 | Ciclo completo hasta ENTREGADO | Centro, Notif, Auditoria y Dashboard actualizados en cada paso; dashboard muestra E001=ENTREGADO |
| 3 | Desregistrar SistemaNotificacion | E002 cambia a EN_TRANSITO: Centro, Auditoria y Dashboard reciben el evento; Notif no |
| 4 | Múltiples envíos, observadores compartidos | Dashboard muestra E001=ENTREGADO, E002=EN_TRANSITO, E003=DEVUELTO |
| 5 | Auditoría total | Registros de todos los cambios de estado de E001, E002 y E003 |

---

## Decisiones de Diseño

**¿Por qué `cambiarEstado` está en `Envio` y no en una clase Subject separada?**  
En Java no existe herencia múltiple, y `Envio` ya está en `com.logismart.dominio`. Introducir una superclase `SujetoObservable` requeriría refactorizar toda la jerarquía. Es más limpio y coherente con el resto del proyecto manejar la lista de observadores directamente en `Envio`, que es el único sujeto observable del sistema.

**¿Por qué los observadores reciben el evento como `String` en lugar de un enum?**  
Mantiene coherencia con el patrón Mediator del mismo Hito 11, donde los eventos también se manejan como `String`. En un sistema productivo, un enum o un objeto de evento tipado sería preferible para evitar errores de tipeo.

**¿Por qué `DashboardObservador` usa un `Map` en lugar de una lista?**  
El dashboard necesita mostrar el estado más reciente de cada envío, no el historial. Un `Map<String, String>` garantiza que al actualizar E001 tres veces, el dashboard siempre muestre el último estado, sin duplicados.

**¿Por qué `SistemaAuditoriaObservador` es un observador nuevo en lugar de reusar `SistemaAuditoria` del Mediator?**  
`SistemaAuditoria` del paquete `com.logismart.mediator` es un componente pasivo que recibe llamadas directas del mediador. Aquí se necesita que reaccione a eventos del sujeto mediante la interfaz `ObservadorEnvio`. Son responsabilidades distintas; crear un observador específico mantiene la separación de paquetes y evita que `SistemaAuditoria` tenga dependencias de dos patrones.

---

## Ventajas y Desventajas

**Ventajas**
- Desacoplamiento: `Envio` no conoce qué observadores están registrados ni qué hacen con la notificación.
- Agregar un nuevo observador (ej. sistema de métricas) no requiere modificar `Envio`.
- Los observadores se pueden registrar y desregistrar en tiempo de ejecución.

**Desventajas**
- Si hay muchos observadores o el procesamiento en `actualizar` es costoso, un cambio de estado puede degradar la performance.
- El orden de notificación depende del orden de registro en la lista, lo que puede ser inesperado.
- Si un observador lanza una excepción en `actualizar`, los observadores siguientes no son notificados (no hay manejo de errores en esta implementación).
