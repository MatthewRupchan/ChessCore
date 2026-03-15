package calculator

import calculator.StraightMovingPieceHelper.getFakeBoardForControlledSquares
import calculator.StraightMovingPieceHelper.getMovesInDirection
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object ValidRookMoveCalculator {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    fun getValidMoves(rook: Piece, board: Board): Set<Move> {
        if (rook.pieceType != PieceType.ROOK && rook.pieceType != PieceType.QUEEN) {
            logger.error("Asked to move $rook like a rook when it isn't a rook")
            return setOf()
        }
        return buildSet {
            addAll(getMovesInDirection(rook, board) { loc ->
                (loc.rank + 1)?.let { Location(it, loc.file) }
            })
            addAll(getMovesInDirection(rook, board) { loc ->
                (loc.rank - 1)?.let { Location(it, loc.file) }
            })
            addAll(getMovesInDirection(rook, board) { loc ->
                (loc.file + 1)?.let { Location(loc.rank, it) }
            })
            addAll(getMovesInDirection(rook, board) { loc ->
                (loc.file - 1)?.let { Location(loc.rank, it) }
            })
        }
    }

    fun getControlledSquares(rook: Piece, board: Board): List<Location> {
        return getValidMoves(rook, getFakeBoardForControlledSquares(rook, board)).map { it.to.location }
    }
}