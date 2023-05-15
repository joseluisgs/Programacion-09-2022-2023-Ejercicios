module dev.kuromiichi.emmafernandezjuegocanicas {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;


    opens dev.kuromiichi.emmafernandezjuegocanicas to javafx.fxml;
    exports dev.kuromiichi.emmafernandezjuegocanicas;

    opens dev.kuromiichi.emmafernandezjuegocanicas.controllers to javafx.fxml;
    exports dev.kuromiichi.emmafernandezjuegocanicas.controllers;
}
