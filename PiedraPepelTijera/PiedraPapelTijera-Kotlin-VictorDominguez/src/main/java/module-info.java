module com.example.piedrapapeltijera {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires kotlin.result.jvm;
    requires io.github.microutils.kotlinlogging;
    requires org.slf4j;


    opens com.example.piedrapapeltijera.controllers to javafx.fxml;
    exports com.example.piedrapapeltijera;
}