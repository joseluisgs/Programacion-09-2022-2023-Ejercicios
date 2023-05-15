package oscargrc.chinos.routes
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.scene.layout.Pane
import javafx.stage.Modality
import javafx.stage.Stage
import mu.KotlinLogging
import oscargrc.chinos.controllers.ChinosController
import java.io.InputStream
import java.net.URL


/**
 * Clase que gestiona las rutas de la aplicación
 */
private var logger = KotlinLogging.logger {  }
object RoutesManager {
    // Necesitamos siempre saber
    private lateinit var mainStage: Stage // La ventana principal
    private lateinit var _activeStage: Stage // La ventana actual
    val activeStage: Stage
        get() = _activeStage
    lateinit var app: Application // La aplicación

    // Podemos tener una cache de escenas cargadas
    private var scenesMap: HashMap<String, Pane> = HashMap()

    // Definimos las rutas de las vistas que tengamos
    enum class View(val fxml: String) {
        MAIN("views/chinos-view.fxml"),
        ACERCADE("views/chinosAcercaDe-view.fxml"),
        RESULTADO("views/resultado-view.fxml")
    }
    // Recuerda
    // Si cambiamos de ventana, cambiamos una stage y una scena
    // Si mantenemos la ventana, cambiamos solo la scena

    // Inicializamos la scena principal
    fun initMainStage(stage: Stage) {
        // Configuramos todo como vienen por defecto
        val fxmlLoader = FXMLLoader(getResource(View.MAIN.fxml))
        val parentRoot = fxmlLoader.load<Pane>()
        val scene = Scene(parentRoot, 600.0, 400.0)


        // Otros métodos para configurar el stage
        stage.title = "Chinos"
        // Podemos añadirles iconos o el estilo de cada ventana
        stage.icons.add(Image(getResourceAsStream("icons/logo.png")))

        // incluso el evento de cerrar
        stage.setOnCloseRequest {
            // Podemos hacer algo antes de cerrar
            // println("Cerrando la ventana")
            val controller = fxmlLoader.getController<ChinosController>()
            controller.onOnCloseAction()
        }

        // Le asignamos la scena
        stage.scene = scene

        // si vamos a querer cerrarla o ocultarla debemos salvarla
        // val oldStage = mainStage

        // Guardamos la scena y el stage
        mainStage = stage
        // y es la actual
        _activeStage = stage

        // Cerramos la anterior
        // oldStage.close()

        // Mostramos la nueva
        mainStage.show()

    }

    // Abrimos one como una nueva ventana
    fun initAcercaDeStage() {
        logger.debug { "Inicializando AcercaDeStage" }

        val fxmlLoader = FXMLLoader(getResource(View.ACERCADE.fxml))
        val parentRoot = fxmlLoader.load<Pane>()
        val scene = Scene(parentRoot, 491.0, 367.0)
        val stage = Stage()
        stage.title = "Acerca De Nosotros"
        stage.icons.add(Image(getResourceAsStream("icons/logo.png")))
        stage.scene = scene
        stage.initOwner(mainStage)
        stage.initModality(Modality.WINDOW_MODAL)
        stage.isResizable = false
        stage.show()
    }
    fun reset(){


    }



    // Repetimos por cada stage configurando el stage y la scena
    //....

    // Abrimos one como una nueva ventana
    fun initPruebaEscenasStage() {
        // Configuramos todo como vienen por defecto
        val fxmlLoader = FXMLLoader(getResource(View.ACERCADE.fxml))
        val parentRoot = fxmlLoader.load<Pane>()
        // Fijamos su tamaño
        val scene = Scene(parentRoot, 320.0, 240.0)

        val stage = Stage()
        stage.title = "Acerca de nosotros"
        stage.scene = scene

        // Otros métodos para configurar el stage
        // Podemos añadirles iconos o el estilo de cada ventana
        // stage.icons.add(Image(this.getResourceAsStream("icons/java.png")))

        // Vamos abrirla como modal
        stage.initOwner(mainStage)
        stage.initModality(Modality.WINDOW_MODAL)

        // si vamos a querer cerrarla o ocultarla debemos salvarla
        // val oldStage = mainStage

        // Guardamos la scena y el stage
        // mainStage = stage

        // Cerramos la anterior
        // oldStage.close()

        // ahora la activa soy yo por ser modal
        _activeStage = stage

        // Mostramos la nueva
        stage.show()
    }

    // O podemos hacer uno genérico, añade las opciones que necesites
    fun initStage(
        view: View,
        title: String = "Nueva ventana",
        modal: Boolean = false,
        icon: String = "",
        width: Double = 320.0,
        height: Double = 240.0,
        closePrevious: Boolean = true,
    ) {
        // Configuramos todo como vienen por defecto
        val fxmlLoader = FXMLLoader(getResource(view.fxml))
        val parentRoot = fxmlLoader.load<Pane>()
        // Fijamos su tamaño
        val scene = Scene(parentRoot, width, height)

        val stage = Stage()
        stage.title = title
        stage.scene = scene

        // Otros métodos para configurar el stage
        // Podemos añadirles iconos o el estilo de cada ventana
        if (icon.isNotEmpty()) stage.icons.add(Image(getResourceAsStream(icon)))

        // Vamos abrirla como modal
        if (modal) {
            stage.initOwner(mainStage)
            stage.initModality(Modality.WINDOW_MODAL)
        }

        if (closePrevious) {
            // si vamos a querer cerrarla o ocultarla debemos salvarla
            val oldStage = mainStage

            // Guardamos la scena y el stage
            mainStage = stage

            // Cerramos la anterior
            oldStage.close()
        }


        // ahora la activa soy yo
        _activeStage = stage

        // Mostramos
        stage.show()
    }

    // Aquí definimos los FXML de cada scena, si no le pasamos nada es la activa
    fun changeScene(
        myStage: Stage = activeStage,
        view: View,
        width: Double = 320.0,
        height: Double = 240.0,
    ) {
        val parentRoot = FXMLLoader.load<Pane>(getResource(view.fxml))
        val scene = Scene(parentRoot, width, height)
        myStage.scene = scene
    }


    fun addScene(view: View) {
        scenesMap[view.fxml] = FXMLLoader.load(getResource(view.fxml))
    }

    fun removeScene(view: View) {
        scenesMap.remove(view.fxml)
    }

    fun getResource(resource: String): URL {
        return app::class.java.getResource(resource)
            ?: throw RuntimeException("No se ha encontrado el recurso: $resource")
    }

    fun getResourceAsStream(resource: String): InputStream {
        return app::class.java.getResourceAsStream(resource)
            ?: throw RuntimeException("No se ha encontrado el recurso como stream: $resource")
    }

}

