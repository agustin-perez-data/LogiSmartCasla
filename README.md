# LogiSmart — Sistema de Gestión Logística

> **TPO — Proceso de Desarrollo de Software · Grupo CASLA**
> Implementación de los 23 patrones de diseño GoF (creacionales, estructurales y de comportamiento), principios SOLID y GRASP, sobre un dominio real de logística y envíos.

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.org/projects/jdk/21/)
[![Build](https://img.shields.io/badge/build-Maven-blue.svg)](https://maven.apache.org/)
[![Tests](https://img.shields.io/badge/tests-107%20passing-brightgreen.svg)](#pruebas)

---

## Tabla de contenidos

- [Descripción](#descripción)
- [Cómo compilar y ejecutar](#cómo-compilar-y-ejecutar)
- [Pruebas](#pruebas)
- [Arquitectura](#arquitectura)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Catálogo de patrones](#catálogo-de-patrones)
- [Los 12 hitos](#los-12-hitos)
- [Documentación](#documentación)

---

## Descripción

**LogiSmart** es un sistema de gestión logística que modela el ciclo de vida completo de un envío:
creación, validación, cálculo de costos, asignación de vehículos y rutas, integración con
proveedores externos (DHL, FedEx, UPS, PayPal, Stripe, Google Maps, etc.), seguimiento de estados
y generación de reportes.

El proyecto se construyó de forma **incremental a lo largo de 12 hitos**, partiendo del modelado del
dominio y la asignación de responsabilidades (GRASP), e incorporando progresivamente los **23 patrones
de diseño del catálogo GoF**. Cada patrón se aplica sobre una necesidad real del dominio, no como
ejemplo aislado.

| Métrica | Valor |
|---|---:|
| Hitos completados | **12** |
| Clases / interfaces Java | **238** |
| Paquetes | **24** |
| Patrones GoF aplicados | **23** |
| Tests JUnit 5 (casos de prueba) | **107** |
| Demos ejecutables (`main`) | **23** |

---

## Cómo compilar y ejecutar

Requisitos: **JDK 21** y **Maven 3.9+**.

```bash
# Compilar
mvn compile

# Ejecutar la suite completa de tests (107 casos)
mvn test

# Empaquetar
mvn package

# Ejecutar una demo de un patrón concreto (ejemplos)
mvn exec:java -Dexec.mainClass=com.logismart.Main
mvn exec:java -Dexec.mainClass=com.logismart.strategy.StrategyDemo
mvn exec:java -Dexec.mainClass=com.logismart.integracion.IntegracionHito10Demo
```

> Cada paquete de patrón incluye una clase `*Demo` con un `main` que ejecuta escenarios
> ilustrativos por consola. Las clases `*Demo` son demostraciones; la verificación
> automática real vive en `src/test/java` (ver [Pruebas](#pruebas)).

---

## Pruebas

La suite contiene **107 casos de prueba JUnit 5** que verifican el comportamiento real de cada
patrón con *assertions* (no `System.out`). Se ejecutan con `mvn test`.

| Grupo de patrones | Hito | Casos |
|---|:--:|---:|
| Creacionales — Factory / Abstract Factory / Singleton | 6, 7 | 17 |
| Estructurales — Decorator / Proxy / Flyweight / Facade / Adapter | 8 | 34 |
| Comportamiento I — Chain / Command / Interpreter | 10 | 19 |
| Comportamiento II — Iterator / Mediator / Memento / Observer | 11 | 18 |
| Comportamiento III — State / Strategy / Template Method / Visitor | 12 | 19 |
| **Total** | | **107** |

```
Tests run: 107, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

Los tests reflejan, entre otras cosas:
- **Singleton**: la misma instancia se devuelve siempre (`assertSame`) y el estado se comparte.
- **Factory**: el tipo concreto producido corresponde al parámetro pedido.
- **Decorator**: el costo y los servicios se acumulan correctamente al apilar decoradores.
- **Chain**: un envío válido atraviesa toda la cadena; cada validador corta el flujo ante su error.
- **Command**: `ejecutar` produce el efecto, `deshacer` lo revierte, `rehacer` lo reaplica.
- **State**: las transiciones válidas cambian de estado y las inválidas no.
- **Strategy**: cada estrategia produce el costo esperado según su fórmula.
- **Visitor**: cada visitante recorre la jerarquía y acumula su resultado.

---

## Arquitectura

LogiSmart sigue una **arquitectura en capas** con separación clara de responsabilidades.
La descripción completa, con diagramas, está en **[docs/ARQUITECTURA.md](docs/ARQUITECTURA.md)**
y **[docs/diagramas/](docs/diagramas/)**.

```
┌─────────────────────────────────────────────────────────────┐
│  Presentación / Entrada     Main · LogiSmartApp · *Demo       │
├─────────────────────────────────────────────────────────────┤
│  Controlador / Facade       LogiSmartController ·             │
│                             ServicioLogisticaFacade           │
├─────────────────────────────────────────────────────────────┤
│  Servicios / Aplicación     servicios · strategy · chain ·    │
│                             command · interpreter · mediator  │
├─────────────────────────────────────────────────────────────┤
│  Dominio                    dominio · state · visitor ·       │
│                             decorator · memento · observer    │
├─────────────────────────────────────────────────────────────┤
│  Datos / Persistencia       datos (repositorio · mapper ·     │
│                             lazy proxy · unit of work) · proxy│
├─────────────────────────────────────────────────────────────┤
│  Integración externa        servicios/adapters ·             │
│                             servicios/externas · factory      │
├─────────────────────────────────────────────────────────────┤
│  Infraestructura            singleton (config, logger, cache, │
│                             pool de conexiones)               │
└─────────────────────────────────────────────────────────────┘
```

---

## Estructura del proyecto

```
LogiSmartCasla/
├── pom.xml                      ← Proyecto Maven (Java 21, JUnit 5)
├── README.md
├── src/
│   ├── main/java/com/logismart/
│   │   ├── dominio/             ← Entidades del dominio (Envío, Ruta, Vehículo, Usuario…)
│   │   ├── controlador/         ← Controller (GRASP)
│   │   ├── servicios/           ← Estrategias de costo, adapters, reportes, proveedores
│   │   ├── factory/             ← Factory Method + Abstract Factory
│   │   ├── singleton/           ← Config, Logger, Cache, Pool de conexiones
│   │   ├── decorator/ proxy/ facade/ flyweight/   ← Patrones estructurales
│   │   ├── chain/ command/ interpreter/           ← Comportamiento I (Hito 10)
│   │   ├── iterator/ mediator/ memento/ observer/ ← Comportamiento II (Hito 11)
│   │   ├── state/ strategy/ template/ visitor/    ← Comportamiento III (Hito 12)
│   │   ├── datos/              ← Repositorios, mappers, lazy proxies, unit of work
│   │   ├── integracion/ eventdriven/ avanzada/    ← Demos de integración por hito
│   │   ├── Main.java · LogiSmartApp.java
│   └── test/java/com/logismart/ ← 107 tests JUnit 5 (uno por paquete de patrón)
└── docs/
    ├── ARQUITECTURA.md          ← Documento de arquitectura
    ├── PATRONES.md              ← Catálogo de los 23 patrones GoF
    ├── diagramas/               ← Diagramas UML consolidados (Mermaid)
    └── hitos/                   ← Documentación de los 12 hitos
        ├── hito01-fundamentos/ … hito12-comportamiento-3/
```

---

## Catálogo de patrones

Los **23 patrones GoF** están implementados. El catálogo completo con clase, paquete, hito y test
asociado está en **[docs/PATRONES.md](docs/PATRONES.md)**. Resumen:

| Categoría | Patrones |
|---|---|
| **Creacionales** | Singleton · Factory Method · Abstract Factory · Builder · Prototype |
| **Estructurales** | Adapter · Bridge · Composite · Decorator · Facade · Flyweight · Proxy |
| **Comportamiento** | Chain of Responsibility · Command · Interpreter · Iterator · Mediator · Memento · Observer · State · Strategy · Template Method · Visitor |

---

## Los 12 hitos

| Hito | Tema | Documentación |
|:--:|---|---|
| 1 | Fundamentos y modelado del dominio | [docs/hitos/hito01-fundamentos](docs/hitos/hito01-fundamentos/) |
| 2 | Diseño OO: clases, relaciones, cohesión y acoplamiento | [docs/hitos/hito02-diseno-oo](docs/hitos/hito02-diseno-oo/) |
| 3 | Implementación inicial del dominio | [docs/hitos/hito03-implementacion](docs/hitos/hito03-implementacion/) |
| 4 | Diagramas de secuencia | [docs/hitos/hito04-secuencia](docs/hitos/hito04-secuencia/) |
| 5 | Asignación de responsabilidades (GRASP) | [docs/hitos/hito05-grasp](docs/hitos/hito05-grasp/) |
| 6 | Patrones creacionales I — Singleton, Factory Method | [docs/hitos/hito06-creacionales-1](docs/hitos/hito06-creacionales-1/) |
| 7 | Patrones creacionales II — Abstract Factory, Builder, Prototype | [docs/hitos/hito07-creacionales-2](docs/hitos/hito07-creacionales-2/) |
| 8 | Patrones estructurales — Adapter, Decorator, Facade, Proxy, Flyweight | [docs/hitos/hito08-estructurales](docs/hitos/hito08-estructurales/) |
| 9 | Diagramas UML consolidados | [docs/hitos/hito09-diagramas](docs/hitos/hito09-diagramas/) |
| 10 | Comportamiento I — Chain, Command, Interpreter | [docs/hitos/hito10-comportamiento-1](docs/hitos/hito10-comportamiento-1/) |
| 11 | Comportamiento II — Iterator, Mediator, Memento, Observer | [docs/hitos/hito11-comportamiento-2](docs/hitos/hito11-comportamiento-2/) |
| 12 | Comportamiento III — State, Strategy, Template Method, Visitor | [docs/hitos/hito12-comportamiento-3](docs/hitos/hito12-comportamiento-3/) |

---

## Documentación

- **[docs/ARQUITECTURA.md](docs/ARQUITECTURA.md)** — capas, principios de diseño, decisiones.
- **[docs/PATRONES.md](docs/PATRONES.md)** — catálogo de los 23 patrones GoF.
- **[docs/diagramas/](docs/diagramas/)** — diagramas UML (paquetes, dominio, patrones).
- **[docs/hitos/](docs/hitos/)** — documentación detallada de cada hito.

---

*Grupo CASLA — Universidad Argentina de la Empresa (UADE) — Proceso de Desarrollo de Software.*
