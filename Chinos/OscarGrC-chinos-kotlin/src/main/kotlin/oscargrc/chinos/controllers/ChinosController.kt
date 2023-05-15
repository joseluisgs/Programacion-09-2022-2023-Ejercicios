package oscargrc.chinos.controllers

import oscargrc.chinos.routes.RoutesManager
import oscargrc.chinos.data.viewmodels.DemoViewModel

import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.GridPane
import javafx.scene.layout.VBox
import mu.KotlinLogging
import oscargrc.chinos.data.parameters.DemoParameterData
import oscargrc.chinos.models.ChinosState
import java.util.*
private val logger = KotlinLogging.logger {  }
class ChinosController {
    @FXML
    private lateinit var eleccionUsuarioSpinner:Spinner<Int>
    @FXML
    private lateinit var eleccionUsuarioSuManoSpinner:Spinner<Int>
    @FXML
    private lateinit var eleccionButton: Button
    @FXML
    private lateinit var derrotasTextArea: TextField
    @FXML
    private lateinit var victoriasTextArea: TextField
    @FXML
    private lateinit var empezasrButton: Button
    @FXML
    private lateinit var numBolasSpinner: Spinner<Int>
    @FXML
    private lateinit var numRondasSpinner: Spinner<Int>
    @FXML
    private lateinit var panelSplit: SplitPane
    @FXML
    private lateinit var panelCompletoPartida: VBox
    @FXML
    private lateinit var panelNumeros: GridPane
    @FXML
    private lateinit var menuAcercaDe: MenuItem
    @FXML
    private  var cero = ImageView()
    @FXML
    private  var ceronegado= ImageView()
    @FXML
    private  var uno= ImageView()
    @FXML
    private  var unonegado= ImageView()
    @FXML
    private  var dos= ImageView()
    @FXML
    private  var dosnegado= ImageView()
    @FXML
    private  var tres= ImageView()
    @FXML
    private  var tresnegado= ImageView()
    @FXML
    private  var cuatro= ImageView()
    @FXML
    private  var cuatronegado= ImageView()
    @FXML
    private  var cinco= ImageView()
    @FXML
    private  var cinconegado= ImageView()
    @FXML
    private  var seis= ImageView()
    @FXML
    private  var seisnegado= ImageView()
    @FXML
    private  var siete= ImageView()
    @FXML
    private  var sietenegado= ImageView()
    @FXML
    private var ocho= ImageView()
    @FXML
    private  var ochonegado= ImageView()
    @FXML
    private  var nueve= ImageView()
    @FXML
    private  var nuevenegado= ImageView()
    @FXML
    private  var diez= ImageView()
    @FXML
    private  var dieznegado= ImageView()
    @FXML
    private  var once= ImageView()
    @FXML
    private  var oncenegado= ImageView()
    @FXML
    private  var doce= ImageView()
    @FXML
    private  var docenegado= ImageView()
    @FXML
    private  var trece= ImageView()
    @FXML
    private  var trecenegado= ImageView()
    @FXML
    private  var catorce= ImageView()
    @FXML
    private  var catorcenegado= ImageView()

    private var estado = ChinosState()
    private var isPrimeraRonda = true
    private var resultadoChinos:Int = 0
    private var listaExistente = emptyList<Int>()
    private var eleccionIa = 0
    @FXML
    private fun initialize() {
        logger.info { "Inicializando  Controller FXML" }
        //paneles
        panelCompletoPartida.isVisible = false
        // Eventos de los menus
        menuAcercaDe.setOnAction { onAcercaDeAction() }

        // Eventos de los botones
        empezasrButton.setOnAction { onComenzarAction() }
        eleccionButton.setOnAction { onSelectAction() }

        // Iniciamos los spinners

        numRondasSpinner.valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(1, 3, 1)
        numBolasSpinner.valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(3,7,3,2)
        /*falta el de eleccion usuario y su mano*/

        //Iniciamos TextArea
        derrotasTextArea.text = "0"
        victoriasTextArea.text = "0"


    }
    @FXML
    private fun onComenzarAction() {
        println("Boton Comenzar pulsado")
       // primero vemos cuantos paneles tenemos que poner visibles
        if(numBolasSpinner.value== 5){
            siete.isVisible = true
            ocho.isVisible = true
            nueve.isVisible = true
            diez.isVisible = true
        }
        if(numBolasSpinner.value==7) {
            siete.isVisible = true
            ocho.isVisible = true
            nueve.isVisible = true
            diez.isVisible = true
            once.isVisible = true
            doce.isVisible = true
            trece.isVisible = true
            catorce.isVisible = true
        }
        //bloqear opciones
        numRondasSpinner.isDisable = true
        numBolasSpinner.isDisable = true
        empezasrButton.isVisible = false
        //calcular spinner
        eleccionUsuarioSuManoSpinner.valueFactory =SpinnerValueFactory.IntegerSpinnerValueFactory(0,
            numBolasSpinner.value,0)
        eleccionUsuarioSpinner.valueFactory=SpinnerValueFactory.IntegerSpinnerValueFactory(0,
            (numBolasSpinner.value*2),0)

        //mostramos panel
        panelCompletoPartida.isVisible = true
        panelNumeros.isVisible = true

    }




