module com.example.concesionario {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;

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
    requires java.sql;

    // Koin
    requires koin.core.jvm;

    // MyBatis
    requires org.mybatis;

    opens com.example.concesionario to javafx.fxml;
    exports com.example.concesionario;

    opens com.example.concesionario.controllers to javafx.fxml;
    exports com.example.concesionario.controllers;

    opens com.example.concesionario.dto to com.google.gson;

    opens com.example.concesionario.models to javafx.fxml;
    exports com.example.concesionario.models;

    opens com.example.concesionario.repositories.vehicle to javafx.fxml;
    exports com.example.concesionario.repositories.vehicle;

    opens com.example.concesionario.services.storage.vehicle to javafx.fxml;
    exports com.example.concesionario.services.storage.vehicle;

}