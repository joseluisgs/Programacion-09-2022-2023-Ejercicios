module es.sergiomisas.juegodelasbolas {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;


    opens es.sergiomisas.juegodelasbolas to javafx.fxml;
    exports es.sergiomisas.juegodelasbolas;

    opens es.sergiomisas.juegodelasbolas.controllers to javafx.fxml;
    exports es.sergiomisas.juegodelasbolas.controllers;
}
