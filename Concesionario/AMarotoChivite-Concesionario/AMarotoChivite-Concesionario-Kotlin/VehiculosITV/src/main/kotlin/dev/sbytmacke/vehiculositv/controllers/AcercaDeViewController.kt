package dev.sbytmacke.vehiculositv.controllers

import javafx.fxml.FXML
import javafx.scene.control.Hyperlink
import java.awt.Desktop
import java.net.URI

class AcercaDeViewController {

    @FXML
    private lateinit var myGithubLink: Hyperlink

    @FXML
    fun initialize() {
        myGithubLink.setOnAction {
            val url = "https://github.com/Sbytmacke"
            val uri = URI.create(url)
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(uri)
            }
        }
    }
}