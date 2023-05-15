module com.example.ejerciciobolaschinasivanrc {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;

    requires io.github.microutils.kotlinlogging;
    requires org.slf4j;
    requires koin.core.jvm;
    requires kotlin.result.jvm;

    opens com.example.ejerciciobolaschinasivanrc.controllers to javafx.fxml;
    exports com.example.ejerciciobolaschinasivanrc;
}