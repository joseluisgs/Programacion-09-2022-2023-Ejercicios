package dev.sbytmacke.matrix.viewmodels

import dev.sbytmacke.matrix.factories.FactoryChosenOneCharacter
import dev.sbytmacke.matrix.factories.FactoryGenericCharacter
import dev.sbytmacke.matrix.factories.FactoryInfectCharacter
import dev.sbytmacke.matrix.models.Character
import dev.sbytmacke.matrix.models.ChosenOneCharacter
import dev.sbytmacke.matrix.models.GenericCharacter
import dev.sbytmacke.matrix.models.InfectCharacter
import javafx.beans.property.SimpleObjectProperty
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

val STORAGE_INFECT_ELIMINATED = ArrayList<InfectCharacter>()

class SimulationViewModel {

    // El estado que estaremos compartiendo, con los datos deseados para pasar a la interfaz
    data class SimulationState(
        val operacion: String = "",
        val cuadrante: String = "",
        val tiempoActual: Int = 0,
        val generatedCharacters: Int = 0,
    )

    // Estado para favorecer la reactividad
    val stateSimulation: SimpleObjectProperty<SimulationState> = SimpleObjectProperty(SimulationState())

    //Mapa de la simulación
    private val sizeRow = 5
    private val sizeColumn = 5
    private var map: Array<Array<Character?>> = Array(sizeRow) { Array(sizeColumn) { null } }

    // Cola de los personajes que se irán introduciendo en el mapa
    private var queueGenericChar: ArrayDeque<GenericCharacter> = ArrayDeque()

    // Para el informe almacén de los personajes generados
    private val storageCharGenerated = ArrayList<Character>()

    // Iniciamos la simulación
    fun startSimulation() {
        logger.info { "Iniciando simulación" }

        printOperation("Iniciando simulación")

        // Instancio enemigos en mi Pila
        initStorageGenericCharacter()

        // Colocamos todos los personajes
        insertCharacters(map)

        // Inicio de la simulación
        simulationMatrix(map)

        printFinalInform(map)
    }

    /**
     * Creamos una cola donde introducimos los personajes genéricos en la última posición, para
     * posteriormente sacarlos de la primera posición que se han ido arrastrando hacia el índice 0
     */
    private fun initStorageGenericCharacter() {
        logger.debug { "Generando personajes genéricos" }

        for (i in 1..200) {
            queueGenericChar.add(FactoryGenericCharacter.generate())
        }
    }

    /**
     * Introducimos los objetos en el mapa
     * @param matrix es la matriz del mapa
     */
    private fun insertCharacters(matrix: Array<Array<Character?>>) {
        // Generamos de manera aleatoria a los dos personajes principales
        val neo: ChosenOneCharacter = FactoryChosenOneCharacter.generate()
        val smith: InfectCharacter = FactoryInfectCharacter.generate()

        // Repetimos hasta que se generen los dos personajes en distintas posiciones
        do {
            var count = 0
            for (i in 1..2) {
                if (i == 1) {
                    val randomRow = (0 until sizeRow).random()
                    val randomColumn = (0 until sizeColumn).random()
                    matrix[randomRow][randomColumn] = neo
                    // Actualizamos su localización
                    (matrix[randomRow][randomColumn] as ChosenOneCharacter).updateLocation(randomRow, randomColumn)
                    count++
                    storageCharGenerated.add(neo)
                    // Actualizamos el estado
                    updateCharactersGeneratedState(stateSimulation.value.generatedCharacters + 1)
                } else {
                    val randomRow = (0 until sizeRow).random()
                    val randomColumn = (0 until sizeColumn).random()
                    if (matrix[randomRow][randomColumn] !is ChosenOneCharacter) {
                        matrix[randomRow][randomColumn] = smith
                        // Actualizamos su localización
                        (matrix[randomRow][randomColumn] as InfectCharacter).updateLocation(randomRow, randomColumn)
                        count++
                        storageCharGenerated.add(smith)
                        // Actualizamos el estado
                        updateCharactersGeneratedState(stateSimulation.value.generatedCharacters + 1)
                    }
                }
            }
        } while (count != 2)

        // Introducimos el resto de personajes genéricos, por el mapa
        insertRestGenericCharactersToTheMap(matrix)
    }

