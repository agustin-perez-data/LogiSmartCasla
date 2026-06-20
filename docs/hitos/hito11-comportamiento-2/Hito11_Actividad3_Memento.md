# Hito 11 — Actividad 3: Memento

**Proyecto:** LogiSmart - Sistema de Gestión de Logística  
**Patrón:** Memento  
**Paquete:** `com.logismart.memento`

---

## Descripción del Patrón

El patrón **Memento** permite capturar y externalizar el estado interno de un objeto sin violar su encapsulamiento, de modo que el objeto pueda ser restaurado a ese estado más tarde.

En LogiSmart se usa para registrar el historial de estados de un `Envio` a lo largo de su ciclo de vida (CONFIRMADO → EN_PREPARACION → EN_TRANSITO → ENTREGADO), permitiendo deshacer cambios de estado y navegar el historial hacia adelante y atrás.

Los tres participantes del patrón son:

- **Originador:** `com.logismart.dominio.Envio` — crea y restaura mementos.
- **Memento:** `MementoEnvio` — encapsula una instantánea del estado del envío.
- **Caretaker:** `HistorialEnvios` — gestiona la lista de mementos sin inspeccionar su contenido.

---

## Diagrama de Clases

![Diagrama de Clases Memento](files/3_clases_memento.png)

```
com.logismart.dominio
──────────────────────────────────────────────────────────
Envio  (Originador)
- estado: String
- origen: String
- destino: String
- peso: double
- costo: double
──────────────────────────────────────────────────────────
+ crearMemento(): MementoEnvio
+ restaurarDesdeMemento(m: MementoEnvio): void
+ cambiarEstado(nuevoEstado: String): void
+ obtenerEstado(): String
        │ crea / restaura
        ▼
com.logismart.memento
──────────────────────────────────────────────────────────
MementoEnvio  (Memento)
- estado: String
- origen: String
- destino: String
- peso: double
- costo: double
- timestamp: LocalDateTime
──────────────────────────────────────────────────────────
+ obtenerEstado(): String
+ obtenerOrigen(): String
+ obtenerDestino(): String
+ obtenerPeso(): double
+ obtenerCosto(): double
+ obtenerTimestamp(): LocalDateTime
+ toString(): String

HistorialEnvios  (Caretaker)
- historial: List<MementoEnvio>
- indiceActual: int
──────────────────────────────────────────────────────────
+ guardarEstado(envio: Envio): void
+ irAlEstadoAnterior(envio: Envio): boolean
+ irAlEstadoSiguiente(envio: Envio): boolean
+ mostrarHistorial(): void
+ obtenerTamaño(): int
```

---

## Diagrama de Secuencia

Flujo de guardado, retroceso y avance de estado de un envío.

```
Cliente    Envio       HistorialEnvios    MementoEnvio
   │         │                │                │
   │cambiarEstado("EN_PREPARACION")            │
   │────────>│                │                │
   │         │                │                │
   │guardarEstado(envio)       │                │
   │──────────────────────────>│                │
   │         │crearMemento()  │                │
   │         │<───────────────│                │
   │         │                │ new Memento()  │
   │         │────────────────────────────────>│
   │         │   memento      │                │
   │         │<────────────────────────────────│
   │         │────────────────>│               │
   │         │                 │historial.add  │
   │         │                 │indice++       │
   │<──────────────────────────│               │
   │         │                 │               │
   │cambiarEstado("EN_TRANSITO")               │
   │────────>│                 │               │
   │guardarEstado(envio)        │               │
   │──────────────────────────>│               │
   │         │crearMemento()  │               │
   │         │<───────────────│               │
   │         │────────────────────────────────>│
   │         │<────────────────────────────────│
   │         │────────────────>│               │
   │<──────────────────────────│               │
   │         │                 │               │
   │irAlEstadoAnterior(envio)  │               │
   │──────────────────────────>│               │
   │         │                 │historial[i-1] │
   │         │restaurarDesdeMemento(m)         │
   │         │<───────────────│               │
   │         │estado="EN_PREPARACION"          │
   │<──────────────────────────│               │
   │         │                 │               │
   │irAlEstadoSiguiente(envio) │               │
   │──────────────────────────>│               │
   │         │                 │historial[i+1] │
   │         │restaurarDesdeMemento(m)         │
   │         │<───────────────│               │
   │         │estado="EN_TRANSITO"             │
   │<──────────────────────────│               │
```

---

## Modificación a `com.logismart.dominio.Envio`

Para soportar el patrón Memento se agregan los siguientes elementos a la clase `Envio` existente:

