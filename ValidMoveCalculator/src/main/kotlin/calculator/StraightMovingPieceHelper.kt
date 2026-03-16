package calculator

import org.slf4j.Logger
import org.slf4j.LoggerFactory


object StraightMovingPieceHelper {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    fun getMovesInDirection(piece: Piece, board: Board, incrementLocation: (Location) -> Location?) = buildSet {
        var location: Location? = piece.location
        do {
            location = incrementLocation(location!!)
            if (location == null) {
                break
            }
            val targetPiece = board.isPieceAt(location)

            when {
                targetPiece == null -> {
                    logger.debug("${piece.pieceType} can move to $location")
                    add(Move(from=piece, to=piece.atLocation(location)))
                }
                targetPiece.colour == piece.colour -> {
                    break;
                }
                targetPiece.colour != piece.colour -> {
                    logger.debug("${piece.pieceType} can capture $targetPiece")
                    add(Move(from=piece, to=piece.atLocation(location), capture = targetPiece))
                    break;
                }
            }
        } while(true)
    }

    fun getFakeBoardForControlledSquares(piece: Piece, board: Board): Board {
        return Board(board.pieces.map {
            if (it.colour == piece.colour) {
                Piece(it.colour.opposite(), it.pieceType, it.location)
            } else {
                it
            }
        })
    }
}