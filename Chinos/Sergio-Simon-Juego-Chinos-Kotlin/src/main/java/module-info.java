module dev.sergiosf.chinos {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires open;

    // JvaFX
    opens dev.sergiosf.chinos to javafx.fxml;
    exports dev.sergiosf.chinos;

    // Controller
    opens dev.sergiosf.chinos.controller to javafx.fxml;
    exports dev.sergiosf.chinos.controller;

    // Router
    opens dev.sergiosf.chinos.routes to javafx.fxml;
    exports dev.sergiosf.chinos.routes;

    // Koin
    requires koin.core.jvm;
}