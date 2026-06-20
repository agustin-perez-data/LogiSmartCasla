# Hito 10 — Actividad 3: Interpreter

**Proyecto:** LogiSmart - Sistema de Gestión de Logística  
**Patrón:** Interpreter  
**Paquete:** `com.logismart.interpreter`

---

## Descripción del Patrón

El patrón **Interpreter** define una representación gramatical para un lenguaje y provee un intérprete para evaluarla. Cada regla de la gramática se mapea a una clase.

Dos tipos de expresiones:
- **Terminales:** evalúan un campo del `Envio` directamente. Son las hojas del árbol.
- **No-terminales:** combinan otras expresiones (`AND`, `OR`, `NOT`). Son los nodos internos.

---

## Diagrama de Clases

```
<<interface>>
Expresion
─────────────────────────
+ evaluar(Envio): boolean
        ▲
        ├── ExpresionOrigen       (terminal)
        ├── ExpresionDestino      (terminal)
        ├── ExpresionPeso         (terminal)
        ├── ExpresionCosto        (terminal)
        ├── ExpresionRestringido  (terminal)
        ├── ExpresionAND          (no-terminal: izquierda + derecha)
        ├── ExpresionOR           (no-terminal: izquierda + derecha)
        └── ExpresionNOT          (no-terminal: expresion)
```

---

## Diagrama de Secuencia

Evaluación de `(ORIGEN='BA' AND COSTO>100) AND NOT RESTRINGIDO`:

```
Cliente   AND(raíz)   AND(inner)  ExpOrigen  ExpCosto   NOT       ExpRestringido
   │          │            │          │          │         │              │
   │evaluar() │            │          │          │         │              │
   │─────────>│            │          │          │         │              │
   │          │izq.evaluar()          │          │         │              │
   │          │───────────>│          │          │         │              │
   │          │            │evaluar() │          │         │              │
   │          │            │─────────>│          │         │              │
   │          │            │  true    │          │         │              │
   │          │            │<─────────│          │         │              │
   │          │            │evaluar()            │         │              │
   │          │            │────────────────────>│         │              │
   │          │            │  true               │         │              │
   │          │  true      │<────────────────────│         │              │
   │          │<───────────│          │          │         │              │
   │          │der.evaluar()                               │              │
   │          │───────────────────────────────────────────>│              │
   │          │                                            │evaluar()     │
   │          │                                            │─────────────>│
   │          │                                            │   false      │
   │          │                                            │<─────────────│
   │          │  !false = true                             │              │
   │          │<───────────────────────────────────────────│              │
   │  true    │                                                            │
   │<─────────│                                                            │
```

---

## Implementación

### `Expresion.java`

```java
package com.logismart.interpreter;

import com.logismart.dominio.Envio;

public interface Expresion {
    boolean evaluar(Envio envio);
}
```

---

### `ExpresionOrigen.java`

```java
package com.logismart.interpreter;

import com.logismart.dominio.Envio;

public class ExpresionOrigen implements Expresion {

    private final String valor;

    public ExpresionOrigen(String valor) {
        this.valor = valor;
    }

    @Override
    public boolean evaluar(Envio envio) {
        return valor.equals(envio.getOrigen());
    }
}
```

---

### `ExpresionDestino.java`

```java
package com.logismart.interpreter;

import com.logismart.dominio.Envio;

public class ExpresionDestino implements Expresion {

    private final String valor;

    public ExpresionDestino(String valor) {
        this.valor = valor;
    }

    @Override
    public boolean evaluar(Envio envio) {
        return valor.equals(envio.getDestino());
    }
}
```

---

### `ExpresionPeso.java`

```java
package com.logismart.interpreter;

import com.logismart.dominio.Envio;

public class ExpresionPeso implements Expresion {

    private final double valor;
    private final String operador; // "<", ">", "="

    public ExpresionPeso(double valor, String operador) {
        this.valor = valor;
        this.operador = operador;
    }

    @Override
    public boolean evaluar(Envio envio) {
        switch (operador) {
            case "<":  return envio.getPeso() < valor;
            case ">":  return envio.getPeso() > valor;
            case "=":  return envio.getPeso() == valor;
            default:   return false;
        }
    }
}
```