    @FXML
    fun onSelectAction() {
        var mensaje = Alert(Alert.AlertType.INFORMATION)
      // generamos un numero total al azar dentro de limites si es la primera ronda
        if(isPrimeraRonda){
            eleccionUsuarioSuManoSpinner.isDisable = true
            iniciarListaExistente()
      resultadoChinos=  eleccionUsuarioSuManoSpinner.value + (0 until  numBolasSpinner.value).random()
        println(" El numero es ${resultadoChinos}")
      //
        isPrimeraRonda=false}
      // ahora comprobamos si el numero es el adecuado
        var eleccionUsuario:Int = eleccionUsuarioSpinner.value
      if(resultadoChinos == eleccionUsuario){
          mensaje.title = "Acierto"
          mensaje.contentText ="Ganastes, el numero es ${eleccionUsuario}"
          mensaje.showAndWait()
          isPrimeraRonda = true
          eleccionUsuarioSuManoSpinner.isDisable = false
          //sumamos victoria
          victoriasTextArea.text = ((victoriasTextArea.text.toInt()) + 1 ).toString()
          //comprobar si se gano la partida
          if(victoriasTextArea.text.toInt()==numRondasSpinner.value){
              mensaje.title = "Ganaste la partida"
              mensaje.contentText ="Ganastes!! el resultado final es ${victoriasTextArea.text}-${derrotasTextArea.text}"
              mensaje.showAndWait()
              panelCompletoPartida.isVisible = false
              empezasrButton.isVisible = true
              numBolasSpinner.isDisable = false
              numRondasSpinner.isDisable = false
              eleccionUsuarioSuManoSpinner.isDisable = false
              victoriasTextArea.text = "0"
              derrotasTextArea.text ="0"
              panelCompletoPartida.isVisible = false
          }
          //  permitir edicion eleccionMano + fun reset numeros
            resetNumeros()

      }else{
          //primero tachamos el numero
         borrarNumero(eleccionUsuario)
          //quitamos la opcion del spinner para ello
          listaExistente.drop(eleccionUsuario)
          val nuevaLista = FXCollections.observableArrayList(listaExistente)
          eleccionUsuarioSpinner.valueFactory= SpinnerValueFactory.ListSpinnerValueFactory(nuevaLista)
          //ahora la IA
          eleccionIa = listaExistente.random()
          //repetimos logica
          if(resultadoChinos==eleccionIa){
              mensaje.title = "Acierto Adversario"
              mensaje.contentText ="Perdiste, el numero es ${eleccionIa} y el adversario lo acerto"
              mensaje.showAndWait()
              isPrimeraRonda = true
              eleccionUsuarioSuManoSpinner.isDisable = false
              //sumamos victoria
              derrotasTextArea.text = ((victoriasTextArea.text.toInt()) + 1 ).toString()
              resetNumeros()
              //comprobar si se gano la partida
              if(derrotasTextArea.text.toInt()==numRondasSpinner.value){
                  mensaje.title = "Perdiste la partida"
                  mensaje.contentText ="Perdistes!! el resultado final es ${victoriasTextArea}-${derrotasTextArea}"
                  panelCompletoPartida.isVisible = false
                  empezasrButton.isVisible = true
                  mensaje.showAndWait()
                  panelCompletoPartida.isVisible = false
                  empezasrButton.isVisible = true
                  numBolasSpinner.isDisable = false
                  numRondasSpinner.isDisable = false
                  eleccionUsuarioSuManoSpinner.isDisable = false
                  victoriasTextArea.text = "0"
                  derrotasTextArea.text ="0"
                  panelCompletoPartida.isVisible = false
              }

              //  permitir edicion eleccionMano + fun reset numeros
          }else{
              //primero tachamos el numero
              borrarNumero(eleccionIa)
              //sobreescribimos mensaje ventana emergente
              mensaje.title = "Fallasteis los dos "
              mensaje.contentText = "El jugador escoje ${eleccionUsuario} y falla \n " +
                      "El adversario esocoje ${eleccionIa} y tambien falla"
              mensaje.showAndWait()
              //quitamos la opcion del spinner para ello
              listaExistente.drop(eleccionIa)
              val nuevaLista = FXCollections.observableArrayList(listaExistente)
              eleccionUsuarioSpinner.valueFactory= SpinnerValueFactory.ListSpinnerValueFactory(nuevaLista)

          }
      }
    }