```java
// Campo nuevo
private String estado = "CONFIRMADO";

// Métodos nuevos
public MementoEnvio crearMemento() {
    return new com.logismart.memento.MementoEnvio(estado, origen, destino, peso, costo);
}

public void restaurarDesdeMemento(com.logismart.memento.MementoEnvio memento) {
    this.estado  = memento.obtenerEstado();
    this.origen  = memento.obtenerOrigen();
    this.destino = memento.obtenerDestino();
    this.peso    = memento.obtenerPeso();
    this.costo   = memento.obtenerCosto();
}

public void cambiarEstado(String nuevoEstado) {
    System.out.println("[Envio " + id + "] Estado: " + this.estado + " → " + nuevoEstado);
    this.estado = nuevoEstado;
}

public String obtenerEstado() {
    return estado;
}
```

> **Nota:** Los campos `origen`, `destino`, `peso` y `costo` deben pasar de `private final` a `private` para que `restaurarDesdeMemento` pueda modificarlos. El `id` no se incluye en el memento porque es inmutable e identifica al envío.

---

## Implementación

### `MementoEnvio.java`

```java
package com.logismart.memento;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MementoEnvio {

    private final String        estado;
    private final String        origen;
    private final String        destino;
    private final double        peso;
    private final double        costo;
    private final LocalDateTime timestamp;

    public MementoEnvio(String estado, String origen, String destino,
                        double peso, double costo) {
        this.estado    = estado;
        this.origen    = origen;
        this.destino   = destino;
        this.peso      = peso;
        this.costo     = costo;
        this.timestamp = LocalDateTime.now();
    }

    public String        obtenerEstado()     { return estado; }
    public String        obtenerOrigen()     { return origen; }
    public String        obtenerDestino()    { return destino; }
    public double        obtenerPeso()       { return peso; }
    public double        obtenerCosto()      { return costo; }
    public LocalDateTime obtenerTimestamp()  { return timestamp; }

    @Override
    public String toString() {
        return "[" + timestamp.format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS")) + "] "
                + estado + " | " + origen + " → " + destino
                + " | " + peso + "kg | $" + costo;
    }
}
```

---

### `HistorialEnvios.java`

```java
package com.logismart.memento;

import com.logismart.dominio.Envio;

import java.util.ArrayList;
import java.util.List;

public class HistorialEnvios {

    private final List<MementoEnvio> historial = new ArrayList<>();
    private int indiceActual = -1;

    /**
     * Guarda el estado actual del envío en el historial.
     * Si el puntero no está al final (hay estados "futuros"), los descarta.
     */
    public void guardarEstado(Envio envio) {
        // Descartar estados hacia adelante del puntero actual
        while (historial.size() > indiceActual + 1) {
            historial.remove(historial.size() - 1);
        }
        MementoEnvio memento = envio.crearMemento();
        historial.add(memento);
        indiceActual++;
        System.out.println("[Historial] Estado guardado (#" + indiceActual + "): " + memento);
    }

    /**
     * Restaura el estado anterior. Devuelve false si ya está en el inicio.
     */
    public boolean irAlEstadoAnterior(Envio envio) {
        if (indiceActual <= 0) {
            System.out.println("[Historial] Ya está en el estado inicial — no se puede retroceder");
            return false;
        }
        indiceActual--;
        MementoEnvio memento = historial.get(indiceActual);
        envio.restaurarDesdeMemento(memento);
        System.out.println("[Historial] Restaurado al estado #" + indiceActual + ": " + memento);
        return true;
    }

    /**
     * Restaura el estado siguiente. Devuelve false si ya está al final.
     */
    public boolean irAlEstadoSiguiente(Envio envio) {
        if (indiceActual >= historial.size() - 1) {
            System.out.println("[Historial] Ya está en el estado más reciente — no se puede avanzar");
            return false;
        }
        indiceActual++;
        MementoEnvio memento = historial.get(indiceActual);
        envio.restaurarDesdeMemento(memento);
        System.out.println("[Historial] Avanzado al estado #" + indiceActual + ": " + memento);
        return true;
    }

    public void mostrarHistorial() {
        System.out.println("\n=== Historial de Estados ===");
        for (int i = 0; i < historial.size(); i++) {
            String cursor = (i == indiceActual) ? " ◄ actual" : "";
            System.out.println("  " + i + ". " + historial.get(i) + cursor);
        }
        System.out.println();
    }

    public int obtenerTamaño() {
        return historial.size();
    }
}
```

---

## Casos de Prueba — `MementoDemo.java`