    private fun insertRestGenericCharactersToTheMap(matrix: Array<Array<Character?>>) {
        for (i in matrix.indices) {
            for (j in 0 until matrix[i].size) {
                if (matrix[i][j] !is ChosenOneCharacter && matrix[i][j] !is InfectCharacter) {
                    storageCharGenerated.add(queueGenericChar.first())
                    matrix[i][j] = queueGenericChar.first()
                    // Y lo eliminamos del almacén
                    queueGenericChar.removeFirst()
                    // Actualizamos su localización
                    (matrix[i][j] as GenericCharacter).updateLocation(i, j)
                    // Actualizamos el estado
                    updateCharactersGeneratedState(stateSimulation.value.generatedCharacters + 1)
                }
            }
        }
    }

    // Actualiza e imprime en la interfaz, para que el usuario sepa qué ha ocurrido
    private fun printOperation(message: String) {
        stateSimulation.value = stateSimulation.value.copy(operacion = stateSimulation.value.operacion + "\n$message")
    }

    private fun printCuadrante() {
        stateSimulation.value = stateSimulation.value.copy(cuadrante = printMap(map))
    }

    private fun simulationMatrix(map: Array<Array<Character?>>) {
        var time = 0
        var count = 1

        do {
            time += 1000

            updateTimeState(time)

            if (time % 1000 == 0) {
                printOperation("Confirmando muerte de personaje:")

                checkAutoDead(map)

                logger.debug { queueGenericChar.size }
            }

            if (time % 2000 == 0) {
                printOperation("Acción de los infectados")

                actionInfect(map)
            }

            if (time % 5000 == 0) {
                printOperation("Añadiento infectados muertos en el almacén:")

                val newStorageVirus = actionNeo(map)
                STORAGE_INFECT_ELIMINATED.addAll(newStorageVirus)
            }

            if (time % 10000 == 0) {
                printOperation("Generando nuevos personajes genéricos:")

                setNewGeneric(map)
            }

            Thread.sleep(1000)

            printCuadrante()

            printOperation("=== ROUND $count (cada ronda es 1 segundo)===")
            printOperation("")

            count++
        } while (!checkAllInfect(map) && time != 500000)
    }

    private fun printFinalInform(map: Array<Array<Character?>>) {

        // Posición de NEO FINAL
        for (i in map.indices) {
            for (j in map.indices) {
                val chosenOne = map[i][j]
                if (chosenOne is ChosenOneCharacter) {
                    printOperation("Posición de Neo Final: (fila = ${i + 1}) (columnas = ${j + 1})")
                }
            }
        }

        printOperation("Informe Final Virus Eliminados:")
        STORAGE_INFECT_ELIMINATED.sortedByDescending { it.dateCreate }.forEach { printOperation(it.show()) }

        printOperation("Informe Final Personajes Generados en el sistema:")
        storageCharGenerated.sortedBy { it.id }.forEach {
            if (it is GenericCharacter) {
                printOperation(it.show())
            }
            if (it is InfectCharacter) {
                printOperation(it.show())
            }
            if (it is ChosenOneCharacter) {
                printOperation(it.show())
            }
        }
    }

    private fun setNewGeneric(map: Array<Array<Character?>>) {
        for (i in map.indices) {
            for (j in map.indices) {
                if (map[i][j] !is ChosenOneCharacter && map[i][j] !is InfectCharacter) {
                    map[i][j] = queueGenericChar.first()
                    storageCharGenerated.add(queueGenericChar.first())
                    queueGenericChar.removeFirst()
                }
            }
        }
    }

