package manager.application

import calculator.ValidMoveCalculatorImpl
import manager.flow.GameFlow

fun main() {
    GameFlow(ValidMoveCalculatorImpl()).startGame()
}