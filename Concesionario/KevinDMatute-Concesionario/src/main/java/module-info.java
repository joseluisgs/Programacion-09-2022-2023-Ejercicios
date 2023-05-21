module dev.kevin.vehiculosfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires io.github.microutils.kotlinlogging;
    requires kotlin.stdlib.jdk7;
    requires sqlite.driver;
    requires runtime.jvm;
    requires koin.core.jvm;
    requires kotlin.result.jvm;
    requires koin.logger.slf4j;
    requires org.slf4j;
    requires java.sql;


    opens dev.kevin.vehiculosfx to javafx.fxml;
    exports dev.kevin.vehiculosfx;

    opens dev.kevin.vehiculosfx.controllers to javafx.fxml;
    exports dev.kevin.vehiculosfx.controllers;

    opens dev.kevin.vehiculosfx.routes to javafx.fxml;
    exports dev.kevin.vehiculosfx.routes;

    opens dev.kevin.vehiculosfx.models to javafx.fxml;
    exports dev.kevin.vehiculosfx.models;

}