    /**
     * El personaje elegido decide si se cree ser el elegido no, si es TRUE, derrota a todos los agentes que infectan de su alrededor.
     * Siempre se moverá de manera aleatoria, si hay un personaje que infecte lo elimina, si hay un personaje genérico cambia de posición
     */
    private fun actionNeo(map: Array<Array<Character?>>): ArrayList<InfectCharacter> {
        val storageInfectEliminated = ArrayList<InfectCharacter>()
        var actualPositionRowNeo = 0
        var actualPositionColumnNeo = 0

        for (i in map.indices) {
            for (j in map.indices) {
                val chosenOne = map[i][j] as? ChosenOneCharacter
                if (chosenOne != null) {
                    actualPositionRowNeo = i
                    actualPositionColumnNeo = j

                    if (chosenOne.isChosenOne()) {
                        checkAndEliminateNeighbor(map, storageInfectEliminated, i - 1, j) // Top neighbor
                        checkAndEliminateNeighbor(map, storageInfectEliminated, i + 1, j) // Bottom neighbor
                        checkAndEliminateNeighbor(map, storageInfectEliminated, i, j - 1) // Left neighbor
                        checkAndEliminateNeighbor(map, storageInfectEliminated, i, j + 1) // Right neighbor
                        checkAndEliminateNeighbor(map, storageInfectEliminated, i - 1, j - 1) // Top-left neighbor
                        checkAndEliminateNeighbor(map, storageInfectEliminated, i - 1, j + 1) // Top-right neighbor
                        checkAndEliminateNeighbor(map, storageInfectEliminated, i + 1, j - 1) // Bottom-left neighbor
                        checkAndEliminateNeighbor(map, storageInfectEliminated, i + 1, j + 1) // Bottom-right neighbor
                    }

                    moveChosenOneToRandomPosition(map, chosenOne, actualPositionRowNeo, actualPositionColumnNeo)
                }
            }
        }
        return storageInfectEliminated
    }

    private fun checkAndEliminateNeighbor(
        map: Array<Array<Character?>>,
        storageInfectEliminated: ArrayList<InfectCharacter>,
        row: Int,
        column: Int,
    ) {
        if (isValidPosition(map, row, column)) {
            val neighbor = map[row][column]
            if (neighbor is InfectCharacter) {
                storageInfectEliminated.add(neighbor)
                map[row][column] = null
            }
        }
    }

    private fun isValidPosition(map: Array<Array<Character?>>, row: Int, column: Int): Boolean {
        val size = map.size
        return row in 0 until size && column in 0 until size
    }

    private fun moveChosenOneToRandomPosition(
        map: Array<Array<Character?>>,
        chosenOne: ChosenOneCharacter,
        currentRow: Int,
        currentColumn: Int,
    ) {
        val randomRow = (map.indices).random()
        val randomColumn = (0 until map[0].size).random()

        if (map[randomRow][randomColumn] is InfectCharacter) {
            val infectedChar = map[randomRow][randomColumn] as InfectCharacter
            STORAGE_INFECT_ELIMINATED.add(infectedChar)
            map[randomRow][randomColumn] = chosenOne
            map[currentRow][currentColumn] = null
        }

        if (map[randomRow][randomColumn] is GenericCharacter) {
            val genericChar = map[randomRow][randomColumn]
            val chosenChar = map[currentRow][currentColumn]
            map[randomRow][randomColumn] = chosenChar
            map[currentRow][currentColumn] = genericChar
        }
    }

    /**
     * Actuarán aquellos que sean objetos que infecten, en función de la capacidad de infección que tenga, es decir
     * es el número de objetos alrededor del que infecta pueden ser infectados
     * En caso de que se infecten todos alrededor, ya no infecta más (al personaje ChosenOne no lo infecta)
     * @param map donde se efectuará el cambio
     */
    private fun actionInfect(map: Array<Array<Character?>>) {
        val maxCharInfectToAction = countInfects(map)
        var countInfect = 0

        for (i in map.indices) {
            for (j in map.indices) {
                val infectChar = map[i][j] as? InfectCharacter
                if (infectChar != null && countInfect < maxCharInfectToAction) {
                    countInfect++
                    var infectedCount = 0

                    infectedCount += infectNeighbor(map, infectChar, i - 1, j) // Top neighbor
                    infectedCount += infectNeighbor(map, infectChar, i + 1, j) // Bottom neighbor
                    infectedCount += infectNeighbor(map, infectChar, i, j - 1) // Left neighbor
                    infectedCount += infectNeighbor(map, infectChar, i, j + 1) // Right neighbor
                    infectedCount += infectNeighbor(map, infectChar, i - 1, j - 1) // Top-left neighbor
                    infectedCount += infectNeighbor(map, infectChar, i - 1, j + 1) // Top-right neighbor
                    infectedCount += infectNeighbor(map, infectChar, i + 1, j - 1) // Bottom-left neighbor
                    infectedCount += infectNeighbor(map, infectChar, i + 1, j + 1) // Bottom-right neighbor

                    if (infectedCount < infectChar.capacityInfect) {
                        continue
                    }
                }
            }
        }
    }

