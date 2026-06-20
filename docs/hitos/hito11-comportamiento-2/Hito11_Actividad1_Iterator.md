# Hito 11 — Actividad 1: Iterator

**Proyecto:** LogiSmart - Sistema de Gestión de Logística  
**Patrón:** Iterator  
**Paquete:** `com.logismart.iterator`

---

## Descripción del Patrón

El patrón **Iterator** provee una forma de acceder secuencialmente a los elementos de una colección sin exponer su representación interna (array, lista, mapa, árbol, etc.). El cliente itera siempre de la misma manera, sin importar cómo esté almacenada la colección.

En LogiSmart se usa para recorrer envíos almacenados en tres estructuras distintas — array de tamaño fijo, lista enlazada y tabla hash — usando exactamente la misma interfaz `IteradorEnvios`.

---

## Diagrama de Clases

![Diagrama de Clases Iterator](files/1_clases_iterator.png)

```
<<interface>>                          <<interface>>
IteradorEnvios                         ColeccionEnvios
─────────────────────────              ─────────────────────────────
+ tieneSiguiente(): boolean            + crearIterador(): IteradorEnvios
+ obtenerSiguiente(): Envio            + agregar(envio: Envio): void
+ reiniciar(): void                    + remover(envio: Envio): void
                                       + obtenerTamaño(): int
                                                ▲
                           ┌────────────────────┼────────────────────┐
                           │                    │                    │
                    ColeccionArray       ColeccionLista        ColeccionHash
                    - envios: Envio[]    - cabeza: Nodo        - envios: Map<>
                    - tamaño: int        - tamaño: int
                    (inner: IteradorArray) (inner: IteradorLista) (inner: IteradorHash)
```

---

## Diagrama de Secuencia

```
Cliente     ColeccionArray    IteradorArray     Envio
   │               │                │             │
   │ agregar(e1)   │                │             │
   │──────────────>│                │             │
   │ agregar(e2)   │                │             │
   │──────────────>│                │             │
   │               │                │             │
   │ crearIterador()               │             │
   │──────────────>│                │             │
   │               │ new IteradorArray()          │
   │               │───────────────>│             │
   │    iterador   │                │             │
   │<──────────────│                │             │
   │               │                │             │
   │ tieneSiguiente()               │             │
   │───────────────────────────────>│             │
   │    true        │               │             │
   │<───────────────────────────────│             │
   │               │                │             │
   │ obtenerSiguiente()             │             │
   │───────────────────────────────>│             │
   │    envio1      │               │             │
   │<───────────────────────────────│             │
   │               │                │             │
   │ (... repite hasta tieneSig = false ...)      │
   │               │                │             │
   │ reiniciar()   │                │             │
   │───────────────────────────────>│             │
   │               │  indice = 0    │             │
   │<───────────────────────────────│             │
```

---

## Implementación

### `IteradorEnvios.java`

```java
package com.logismart.iterator;

import com.logismart.dominio.Envio;

public interface IteradorEnvios {
    boolean tieneSiguiente();
    Envio obtenerSiguiente();
    void reiniciar();
}
```

---

### `ColeccionEnvios.java`

```java
package com.logismart.iterator;

import com.logismart.dominio.Envio;

public interface ColeccionEnvios {
    IteradorEnvios crearIterador();
    void agregar(Envio envio);
    void remover(Envio envio);
    int obtenerTamaño();
}
```

---

### `ColeccionArray.java`

```java
package com.logismart.iterator;

import com.logismart.dominio.Envio;

public class ColeccionArray implements ColeccionEnvios {

    private Envio[] envios = new Envio[1000];
    private int tamaño = 0;

    @Override
    public IteradorEnvios crearIterador() {
        return new IteradorArray();
    }

    @Override
    public void agregar(Envio envio) {
        if (tamaño < envios.length) {
            envios[tamaño++] = envio;
        }
    }

    @Override
    public void remover(Envio envio) {
        for (int i = 0; i < tamaño; i++) {
            if (envios[i].getId().equals(envio.getId())) {
                System.arraycopy(envios, i + 1, envios, i, tamaño - i - 1);
                tamaño--;
                break;
            }
        }
    }

    @Override
    public int obtenerTamaño() {
        return tamaño;
    }

    private class IteradorArray implements IteradorEnvios {
        private int indice = 0;

        @Override
        public boolean tieneSiguiente() {
            return indice < tamaño;
        }

        @Override
        public Envio obtenerSiguiente() {
            return envios[indice++];
        }

        @Override
        public void reiniciar() {
            indice = 0;
        }
    }
}
```

