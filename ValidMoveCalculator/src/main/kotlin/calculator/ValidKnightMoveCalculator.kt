package calculator

import org.slf4j.Logger
import org.slf4j.LoggerFactory

object ValidKnightMoveCalculator {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    fun getValidMoves(knight: Piece, board: Board): Set<Move> {
        if (knight.pieceType != PieceType.KNIGHT) {
            logger.error("Asked to move $knight like a knight when it isn't a knight")
            return setOf()
        }
        return buildSet {
            getKnightLocations(knight).forEach { location ->
                val target = board.isPieceAt(location)
                when {
                    target == null -> {
                        logger.info("Knight moving to $location")
                        add(Move(from=knight, to=knight.atLocation(location)))
                    }
                    target.colour != knight.colour -> {
                        logger.info("Knight capturing $target")
                        add(Move(from = knight, to = knight.atLocation(location), capture = target))
                    }
                }
            }
        }
    }

    fun getKnightLocations(knight: Piece): List<Location> {
        return listOf(
            Pair(2, 1),
            Pair(2, -1),
            Pair(-2, 1),
            Pair(-2, -1),
            Pair(1, 2),
            Pair(1, -2),
            Pair(-1, 2),
            Pair(-1, -2),
        ).mapNotNull { (rank, file) ->
            (knight.location.rank + rank)?.let { r ->
                (knight.location.file + file)?.let { f ->
                    Location(r,f)
                }
            }
        }
    }
}