    private fun infectNeighbor(
        map: Array<Array<Character?>>,
        infectChar: InfectCharacter,
        row: Int,
        column: Int,
    ): Int {
        if (isValidPosition(map, row, column)) {
            val neighbor = map[row][column] as? GenericCharacter
            if (neighbor != null) {
                printOperation("Se infecta en, fila: $row columna: $column")
                map[row][column] = infectChar.infect(neighbor)
                return 1
            }
        }
        return 0
    }

    /**
     * Contador de personajes que infectan para que solo actúen los que existen en una ronda
     */
    private fun countInfects(map: Array<Array<Character?>>): Int {
        var countCharInfect = 0
        for (element in map) {
            for (j in map.indices) {
                if (element[j] is InfectCharacter) {
                    printOperation("Infectados contador: $countCharInfect ")
                    countCharInfect++
                }
            }
        }
        return countCharInfect
    }

    /**
     * Vemos si un personaje genérico tiene menos de 30% de posibilidades de morir, si es así muere y se reemplaza por otro
     * de nuestro almacén de 200 personajes genéricos que simulan una cola
     * @param map el mapa donde cambiaremos el personaje
     */
    private fun checkAutoDead(map: Array<Array<Character?>>) {
        for (i in 0 until map.size - 1) {
            for (j in 0 until map[i].size - 1) {
                if (map[i][j] is GenericCharacter && (map[i][j] as GenericCharacter).probabilityAutoDead > 30) {
                    if (queueGenericChar.isEmpty()) {
                        break
                    } else {
                        printOperation("Se muere un personaje genérico en fila: $i y columnas $j, y generamos otro nuevo...")
                        // Sustituimos de nuestro almacén de cola
                        map[i][j] = queueGenericChar.first()
                        // Actualizamos el estado
                        updateCharactersGeneratedState(stateSimulation.value.generatedCharacters + 1)
                        // Y eliminamos de la cola
                        queueGenericChar.removeFirst()
                    }
                } else if (map[i][j] is GenericCharacter) {
                    (map[i][j] as GenericCharacter).probabilityAutoDead -= 10
                }
            }
        }
        printOperation("\n")
    }

    private fun updateCharactersGeneratedState(charactersGenerated: Int) {
        stateSimulation.value = stateSimulation.value.copy(generatedCharacters = charactersGenerated)
    }

    private fun updateTimeState(tiempo: Int) {
        stateSimulation.value = stateSimulation.value.copy(tiempoActual = tiempo)
    }

    /**
     * Comprobamos que todos sean personajes infectados
     */
    private fun checkAllInfect(map: Array<Array<Character?>>): Boolean {
        val countMaxNoInfect = 1
        var countNoInfect = 0

        for (i in map.indices) {
            for (j in map.indices) {
                if (map[i][j] is ChosenOneCharacter || map[i][j] is GenericCharacter) {
                    countNoInfect++
                }
            }
        }

        if (countNoInfect == countMaxNoInfect) {
            printOperation("HAS PERDIDO, todos están infectados!")
            return true
        }
        return false
    }

    /**
     * Impresión en consola para tener un historial visual
     */
    private fun printMap(matrix: Array<Array<Character?>>): String {
        val sb = StringBuilder()
        for (i in matrix.indices) {
            for (j in 0 until matrix[i].size) {
                if (j == matrix[i].size - 1) {
                    // Salto de linea
                    when (matrix[i][j]) {
                        is ChosenOneCharacter -> sb.append("\uD83D\uDE07" + "\n")
                        is InfectCharacter -> sb.append("\uD83D\uDE08" + "\n")
                        is GenericCharacter -> sb.append("\uD83D\uDE36" + "\n")
                        else -> sb.append("\uD83D\uDCA4" + "\n")
                    }
                } else {
                    when (matrix[i][j]) {
                        is ChosenOneCharacter -> sb.append("\uD83D\uDE07" + " ")
                        is InfectCharacter -> sb.append("\uD83D\uDE08" + " ")
                        is GenericCharacter -> sb.append("\uD83D\uDE36" + " ")
                        else -> sb.append("\uD83D\uDCA4" + " ")
                    }
                }
            }
        }

        sb.append("----------------------------------")
        sb.append("LEYENDA: \nNeo -> (\uD83D\uDE07) \nSmith -> (\uD83D\uDE08) \nGenerico -> (\uD83D\uDE36) \nVacío -> (\uD83D\uDCA4)")

        return sb.toString()
    }
}