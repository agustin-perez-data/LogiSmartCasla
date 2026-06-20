# Hito 11: Patrones de Comportamiento II — Entregables

## Checklist general

### Código Java

| Actividad | Clases | Estado |
|---|---|---|
| **Iterator** | `IteradorEnvios` (interfaz) + `ColeccionEnvios` (interfaz) + `ColeccionArray` + `ColeccionLista` + `ColeccionHash` + `IteratorDemo` | ✅ |
| **Mediator** | `MediadorEnvios` (interfaz) + `MediadorEnviosConcreto` + `CentroDistribucion` + `ComponenteValidador` + `SistemaPago` + `SistemaNotificacion` + `SistemaAuditoria` + `MediadorDemo` | ✅ |
| **Memento** | `MementoEnvio` + `HistorialEnvios` + `MementoDemo` + modificaciones a `Envio` | ✅ |
| **Observer** | `ObservadorEnvio` (interfaz) + `CentroDistribucionObservador` + `SistemaNotificacionObservador` + `SistemaAuditoriaObservador` + `DashboardObservador` + `ObserverDemo` + modificaciones a `Envio` | ✅ |
| **Integración** | `SistemaLogisticaEventDriven` + `IntegracionHito11Demo` | ✅ |

**Total: 27 clases/interfaces Java**

### Documentación Markdown

| Archivo | Contenido |
|---|---|
| `Hito11_Actividad1_Iterator.md` | Descripción, diagramas, implementación, casos, decisiones, pros/cons |
| `Hito11_Actividad2_Mediator.md` | Descripción, diagramas, implementación, casos, decisiones, pros/cons |
| `Hito11_Actividad3_Memento.md` | Descripción, diagramas, modificaciones a `Envio`, implementación, casos, decisiones, pros/cons |
| `Hito11_Actividad4_Observer.md` | Descripción, diagramas, modificaciones a `Envio`, implementación, casos, decisiones, pros/cons |
| `Hito11_Actividad5_Diagramas.md` | 4 diagramas de secuencia ASCII detallados (uno por patrón) |
| `Hito11_Actividad6_Integracion.md` | Flujo integrado, tabla de casos, relación entre patrones |

### Imágenes de Diagramas

| Archivo | Contenido |
|---|---|
| `files/1_clases_iterator.png` | Diagrama de clases — Iterator |
| `files/2_clases_mediator.png` | Diagrama de clases — Mediator |
| `files/3_clases_memento.png` | Diagrama de clases — Memento |
| `files/4_clases_observer.png` | Diagrama de clases — Observer |
| `files/5_arquitectura_eventdriven.png` | Diagrama de arquitectura — Integración |

### Casos de prueba

| Actividad | Casos | Estado |
|---|---|---|
| Iterator | 5 (array, lista, hash, reiniciar, remover+verificar tamaño) | ✅ |
| Mediator | 5 (flujo completo, segundo envío, envío inválido, desacoplamiento, auditoría total) | ✅ |
| Memento | 5 (guardar ciclo de vida, retroceder, retroceder al inicio, avanzar, nuevo estado descarta futuro) | ✅ |
| Observer | 5 (registrar + primer cambio, ciclo completo, desregistrar observador, múltiples envíos, auditoría total) | ✅ |
| Integración | 5 (cola válida, dashboard+auditoría, envío inválido, error+restauración Memento, desacoplamiento) | ✅ |

**Total: 25 casos de prueba**

---

## Estructura de paquetes

```
com.logismart
├── dominio/
│   └── Envio.java                         ← modificado en Hito 11 (ver abajo)
│
├── iterator/
│   ├── IteradorEnvios.java                ← interface iterador
│   ├── ColeccionEnvios.java               ← interface colección
│   ├── ColeccionArray.java                ← array fijo + IteradorArray (inner)
│   ├── ColeccionLista.java                ← lista enlazada + IteradorLista (inner) + Nodo (inner)
│   ├── ColeccionHash.java                 ← HashMap + IteradorHash (inner)
│   └── IteratorDemo.java                  ← 5 casos de prueba
│
├── mediator/
│   ├── MediadorEnvios.java                ← interface mediador
│   ├── MediadorEnviosConcreto.java        ← implementación con switch de eventos
│   ├── CentroDistribucion.java            ← componente: crea y registra envíos
│   ├── ComponenteValidador.java           ← componente: valida envíos (*)
│   ├── SistemaPago.java                   ← componente: procesa pagos
│   ├── SistemaNotificacion.java           ← componente: envía confirmaciones
│   ├── SistemaAuditoria.java              ← receptor pasivo: registra eventos
│   └── MediadorDemo.java                  ← 5 casos de prueba
│
├── memento/
│   ├── MementoEnvio.java                  ← snapshot inmutable del estado de Envio
│   ├── HistorialEnvios.java               ← caretaker: guarda/restaura mementos
│   └── MementoDemo.java                   ← 5 casos de prueba
│
├── observer/
│   ├── ObservadorEnvio.java               ← interface observador
│   ├── CentroDistribucionObservador.java  ← observador concreto: logística
│   ├── SistemaNotificacionObservador.java ← observador concreto: alertas SMS/email
│   ├── SistemaAuditoriaObservador.java    ← observador concreto: registro de auditoría
│   ├── DashboardObservador.java           ← observador concreto: dashboard en tiempo real
│   └── ObserverDemo.java                  ← 5 casos de prueba
│
└── eventdriven/
    ├── SistemaLogisticaEventDriven.java   ← integración de los 4 patrones
    └── IntegracionHito11Demo.java         ← 5 casos de integración
```

---

## Modificaciones a `com.logismart.dominio.Envio`

`Envio` fue modificada en Hito 10 para agregar `costo`, `metodoPago` y `productoId`. En Hito 11 se agregan los siguientes elementos:

### Para Memento (Actividad 3)

- Campo `private String estado = "CONFIRMADO"` — estado del ciclo de vida del envío.
- Campos `origen`, `destino`, `peso`, `costo` cambian de `final` a mutables para permitir restauración.
- Método `crearMemento(): MementoEnvio` — captura el estado actual.
- Método `restaurarDesdeMemento(MementoEnvio)` — restaura todos los campos desde un snapshot.
- Método `cambiarEstado(String)` — actualiza el campo `estado` con log en consola.
- Método `obtenerEstado(): String` — getter del campo `estado`.

### Para Observer (Actividad 4)

- Campo `private final List<ObservadorEnvio> observadores = new ArrayList<>()`.
- Método `adjuntarObservador(ObservadorEnvio)` — registra un observador.
- Método `desadjuntarObservador(ObservadorEnvio)` — elimina un observador.
- Método privado `notificarObservadores(String evento)` — itera la lista y llama `actualizar`.
- El método `cambiarEstado` existente se extiende con una llamada a `notificarObservadores` al final.

---

## Nota de coherencia con Hito 10

(*) El validador en el paquete `com.logismart.mediator` se llama `ComponenteValidador` en lugar de `ValidadorEnvio` para evitar conflicto conceptual con `com.logismart.chain.ValidadorEnvio`, que es la clase abstracta del patrón Chain of Responsibility del Hito 10. Java los distingue por paquete, pero el nombre diferente expresa roles distintos: `ComponenteValidador` es un participante que delega al mediador; `ValidadorEnvio` es un manejador autónomo de la cadena.
