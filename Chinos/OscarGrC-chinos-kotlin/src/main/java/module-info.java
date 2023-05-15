module oscargrc.chinos {

    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires io.github.microutils.kotlinlogging;
    requires org.slf4j;
    requires kotlin.result.jvm;
    requires java.desktop;
    requires open;
    requires koin.logger.slf4j;
    requires koin.core.jvm;

    // Abrimos y exponemos lo que va a usar desde clases con FXML
    opens oscargrc.chinos to javafx.fxml;
    exports oscargrc.chinos;
    // Controladores
    opens  oscargrc.chinos.controllers to javafx.fxml;
    exports  oscargrc.chinos.controllers;
    // Rutas
    opens  oscargrc.chinos.routes to javafx.fxml;
    exports oscargrc.chinos.routes;
}