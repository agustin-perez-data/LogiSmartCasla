# Hito 10 — Actividad 4: Diagramas de Secuencia

---

## Diagrama 1: Chain of Responsibility

Flujo de un envío válido que atraviesa los 5 validadores completos.

```
Cliente    CadenaVal.  ValidDatos  ValidInvent.  ValidPago  ValidSegur.  ValidCapacidad
   │            │           │            │           │           │             │
   │ validar    │           │            │           │           │             │
   │ Envio()    │           │            │           │           │             │
   │───────────>│           │            │           │           │             │
   │            │ validar() │            │           │           │             │
   │            │──────────>│            │           │           │             │
   │            │           │[origen OK] │           │           │             │
   │            │           │[destino OK]│           │           │             │
   │            │           │[peso > 0]  │           │           │             │
   │            │           │ validar()  │           │           │             │
   │            │           │───────────>│           │           │             │
   │            │           │            │[stock OK] │           │             │
   │            │           │            │ validar() │           │             │
   │            │           │            │──────────>│           │             │
   │            │           │            │           │[costo > 0]│             │
   │            │           │            │           │[metodo OK]│             │
   │            │           │            │           │ validar() │             │
   │            │           │            │           │──────────>│             │
   │            │           │            │           │           │[no restring.]│
   │            │           │            │           │           │ validar()   │
   │            │           │            │           │           │────────────>│
   │            │           │            │           │           │             │[espacio OK]
   │            │           │            │           │           │    true     │
   │            │           │            │           │           │<────────────│
   │            │           │            │           │   true    │             │
   │            │           │            │           │<──────────│             │
   │            │           │            │   true    │           │             │
   │            │           │            │<──────────│           │             │
   │            │           │   true     │           │           │             │
   │            │           │<───────────│           │           │             │
   │            │  true     │            │           │           │             │
   │            │<──────────│            │           │           │             │
   │   true     │           │            │           │           │             │
   │<───────────│           │            │           │           │             │
```

**Flujo de corte temprano** (falla en ValidadorDatos):
```
Cliente    CadenaVal.  ValidDatos
   │            │           │
   │ validar    │           │
   │ Envio()    │           │
   │───────────>│           │
   │            │ validar() │
   │            │──────────>│
   │            │           │[origen vacío → false]
   │            │  false    │
   │            │<──────────│
   │   false    │           │
   │<───────────│           │
   (cadena se corta, no llega a los siguientes validadores)
```

---

## Diagrama 2: Command (ejecutar + deshacer)

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
   │               │                │                 │(cancelado)
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
   │               │                │   "ENV-002"     │
   │               │                │<────────────────│
   │<──────────────│                │                 │
```

---

## Diagrama 3: Interpreter

Evaluación de la expresión compuesta: `(ORIGEN='BA' AND COSTO>100) AND NOT RESTRINGIDO`

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
   │          │            │<────────────────────│         │              │
   │          │  true      │          │          │         │              │
   │          │<───────────│          │          │         │              │
   │          │der.evaluar()                               │              │
   │          │───────────────────────────────────────────>│              │
   │          │                                            │evaluar()     │
   │          │                                            │─────────────>│
   │          │                                            │   false      │
   │          │                                            │<─────────────│
   │          │  !false = true                             │              │
   │          │<───────────────────────────────────────────│              │
   │  true    │            │          │          │         │              │
   │<─────────│            │          │          │         │              │
```

**Nota:** si `izquierda.evaluar()` devuelve `false` en `ExpresionAND`, Java cortocircuita (`&&`) y **no llama** a `derecha.evaluar()`, lo que optimiza automáticamente la evaluación.