---

### `ColeccionLista.java`

```java
package com.logismart.iterator;

import com.logismart.dominio.Envio;

public class ColeccionLista implements ColeccionEnvios {

    private Nodo cabeza;
    private int tamaño = 0;

    @Override
    public IteradorEnvios crearIterador() {
        return new IteradorLista();
    }

    @Override
    public void agregar(Envio envio) {
        Nodo nuevoNodo = new Nodo(envio);
        if (cabeza == null) {
            cabeza = nuevoNodo;
        } else {
            Nodo actual = cabeza;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = nuevoNodo;
        }
        tamaño++;
    }

    @Override
    public void remover(Envio envio) {
        if (cabeza != null && cabeza.envio.getId().equals(envio.getId())) {
            cabeza = cabeza.siguiente;
            tamaño--;
            return;
        }
        Nodo actual = cabeza;
        while (actual != null && actual.siguiente != null) {
            if (actual.siguiente.envio.getId().equals(envio.getId())) {
                actual.siguiente = actual.siguiente.siguiente;
                tamaño--;
                return;
            }
            actual = actual.siguiente;
        }
    }

    @Override
    public int obtenerTamaño() {
        return tamaño;
    }

    private class IteradorLista implements IteradorEnvios {
        private Nodo actual = cabeza;

        @Override
        public boolean tieneSiguiente() {
            return actual != null;
        }

        @Override
        public Envio obtenerSiguiente() {
            Envio envio = actual.envio;
            actual = actual.siguiente;
            return envio;
        }

        @Override
        public void reiniciar() {
            actual = cabeza;
        }
    }

    private class Nodo {
        Envio envio;
        Nodo siguiente;

        Nodo(Envio envio) {
            this.envio = envio;
        }
    }
}
```

---

### `ColeccionHash.java`

```java
package com.logismart.iterator;

import com.logismart.dominio.Envio;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ColeccionHash implements ColeccionEnvios {

    private final Map<String, Envio> envios = new HashMap<>();

    @Override
    public IteradorEnvios crearIterador() {
        return new IteradorHash();
    }

    @Override
    public void agregar(Envio envio) {
        envios.put(envio.getId(), envio);
    }

    @Override
    public void remover(Envio envio) {
        envios.remove(envio.getId());
    }

    @Override
    public int obtenerTamaño() {
        return envios.size();
    }

    private class IteradorHash implements IteradorEnvios {
        private Iterator<Envio> iterador = envios.values().iterator();

        @Override
        public boolean tieneSiguiente() {
            return iterador.hasNext();
        }

        @Override
        public Envio obtenerSiguiente() {
            return iterador.next();
        }

        @Override
        public void reiniciar() {
            iterador = envios.values().iterator();
        }
    }
}
```

---

## Casos de Prueba — `IteratorDemo.java`

