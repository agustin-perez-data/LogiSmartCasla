# Catálogo de Patrones de Diseño — LogiSmart

Este documento mapea los **23 patrones de diseño GoF** a su implementación concreta en LogiSmart:
en qué paquete viven, qué problema del dominio resuelven, qué clases participan, en qué hito se
introdujeron y qué tests los verifican.

> Convención de paquetes: raíz `com.logismart`.

---

## Creacionales (5)

### 1. Singleton
- **Paquete:** `singleton`
- **Problema:** servicios transversales que deben tener una única instancia compartida (configuración, log, cache, pool de conexiones, registro de proveedores).
- **Clases:** `ConfiguracionLogiSmart`, `ConfiguracionSistema`, `LoggerLogiSmart`, `Logger`, `CacheLogiSmart`, `PoolDeConexiones`, `RegistroDeProveedores`.
- **Hito:** 6 · **Tests:** `singleton/SingletonTest` (7).

### 2. Factory Method
- **Paquete:** `factory`
- **Problema:** crear objetos del dominio (envíos, vehículos, usuarios, notificadores) sin acoplar al cliente a las clases concretas.
- **Clases:** `EnvioFactory`, `VehiculoFactory`, `UsuarioFactory`, `NotificadorFactory`, `EstrategiaDeCostosFactory`, `ProveedorDeIntegracionFactory`.
- **Hito:** 6 · **Tests:** `factory/FactoryTest` (parte de los 10).

### 3. Abstract Factory
- **Paquete:** `factory`
- **Problema:** producir familias de objetos coherentes por región (Argentina / Brasil): vehículo + calculador de costos + proveedor de mapas.
- **Clases:** `LogiSmartFactory` (abstracta), `LogiSmartFactoryArgentina`, `LogiSmartFactoryBrasil`.
- **Hito:** 7 · **Tests:** `factory/FactoryTest` (familias AR/BR).

### 4. Builder
- **Paquete:** `dominio`
- **Problema:** construir un `Envio` con muchos atributos opcionales (peso, frágil, refrigeración, firma…) de forma legible y segura.
- **Clases:** `Envio.EnvioBuilder`.
- **Hito:** 7 · **Verificación:** usado por `Main` y por la mayoría de los tests al construir envíos.

### 5. Prototype
- **Paquete:** `dominio`
- **Problema:** clonar un envío como plantilla sin recrearlo campo a campo (y sin compartir observadores con el original).
- **Clases:** `Envio implements Cloneable` → `Envio.clone()`.
- **Hito:** 7 · **Verificación:** `Main` (clonado de envíos).

---

## Estructurales (7)

### 6. Adapter
- **Paquete:** `servicios/adapters` (+ `servicios/externas`, interfaces `ProveedorEnvio` / `ProveedorPago`)
- **Problema:** integrar APIs externas heterogéneas (DHL, FedEx, UPS, PayPal, Stripe) tras una interfaz común del sistema.
- **Clases:** `AdapterDHL`, `AdapterFedEx`, `AdapterUPS`, `AdapterPayPal`, `AdapterStripe`.
- **Hito:** 8 · **Tests:** `servicios/adapters/AdaptersTest` (7, incluye polimorfismo).

### 7. Bridge
- **Paquete:** `servicios/reportes`
- **Problema:** desacoplar el *tipo* de reporte de su *formato* de salida, evitando la explosión de subclases (Reporte×Formato).
- **Clases:** `Reporte` (abstracción) + `ReporteEnvios` / `ReporteIngresos` / `ReporteDesempenoProveedores`; `GeneradorReporte` (implementor) + `GeneradorJSON` / `GeneradorCSV` / `GeneradorExcel` / `GeneradorPDF`.
- **Hito:** 8.

### 8. Composite
- **Paquete:** `visitor` (red de centros de distribución)
- **Problema:** tratar centros individuales y agrupaciones jerárquicas de centros de forma uniforme.
- **Clases:** `CentroDistribucion` (componente), `PuntoEntrega` (hoja), `CentroRegional` (compuesto, contiene subcentros).
- **Hito:** 8 · **Tests:** ejercitado en `visitor/VisitorCentroTest` (árbol anidado).

