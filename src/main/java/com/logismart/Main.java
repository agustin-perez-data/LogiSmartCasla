package com.logismart;

import com.logismart.controlador.LogiSmartController;
import com.logismart.dominio.Envio;
import com.logismart.dominio.EnvioExpress;
import com.logismart.dominio.TipoEnvio;
import com.logismart.factory.EnvioFactory;
import com.logismart.singleton.ConfiguracionSistema;
import com.logismart.singleton.Logger;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        System.out.println("=== DEMO ENVIO BUILDER ===\n");

        // Uso simple — solo atributos requeridos
        Envio envio1 = new Envio.EnvioBuilder("ENV001", "Buenos Aires", "Córdoba")
                .build();

        System.out.println("-- Envio simple --");
        System.out.println("ID:     " + envio1.getId());
        System.out.println("Origen: " + envio1.getOrigen());
        System.out.println("Destino:" + envio1.getDestino());
        System.out.println("Estado: " + envio1.getEstado());
        System.out.println("Fragil: " + envio1.isFragil());
        System.out.println();

        // Uso complejo — múltiples atributos opcionales
        Envio envio2 = new Envio.EnvioBuilder("ENV002", "Buenos Aires", "Mendoza")
                .descripcion("Medicinas urgentes")
                .peso(2.5)
                .fragil(true)
                .requiereSignatura(true)
                .requiereRefrigeracion(true)
                .requiereAseguranza(true)
                .instruccionesEspeciales("Mantener en frio entre 2-8°C")
                .contactoEmergencia("Dr. Garcia: 555-1234")
                .horaEntregaPreferida(LocalTime.of(14, 0))
                .build();

        System.out.println("ID:                     " + envio2.getId());
        System.out.println("Descripcion:            " + envio2.getDescripcion());
        System.out.println("Peso:                   " + envio2.getPeso() + " kg");
        System.out.println("Fragil:                 " + envio2.isFragil());
        System.out.println("Requiere Signatura:     " + envio2.isRequiereSignatura());
        System.out.println("Requiere Refrigeracion: " + envio2.isRequiereRefrigeracion());
        System.out.println("Requiere Aseguranza:    " + envio2.isRequiereAseguranza());
        System.out.println("Instrucciones:          " + envio2.getInstruccionesEspeciales());
        System.out.println("Contacto emergencia:    " + envio2.getContactoEmergencia());
        System.out.println("Hora preferida:         " + envio2.getHoraEntregaPreferida());
        System.out.println();

        // Uso con EnvioFactory (Builder + Factory juntos)
        Envio envio3 = EnvioFactory.crearEnvio(
                TipoEnvio.EXPRESS, "ENV003",
                "Rosario", "Córdoba",
                8.0, LocalDate.now().plusDays(1),
                "09:00", "12:00",
                "Av. Colón 123", -31.4, -64.1, 1);

        System.out.println("Tipo:           " + envio3.getClass().getSimpleName());
        System.out.println("Multiplicador:  " + ((EnvioExpress) envio3).getMultiplicadorCosto());
        System.out.println("Prioridad:      " + ((EnvioExpress) envio3).getPrioridad());
        System.out.println();

        // ACTIVIDAD 5 — Prototype: clonar 100 envios desde un prototipo
        System.out.println(" DEMO PROTOTYPE (Cloneable)");

        Envio prototipo = new Envio.EnvioBuilder("ENV-PROTO", "Buenos Aires", "Córdoba")
                .descripcion("Medicinas")
                .peso(2.5)
                .fragil(true)
                .build();

        List<Envio> envios = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            Envio clon = prototipo.clone();
            clon.setId("ENV-" + (1000 + i));
            envios.add(clon);
        }

        System.out.println("Prototipo:        " + prototipo.getId() + " | fragil=" + prototipo.isFragil());
        System.out.println("Clones generados: " + envios.size());
        System.out.println("Primer clon ID:   " + envios.get(0).getId());
        System.out.println("Ultimo clon ID:   " + envios.get(99).getId());
        System.out.println("Clon es distinto objeto del prototipo: " + (envios.get(0) != prototipo));
        System.out.println("Clon comparte valores del prototipo — fragil: " + envios.get(0).isFragil());

        // ACTIVIDAD 6 — Integración Completa: todos los patrones juntos
        // Singleton
        Logger logger = Logger.getInstance();
        ConfiguracionSistema config = ConfiguracionSistema.getInstance();
        logger.info("Max envios por ruta: " + config.getMaxEnviosPorRuta());

        // Abstract Factory — selecciona implementaciones segun region
        LogiSmartController app = new LogiSmartController("Argentina");

        app.crearUsuario("cliente",   "Juan Pérez");
        app.crearUsuario("operador",  "Maria García");
        app.crearEnvio("Buenos Aires", "Córdoba");
        app.crearEnviosMultiples(100);

        app.procesarEnvio("Buenos Aires", "Mendoza", 5.0);

        System.out.println("  Usuarios creados: " + app.getTotalUsuarios());
        System.out.println("  Envios totales:   " + app.getTotalEnvios());
    }
}
