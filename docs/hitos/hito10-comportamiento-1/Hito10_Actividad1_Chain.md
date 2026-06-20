# Hito 10 — Actividad 1: Chain of Responsibility

**Proyecto:** LogiSmart - Sistema de Gestión de Logística  
**Patrón:** Chain of Responsibility  
**Paquete:** `com.logismart.chain`

---

## Descripción del Patrón

El patrón **Chain of Responsibility** permite pasar una solicitud a través de una cadena de manejadores. Cada manejador decide si procesa la solicitud o la delega al siguiente. El emisor no necesita saber qué manejador la resolverá.

En LogiSmart se usa para validar un envío a través de 5 niveles antes de confirmarlo: datos, inventario, pago, seguridad y capacidad. Si cualquier validador falla, la cadena se interrumpe y el envío es rechazado.

---

## Diagrama de Clases

```
                    ┌─────────────────────────────┐
                    │   <<abstract>>               │
                    │   ValidadorEnvio             │
                    │─────────────────────────────│
                    │ # siguiente: ValidadorEnvio  │
                    │─────────────────────────────│
                    │ + setSiguiente(ValidadorEnvio)│
                    │ + validar(Envio): boolean    │
                    │ + obtenerNombre(): String    │
                    └─────────────────────────────┘
                               ▲
        ┌──────────┬───────────┼───────────┬──────────┐
        │          │           │           │          │
ValidadorDatos  ValidadorInv.  ValidadorPago  ValidadorSeg.  ValidadorCapacidad

┌───────────────────────────────────┐
│       CadenaValidadores           │
│ - primerValidador: ValidadorEnvio │
│ + validarEnvio(Envio): boolean    │
└───────────────────────────────────┘

<<interface>>                <<interface>>
SistemaInventario            SistemaCapacidad
+ verificarStock(String)     + hayEspacioDisponible(double)
```

---

## Diagrama de Secuencia

```
Cliente    CadenaVal.  ValidDatos  ValidInvent.  ValidPago  ValidSegur.  ValidCapacidad
   │            │           │            │           │           │             │
   │validarEnvio│           │            │           │           │             │
   │───────────>│           │            │           │           │             │
   │            │ validar() │            │           │           │             │
   │            │──────────>│            │           │           │             │
   │            │           │  ✓ OK      │           │           │             │
   │            │           │ validar()  │           │           │             │
   │            │           │───────────>│           │           │             │
   │            │           │            │  ✓ OK     │           │             │
   │            │           │            │ validar() │           │             │
   │            │           │            │──────────>│           │             │
   │            │           │            │           │  ✓ OK     │             │
   │            │           │            │           │ validar() │             │
   │            │           │            │           │──────────>│             │
   │            │           │            │           │           │  ✓ OK       │
   │            │           │            │           │           │ validar()   │
   │            │           │            │           │           │────────────>│
   │            │           │            │           │           │    true     │
   │<───────────┤ true       │            │           │           │             │
```

---

## Implementación

### Modificación a `Envio.java` (campos agregados para este Hito)

```java
// Nuevos campos en com.logismart.dominio.Envio

// En la clase:
private double costo;
private String metodoPago;
private String productoId;

// En el constructor (from builder):
this.costo      = builder.costo;
this.metodoPago = builder.metodoPago;
this.productoId = builder.productoId;

// Getters:
public double getCosto()       { return costo; }
public String getMetodoPago()  { return metodoPago; }
public String getProductoId()  { return productoId; }

// En EnvioBuilder — campos con defaults:
private double costo       = 0.0;
private String metodoPago  = "";
private String productoId  = "";

// Builder setters:
public EnvioBuilder costo(double costo)           { this.costo = costo; return this; }
public EnvioBuilder metodoPago(String metodoPago) { this.metodoPago = metodoPago; return this; }
public EnvioBuilder productoId(String productoId) { this.productoId = productoId; return this; }
```

---

### `SistemaInventario.java`

```java
package com.logismart.chain;

public interface SistemaInventario {
    boolean verificarStock(String productoId);
}
```

---

### `SistemaCapacidad.java`

```java
package com.logismart.chain;

public interface SistemaCapacidad {
    boolean hayEspacioDisponible(double peso);
}
```

---

### `ValidadorEnvio.java`

```java
package com.logismart.chain;

import com.logismart.dominio.Envio;

public abstract class ValidadorEnvio {

    protected ValidadorEnvio siguiente;

    public void setSiguiente(ValidadorEnvio siguiente) {
        this.siguiente = siguiente;
    }

    public abstract boolean validar(Envio envio);

    public abstract String obtenerNombre();
}
```

