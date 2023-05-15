package dev.sbytmacke.thechinos.controllers

import dev.sbytmacke.thechinos.MainApp
import javafx.fxml.FXML
import javafx.scene.control.Hyperlink
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import java.awt.Desktop
import java.net.URI

class AcercaDeViewController {

    @FXML
    private lateinit var infoImage: ImageView

    @FXML
    private lateinit var myGithubLink: Hyperlink

    @FXML
    fun initialize() {
        // Imagen estática
        infoImage.image = Image(MainApp::class.java.getResourceAsStream("/icons/info.png"))

        // Link
        myGithubLink.setOnAction {
            val url = "https://github.com/Sbytmacke"
            val uri = URI.create(url)
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(uri)
            }
        }
    }
}