# Juego de las Canicas

## Descripción

Juego sencillo de adivinar canicas. El jugador deberá introducir el número de canicas que cree que tiene el rival, y si acierta ganará un punto. Si falla, el rival gana un punto. Se juega al mejor de 5, el primero que llegue a 3 puntos ganará la ronda.

## Ventanas

La ventana principal del juego contiene contadores de rondas ganadas y puntos por jugador, así como la sección del juego donde se puede seleccionar un número de canicas. Al pulsar el botón, se calculará el resultado.

Además de la ventana principal, existe una ventana de instrucciones y otra de "Acerca de", ambas accesibles desde el menú superior.

Al terminar una ronda, el juego pregunta si se desea seguir jugando. En caso afirmativo, se continúa el juego. De lo contrario, el juego terminará y el programa se cerrará.

## Lógica

Las ventanas están controladas por clases controladoras independientes (cada una tiene su controlador). Además, la lógica del juego está programada en un objeto "Juego" que actúa de viewmodel, ya que los contadores están enlazados al estado del objeto.

Al pulsar el botón de confirmar en la ventana principal, se hace una llamada al objeto "Juego" y se avanza un paso en la partida. Este objeto gestiona las rondas, las victorias y las derrotas, así como las ventanas emergentes que avisan al jugador de si ha ganado o perdido.

