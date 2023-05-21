module com.example.gestionvehiculosconimagenes_kotlin {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;

    // Logger
    requires io.github.microutils.kotlinlogging;
    requires koin.logger.slf4j;
    requires org.slf4j;

    // Result
    requires kotlin.result.jvm;

    // SqlDeLight
    requires runtime.jvm;
    requires sqlite.driver;
    requires java.sql;

    // Koin
    requires koin.core.jvm;

    opens com.example.gestionvehiculosconimagenes_kotlin to javafx.fxml;
    exports com.example.gestionvehiculosconimagenes_kotlin;

    opens com.example.gestionvehiculosconimagenes_kotlin.controllers to javafx.fxml;
    exports com.example.gestionvehiculosconimagenes_kotlin.controllers;

    opens com.example.gestionvehiculosconimagenes_kotlin.model to javafx.fxml;
    exports com.example.gestionvehiculosconimagenes_kotlin.model;
}