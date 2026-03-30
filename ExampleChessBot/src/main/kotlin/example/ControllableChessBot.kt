package example

import bot.ChessBot
import calculator.Board
import calculator.Move
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ControllableChessBot : ChessBot {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun getNextMove(validMoves: Set<Move>, board: Board, moveHistory: List<Move>): Move {

        println("Choose a move:")
        validMoves.groupBy { it.from.pieceType }.forEach { (_, moves) ->
            println(moves.joinToString(", ") { it.getNotation() })
        }
        println("Opps last move: ${moveHistory.lastOrNull()?.getNotation()}")

        var result: Move? = null
        while(result == null) {
            println("Pick:")
           val input = readln()
            if (input == "dump") {
                board.pieces.forEach {
                    // Gives a copy-pasteable string so you can load up a board easily
                    println(it.getDebugString())
                }
            } else {
                result = validMoves.firstOrNull { it.getNotation() == input }
            }
        }
        return result
    }
}
