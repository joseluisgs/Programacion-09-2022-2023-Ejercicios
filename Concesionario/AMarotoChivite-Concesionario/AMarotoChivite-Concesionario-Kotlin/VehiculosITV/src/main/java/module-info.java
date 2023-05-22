module dev.sbytmacke.vehiculositv {
    requires kotlin.stdlib;
    requires java.desktop;

    // JavaFX
    requires javafx.controls;
    requires javafx.fxml;

    // SqlDeLight
    requires runtime.jvm;
    requires sqlite.driver;
    requires java.sql;

    // Logger
    requires io.github.microutils.kotlinlogging;
    requires koin.logger.slf4j;
    requires org.slf4j;

    // Koin
    requires koin.core.jvm;
    requires com.google.gson;
    requires kotlin.result.jvm;

    // Proyecto apertura
    opens dev.sbytmacke.vehiculositv to javafx.fxml;
    exports dev.sbytmacke.vehiculositv;

    // Controllers apertura
    opens dev.sbytmacke.vehiculositv.controllers to javafx.fxml;
    exports dev.sbytmacke.vehiculositv.controllers;

    // Modelos
    opens dev.sbytmacke.vehiculositv.models to javafx.fxml, com.google.gson;
    exports dev.sbytmacke.vehiculositv.models;

}