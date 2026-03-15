package calculator

import org.slf4j.Logger
import org.slf4j.LoggerFactory

object ValidQueenMoveCalculator {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    fun getValidMoves(queen: Piece, board: Board): Set<Move> {
        if (queen.pieceType != PieceType.QUEEN) {
            logger.error("Asked to move $queen like a queen when it isn't a queen")
            return setOf()
        }
        return buildSet {
            addAll(ValidRookMoveCalculator.getValidMoves(queen, board))
            addAll(ValidBishopMoveCalculator.getValidMoves(queen, board))
        }
    }
}