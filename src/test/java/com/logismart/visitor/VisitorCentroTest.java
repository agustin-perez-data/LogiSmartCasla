package com.logismart.visitor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Patron Visitor: recorridos sobre centros de distribucion")
class VisitorCentroTest {

    private static final double DELTA = 0.0001;

    // Arma: CentroRegional[ Norte ] -> { PuntoA(90), PuntoB(50), CentroRegional[Sur] -> PuntoC(85) }
    private CentroRegional arbolDeCentros() {
        CentroRegional norte = new CentroRegional("Norte");
        norte.agregarSubcentro(new PuntoEntrega("PuntoA", 90.0));
        norte.agregarSubcentro(new PuntoEntrega("PuntoB", 50.0));

        CentroRegional sur = new CentroRegional("Sur");
        sur.agregarSubcentro(new PuntoEntrega("PuntoC", 85.0));
        norte.agregarSubcentro(sur);
        return norte;
    }

    @Test
    @DisplayName("VisitorCalculoOcupacion promedia la ocupacion de los puntos visitados")
    void calculoOcupacionPromedio() {
        VisitorCalculoOcupacion visitor = new VisitorCalculoOcupacion();
        arbolDeCentros().aceptar(visitor);

        // (90 + 50 + 85) / 3
        assertEquals((90.0 + 50.0 + 85.0) / 3.0, visitor.obtenerOcupacionPromedio(), DELTA);
    }

    @Test
    @DisplayName("VisitorCalculoCostoOperativo suma ocupacion*10 de cada punto")
    void calculoCostoOperativoTotal() {
        VisitorCalculoCostoOperativo visitor = new VisitorCalculoCostoOperativo();
        arbolDeCentros().aceptar(visitor);

        assertEquals((90.0 + 50.0 + 85.0) * 10.0, visitor.obtenerCostoTotal(), DELTA);
    }

    @Test
    @DisplayName("VisitorBusquedaPuntosCriticos detecta solo puntos sobre el umbral")
    void busquedaPuntosCriticos() {
        VisitorBusquedaPuntosCriticos visitor = new VisitorBusquedaPuntosCriticos(80.0);
        arbolDeCentros().aceptar(visitor);

        List<String> criticos = visitor.obtenerPuntosCriticos();

        assertEquals(2, criticos.size(), "PuntoA(90) y PuntoC(85) superan el umbral 80");
        assertTrue(criticos.stream().anyMatch(p -> p.startsWith("PuntoA")));
        assertTrue(criticos.stream().anyMatch(p -> p.startsWith("PuntoC")));
        assertTrue(criticos.stream().noneMatch(p -> p.startsWith("PuntoB")),
                "PuntoB(50) no debe ser critico");
    }

    @Test
    @DisplayName("VisitorGeneradorReporte produce un reporte jerarquico indentado")
    void generadorReporteJerarquico() {
        VisitorGeneradorReporte visitor = new VisitorGeneradorReporte();
        arbolDeCentros().aceptar(visitor);

        String reporte = visitor.obtenerReporte();

        assertTrue(reporte.contains("+ Norte"), "Debe listar el centro raiz");
        assertTrue(reporte.contains("+ Sur"), "Debe listar el subcentro");
        assertTrue(reporte.contains("- PuntoA"), "Debe listar los puntos de entrega");
        // El subcentro Sur esta mas profundo que su punto raiz: indentacion mayor en PuntoC.
        int idxPuntoA = reporte.indexOf("- PuntoA");
        int idxPuntoC = reporte.indexOf("- PuntoC");
        assertTrue(idxPuntoA >= 0 && idxPuntoC > idxPuntoA,
                "PuntoC debe aparecer despues de PuntoA en el recorrido");
    }
}
