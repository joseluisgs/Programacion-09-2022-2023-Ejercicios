package dev.sergiosf.concesionario.controllers

import com.vaadin.open.Open
import javafx.fxml.FXML
import javafx.scene.control.Hyperlink

class AcercaDeController {

    @FXML
    lateinit var linkGitHub: Hyperlink

    @FXML
    fun initialize() {
        linkGitHub.setOnAction {
            val url = "https://github.com/sergiosimonf"
            Open.open(url)
        }
    }
}