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
                    logger.info("${piece.pieceType} can move to $location")
                    add(Move(from=piece, to=piece.atLocation(location)))
                }
                targetPiece.colour == piece.colour -> {
                    break;
                }
                targetPiece.colour != piece.colour -> {
                    logger.info("${piece.pieceType} can capture $targetPiece")
                    add(Move(from=piece, to=piece.atLocation(location), capture = targetPiece))
                    break;
                }
            }
        } while(true)
    }
}