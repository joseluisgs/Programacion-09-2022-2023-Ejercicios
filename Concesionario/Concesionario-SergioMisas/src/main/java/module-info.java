module es.sergiomisas.concesionario {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires open;

    // Logger
    requires io.github.microutils.kotlinlogging;
    requires koin.logger.slf4j;
    requires org.slf4j;

    // Gson
    requires com.google.gson;

    // Result
    requires kotlin.result.jvm;

    // SqlDeLight
    requires runtime.jvm;
    requires sqlite.driver;
    // Como no pongas esto te vas a volver loco con los errores
    requires java.sql;

    // Koin
    requires koin.core.jvm;


    // Abrimos y exponemos el paquete a JavaFX
    opens es.sergiomisas.concesionario to javafx.fxml;
    exports es.sergiomisas.concesionario;

    // Controladores
    opens es.sergiomisas.concesionario.controllers to javafx.fxml;
    exports es.sergiomisas.concesionario.controllers;

    // Rutas
    opens es.sergiomisas.concesionario.routes to javafx.fxml;
    exports es.sergiomisas.concesionario.routes;

    // dtos, abrimos a Gson
    opens es.sergiomisas.concesionario.dto.json to com.google.gson;

    // Modelos a javafx para poder usarlos en las vistas
    opens es.sergiomisas.concesionario.models to javafx.fxml;
    exports es.sergiomisas.concesionario.models;

}
