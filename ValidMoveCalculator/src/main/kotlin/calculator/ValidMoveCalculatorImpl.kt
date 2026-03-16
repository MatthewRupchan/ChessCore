package calculator

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ValidMoveCalculatorImpl : ValidMoveCalculator {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun getValidMoves(board: Board, moveHistory: List<Move>, checkChecks: Boolean, moveAs: Colour?): Set<Move> {
        // TODO use coroutines
        // TODO we must prevent move that allow you to hang a king!!
        val lastMove = moveHistory.lastOrNull()
        val turn = moveAs ?: lastMove?.to?.colour?.opposite() ?: Colour.WHITE
        logger.debug("$turn to move")
        return getValidMovesInternal(board, moveHistory, turn, checkChecks)
    }

    private fun getValidMovesInternal(board: Board, moveHistory: List<Move>, turn: Colour, checkChecks: Boolean): Set<Move> {
        // TODO use coroutines
        val lastMove = moveHistory.lastOrNull()
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
        }.filter { move ->
            if (checkChecks) {
                val newState = board.applyMove(move)
                getValidMovesInternal(newState, moveHistory.plus(move), turn.opposite(), false).none {
                    it.captures?.pieceType == PieceType.KING
                }
            } else {
                true
            }
        }.toSet()
    }
}