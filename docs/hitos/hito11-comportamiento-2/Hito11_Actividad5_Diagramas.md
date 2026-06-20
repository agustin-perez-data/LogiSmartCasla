# Hito 11 — Actividad 5: Diagramas de Secuencia

---

## Diagrama 1: Iterator

Flujo de iteración completa sobre una `ColeccionArray` con reinicio.

```
Cliente      ColeccionArray    IteradorArray     Envio
   │               │                │              │
   │ agregar(e1)   │                │              │
   │──────────────>│                │              │
   │ agregar(e2)   │                │              │
   │──────────────>│                │              │
   │ agregar(e3)   │                │              │
   │──────────────>│                │              │
   │               │                │              │
   │ crearIterador()               │              │
   │──────────────>│                │              │
   │               │ new IteradorArray()           │
   │               │───────────────>│              │
   │   iterador    │                │              │
   │<──────────────│                │              │
   │               │                │              │
   │ tieneSiguiente()               │              │
   │───────────────────────────────>│              │
   │     true       │               │              │
   │<───────────────────────────────│              │
   │ obtenerSiguiente()             │              │
   │───────────────────────────────>│              │
   │     envio1     │               │ envio1       │
   │<───────────────────────────────│              │
   │               │                │              │
   │ tieneSiguiente()               │              │
   │───────────────────────────────>│              │
   │     true       │               │              │
   │<───────────────────────────────│              │
   │ obtenerSiguiente()             │              │
   │───────────────────────────────>│              │
   │     envio2     │               │ envio2       │
   │<───────────────────────────────│              │
   │               │                │              │
   │ tieneSiguiente()               │              │
   │───────────────────────────────>│              │
   │     true       │               │              │
   │<───────────────────────────────│              │
   │ obtenerSiguiente()             │              │
   │───────────────────────────────>│              │
   │     envio3     │               │ envio3       │
   │<───────────────────────────────│              │
   │               │                │              │
   │ tieneSiguiente()               │              │
   │───────────────────────────────>│              │
   │     false      │               │              │
   │<───────────────────────────────│              │
   │               │                │              │
   │ reiniciar()   │                │              │
   │───────────────────────────────>│              │
   │               │                │ indice = 0   │
   │<───────────────────────────────│              │
   │               │                │              │
   │ (segunda iteración desde el inicio)           │
   │ tieneSiguiente() → true        │              │
   │ obtenerSiguiente() → envio1    │              │
   │ ...            │               │              │
```

**Flujo de iteración sobre ColeccionHash** (comportamiento distinto: orden no garantizado):

```
Cliente      ColeccionHash     IteradorHash      Envio
   │               │                │              │
   │ agregar(e1)   │                │              │
   │──────────────>│                │              │
   │ agregar(e2)   │                │              │
   │──────────────>│                │              │
   │               │                │              │
   │ crearIterador()               │              │
   │──────────────>│                │              │
   │               │ new IteradorHash()            │
   │               │ (envios.values().iterator())  │
   │               │───────────────>│              │
   │   iterador    │                │              │
   │<──────────────│                │              │
   │               │                │              │
   │ tieneSiguiente() → hasNext()   │              │
   │───────────────────────────────>│              │
   │     true/false │               │              │
   │<───────────────────────────────│              │
   │               │                │              │
   │ reiniciar()   │                │              │
   │───────────────────────────────>│              │
   │               │                │ iterador =   │
   │               │                │ values()     │
   │               │                │ .iterator()  │
   │<───────────────────────────────│              │
```

---

## Diagrama 2: Mediator

Flujo completo de creación de envío: ENVIO_CREADO → VALIDACION_OK → PAGO_CONFIRMADO → NOTIFICACION_ENVIADA → ENVIO_REGISTRADO.

```
Centro    Mediador    Auditoria  Validador   Pago    Notificador
  │           │           │          │         │          │
  │crearEnvio │           │          │         │          │
  │──────────>│           │          │         │          │
  │   notificar("ENVIO_CREADO", envio)         │          │
  │           │─────────────────────>│         │          │
  │           │ auditoria.registrar("ENVIO_CREADO")       │
  │           │──────────>│          │         │          │
  │           │           │          │         │          │
  │           │ validador.validar(envio)        │          │
  │           │─────────────────────>│         │          │
  │           │   notificar("VALIDACION_OK", envio)        │
  │           │<─────────────────────│         │          │
  │           │ auditoria.registrar("VALIDACION_OK")      │
  │           │──────────>│          │         │          │
  │           │           │          │         │          │
  │           │ pago.procesarPago(envio)        │          │
  │           │───────────────────────────────>│          │
  │           │   notificar("PAGO_CONFIRMADO", envio)     │
  │           │<───────────────────────────────│          │
  │           │ auditoria.registrar("PAGO_CONFIRMADO")    │
  │           │──────────>│          │         │          │
  │           │           │          │         │          │
  │           │ notificador.enviarConfirmacion(envio)      │
  │           │──────────────────────────────────────────>│
  │           │   notificar("NOTIFICACION_ENVIADA", envio) │
  │           │<──────────────────────────────────────────│
  │           │ auditoria.registrar("NOTIFICACION_ENVIADA")
  │           │──────────>│          │         │          │
  │           │           │          │         │          │
  │           │ centro.registrarEnvio(envio)   │          │
  │           │─>│        │          │         │          │
  │           │   notificar("ENVIO_REGISTRADO", envio)    │
  │           │<──│        │          │         │          │
  │           │ auditoria.registrar("ENVIO_REGISTRADO")   │
  │           │──────────>│          │         │          │
  │<──────────│           │          │         │          │
```

