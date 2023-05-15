package dev.kuromiichi.emmafernandezjuegocanicas.controllers

import dev.kuromiichi.emmafernandezjuegocanicas.data.routes.RoutesManager
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView

class AcercaDeController {
    @FXML
    private lateinit var labelCreadoPor: Label

    @FXML
    private lateinit var imageViewCanica: ImageView

    @FXML
    private fun initialize() {
        labelCreadoPor.text = "Creado por: Emma Fernández Barranco"
        imageViewCanica.image = Image(RoutesManager.getResource("images/canica.png").toExternalForm())
    }
}