---

### `ValidadorDatos.java`

```java
package com.logismart.chain;

import com.logismart.dominio.Envio;

public class ValidadorDatos extends ValidadorEnvio {

    @Override
    public boolean validar(Envio envio) {
        System.out.println("[" + obtenerNombre() + "] Validando...");

        if (envio.getOrigen() == null || envio.getOrigen().isEmpty()) {
            System.err.println("  ✗ Origen inválido");
            return false;
        }
        if (envio.getDestino() == null || envio.getDestino().isEmpty()) {
            System.err.println("  ✗ Destino inválido");
            return false;
        }
        if (envio.getPeso() <= 0) {
            System.err.println("  ✗ Peso inválido");
            return false;
        }

        System.out.println("  ✓ Datos válidos");
        return siguiente == null || siguiente.validar(envio);
    }

    @Override
    public String obtenerNombre() {
        return "ValidadorDatos";
    }
}
```

---

### `ValidadorInventario.java`

```java
package com.logismart.chain;

import com.logismart.dominio.Envio;

public class ValidadorInventario extends ValidadorEnvio {

    private final SistemaInventario inventario;

    public ValidadorInventario(SistemaInventario inventario) {
        this.inventario = inventario;
    }

    @Override
    public boolean validar(Envio envio) {
        System.out.println("[" + obtenerNombre() + "] Verificando stock...");

        if (!inventario.verificarStock(envio.getProductoId())) {
            System.err.println("  ✗ Stock insuficiente");
            return false;
        }

        System.out.println("  ✓ Stock disponible");
        return siguiente == null || siguiente.validar(envio);
    }

    @Override
    public String obtenerNombre() {
        return "ValidadorInventario";
    }
}
```

---

### `ValidadorPago.java`

```java
package com.logismart.chain;

import com.logismart.dominio.Envio;

public class ValidadorPago extends ValidadorEnvio {

    @Override
    public boolean validar(Envio envio) {
        System.out.println("[" + obtenerNombre() + "] Verificando pago...");

        if (envio.getCosto() <= 0) {
            System.err.println("  ✗ Costo inválido");
            return false;
        }
        if (envio.getMetodoPago() == null || envio.getMetodoPago().isEmpty()) {
            System.err.println("  ✗ Método de pago no especificado");
            return false;
        }

        System.out.println("  ✓ Pago válido");
        return siguiente == null || siguiente.validar(envio);
    }

    @Override
    public String obtenerNombre() {
        return "ValidadorPago";
    }
}
```

---

### `ValidadorSeguridad.java`

```java
package com.logismart.chain;

import com.logismart.dominio.Envio;

public class ValidadorSeguridad extends ValidadorEnvio {

    @Override
    public boolean validar(Envio envio) {
        System.out.println("[" + obtenerNombre() + "] Verificando restricciones...");

        if (envio.getDestino().contains("Restringido")) {
            System.err.println("  ✗ Destino restringido");
            return false;
        }

        System.out.println("  ✓ Seguridad OK");
        return siguiente == null || siguiente.validar(envio);
    }

    @Override
    public String obtenerNombre() {
        return "ValidadorSeguridad";
    }
}
```

---

### `ValidadorCapacidad.java`

```java
package com.logismart.chain;

import com.logismart.dominio.Envio;

public class ValidadorCapacidad extends ValidadorEnvio {

    private final SistemaCapacidad sistemaCapacidad;

    public ValidadorCapacidad(SistemaCapacidad sistemaCapacidad) {
        this.sistemaCapacidad = sistemaCapacidad;
    }

    @Override
    public boolean validar(Envio envio) {
        System.out.println("[" + obtenerNombre() + "] Verificando capacidad...");

        if (!sistemaCapacidad.hayEspacioDisponible(envio.getPeso())) {
            System.err.println("  ✗ No hay espacio disponible");
            return false;
        }

        System.out.println("  ✓ Capacidad disponible");
        return siguiente == null || siguiente.validar(envio);
    }

    @Override
    public String obtenerNombre() {
        return "ValidadorCapacidad";
    }
}
```

---

### `CadenaValidadores.java`

