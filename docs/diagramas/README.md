# Diagramas UML — LogiSmart

Diagramas consolidados del sistema en **Mermaid** (GitHub los renderiza automáticamente).
Los diagramas históricos por hito están en cada carpeta de [`../hitos/`](../hitos/) (PNG en el
Hito 9 y Hito 11, `.mmd` en el Hito 12).

Índice:
1. [Diagrama de paquetes / capas](#1-diagrama-de-paquetes--capas)
2. [Diagrama de clases del dominio](#2-diagrama-de-clases-del-dominio)
3. [Patrones creacionales](#3-patrones-creacionales)
4. [Patrones estructurales](#4-patrones-estructurales)
5. [Patrones de comportamiento](#5-patrones-de-comportamiento)
6. [Ciclo de vida del envío (State)](#6-ciclo-de-vida-del-envío-state)

---

## 1. Diagrama de paquetes / capas

```mermaid
graph TD
    entrada["Entrada\nMain · LogiSmartApp · *Demo"]
    coord["Coordinación\ncontrolador · facade"]
    app["Aplicación\nservicios · strategy · chain · command · interpreter · mediator"]
    dom["Dominio\ndominio · state · visitor · decorator · memento · observer"]
    datos["Datos\ndatos · proxy"]
    ext["Integración\nservicios/adapters · servicios/externas · factory"]
    infra["Infraestructura\nsingleton"]

    entrada --> coord
    coord --> app
    app --> dom
    app --> datos
    app --> ext
    app -.-> infra
    datos -.-> infra
    ext -.-> infra
```

---

## 2. Diagrama de clases del dominio

```mermaid
classDiagram
    class Envio {
        -String id
        -String origen
        -String destino
        -double peso
        -boolean fragil
        -EstadoEnvio estado
        +clone() Envio
        +cambiarEstado(EstadoEnvio)
        +calcularCosto() double
    }
    class EnvioBuilder {
        +peso(double) EnvioBuilder
        +fragil(boolean) EnvioBuilder
        +build() Envio
    }
    Envio ..> EnvioBuilder : Builder

    class Usuario {
        <<abstract>>
        -String nombre
    }
    Usuario <|-- Cliente
    Usuario <|-- Operador
    Usuario <|-- Conductor
    Usuario <|-- Admin

    class Vehiculo {
        <<abstract>>
        +capacidad() double
    }
    Vehiculo <|-- Moto
    Vehiculo <|-- Auto
    Vehiculo <|-- Camioneta
    Vehiculo <|-- Camion

    class Ruta
    class Flota

    Cliente "1" --> "*" Envio : realiza
    Ruta "1" --> "*" Envio : agrupa
    Flota "1" --> "*" Vehiculo : administra
    Ruta --> Vehiculo : asignado
```

---

## 3. Patrones creacionales

### Abstract Factory (familias por región)

```mermaid
classDiagram
    class LogiSmartFactory {
        <<abstract>>
        +crearVehiculo() Vehiculo
        +crearCalculadorCostos() CalculadorCostos
        +crearProveedorMapas() ProveedorMapas
    }
    LogiSmartFactory <|-- LogiSmartFactoryArgentina
    LogiSmartFactory <|-- LogiSmartFactoryBrasil
    LogiSmartFactoryArgentina ..> Auto
    LogiSmartFactoryArgentina ..> GoogleMapsArgentina
    LogiSmartFactoryBrasil ..> Moto
    LogiSmartFactoryBrasil ..> HereMaps
```

### Singleton (infraestructura)

```mermaid
classDiagram
    class ConfiguracionLogiSmart {
        -ConfiguracionLogiSmart instancia$
        +obtenerInstancia()$ ConfiguracionLogiSmart
    }
    class LoggerLogiSmart {
        -LoggerLogiSmart instancia$
        +obtenerInstancia()$ LoggerLogiSmart
    }
    class PoolDeConexiones {
        +obtenerConexion()
        +liberarConexion()
    }
```

---

## 4. Patrones estructurales

### Decorator (servicios opcionales del envío)

```mermaid
classDiagram
    class Envio {
        <<interface>>
        +costo() double
        +descripcion() String
    }
    Envio <|.. EnvioBasico
    Envio <|.. DecoradorEnvio
    DecoradorEnvio o--> Envio : envuelve
    DecoradorEnvio <|-- DecoradorSeguro
    DecoradorEnvio <|-- DecoradorPrioritario
    DecoradorEnvio <|-- DecoradorRastreoGPS
    DecoradorEnvio <|-- DecoradorNotificacionesSMS
```

### Composite + Visitor (red de centros)

```mermaid
classDiagram
    class CentroDistribucion {
        <<interface>>
        +aceptar(VisitorCentro)
    }
    CentroDistribucion <|.. PuntoEntrega
    CentroDistribucion <|.. CentroRegional
    CentroRegional o--> CentroDistribucion : subcentros

    class VisitorCentro {
        <<interface>>
        +visitar(PuntoEntrega)
        +entrarCentro(CentroRegional)
    }
    VisitorCentro <|.. VisitorCalculoOcupacion
    VisitorCentro <|.. VisitorGeneradorReporte
    VisitorCentro <|.. VisitorCalculoCostoOperativo
    VisitorCentro <|.. VisitorBusquedaPuntosCriticos
```

### Bridge (reporte × formato)

```mermaid
classDiagram
    class Reporte {
        <<abstract>>
        -GeneradorReporte generador
        +setGenerador(GeneradorReporte)
        +generar()
    }
    Reporte <|-- ReporteEnvios
    Reporte <|-- ReporteIngresos
    Reporte <|-- ReporteDesempenoProveedores
    class GeneradorReporte {
        <<interface>>
        +formatear(datos) String
    }
    GeneradorReporte <|.. GeneradorJSON
    GeneradorReporte <|.. GeneradorCSV
    GeneradorReporte <|.. GeneradorExcel
    GeneradorReporte <|.. GeneradorPDF
    Reporte o--> GeneradorReporte : bridge
```

### Adapter (proveedores externos)

```mermaid
classDiagram
    class ProveedorEnvio {
        <<interface>>
        +crearEnvio(Envio) String
        +rastrear(String) String
    }
    ProveedorEnvio <|.. AdapterDHL
    ProveedorEnvio <|.. AdapterFedEx
    ProveedorEnvio <|.. AdapterUPS
    AdapterDHL ..> DHLAPI : adapta
    AdapterFedEx ..> FedExAPI : adapta
    AdapterUPS ..> UPSConnector : adapta
```

---

## 5. Patrones de comportamiento

### Chain of Responsibility (validación de envíos)

```mermaid
classDiagram
    class ValidadorEnvio {
        <<abstract>>
        -ValidadorEnvio siguiente
        +validarEnvio(Envio) boolean
    }
    ValidadorEnvio <|-- ValidadorDatos
    ValidadorEnvio <|-- ValidadorInventario
    ValidadorEnvio <|-- ValidadorPago
    ValidadorEnvio <|-- ValidadorSeguridad
    ValidadorEnvio <|-- ValidadorCapacidad
    ValidadorEnvio o--> ValidadorEnvio : siguiente
```

### Strategy (cálculo de costo)

```mermaid
classDiagram
    class EstrategiaCalculoCosto {
        <<interface>>
        +calcular(Envio) double
    }
    EstrategiaCalculoCosto <|.. EstrategiaDistancia
    EstrategiaCalculoCosto <|.. EstrategiaPeso
    EstrategiaCalculoCosto <|.. EstrategiaUrgencia
    EstrategiaCalculoCosto <|.. EstrategiaVolumen
    EstrategiaCalculoCosto <|.. EstrategiaHibrida
    Envio o--> EstrategiaCalculoCosto : estrategia
```

### Observer (notificación de cambios)

```mermaid
classDiagram
    class ObservadorEnvio {
        <<interface>>
        +actualizar(Envio)
    }
    ObservadorEnvio <|.. SistemaNotificacionObservador
    ObservadorEnvio <|.. DashboardObservador
    ObservadorEnvio <|.. SistemaAuditoriaObservador
    ObservadorEnvio <|.. CentroDistribucionObservador
    Envio o--> ObservadorEnvio : observadores
```

---

## 6. Ciclo de vida del envío (State)

```mermaid
stateDiagram-v2
    [*] --> Confirmado
    Confirmado --> EnTransito : validar()
    Confirmado --> Cancelado : cancelar()
    EnTransito --> EnReparto : despachar()
    EnTransito --> Retenido : retener()
    Retenido --> EnTransito : liberar()
    EnReparto --> Entregado : entregar()
    EnReparto --> Retenido : retener()
    Entregado --> [*]
    Cancelado --> [*]
```