    private fun onAcercaDeAction() {

        RoutesManager.initAcercaDeStage()
    }
    // Método para salir de la aplicación
    fun onOnCloseAction() {
        logger.debug { "onOnCloseAction" }
        val alert = Alert(Alert.AlertType.CONFIRMATION)
        alert.title = "Salir"
        alert.contentText = "¿Desea salir?"
        val result = alert.showAndWait()
        if (result.get() == ButtonType.OK) {
            val alert2 = Alert(Alert.AlertType.CONFIRMATION)
            alert2.title = "Salir"
            alert2.contentText = "¿Seguro?"
            val result = alert2.showAndWait()
            if (result.get() == ButtonType.OK) {
                val alert3 = Alert(Alert.AlertType.CONFIRMATION)
                alert3.title = "Salir"
                alert3.contentText = "¿De verdad? :("
                val result = alert3.showAndWait()
                if (result.get() == ButtonType.OK) {
                    Platform.exit() // O System.exit(0)
                } else {
                    alert.close()
                }
            } else {
                alert.close()
            }
        } else {
            alert.close()
        }
    }

    private fun borrarNumero(numero:Int){
        when(numero){
            0 -> {
                // cambiamos el numero visible
                cero.isVisible= false
                ceronegado.isVisible = true
            }
            1 -> { uno.isVisible= false ; unonegado.isVisible = true}
            2 -> { dos.isVisible= false ; dosnegado.isVisible = true}
            3 -> { tres.isVisible= false ; tresnegado.isVisible = true}
            4 -> { cuatro.isVisible= false ; cuatronegado.isVisible = true}
            5 -> { cinco.isVisible= false ; cinconegado.isVisible = true}
            6 -> { seis.isVisible= false ; seisnegado.isVisible = true}
            7 -> { siete.isVisible= false ; sietenegado.isVisible = true}
            8 -> { ocho.isVisible= false ; ochonegado.isVisible = true}
            9 -> { nueve.isVisible= false ; nuevenegado.isVisible = true}
            10 -> { diez.isVisible= false ; dieznegado.isVisible = true}
            11 -> { once.isVisible= false ; oncenegado.isVisible = true}
            12 -> { doce.isVisible= false ; docenegado.isVisible = true}
            13 -> { trece.isVisible= false ; trecenegado.isVisible = true}
            14 -> { catorce.isVisible= false ; catorcenegado.isVisible = true}
        }
    }
    private fun resetNumeros(){
    when(numBolasSpinner.value){
        3-> {
            ceronegado.isVisible = false
            cero.isVisible = true
            unonegado.isVisible = false
            uno.isVisible = true
            dos.isVisible = true
            dosnegado.isVisible = false
            tresnegado.isVisible = false
            tres.isVisible = true
            cuatro.isVisible = true
            cuatronegado.isVisible = false
            cinco.isVisible = true
            cinconegado.isVisible = false
            seis.isVisible = true
            seisnegado.isVisible = false
        }
        5-> { ceronegado.isVisible = false
            cero.isVisible = true
            unonegado.isVisible = false
            uno.isVisible = true
            dos.isVisible = true
            dosnegado.isVisible = false
            tresnegado.isVisible = false
            tres.isVisible = true
            cuatro.isVisible = true
            cuatronegado.isVisible = false
            cinco.isVisible = true
            cinconegado.isVisible = false
            seis.isVisible = true
            seisnegado.isVisible = false
            siete.isVisible = true
            sietenegado.isVisible = false
            ocho.isVisible = true
            ochonegado.isVisible = false
            nueve.isVisible = true
            nuevenegado.isVisible = false
            diez.isVisible = true
            dieznegado.isVisible = false

        }
        7-> {ceronegado.isVisible = false
            cero.isVisible = true
            unonegado.isVisible = false
            uno.isVisible = true
            dos.isVisible = true
            dosnegado.isVisible = false
            tresnegado.isVisible = false
            tres.isVisible = true
            cuatro.isVisible = true
            cuatronegado.isVisible = false
            cinco.isVisible = true
            cinconegado.isVisible = false
            seis.isVisible = true
            seisnegado.isVisible = false
            siete.isVisible = true
            sietenegado.isVisible = false
            ocho.isVisible = true
            ochonegado.isVisible = false
            nueve.isVisible = true
            nuevenegado.isVisible = false
            diez.isVisible = true
            dieznegado.isVisible = false
            once.isVisible = true
            oncenegado.isVisible = false
            doce.isVisible = true
            docenegado.isVisible = false
            trece.isVisible = true
            trecenegado.isVisible = false
            catorce.isVisible = true
            catorcenegado.isVisible = false
        }
    }
    }
    fun iniciarListaExistente(){
        when(numBolasSpinner.value){
            3->{ listaExistente = mutableListOf(0,1,2,3,4,5,6)}
            5->{ listaExistente = mutableListOf(0,1,2,3,4,5,6,7,8,9,10)}
            7->{ listaExistente = mutableListOf(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14)}
        }
    }
}