---

### `ExpresionCosto.java`

```java
package com.logismart.interpreter;

import com.logismart.dominio.Envio;

public class ExpresionCosto implements Expresion {

    private final double valor;
    private final String operador; // "<", ">", "="

    public ExpresionCosto(double valor, String operador) {
        this.valor = valor;
        this.operador = operador;
    }

    @Override
    public boolean evaluar(Envio envio) {
        switch (operador) {
            case "<":  return envio.getCosto() < valor;
            case ">":  return envio.getCosto() > valor;
            case "=":  return envio.getCosto() == valor;
            default:   return false;
        }
    }
}
```

---

### `ExpresionRestringido.java`

```java
package com.logismart.interpreter;

import com.logismart.dominio.Envio;

public class ExpresionRestringido implements Expresion {

    @Override
    public boolean evaluar(Envio envio) {
        return envio.getDestino().contains("Restringido");
    }
}
```

---

### `ExpresionAND.java`

```java
package com.logismart.interpreter;

import com.logismart.dominio.Envio;

public class ExpresionAND implements Expresion {

    private final Expresion izquierda;
    private final Expresion derecha;

    public ExpresionAND(Expresion izquierda, Expresion derecha) {
        this.izquierda = izquierda;
        this.derecha = derecha;
    }

    @Override
    public boolean evaluar(Envio envio) {
        return izquierda.evaluar(envio) && derecha.evaluar(envio);
    }
}
```

---

### `ExpresionOR.java`

```java
package com.logismart.interpreter;

import com.logismart.dominio.Envio;

public class ExpresionOR implements Expresion {

    private final Expresion izquierda;
    private final Expresion derecha;

    public ExpresionOR(Expresion izquierda, Expresion derecha) {
        this.izquierda = izquierda;
        this.derecha = derecha;
    }

    @Override
    public boolean evaluar(Envio envio) {
        return izquierda.evaluar(envio) || derecha.evaluar(envio);
    }
}
```

---

### `ExpresionNOT.java`

```java
package com.logismart.interpreter;

import com.logismart.dominio.Envio;

public class ExpresionNOT implements Expresion {

    private final Expresion expresion;

    public ExpresionNOT(Expresion expresion) {
        this.expresion = expresion;
    }

    @Override
    public boolean evaluar(Envio envio) {
        return !expresion.evaluar(envio);
    }
}
```

---

## Casos de Prueba — `InterpreterDemo.java`