### 9. Decorator
- **Paquete:** `decorator`
- **Problema:** agregar servicios opcionales a un envío (seguro, prioritario, rastreo GPS, SMS) sin crear una subclase por combinación.
- **Clases:** `Envio` (componente), `EnvioBasico`, `DecoradorEnvio` (base), `DecoradorSeguro`, `DecoradorPrioritario`, `DecoradorRastreoGPS`, `DecoradorNotificacionesSMS`.
- **Hito:** 8 · **Tests:** `decorator/DecoradorEnvioTest` (8).

### 10. Facade
- **Paquete:** `facade`
- **Problema:** ofrecer una interfaz simple sobre subsistemas complejos (inventario, pagos, rastreo, notificaciones, reportes).
- **Clases:** `ServicioLogisticaFacade` + `facade/subsistemas/*`.
- **Hito:** 8 · **Tests:** `facade/ServicioLogisticaFacadeTest` (6).

### 11. Flyweight
- **Paquete:** `flyweight`
- **Problema:** compartir objetos `Ubicacion` repetidos (misma ciudad/CP) en lugar de duplicarlos.
- **Clases:** `Ubicacion` (flyweight), `FabricaUbicaciones` (pool con clave).
- **Hito:** 8 · **Tests:** `flyweight/FabricaUbicacionesTest` (6, verifica `assertSame`).

### 12. Proxy
- **Paquete:** `proxy` (+ `datos/lazy`)
- **Problema:** controlar el acceso al repositorio real, cacheando y difiriendo la carga (lazy loading).
- **Clases:** `RepositorioEnvios` (sujeto), `RepositorioEnviosReal`, `ProxyRepositorioEnvios`; lazy proxies en `datos/lazy`.
- **Hito:** 8 · **Tests:** `proxy/ProxyRepositorioEnviosTest` (7).

---

## De comportamiento (11)

### 13. Chain of Responsibility
- **Paquete:** `chain`
- **Problema:** validar un envío con una cadena de validadores donde cada uno decide pasar o cortar.
- **Clases:** `ValidadorEnvio` (handler abstracto) + `ValidadorDatos`, `ValidadorInventario`, `ValidadorPago`, `ValidadorSeguridad`, `ValidadorCapacidad`; `CadenaValidadores`.
- **Hito:** 10 · **Tests:** `chain/CadenaValidadoresTest` (8).

### 14. Command
- **Paquete:** `command`
- **Problema:** encapsular operaciones sobre envíos como objetos, con cola y soporte de undo/redo.
- **Clases:** `Comando` (interfaz) + `ComandoCrearEnvio`, `ComandoCancelarEnvio`, `ComandoActualizarEstado`, `ComandoCambiarMetodoPago`, `ComandoAgregarServicio`; `ColaComandos` (invoker), `ServicioEnvios` (receptor).
- **Hito:** 10 · **Tests:** `command/ColaComandosTest` (5).

### 15. Interpreter
- **Paquete:** `interpreter`
- **Problema:** evaluar reglas de negocio (origen, destino, peso, costo, restricción) combinables con AND/OR/NOT.
- **Clases:** `Expresion` + terminales `ExpresionOrigen/Destino/Peso/Costo/Restringido` + no-terminales `ExpresionAND/OR/NOT`.
- **Hito:** 10 · **Tests:** `interpreter/ExpresionTest` (6).

### 16. Iterator
- **Paquete:** `iterator`
- **Problema:** recorrer colecciones de envíos de distinta implementación (array, lista, hash) con una interfaz uniforme.
- **Clases:** `IteradorEnvios`, `ColeccionEnvios`, `ColeccionArray`, `ColeccionLista`, `ColeccionHash`.
- **Hito:** 11 · **Tests:** `iterator/IteradorEnviosTest` (5).

### 17. Mediator
- **Paquete:** `mediator`
- **Problema:** coordinar subsistemas (pago, notificación, auditoría, centro) sin que se conozcan entre sí.
- **Clases:** `MediadorEnvios` + `MediadorEnviosConcreto`; colegas `CentroDistribucion`, `SistemaPago`, `SistemaNotificacion`, `SistemaAuditoria`.
- **Hito:** 11 · **Tests:** `mediator/MediadorEnviosTest` (4).

