package oscargrc.chinos.controllers

import javafx.fxml.FXML
import javafx.scene.control.Spinner
import javafx.scene.control.TextField
import oscargrc.chinos.data.parameters.DemoParameterData.mensaje

class ResultController() {
    @FXML
    private lateinit var resultado: TextField


    fun initialize() {
        resultado.text=mensaje
    }
}