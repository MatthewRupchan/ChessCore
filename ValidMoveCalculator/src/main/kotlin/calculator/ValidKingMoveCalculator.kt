package calculator

import org.slf4j.Logger
import org.slf4j.LoggerFactory

object ValidKingMoveCalculator {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    fun getValidMoves(king: Piece, board: Board, moveHistory: List<Move>): Set<Move> {
        if (king.pieceType != PieceType.KING) {
            logger.error("Asked to move $king like a King when it isn't a King")
            return setOf()
        }
        return buildSet {
            val mySquares = getControlledSquares(king)
            val opponentsSquares = getOpponentControlledSquares(king, board)

            addAll(mySquares.filter {
                board.isPieceAt(it)?.colour?.let { c -> c != king.colour } ?: true
            }.filter {
                it !in opponentsSquares
            }.map {
                Move(from = king, to = king.atLocation(it), capture = board.isPieceAt(it))
            })


            // and not in check
            val noKingMovesMadeYet = noKingMovesMade(king, moveHistory) && (king.location !in opponentsSquares)

            if (noKingMovesMadeYet && kingRookCanCastle(king, board, moveHistory)) {
                val transferSpaces = if (king.colour == Colour.WHITE) {
                    listOf(Location(Rank(1), File.F), Location(Rank(1), File.G))
                } else {
                    listOf(Location(Rank(8), File.F), Location(Rank(8), File.G))
                }

                val noPiecesInTheWay = board.pieces.none { it.location in transferSpaces }
                val noDanger = opponentsSquares.none { it in transferSpaces }

                if (noPiecesInTheWay && noDanger) {
                    add(Move(
                        froms=listOf(king, getRookForColour(king, File.H)),
                        tos=listOf(king.atLocation(Location(king.location.rank, File.G)), getRookForColour(king, File.F))))
                }
            }

            if (noKingMovesMadeYet && queenRookCanCastle(king, board, moveHistory)) {
                val transferSpaces = if (king.colour == Colour.WHITE) {
                    listOf(Location(Rank(1), File.B), Location(Rank(1), File.C), Location(Rank(1), File.D))
                } else {
                    listOf(Location(Rank(8), File.B), Location(Rank(8), File.C), Location(Rank(8), File.D))
                }

                val noPiecesInTheWay = board.pieces.none { it.location in transferSpaces }
                val noDanger = opponentsSquares.none { it in transferSpaces }

                if (noPiecesInTheWay && noDanger) {
                    add(Move(
                        froms=listOf(king, getRookForColour(king, File.A)),
                        tos=listOf(king.atLocation(Location(king.location.rank, File.C)), getRookForColour(king, File.D))))
                }
            }
        }
    }

    fun getControlledSquares(king: Piece): List<Location> {
        return buildList {
            add(king.location + Pair(1,1))
            add(king.location + Pair(1,-1))
            add(king.location + Pair(-1,1))
            add(king.location + Pair(-1,-1))
            add(king.location + Pair(1,0))
            add(king.location + Pair(0,1))
            add(king.location + Pair(-1,0))
            add(king.location + Pair(0,-1))
        }.filterNotNull()
    }

    private fun getOpponentControlledSquares(king: Piece, board: Board): List<Location> {
       val boardMinusMe = Board(board.pieces.minus(king))
        return board.pieces.filter { it.colour == king.colour.opposite() }.flatMap {
            when(it.pieceType) {
                PieceType.PAWN -> ValidPawnMoveCalculator.getControlledSquares(it)
                PieceType.ROOK -> ValidRookMoveCalculator.getControlledSquares(it, boardMinusMe)
                PieceType.KNIGHT -> ValidKnightMoveCalculator.getControlledSquares(it)
                PieceType.BISHOP -> ValidBishopMoveCalculator.getControlledSquares(it, boardMinusMe)
                PieceType.KING -> ValidKingMoveCalculator.getControlledSquares(it)
                PieceType.QUEEN -> ValidQueenMoveCalculator.getControlledSquares(it, boardMinusMe)
            }
        }
    }

    private fun noKingMovesMade(king: Piece, moveHistory: List<Move>) = moveHistory.none {
        it.from.pieceType == PieceType.KING && it.from.colour == king.colour
    }

    private fun kingRookCanCastle(king: Piece, board: Board, moveHistory: List<Move>) = moveHistory.none {
        it.from == getRookForColour(king, File.H)
    } && board.pieces.any { it == getRookForColour(king, File.H)}

    private fun queenRookCanCastle(king: Piece, board: Board, moveHistory: List<Move>) = moveHistory.none {
            it.from == getRookForColour(king, File.A)
    } && board.pieces.any { it == getRookForColour(king, File.A)}

    private fun getRookForColour(king: Piece, file: File): Piece =
        if (king.colour == Colour.WHITE) {
            Piece.whiteRook(Location(Rank(1), file))
        } else {
            Piece.blackRook(Location(Rank(8), file))
        }
}