# Explicación

## Funcionamiento de la UI

Ejercicio de los chinos: El usuario debe de elegir los parámetros de la partida (numero de rondas) y el número de bolas que va a coger siempre sera un número impar del 1 al 7 mediante un botón spiner igual que para el otro jugador (IA). Cuando este listo el usuario le debe de dar al botón de jugar y comenzará la partida el usuario debe de acertar el número total de bolas contando las suyas y las del oponente. Habrá 3 posibles resultados los cuales serán notificados al usuario mediante una pantalla de advertencia empate ninguno ha acertado se sigue jugando, empate porque los dos han acertado y se acaba la ronda o uno de los jugadores ha acertado el número antes que el otro jugador y si ninguno acierta se acaba la ronda con empates. La puntuación se recoge en un marcador

## Estructura del programa

### Controlador

Aquí se encuentra la lógica del programa tanto de la interfaz como la del juego

### Di

Carpeta dedicada al inyector de dependencias de Koin el cual por alguna razón me daba fallos y no he podido continuar lo revisare con más calma.

## TODO

Quería crear la carpeta viewmodels para aligerar al lógica del controlador ademas de hacer binding bidireccional de los datos a los datos de sate y así que la interfaz y la lógica compartan los datos
