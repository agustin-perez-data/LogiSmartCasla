# Hito 10 — Actividad 2: Command

**Proyecto:** LogiSmart - Sistema de Gestión de Logística  
**Patrón:** Command  
**Paquete:** `com.logismart.command`

---

## Descripción del Patrón

El patrón **Command** encapsula una solicitud como un objeto, permitiendo parametrizar clientes con distintas solicitudes, encolar operaciones y soportar deshacer/rehacer (undo/redo).

**Roles:**
- **Command** (`Comando`): declara `ejecutar()`, `deshacer()` y `obtenerDescripcion()`.
- **ConcreteCommand**: implementa la operación y guarda el estado necesario para revertirla.
- **Receiver** (`ServicioEnvios`): contiene la lógica de negocio real.
- **Invoker** (`ColaComandos`): ejecuta comandos y mantiene el historial para undo/redo.

---

## Diagrama de Clases

```
<<interface>>
Comando
─────────────────────────────────
+ ejecutar()
+ deshacer()
+ obtenerDescripcion(): String
        ▲
        │ implements
┌───────┴───────────────────────────────────────────────────┐
ComandoCrearEnvio   ComandoCancelarEnvio   ComandoActualizarEstado
ComandoCambiarMetodoPago   ComandoAgregarServicio

Todos referencian ──────────────────────► ServicioEnvios (Receptor)

ColaComandos
─────────────────────────────────
- historial: List<Comando>
- indiceActual: int
─────────────────────────────────
+ ejecutar(Comando)
+ deshacer()
+ rehacer()
+ mostrarHistorial()
```

---

## Diagrama de Secuencia

```
Cliente      ColaComandos   ComandoCrearEnvio   ServicioEnvios
   │               │                │                 │
   │ ejecutar(cmd) │                │                 │
   │──────────────>│                │                 │
   │               │ cmd.ejecutar() │                 │
   │               │───────────────>│                 │
   │               │                │ crearEnvio(env) │
   │               │                │────────────────>│
   │               │                │   "ENV-001"     │
   │               │                │<────────────────│
   │               │ historial.add  │                 │
   │               │ indice++       │                 │
   │<──────────────│                │                 │
   │               │                │                 │
   │ deshacer()    │                │                 │
   │──────────────>│                │                 │
   │               │ cmd.deshacer() │                 │
   │               │───────────────>│                 │
   │               │                │cancelarEnvio()  │
   │               │                │────────────────>│
   │               │ indice--       │                 │
   │<──────────────│                │                 │
   │               │                │                 │
   │ rehacer()     │                │                 │
   │──────────────>│                │                 │
   │               │ indice++       │                 │
   │               │ cmd.ejecutar() │                 │
   │               │───────────────>│                 │
   │               │                │ crearEnvio(env) │
   │               │                │────────────────>│
   │<──────────────│                │                 │
```

---

## Implementación

### `Comando.java`

```java
package com.logismart.command;

public interface Comando {
    void ejecutar();
    void deshacer();
    String obtenerDescripcion();
}
```

---

### `ServicioEnvios.java`

