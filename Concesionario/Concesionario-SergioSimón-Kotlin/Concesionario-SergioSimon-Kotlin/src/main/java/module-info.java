module dev.sergiosf.concesionario {
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
//    requires runtime.jvm;
//    requires sqlite.driver;

    // Como no pongas esto te vas a volver loco con los errores
    requires java.sql;

    // Koin
    requires koin.core.jvm;

    // Abrimos y exponemos el paquete a JavaFX
    opens dev.sergiosf.concesionario to javafx.fxml;
    exports dev.sergiosf.concesionario;

    // Controladores
    opens dev.sergiosf.concesionario.controllers to javafx.fxml;
    exports dev.sergiosf.concesionario.controllers;

    // Rutas
    opens dev.sergiosf.concesionario.routes to javafx.fxml;
    exports dev.sergiosf.concesionario.routes;

    // Modelos a javafx para poder usarlos en las vistas
    opens dev.sergiosf.concesionario.models to javafx.fxml;
    exports dev.sergiosf.concesionario.models;
}