```java
package com.logismart.chain;

import com.logismart.dominio.Envio;

public class CadenaValidadores {

    private final ValidadorEnvio primerValidador;

    public CadenaValidadores(SistemaInventario inventario, SistemaCapacidad capacidad) {
        ValidadorEnvio validador1 = new ValidadorDatos();
        ValidadorEnvio validador2 = new ValidadorInventario(inventario);
        ValidadorEnvio validador3 = new ValidadorPago();
        ValidadorEnvio validador4 = new ValidadorSeguridad();
        ValidadorEnvio validador5 = new ValidadorCapacidad(capacidad);

        validador1.setSiguiente(validador2);
        validador2.setSiguiente(validador3);
        validador3.setSiguiente(validador4);
        validador4.setSiguiente(validador5);

        this.primerValidador = validador1;
    }

    public boolean validarEnvio(Envio envio) {
        System.out.println("\n=== Validando Envío ===");
        return primerValidador.validar(envio);
    }
}
```

---

## Casos de Prueba — `ChainDemo.java`

```java
package com.logismart.chain;

import com.logismart.dominio.Envio;

public class ChainDemo {

    public static void main(String[] args) {
        SistemaInventario inventario = productoId -> true;
        SistemaCapacidad capacidad   = peso -> peso <= 1000;

        CadenaValidadores cadena = new CadenaValidadores(inventario, capacidad);

        // Caso 1: Envío completamente válido
        Envio envio1 = new Envio.EnvioBuilder("E001", "Buenos Aires", "Córdoba")
                .peso(5.0).costo(150.0).metodoPago("TARJETA").productoId("PROD-001")
                .build();
        if (cadena.validarEnvio(envio1)) {
            System.out.println("→ ✓ Envío aprobado\n");
        }

        // Caso 2: Origen vacío → rechazado en ValidadorDatos
        Envio envio2 = new Envio.EnvioBuilder("E002", "", "Córdoba")
                .peso(5.0).costo(150.0).metodoPago("TARJETA").productoId("PROD-001")
                .build();
        if (!cadena.validarEnvio(envio2)) {
            System.out.println("→ ✗ Envío rechazado\n");
        }

        // Caso 3: Peso negativo → rechazado en ValidadorDatos
        Envio envio3 = new Envio.EnvioBuilder("E003", "Buenos Aires", "Córdoba")
                .peso(-5.0).costo(150.0).metodoPago("TARJETA").productoId("PROD-001")
                .build();
        if (!cadena.validarEnvio(envio3)) {
            System.out.println("→ ✗ Envío rechazado\n");
        }

        // Caso 4: Costo 0 → rechazado en ValidadorPago
        Envio envio4 = new Envio.EnvioBuilder("E004", "Buenos Aires", "Córdoba")
                .peso(5.0).costo(0).metodoPago("TARJETA").productoId("PROD-001")
                .build();
        if (!cadena.validarEnvio(envio4)) {
            System.out.println("→ ✗ Envío rechazado\n");
        }

        // Caso 5: Destino restringido → rechazado en ValidadorSeguridad
        Envio envio5 = new Envio.EnvioBuilder("E005", "Buenos Aires", "Zona Restringida")
                .peso(5.0).costo(150.0).metodoPago("TARJETA").productoId("PROD-001")
                .build();
        if (!cadena.validarEnvio(envio5)) {
            System.out.println("→ ✗ Envío rechazado\n");
        }
    }
}
```

| # | Descripción | Falla en | Resultado |
|---|---|---|---|
| 1 | Envío completamente válido | — | ✓ Aprobado |
| 2 | Origen vacío | `ValidadorDatos` | ✗ Rechazado |
| 3 | Peso negativo | `ValidadorDatos` | ✗ Rechazado |
| 4 | Costo cero | `ValidadorPago` | ✗ Rechazado |
| 5 | Destino restringido | `ValidadorSeguridad` | ✗ Rechazado |

---

## Decisiones de Diseño

**¿Por qué clase abstracta y no interfaz para `ValidadorEnvio`?**  
La clase abstracta permite definir el campo `siguiente` y `setSiguiente()` una sola vez. Con interfaz, cada validador repetiría ese código.

**¿Por qué interfaces para `SistemaInventario` y `SistemaCapacidad`?**  
Permite inyectar cualquier implementación (real, mock, lambda) sin modificar los validadores. Sigue el principio de Inversión de Dependencias.

**¿Por qué `CadenaValidadores` encapsula el ensamblado?**  
El cliente no necesita conocer el orden ni los detalles internos. Agregar un validador nuevo solo requiere modificar esta clase.

---

## Ventajas y Desventajas

**Ventajas**
- Bajo acoplamiento: el cliente solo interactúa con `CadenaValidadores`.
- Open/Closed: se agregan validadores nuevos sin modificar los existentes.
- Responsabilidad única: cada clase valida exactamente una condición.

**Desventajas**
- Sin garantía de manejo si la cadena está mal construida.
- Debugging más difícil al existir múltiples saltos.
- Pequeño overhead de rendimiento por las llamadas encadenadas.