```java
package com.logismart.command;

import com.logismart.dominio.Envio;
import java.util.HashMap;
import java.util.Map;

public class ServicioEnvios {

    private final Map<String, String> estados     = new HashMap<>();
    private final Map<String, String> metodosPago = new HashMap<>();
    private final Map<String, String> servicios   = new HashMap<>();
    private int contador = 0;

    public String crearEnvio(Envio envio) {
        String numero = "ENV-" + String.format("%03d", ++contador);
        estados.put(numero, "CONFIRMADO");
        metodosPago.put(numero, envio.getMetodoPago() != null ? envio.getMetodoPago() : "");
        System.out.println("  [Servicio] Envío creado: " + numero);
        return numero;
    }

    public void cancelarEnvio(String numero) {
        estados.put(numero, "CANCELADO");
        System.out.println("  [Servicio] Envío cancelado: " + numero);
    }

    public void reactivarEnvio(String numero) {
        estados.put(numero, "CONFIRMADO");
        System.out.println("  [Servicio] Envío reactivado: " + numero);
    }

    public String obtenerEstado(String numero) {
        return estados.getOrDefault(numero, "DESCONOCIDO");
    }

    public void actualizarEstado(String numero, String nuevoEstado) {
        estados.put(numero, nuevoEstado);
        System.out.println("  [Servicio] Estado de " + numero + " → " + nuevoEstado);
    }

    public String obtenerMetodoPago(String numero) {
        return metodosPago.getOrDefault(numero, "");
    }

    public void cambiarMetodoPago(String numero, String nuevoMetodo) {
        metodosPago.put(numero, nuevoMetodo);
        System.out.println("  [Servicio] Método de pago de " + numero + " → " + nuevoMetodo);
    }

    public void agregarServicio(String numero, String nombreServicio) {
        servicios.put(numero + "_" + nombreServicio, nombreServicio);
        System.out.println("  [Servicio] Servicio agregado a " + numero + ": " + nombreServicio);
    }

    public void removerServicio(String numero, String nombreServicio) {
        servicios.remove(numero + "_" + nombreServicio);
        System.out.println("  [Servicio] Servicio removido de " + numero + ": " + nombreServicio);
    }
}
```

---

### `ComandoCrearEnvio.java`

```java
package com.logismart.command;

import com.logismart.dominio.Envio;

public class ComandoCrearEnvio implements Comando {

    private final ServicioEnvios servicio;
    private final Envio envio;
    private String numeroSeguimiento;

    public ComandoCrearEnvio(ServicioEnvios servicio, Envio envio) {
        this.servicio = servicio;
        this.envio = envio;
    }

    @Override
    public void ejecutar() {
        System.out.println("[Comando] Creando envío...");
        numeroSeguimiento = servicio.crearEnvio(envio);
        System.out.println("✓ Envío creado: " + numeroSeguimiento);
    }

    @Override
    public void deshacer() {
        System.out.println("[Comando] Deshaciendo: Cancelando envío...");
        servicio.cancelarEnvio(numeroSeguimiento);
        System.out.println("✓ Envío cancelado");
    }

    @Override
    public String obtenerDescripcion() {
        return "Crear envío de " + envio.getOrigen() + " a " + envio.getDestino();
    }

    public String getNumeroSeguimiento() {
        return numeroSeguimiento;
    }
}
```

---

### `ComandoCancelarEnvio.java`

```java
package com.logismart.command;

public class ComandoCancelarEnvio implements Comando {

    private final ServicioEnvios servicio;
    private final String numeroSeguimiento;

    public ComandoCancelarEnvio(ServicioEnvios servicio, String numeroSeguimiento) {
        this.servicio = servicio;
        this.numeroSeguimiento = numeroSeguimiento;
    }

    @Override
    public void ejecutar() {
        System.out.println("[Comando] Cancelando envío: " + numeroSeguimiento);
        servicio.cancelarEnvio(numeroSeguimiento);
        System.out.println("✓ Envío cancelado");
    }

    @Override
    public void deshacer() {
        System.out.println("[Comando] Deshaciendo: Reactivando envío...");
        servicio.reactivarEnvio(numeroSeguimiento);
        System.out.println("✓ Envío reactivado");
    }

    @Override
    public String obtenerDescripcion() {
        return "Cancelar envío: " + numeroSeguimiento;
    }
}
```

---

### `ComandoActualizarEstado.java`

```java
package com.logismart.command;

public class ComandoActualizarEstado implements Comando {

    private final ServicioEnvios servicio;
    private final String numeroSeguimiento;
    private final String nuevoEstado;
    private String estadoAnterior;

    public ComandoActualizarEstado(ServicioEnvios servicio, String numeroSeguimiento, String nuevoEstado) {
        this.servicio = servicio;
        this.numeroSeguimiento = numeroSeguimiento;
        this.nuevoEstado = nuevoEstado;
    }

    @Override
    public void ejecutar() {
        System.out.println("[Comando] Actualizando estado a: " + nuevoEstado);
        estadoAnterior = servicio.obtenerEstado(numeroSeguimiento);
        servicio.actualizarEstado(numeroSeguimiento, nuevoEstado);
        System.out.println("✓ Estado actualizado");
    }

    @Override
    public void deshacer() {
        System.out.println("[Comando] Deshaciendo: Restaurando estado anterior...");
        servicio.actualizarEstado(numeroSeguimiento, estadoAnterior);
        System.out.println("✓ Estado restaurado: " + estadoAnterior);
    }

    @Override
    public String obtenerDescripcion() {
        return "Actualizar estado a: " + nuevoEstado;
    }
}
```