```java
package com.logismart.interpreter;

import com.logismart.dominio.Envio;

public class InterpreterDemo {

    public static void main(String[] args) {
        Envio envio1 = new Envio.EnvioBuilder("E001", "Buenos Aires", "Córdoba")
                .peso(5.0).costo(150.0).metodoPago("TARJETA").productoId("PROD-001")
                .build();

        Envio envio2 = new Envio.EnvioBuilder("E002", "Rosario", "Mendoza")
                .peso(15.0).costo(80.0).metodoPago("EFECTIVO").productoId("PROD-002")
                .build();

        Envio envio3 = new Envio.EnvioBuilder("E003", "Buenos Aires", "Zona Restringida")
                .peso(3.0).costo(200.0).metodoPago("TARJETA").productoId("PROD-003")
                .build();

        // Caso 1: Regla simple — ORIGEN = "Buenos Aires"
        Expresion regla1 = new ExpresionOrigen("Buenos Aires");
        System.out.println("--- Caso 1: ORIGEN = 'Buenos Aires' ---");
        System.out.println("  Envío 1 (BA→Córdoba):      " + regla1.evaluar(envio1)); // true
        System.out.println("  Envío 2 (Rosario→Mendoza): " + regla1.evaluar(envio2)); // false

        // Caso 2: AND — ORIGEN = "Buenos Aires" AND PESO < 10
        Expresion regla2 = new ExpresionAND(
                new ExpresionOrigen("Buenos Aires"),
                new ExpresionPeso(10, "<")
        );
        System.out.println("\n--- Caso 2: ORIGEN='Buenos Aires' AND PESO < 10 ---");
        System.out.println("  Envío 1 (peso 5):  " + regla2.evaluar(envio1)); // true
        System.out.println("  Envío 2 (peso 15): " + regla2.evaluar(envio2)); // false

        // Caso 3: OR — DESTINO = "Córdoba" OR DESTINO = "Mendoza"
        Expresion regla3 = new ExpresionOR(
                new ExpresionDestino("Córdoba"),
                new ExpresionDestino("Mendoza")
        );
        System.out.println("\n--- Caso 3: DESTINO='Córdoba' OR DESTINO='Mendoza' ---");
        System.out.println("  Envío 1 (Córdoba):          " + regla3.evaluar(envio1)); // true
        System.out.println("  Envío 2 (Mendoza):          " + regla3.evaluar(envio2)); // true
        System.out.println("  Envío 3 (Zona Restringida): " + regla3.evaluar(envio3)); // false

        // Caso 4: NOT — NOT RESTRINGIDO
        Expresion regla4 = new ExpresionNOT(new ExpresionRestringido());
        System.out.println("\n--- Caso 4: NOT RESTRINGIDO ---");
        System.out.println("  Envío 1 (Córdoba):          " + regla4.evaluar(envio1)); // true
        System.out.println("  Envío 3 (Zona Restringida): " + regla4.evaluar(envio3)); // false

        // Caso 5: Compleja — (ORIGEN='BA' AND COSTO>100) AND NOT RESTRINGIDO
        Expresion regla5 = new ExpresionAND(
                new ExpresionAND(
                        new ExpresionOrigen("Buenos Aires"),
                        new ExpresionCosto(100, ">")
                ),
                new ExpresionNOT(new ExpresionRestringido())
        );
        System.out.println("\n--- Caso 5: (ORIGEN='BA' AND COSTO>100) AND NOT RESTRINGIDO ---");
        System.out.println("  Envío 1 (BA, $150, Córdoba):        " + regla5.evaluar(envio1)); // true
        System.out.println("  Envío 2 (Rosario, $80, Mendoza):    " + regla5.evaluar(envio2)); // false
        System.out.println("  Envío 3 (BA, $200, Restringida):    " + regla5.evaluar(envio3)); // false
    }
}
```

| # | Expresión | Resultado esperado |
|---|---|---|
| 1 | `ORIGEN = 'Buenos Aires'` | true / false |
| 2 | `ORIGEN='BA' AND PESO<10` | true / false |
| 3 | `DESTINO='Córdoba' OR DESTINO='Mendoza'` | true / true / false |
| 4 | `NOT RESTRINGIDO` | true / false |
| 5 | `(ORIGEN='BA' AND COSTO>100) AND NOT RESTRINGIDO` | true / false / false |

---

## Decisiones de Diseño

**¿Por qué interfaz y no clase abstracta para `Expresion`?**  
Las terminales y no-terminales no comparten estado ni comportamiento — solo el contrato `evaluar()`. Una interfaz es suficiente y más limpia.

**¿Por qué `ExpresionPeso` y `ExpresionCosto` reciben el operador como `String`?**  
Permite construir expresiones dinámicamente sin crear una clase por cada combinación campo/operador.

---

## Ventajas y Desventajas

**Ventajas**
- Extensible: agregar una nueva expresión terminal es una sola clase nueva.
- Composable: reglas complejas se construyen combinando piezas simples.
- Reutilizable: las mismas expresiones se combinan en distintas reglas.

**Desventajas**
- Explosión de clases en gramáticas complejas.
- Sin parser: las reglas deben construirse programáticamente.
- Performance: evaluación recursiva sobre árboles grandes puede ser costosa.
