module dev.sbytmacke.matrix {
    requires kotlin.stdlib;
    requires java.desktop;

    // JavaFX
    requires javafx.controls;
    requires javafx.fxml;

    // Loggers
    requires io.github.microutils.kotlinlogging;
    requires koin.logger.slf4j;
    requires org.slf4j;

    // Koin
    requires koin.core.jvm;

    // Result
    requires kotlin.result.jvm;

    // Proyecto apertura
    opens dev.sbytmacke.matrix to javafx.fxml;
    exports dev.sbytmacke.matrix;

    // Controllers apertura
    opens dev.sbytmacke.matrix.controllers to javafx.fxml;
    exports dev.sbytmacke.matrix.controllers;
}