```java
package com.logismart.iterator;

import com.logismart.dominio.Envio;

public class IteratorDemo {

    public static void main(String[] args) {
        // Caso 1: Iterar sobre ColeccionArray
        System.out.println("--- Caso 1: Iterar sobre ColeccionArray ---");
        ColeccionEnvios coleccionArray = new ColeccionArray();
        coleccionArray.agregar(new Envio.EnvioBuilder("E001", "Buenos Aires", "Córdoba")
                .peso(5.0).costo(150.0).metodoPago("TARJETA").productoId("PROD-001").build());
        coleccionArray.agregar(new Envio.EnvioBuilder("E002", "Rosario", "Mendoza")
                .peso(8.0).costo(200.0).metodoPago("EFECTIVO").productoId("PROD-002").build());
        IteradorEnvios iterador = coleccionArray.crearIterador();
        while (iterador.tieneSiguiente()) {
            Envio envio = iterador.obtenerSiguiente();
            System.out.println("  Array: " + envio.getId() + " | " + envio.getOrigen() + " → " + envio.getDestino());
        }

        // Caso 2: Iterar sobre ColeccionLista
        System.out.println("\n--- Caso 2: Iterar sobre ColeccionLista ---");
        ColeccionEnvios coleccionLista = new ColeccionLista();
        coleccionLista.agregar(new Envio.EnvioBuilder("E003", "Córdoba", "Salta")
                .peso(3.0).costo(100.0).metodoPago("TARJETA").productoId("PROD-003").build());
        coleccionLista.agregar(new Envio.EnvioBuilder("E004", "Mendoza", "La Plata")
                .peso(6.0).costo(180.0).metodoPago("EFECTIVO").productoId("PROD-004").build());
        iterador = coleccionLista.crearIterador();
        while (iterador.tieneSiguiente()) {
            Envio envio = iterador.obtenerSiguiente();
            System.out.println("  Lista: " + envio.getId() + " | " + envio.getOrigen() + " → " + envio.getDestino());
        }

        // Caso 3: Iterar sobre ColeccionHash
        System.out.println("\n--- Caso 3: Iterar sobre ColeccionHash ---");
        ColeccionEnvios coleccionHash = new ColeccionHash();
        coleccionHash.agregar(new Envio.EnvioBuilder("E005", "La Plata", "Junín")
                .peso(7.0).costo(160.0).metodoPago("TARJETA").productoId("PROD-005").build());
        coleccionHash.agregar(new Envio.EnvioBuilder("E006", "Junín", "Bahía Blanca")
                .peso(4.0).costo(120.0).metodoPago("EFECTIVO").productoId("PROD-006").build());
        iterador = coleccionHash.crearIterador();
        while (iterador.tieneSiguiente()) {
            Envio envio = iterador.obtenerSiguiente();
            System.out.println("  Hash: " + envio.getId() + " | " + envio.getOrigen() + " → " + envio.getDestino());
        }

        // Caso 4: Reiniciar iterador y reiterar
        System.out.println("\n--- Caso 4: Reiniciar iterador ---");
        iterador.reiniciar();
        System.out.println("Reiterando Hash desde el inicio:");
        while (iterador.tieneSiguiente()) {
            Envio envio = iterador.obtenerSiguiente();
            System.out.println("  Reiterado: " + envio.getId());
        }

        // Caso 5: Remover y verificar tamaño
        System.out.println("\n--- Caso 5: Remover elemento y verificar tamaño ---");
        System.out.println("  Tamaño antes: " + coleccionArray.obtenerTamaño());
        coleccionArray.remover(new Envio.EnvioBuilder("E001", "", "").peso(0).costo(0).metodoPago("").productoId("").build());
        System.out.println("  Tamaño después de remover E001: " + coleccionArray.obtenerTamaño());
        iterador = coleccionArray.crearIterador();
        while (iterador.tieneSiguiente()) {
            Envio envio = iterador.obtenerSiguiente();
            System.out.println("  Restante: " + envio.getId());
        }
    }
}
```

| # | Descripción | Colección | Resultado esperado |
|---|---|---|---|
| 1 | Iterar todos los elementos | `ColeccionArray` | E001, E002 en orden de inserción |
| 2 | Iterar lista enlazada | `ColeccionLista` | E003, E004 en orden de inserción |
| 3 | Iterar tabla hash | `ColeccionHash` | E005, E006 (orden no garantizado) |
| 4 | Reiniciar y reiterar | `ColeccionHash` | Mismos elementos desde el inicio |
| 5 | Remover y verificar tamaño | `ColeccionArray` | tamaño 2→1, E002 permanece |

---

## Decisiones de Diseño

**¿Por qué clases internas privadas para los iteradores?**  
Cada iterador necesita acceder al estado interno de su colección (el array, el nodo cabeza o el mapa). Usar clases internas privadas les da ese acceso sin exponer la estructura al exterior.

**¿Por qué `reiniciar()` en lugar de crear un nuevo iterador?**  
Permite reutilizar el mismo iterador sin crear un nuevo objeto. En `ColeccionHash`, `reiniciar()` recrea el iterador subyacente de Java porque `java.util.Iterator` no tiene un método equivalente.

**¿Por qué dos interfaces (`IteradorEnvios` y `ColeccionEnvios`) y no una sola?**  
Siguen responsabilidades distintas: `ColeccionEnvios` define cómo gestionar la colección, `IteradorEnvios` define cómo recorrerla. Separarlas permite que el cliente dependa solo de `IteradorEnvios` sin conocer la colección.

---

## Ventajas y Desventajas

**Ventajas**
- El cliente no necesita conocer la estructura interna de la colección.
- Se puede agregar una nueva colección (árbol, cola, etc.) sin cambiar el código cliente.
- Múltiples iteradores pueden recorrer la misma colección de forma independiente y simultánea.

**Desventajas**
- Proliferación de clases: cada colección requiere su propia clase iterador.
- Si la colección se modifica durante la iteración, el comportamiento es indefinido (no hay protección `ConcurrentModificationException` en esta implementación).
- `ColeccionHash` no garantiza orden de iteración.