**Flujo de corte por validación fallida** (costo o peso <= 0):

```
Centro    Mediador    Validador
  │           │           │
  │crearEnvio │           │
  │──────────>│           │
  │   notificar("ENVIO_CREADO", envio)
  │           │─────────────────────>│
  │           │ validador.validar(envio)
  │           │─────────────────────>│
  │           │           │[costo 0 o peso 0 → flujo detenido]
  │           │           │ System.err: "Envío inválido"
  │           │           │ (no llama notificar)
  │<──────────│           │
  (pago y notificación no se ejecutan)
```

---

## Diagrama 3: Memento

Flujo de guardado y retroceso de estado de un envío.

```
Cliente    Envio       HistorialEnvios    MementoEnvio
   │         │                │                │
   │ cambiarEstado("EN_PREPARACION")            │
   │────────>│                │                │
   │         │[estado = "EN_PREPARACION"]       │
   │         │                │                │
   │ guardarEstado(envio)      │                │
   │──────────────────────────>│                │
   │         │ crearMemento() │                │
   │         │<───────────────│                │
   │         │ new MementoEnvio(estado, ...)   │
   │         │────────────────────────────────>│
   │         │    memento     │                │
   │         │<────────────────────────────────│
   │         │────────────────>│               │
   │         │                 │historial.add  │
   │         │                 │indice = 0     │
   │<──────────────────────────│               │
   │         │                 │               │
   │ cambiarEstado("EN_TRANSITO")               │
   │────────>│                │               │
   │ guardarEstado(envio)      │               │
   │──────────────────────────>│               │
   │         │ crearMemento() │               │
   │         │<───────────────│               │
   │         │────────────────────────────────>│
   │         │<────────────────────────────────│
   │         │────────────────>│               │
   │         │                 │historial.add  │
   │         │                 │indice = 1     │
   │<──────────────────────────│               │
   │         │                 │               │
   │ irAlEstadoAnterior(envio) │               │
   │──────────────────────────>│               │
   │         │                 │indice-- → 0   │
   │         │                 │memento = hist[0]
   │         │ restaurarDesdeMemento(memento)  │
   │         │<───────────────│               │
   │         │[estado = "EN_PREPARACION"]      │
   │<──────────────────────────│               │
   │         │                 │               │
   │ irAlEstadoAnterior(envio) │               │
   │──────────────────────────>│               │
   │         │                 │[indice <= 0]  │
   │         │                 │"No se puede retroceder"
   │<──────────────────────────│               │
```

---

## Diagrama 4: Observer

Flujo de notificación a múltiples observadores al cambiar estado de un envío.

```
Cliente    Envio        CentroObs.   NotifObs.   AuditoriaObs.  DashboardObs.
   │          │               │           │             │               │
   │ adjuntarObservador(centro)           │             │               │
   │─────────>│               │           │             │               │
   │ adjuntarObservador(notif)            │             │               │
   │─────────>│               │           │             │               │
   │ adjuntarObservador(auditoria)        │             │               │
   │─────────>│               │           │             │               │
   │ adjuntarObservador(dashboard)        │             │               │
   │─────────>│               │           │             │               │
   │          │               │           │             │               │
   │ cambiarEstado("EN_TRANSITO")         │             │               │
   │─────────>│               │           │             │               │
   │          │[estado = "EN_TRANSITO"]   │             │               │
   │          │ notificarObservadores("EN_TRANSITO")    │               │
   │          │──────────────>│           │             │               │
   │          │ actualizar(envio, "EN_TRANSITO")        │               │
   │          │               │           │             │               │
   │          │──────────────────────────>│             │               │
   │          │ actualizar(envio, "EN_TRANSITO")        │               │
   │          │               │           │             │               │
   │          │───────────────────────────────────────>│               │
   │          │ actualizar(envio, "EN_TRANSITO")        │               │
   │          │               │           │             │               │
   │          │────────────────────────────────────────────────────────>│
   │<─────────│               │           │             │               │
   │          │               │           │             │               │
   │ desadjuntarObservador(notif)         │             │               │
   │─────────>│               │           │             │               │
   │          │               │           │             │               │
   │ cambiarEstado("ENTREGADO")           │             │               │
   │─────────>│               │           │             │               │
   │          │ notificarObservadores("ENTREGADO")      │               │
   │          │──────────────>│           │             │               │
   │          │ actualizar(envio, "ENTREGADO") [notif NO recibe]        │
   │          │───────────────────────────────────────>│               │
   │          │ actualizar(envio, "ENTREGADO")          │               │
   │          │────────────────────────────────────────────────────────>│
   │<─────────│               │           │             │               │
```

**Nota:** El orden de notificación es el orden en que los observadores fueron registrados con `adjuntarObservador`. Si `NotifObs` es desregistrado, los demás observadores siguen recibiendo el evento normalmente.

