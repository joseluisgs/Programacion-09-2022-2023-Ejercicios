package oscargrc.chinos.controllers

import com.vaadin.open.Open
import javafx.fxml.FXML
import mu.KotlinLogging

private val logger = KotlinLogging.logger {  }
class chinosAcercaDeController {
    @FXML
    private lateinit var enlaceGithub: javafx.scene.control.Hyperlink
    @FXML
    private lateinit var enlaceDineros: javafx.scene.control.Hyperlink
    // Inicializamos
    @FXML
    fun initialize() {
        logger.info { "Inicializando AcercaDeViewController FXML" }
        enlaceGithub.setOnAction {
            val url = "https://github.com/OscarGrC"
            logger.debug { "Abriendo navegador en el link: $url" }
            Open.open(url)
        }
        enlaceDineros.setOnAction {
            val url = "https://youtu.be/seU9L4eI74E?t=21"
            logger.debug { "Abriendo navegador en el link: $url" }
            Open.open(url)
        }
    }
}