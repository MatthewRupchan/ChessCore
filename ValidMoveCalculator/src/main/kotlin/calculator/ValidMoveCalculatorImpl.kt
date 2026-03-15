package calculator

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ValidMoveCalculatorImpl : ValidMoveCalculator {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun getValidMoves(board: Board, moveHistory: List<Move>): Set<Move> {
        // TODO use coroutines
        val lastMove = moveHistory.lastOrNull()
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
                PieceType.KING -> ValidKingMoveCalculator.getValidMoves(piece, board, moveHistory)
                PieceType.QUEEN ->  ValidQueenMoveCalculator.getValidMoves(piece, board)
            }
        }.toSet()
    }
}