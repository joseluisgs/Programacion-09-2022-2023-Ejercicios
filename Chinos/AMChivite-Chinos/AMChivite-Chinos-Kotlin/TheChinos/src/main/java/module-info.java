module dev.sbytmacke.thechinos {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires java.desktop;
    requires kotlin.result.jvm;

    opens dev.sbytmacke.thechinos to javafx.fxml;
    opens dev.sbytmacke.thechinos.controllers to javafx.fxml;

    exports dev.sbytmacke.thechinos;
    exports dev.sbytmacke.thechinos.controllers;
}