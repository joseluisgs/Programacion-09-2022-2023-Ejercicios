package dev.sergiosf.chinos.controller

import com.vaadin.open.Open
import javafx.fxml.FXML
import javafx.scene.control.Hyperlink

class AboutContoller {

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