### 18. Memento
- **Paquete:** `memento`
- **Problema:** guardar y restaurar el estado de un envío (deshacer cambios).
- **Clases:** `MementoEnvio`, `HistorialEnvios` (caretaker).
- **Hito:** 11 · **Tests:** `memento/MementoEnvioTest` (4).

### 19. Observer
- **Paquete:** `observer`
- **Problema:** notificar a múltiples interesados cuando cambia el estado de un envío.
- **Clases:** `ObservadorEnvio` + `SistemaNotificacionObservador`, `DashboardObservador`, `SistemaAuditoriaObservador`, `CentroDistribucionObservador`.
- **Hito:** 11 · **Tests:** `observer/ObservadorEnvioTest` (5).

### 20. State
- **Paquete:** `state`
- **Problema:** modelar el ciclo de vida del envío con transiciones válidas según el estado actual.
- **Clases:** `EstadoEnvio` (interfaz) + `EstadoConfirmado`, `EstadoEnTransito`, `EstadoEnReparto`, `EstadoEntregado`, `EstadoRetenido`, `EstadoCancelado`.
- **Hito:** 12 · **Tests:** `state/EstadoEnvioStateTest` (5).

### 21. Strategy
- **Paquete:** `strategy` (+ `servicios` para estrategias de costo/optimización)
- **Problema:** intercambiar el algoritmo de cálculo de costo en tiempo de ejecución.
- **Clases:** `EstrategiaCalculoCosto` + `EstrategiaDistancia`, `EstrategiaPeso`, `EstrategiaUrgencia`, `EstrategiaVolumen`, `EstrategiaHibrida`.
- **Hito:** 12 · **Tests:** `strategy/EstrategiaCalculoCostoTest` (6).

### 22. Template Method
- **Paquete:** `template`
- **Problema:** definir el esqueleto del proceso de envío y dejar que cada variante (nacional, internacional, urgente) redefina pasos concretos.
- **Clases:** `ProcesoProcesosEnvio` (abstracta) + `ProcesoNacional`, `ProcesoInternacional`, `ProcesoUrgente`.
- **Hito:** 12 · **Tests:** `template/ProcesoEnvioTemplateTest` (4).

### 23. Visitor
- **Paquete:** `visitor`
- **Problema:** aplicar operaciones nuevas (ocupación, costo operativo, reporte, puntos críticos) sobre la red de centros sin modificar sus clases.
- **Clases:** `VisitorCentro` + `VisitorCalculoOcupacion`, `VisitorGeneradorReporte`, `VisitorCalculoCostoOperativo`, `VisitorBusquedaPuntosCriticos`.
- **Hito:** 12 · **Tests:** `visitor/VisitorCentroTest` (4).

---

## Resumen

| # | Patrón | Categoría | Paquete | Hito |
|--:|---|---|---|:--:|
| 1 | Singleton | Creacional | `singleton` | 6 |
| 2 | Factory Method | Creacional | `factory` | 6 |
| 3 | Abstract Factory | Creacional | `factory` | 7 |
| 4 | Builder | Creacional | `dominio` | 7 |
| 5 | Prototype | Creacional | `dominio` | 7 |
| 6 | Adapter | Estructural | `servicios/adapters` | 8 |
| 7 | Bridge | Estructural | `servicios/reportes` | 8 |
| 8 | Composite | Estructural | `visitor` | 8 |
| 9 | Decorator | Estructural | `decorator` | 8 |
| 10 | Facade | Estructural | `facade` | 8 |
| 11 | Flyweight | Estructural | `flyweight` | 8 |
| 12 | Proxy | Estructural | `proxy`, `datos/lazy` | 8 |
| 13 | Chain of Responsibility | Comportamiento | `chain` | 10 |
| 14 | Command | Comportamiento | `command` | 10 |
| 15 | Interpreter | Comportamiento | `interpreter` | 10 |
| 16 | Iterator | Comportamiento | `iterator` | 11 |
| 17 | Mediator | Comportamiento | `mediator` | 11 |
| 18 | Memento | Comportamiento | `memento` | 11 |
| 19 | Observer | Comportamiento | `observer` | 11 |
| 20 | State | Comportamiento | `state` | 12 |
| 21 | Strategy | Comportamiento | `strategy` | 12 |
| 22 | Template Method | Comportamiento | `template` | 12 |
| 23 | Visitor | Comportamiento | `visitor` | 12 |
