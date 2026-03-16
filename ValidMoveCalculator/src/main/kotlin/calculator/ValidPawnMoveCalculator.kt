package calculator

import org.slf4j.Logger
import org.slf4j.LoggerFactory

object ValidPawnMoveCalculator {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    fun getValidMoves(pawn: Piece, board: Board, lastMove: Move?): Set<Move> {
        if (pawn.pieceType != PieceType.PAWN) {
            logger.error("Asked to move $pawn like a pawn when it isn't a pawn")
            return setOf()
        }
        return buildSet {
            val nextRank = when(pawn.colour) {
                Colour.WHITE -> Rank(pawn.location.rank.value + 1)
                Colour.BLACK -> Rank(pawn.location.rank.value - 1)
            }

            if (isEnPassantPossible(pawn, lastMove)) {
                logger.debug("Pawn can en-passant")
                add(Move(from=pawn, to=pawn.atLocation(Location(nextRank, lastMove!!.to.location.file)), capture= lastMove.to))
            }

            addAll(getForwardMove(pawn, board))

            addAll(getCaptures(pawn, board))
        }
    }

    fun getControlledSquares(pawn: Piece): List<Location> {
        val nextRank = getNextRank(pawn)
        val captureLeft = pawn.location.file.leftOne()?.let { Location(nextRank, it) }
        val captureRight = pawn.location.file.rightOne()?.let { Location(nextRank, it) }
        return listOfNotNull(captureLeft, captureRight)
    }

    private fun getNextRank(pawn: Piece) = when(pawn.colour) {
            Colour.WHITE -> Rank(pawn.location.rank.value + 1)
            Colour.BLACK -> Rank(pawn.location.rank.value - 1)
        }

    private fun isEnPassantPossible(pawn: Piece, lastMove: Move?): Boolean {
        if (lastMove == null) return false
        if (pawn.colour == Colour.WHITE && pawn.location.rank.value != 5) return false
        if (pawn.colour == Colour.BLACK && pawn.location.rank.value != 4) return false
        if (lastMove.to.pieceType != PieceType.PAWN) return false
        if (lastMove.to.colour == pawn.colour) return false
        if (lastMove.from.colour == Colour.WHITE && lastMove.from.location.rank.value != 2) return false
        if (lastMove.from.colour == Colour.BLACK && lastMove.from.location.rank.value != 7) return false
        if (lastMove.to.colour == Colour.WHITE && lastMove.to.location.rank.value != 4) return false
        if (lastMove.to.colour == Colour.BLACK && lastMove.to.location.rank.value != 5) return false
        if (lastMove.to.location.file !in listOf(pawn.location.file.leftOne(), pawn.location.file.rightOne())) return false
        return true
    }

    private fun getForwardMove(pawn: Piece, board: Board):Set<Move> = buildSet {
        val oneStepAhead = Location(getNextRank(pawn), pawn.location.file)
        if (board.isPieceAt(oneStepAhead) == null) {
            if (isNearPromoting(pawn)) {
                logger.debug("Pawn can step into promotion")
                addAll(getPossiblePromotions(oneStepAhead, pawn.colour).map { Move(from=pawn, to=it) })
            } else {
                logger.debug("Pawn can step forward")
                add(Move(from=pawn, to=pawn.atLocation(oneStepAhead)))
                val jumpRank = when(pawn.colour) {
                    Colour.WHITE -> Rank(4)
                    Colour.BLACK -> Rank(5)
                }
                val twoStepsAhead = Location(jumpRank, pawn.location.file)
                if (isOnHomeRank(pawn) && board.isPieceAt(twoStepsAhead) == null) {
                    logger.debug("Pawn can take two steps forward")
                    add(Move(from=pawn, to=pawn.atLocation(twoStepsAhead)))
                }
            }
        }
    }

    private fun getCaptures(pawn: Piece, board: Board) = buildSet {
        val captureSquares = getControlledSquares(pawn)

        if (isNearPromoting(pawn)) {
            captureSquares.forEach { captureSquare ->
                getPawnCapture(captureSquare, board, pawn)?.let { capturablePiece ->
                    logger.debug("Pawn can promote through capturing $capturablePiece")
                    addAll(getPossiblePromotions(capturablePiece.location, pawn.colour).map { Move(from=pawn, to=it, capture=capturablePiece) })
                }
            }
        } else {
            captureSquares.forEach { captureSquare ->
                getPawnCapture(captureSquare, board, pawn)?.let { capturablePiece ->
                    logger.debug("Pawn can capture $capturablePiece")
                    add(Move(from=pawn, to=pawn.atLocation(capturablePiece.location), capture = capturablePiece))
                }
            }
        }
    }

    private fun isNearPromoting(pawn: Piece): Boolean {
        return when(pawn.colour) {
            Colour.WHITE -> pawn.location.rank.value == 7
            Colour.BLACK -> pawn.location.rank.value == 2
        }
    }

    private fun isOnHomeRank(pawn: Piece): Boolean {
        return when(pawn.colour) {
            Colour.WHITE -> pawn.location.rank.value == 2
            Colour.BLACK -> pawn.location.rank.value == 7
        }
    }

    private fun getPossiblePromotions(location: Location, colour: Colour): Set<Piece> {
        return setOf(
            Piece(colour, PieceType.BISHOP, location),
            Piece(colour, PieceType.ROOK, location),
            Piece(colour, PieceType.QUEEN, location),
            Piece(colour, PieceType.KNIGHT, location),
        )
    }

    private fun getPawnCapture(targetSquare: Location?, board: Board, sourcePawn: Piece): Piece? {
        return targetSquare?.let { board.isPieceAt(it) }?.takeIf { it.colour != sourcePawn.colour }
    }
}