```java
package com.logismart.memento;

import com.logismart.dominio.Envio;

public class MementoDemo {

    public static void main(String[] args) {
        HistorialEnvios historial = new HistorialEnvios();

        Envio envio = new Envio.EnvioBuilder("E001", "Buenos Aires", "Córdoba")
                .peso(5.0).costo(150.0).metodoPago("TARJETA").productoId("PROD-001").build();

        // Caso 1: Guardar estado inicial y avanzar estados
        System.out.println("=== Caso 1: Guardar estados del ciclo de vida ===");
        historial.guardarEstado(envio);                   // CONFIRMADO
        envio.cambiarEstado("EN_PREPARACION");
        historial.guardarEstado(envio);
        envio.cambiarEstado("EN_TRANSITO");
        historial.guardarEstado(envio);
        envio.cambiarEstado("ENTREGADO");
        historial.guardarEstado(envio);
        historial.mostrarHistorial();
        System.out.println("Estado actual: " + envio.obtenerEstado());

        // Caso 2: Retroceder un estado
        System.out.println("=== Caso 2: Retroceder un estado ===");
        historial.irAlEstadoAnterior(envio);
        System.out.println("Estado actual: " + envio.obtenerEstado());
        historial.mostrarHistorial();

        // Caso 3: Retroceder al inicio
        System.out.println("=== Caso 3: Retroceder hasta el estado inicial ===");
        historial.irAlEstadoAnterior(envio);
        historial.irAlEstadoAnterior(envio);
        historial.irAlEstadoAnterior(envio); // intenta retroceder más allá del inicio
        System.out.println("Estado actual: " + envio.obtenerEstado());
        historial.mostrarHistorial();

        // Caso 4: Avanzar desde el inicio
        System.out.println("=== Caso 4: Avanzar dos estados ===");
        historial.irAlEstadoSiguiente(envio);
        historial.irAlEstadoSiguiente(envio);
        System.out.println("Estado actual: " + envio.obtenerEstado());
        historial.mostrarHistorial();

        // Caso 5: Nuevo cambio desde el medio descarta el futuro
        System.out.println("=== Caso 5: Nuevo estado desde el medio (descarta futuro) ===");
        System.out.println("Tamaño antes: " + historial.obtenerTamaño());
        envio.cambiarEstado("DEVUELTO");
        historial.guardarEstado(envio);
        System.out.println("Tamaño después: " + historial.obtenerTamaño());
        historial.mostrarHistorial();
        // El estado ENTREGADO (índice 3) ya no existe
        historial.irAlEstadoSiguiente(envio); // debe indicar que ya está al final
    }
}
```

| # | Descripción | Resultado esperado |
|---|---|---|
| 1 | Ciclo de vida completo — 4 estados guardados | historial con 4 entradas, estado actual: ENTREGADO |
| 2 | Retroceder un estado | estado actual: EN_TRANSITO, puntero en índice 2 |
| 3 | Retroceder hasta el inicio + intento extra | estado actual: CONFIRMADO, mensaje de límite alcanzado |
| 4 | Avanzar dos estados desde el inicio | estado actual: EN_TRANSITO, puntero en índice 2 |
| 5 | Nuevo estado desde el medio descarta el futuro | historial queda con 3 entradas (CONFIRMADO, EN_PREPARACION, DEVUELTO), no se puede avanzar |

---

## Decisiones de Diseño

**¿Por qué el `id` no forma parte del memento?**  
El `id` identifica al envío de forma inmutable — nunca cambia en su ciclo de vida. Incluirlo en el memento sería redundante y podría dar la impresión errónea de que la identidad del envío puede restaurarse, lo que introduciría ambigüedad conceptual.

**¿Por qué los campos de `Envio` cambian de `final` a mutables?**  
El patrón Memento necesita que el originador pueda restaurar sus propios campos desde un memento. Mantener `final` imposibilitaría `restaurarDesdeMemento`. Solo `id` permanece `final` por ser identificador permanente.

**¿Por qué `HistorialEnvios` descarta estados futuros al guardar?**  
Este comportamiento es el estándar de undo/redo (similar a editores de texto): si el usuario retrocede dos pasos y realiza una nueva acción, la rama "futura" queda obsoleta y se descarta. Mantenerla generaría un árbol de estados que complica el modelo sin beneficio académico.

**¿Por qué incluir `timestamp` en `MementoEnvio`?**  
Permite que `mostrarHistorial` muestre cuándo ocurrió cada cambio, haciendo el historial auditable y coherente con `SistemaAuditoria` del patrón Mediator del mismo Hito 11.

---

## Ventajas y Desventajas

**Ventajas**
- Permite deshacer/rehacer cambios de estado sin exponer la estructura interna de `Envio`.
- El caretaker (`HistorialEnvios`) no necesita conocer el contenido del memento — solo lo almacena.
- Fácil de extender: agregar un nuevo campo auditable a `Envio` solo requiere actualizar `MementoEnvio` y los dos métodos `crearMemento` / `restaurarDesdeMemento`.

**Desventajas**
- Si `Envio` tiene muchos campos o crece, cada memento puede consumir memoria significativa.
- El historial no está acotado — en producción sería necesario un límite de entradas.
- Requiere que los campos de `Envio` sean mutables, lo que debilita levemente el encapsulamiento del originador.
