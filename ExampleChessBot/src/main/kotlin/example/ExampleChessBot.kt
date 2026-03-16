package example

import bot.ChessBot
import calculator.Board
import calculator.Move
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ExampleChessBot : ChessBot {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun getNextMove(validMoves: Set<Move>, board: Board, moveHistory: List<Move>): Move {
        logger.debug("Attempting to capture.")
        return validMoves.firstOrNull { it.captures != null } ?: validMoves.first()
    }
}
