package com.example.loschinos.controllers

import com.example.javafxdemo.routers.RoutesManager
import javafx.fxml.FXML
import javafx.scene.image.Image
import javafx.scene.image.ImageView

class AcercaDeControllerView {
    @FXML
    private lateinit var imageLogo: ImageView

    @FXML
    fun initialize() {
        imageLogo.image = Image(RoutesManager.getResourceAsStream("images/icon.png"))
    }
}