package dev.sbytmacke.thechinos.viewmodels

import dev.sbytmacke.thechinos.checks.BallCheck
import dev.sbytmacke.thechinos.states.AppState

class BallGameViewModel {

    // Comprobamos, si empatamos, ganamos o perdemos la ronda
    fun isAcertador(decisionBalls: Int, ballsPlayer: Int): BallCheck {
        val randomBallsPosesionIA = (3..7).random()
        val decisionIAtoGuessBallsOfpLAYER = (3..7).random()

        return when {
            decisionIAtoGuessBallsOfpLAYER == ballsPlayer && decisionBalls == randomBallsPosesionIA -> {
                BallCheck.Empate()
            }

            decisionIAtoGuessBallsOfpLAYER == ballsPlayer -> {
                // Aumentamos el contador de la IA ganadora
                AppState.countIAWins.value++
                BallCheck.IAWin()
            }

            decisionBalls == randomBallsPosesionIA -> {
                // Aumentamos el contador del jugador ganador
                AppState.countPlayerWins.value++
                BallCheck.PlayerWin()
            }

            else -> {
                BallCheck.NoWinner()
            }
        }
    }
}