---

### `ComandoCambiarMetodoPago.java`

```java
package com.logismart.command;

public class ComandoCambiarMetodoPago implements Comando {

    private final ServicioEnvios servicio;
    private final String numeroSeguimiento;
    private final String nuevoMetodo;
    private String metodoAnterior;

    public ComandoCambiarMetodoPago(ServicioEnvios servicio, String numeroSeguimiento, String nuevoMetodo) {
        this.servicio = servicio;
        this.numeroSeguimiento = numeroSeguimiento;
        this.nuevoMetodo = nuevoMetodo;
    }

    @Override
    public void ejecutar() {
        System.out.println("[Comando] Cambiando método de pago a: " + nuevoMetodo);
        metodoAnterior = servicio.obtenerMetodoPago(numeroSeguimiento);
        servicio.cambiarMetodoPago(numeroSeguimiento, nuevoMetodo);
        System.out.println("✓ Método de pago cambiado");
    }

    @Override
    public void deshacer() {
        System.out.println("[Comando] Deshaciendo: Restaurando método anterior...");
        servicio.cambiarMetodoPago(numeroSeguimiento, metodoAnterior);
        System.out.println("✓ Método de pago restaurado: " + metodoAnterior);
    }

    @Override
    public String obtenerDescripcion() {
        return "Cambiar método de pago a: " + nuevoMetodo;
    }
}
```

---

### `ComandoAgregarServicio.java`

```java
package com.logismart.command;

public class ComandoAgregarServicio implements Comando {

    private final ServicioEnvios servicio;
    private final String numeroSeguimiento;
    private final String nombreServicio;

    public ComandoAgregarServicio(ServicioEnvios servicio, String numeroSeguimiento, String nombreServicio) {
        this.servicio = servicio;
        this.numeroSeguimiento = numeroSeguimiento;
        this.nombreServicio = nombreServicio;
    }

    @Override
    public void ejecutar() {
        System.out.println("[Comando] Agregando servicio: " + nombreServicio);
        servicio.agregarServicio(numeroSeguimiento, nombreServicio);
        System.out.println("✓ Servicio agregado");
    }

    @Override
    public void deshacer() {
        System.out.println("[Comando] Deshaciendo: Removiendo servicio...");
        servicio.removerServicio(numeroSeguimiento, nombreServicio);
        System.out.println("✓ Servicio removido");
    }

    @Override
    public String obtenerDescripcion() {
        return "Agregar servicio: " + nombreServicio;
    }
}
```

---

### `ColaComandos.java`

```java
package com.logismart.command;

import java.util.ArrayList;
import java.util.List;

public class ColaComandos {

    private final List<Comando> historial = new ArrayList<>();
    private int indiceActual = -1;

    public void ejecutar(Comando comando) {
        // Descarta comandos deshacidos al ejecutar uno nuevo
        while (indiceActual < historial.size() - 1) {
            historial.remove(historial.size() - 1);
        }
        comando.ejecutar();
        historial.add(comando);
        indiceActual++;
    }

    public void deshacer() {
        if (indiceActual >= 0) {
            Comando comando = historial.get(indiceActual);
            comando.deshacer();
            indiceActual--;
            System.out.println("✓ Deshecho: " + comando.obtenerDescripcion());
        } else {
            System.out.println("  (nada que deshacer)");
        }
    }

    public void rehacer() {
        if (indiceActual < historial.size() - 1) {
            indiceActual++;
            Comando comando = historial.get(indiceActual);
            comando.ejecutar();
            System.out.println("✓ Rehecho: " + comando.obtenerDescripcion());
        } else {
            System.out.println("  (nada que rehacer)");
        }
    }

    public void mostrarHistorial() {
        System.out.println("\n=== Historial de Comandos ===");
        for (int i = 0; i < historial.size(); i++) {
            String marca = (i == indiceActual) ? "→" : " ";
            System.out.println(marca + " " + (i + 1) + ". " + historial.get(i).obtenerDescripcion());
        }
        System.out.println();
    }

    public int obtenerTamaño() {
        return historial.size();
    }
}
```

