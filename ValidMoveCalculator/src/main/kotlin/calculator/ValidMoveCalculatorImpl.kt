package calculator

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ValidMoveCalculatorImpl : ValidMoveCalculator {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    // TODO probably need the whole move history, not just last move, to verify castling
    override fun getValidMoves(board: Board, lastMove: Move?): Set<Move> {
        // TODO use coroutines
        val turn = lastMove?.to?.colour?.opposite() ?: Colour.WHITE
        logger.info("$turn to move")
        return board.pieces.filter {
            it.colour == turn
        }.flatMap { piece ->
            when(piece.pieceType) {
                PieceType.PAWN -> ValidPawnMoveCalculator.getValidMoves(piece, board, lastMove)
                PieceType.ROOK -> ValidRookMoveCalculator.getValidMoves(piece, board)
                PieceType.KNIGHT ->  ValidKnightMoveCalculator.getValidMoves(piece, board)
                PieceType.BISHOP ->  ValidBishopMoveCalculator.getValidMoves(piece, board)
                PieceType.KING ->  setOf()
                PieceType.QUEEN ->  ValidQueenMoveCalculator.getValidMoves(piece, board)
            }
        }.toSet()
    }
}