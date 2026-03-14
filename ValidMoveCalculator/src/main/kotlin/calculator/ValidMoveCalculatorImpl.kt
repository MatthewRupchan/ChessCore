package calculator

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ValidMoveCalculatorImpl : ValidMoveCalculator {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun getValidMoves(board: Board, lastMove: Move?): Set<Move> {
        // TODO use coroutines
        board.pieces.flatMap { piece ->
            when(piece.pieceType) {
                PieceType.PAWN -> movePawn(piece, board, lastMove)
                PieceType.ROOK -> setOf()
                PieceType.KNIGHT ->  setOf()
                PieceType.BISHOP ->  setOf()
                PieceType.KING ->  setOf()
                PieceType.QUEEN ->  setOf()
            }
        }
        return setOf()
    }

    private fun movePawn(pawn: Piece, board: Board, lastMove: Move?): List<Move> {
        if (pawn.pieceType != PieceType.PAWN) {
            logger.error("Asked to move $pawn like a pawn when it isn't a pawn")
            return listOf()
        }

        // move correct direction for the colour
        // move 2 at home square
        // en passant
        // promotions
        // captures, but only enemy pieces
        return listOf()
    }
}