---

## Casos de Prueba — `CommandDemo.java`

```java
package com.logismart.command;

import com.logismart.dominio.Envio;

public class CommandDemo {

    public static void main(String[] args) {
        ServicioEnvios servicio = new ServicioEnvios();
        ColaComandos cola = new ColaComandos();

        Envio envio = new Envio.EnvioBuilder("E001", "Buenos Aires", "Córdoba")
                .peso(5.0).costo(150.0).metodoPago("TARJETA").productoId("PROD-001")
                .build();

        // Caso 1: Crear envío
        System.out.println("--- Caso 1: Crear envío ---");
        ComandoCrearEnvio cmdCrear = new ComandoCrearEnvio(servicio, envio);
        cola.ejecutar(cmdCrear);
        String numero = cmdCrear.getNumeroSeguimiento();

        // Caso 2: Actualizar estado
        System.out.println("\n--- Caso 2: Actualizar estado ---");
        cola.ejecutar(new ComandoActualizarEstado(servicio, numero, "EN TRÁNSITO"));

        // Caso 3: Cambiar método de pago
        System.out.println("\n--- Caso 3: Cambiar método de pago ---");
        cola.ejecutar(new ComandoCambiarMetodoPago(servicio, numero, "EFECTIVO"));

        // Caso 4: Agregar servicio adicional
        System.out.println("\n--- Caso 4: Agregar servicio 'Seguro' ---");
        cola.ejecutar(new ComandoAgregarServicio(servicio, numero, "Seguro"));

        // Caso 5: Mostrar historial
        System.out.println("--- Caso 5: Mostrar historial ---");
        cola.mostrarHistorial();

        // Caso 6: Deshacer x2
        System.out.println("--- Caso 6: Deshacer x2 ---");
        cola.deshacer(); // quita Seguro
        cola.deshacer(); // restaura TARJETA
        cola.mostrarHistorial();

        // Caso 7: Rehacer
        System.out.println("--- Caso 7: Rehacer ---");
        cola.rehacer(); // vuelve a EFECTIVO
        cola.mostrarHistorial();
    }
}
```

| # | Operación | Resultado esperado |
|---|---|---|
| 1 | `ejecutar(ComandoCrearEnvio)` | ENV-001 creado, estado CONFIRMADO |
| 2 | `ejecutar(ComandoActualizarEstado → EN TRÁNSITO)` | estado = EN TRÁNSITO |
| 3 | `ejecutar(ComandoCambiarMetodoPago → EFECTIVO)` | metodoPago = EFECTIVO |
| 4 | `ejecutar(ComandoAgregarServicio → Seguro)` | servicio Seguro agregado |
| 5 | `mostrarHistorial()` | 4 entradas, → apunta a índice 3 |
| 6 | `deshacer()` x2 | Seguro removido; metodoPago = TARJETA |
| 7 | `rehacer()` | metodoPago = EFECTIVO nuevamente |

---

## Decisiones de Diseño

**¿Por qué `estadoAnterior` se guarda en `ejecutar()` y no en el constructor?**  
Si se guardara en el constructor, capturaría el estado en el momento de creación del comando, no en el de ejecución. Si el envío cambió entre ambos momentos, el `deshacer()` restauraría un valor incorrecto.

**¿Por qué truncar el historial al ejecutar después de un undo?**  
Es el comportamiento estándar (Word, Photoshop): si deshacés y luego hacés algo nuevo, la rama deshecha desaparece.

---

## Ventajas y Desventajas

**Ventajas**
- Undo/Redo natural con historial + índice.
- El cliente no conoce cómo funciona `ServicioEnvios`.
- El historial es un registro de auditoría completo.

**Desventajas**
- Una clase por cada operación → proliferación de clases.
- El historial ilimitado puede consumir memoria.
- Si el receptor es modificado por otro proceso, el `deshacer` puede ser inconsistente.
