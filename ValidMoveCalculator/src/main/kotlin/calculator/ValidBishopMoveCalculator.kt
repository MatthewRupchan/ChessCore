package calculator

import calculator.StraightMovingPieceHelper.getMovesInDirection
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object ValidBishopMoveCalculator {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    fun getValidMoves(bishop: Piece, board: Board): Set<Move> {
        if (bishop.pieceType != PieceType.BISHOP && bishop.pieceType != PieceType.QUEEN) {
            logger.error("Asked to move $bishop like a bishop when it isn't a bishop")
            return setOf()
        }
        return buildSet {
            addAll(getMovesInDirection(bishop, board) { loc ->
                (loc.rank + 1)?.let { r ->
                    (loc.file + 1)?.let { f -> Location(r, f) }
                }
            })
            addAll(getMovesInDirection(bishop, board) { loc ->
                (loc.rank + 1)?.let { r ->
                    (loc.file - 1)?.let { f -> Location(r, f) }
                }
            })
            addAll(getMovesInDirection(bishop, board) { loc ->
                (loc.rank - 1)?.let { r ->
                    (loc.file + 1)?.let { f -> Location(r, f) }
                }
            })
            addAll(getMovesInDirection(bishop, board) { loc ->
                (loc.rank - 1)?.let { r ->
                    (loc.file - 1)?.let { f -> Location(r, f) }
                }
            })
        }
    }
}