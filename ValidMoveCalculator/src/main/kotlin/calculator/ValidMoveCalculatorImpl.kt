package calculator

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ValidMoveCalculatorImpl : ValidMoveCalculator {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun getValidMoves(board: Board, lastMove: Move?): Set<Move> {
        // TODO use coroutines
        val turn = lastMove?.to?.colour?.opposite() ?: Colour.WHITE
        logger.info("$turn to move")
        return board.pieces.filter {
            it.colour == turn
        }.flatMap { piece ->
            when(piece.pieceType) {
                PieceType.PAWN -> ValidPawnMoveCalculator.getValidMoves(piece, board, lastMove)
                PieceType.ROOK -> setOf()
                PieceType.KNIGHT ->  setOf()
                PieceType.BISHOP ->  setOf()
                PieceType.KING ->  setOf()
                PieceType.QUEEN ->  setOf()
            }
        }.toSet()
    }
}