package dev.sbytmacke.thechinos.checks

sealed class BallCheck {
    class IAWin : BallCheck() {
        override fun message() = "Ha ganado la IA esta ronda"
    }

    class PlayerWin : BallCheck() {
        override fun message() = "Has ganado esta ronda!"
    }

    class Empate : BallCheck() {
        override fun message() = "Empate, se vuelve a jugar la ronda"
    }

    class NoWinner : BallCheck() {
        override fun message() = "Nadie ha ganado"
    }

    abstract fun message(): String
}