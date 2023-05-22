package com.example.concesionario.controllers

import com.example.javafxdemo.routes.RoutesManager
import javafx.fxml.FXML
import javafx.scene.image.Image
import javafx.scene.image.ImageView

class AcercaDeController {
    @FXML
    private lateinit var imageLogo: ImageView

    @FXML
    fun initialize() {
        imageLogo.image = Image(RoutesManager.getResourceAsStream("images/icon.png"))
    }
}