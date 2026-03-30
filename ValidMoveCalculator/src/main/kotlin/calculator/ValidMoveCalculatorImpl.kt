package calculator

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.Executors

class ValidMoveCalculatorImpl : ValidMoveCalculator {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)
    private val scope = CoroutineScope(Executors.newFixedThreadPool(4).asCoroutineDispatcher())

    override fun getValidMoves(board: Board, moveHistory: List<Move>, checkChecks: Boolean, moveAs: Colour?): Set<Move> {
        val lastMove = moveHistory.lastOrNull()
        val turn = moveAs ?: lastMove?.to?.colour?.opposite() ?: Colour.WHITE
        logger.debug("$turn to move")
        return getValidMovesInternal(board, moveHistory, turn, checkChecks)
    }

    private fun getValidMovesInternal(board: Board, moveHistory: List<Move>, turn: Colour, checkChecks: Boolean): Set<Move> {
        val lastMove = moveHistory.lastOrNull()
        return board.pieces.filter {
            it.colour == turn
        }.map { piece ->
            scope.async {
                when(piece.pieceType) {
                    PieceType.PAWN -> ValidPawnMoveCalculator.getValidMoves(piece, board, lastMove)
                    PieceType.ROOK -> ValidRookMoveCalculator.getValidMoves(piece, board)
                    PieceType.KNIGHT ->  ValidKnightMoveCalculator.getValidMoves(piece, board)
                    PieceType.BISHOP ->  ValidBishopMoveCalculator.getValidMoves(piece, board)
                    PieceType.KING -> ValidKingMoveCalculator.getValidMoves(piece, board, moveHistory)
                    PieceType.QUEEN ->  ValidQueenMoveCalculator.getValidMoves(piece, board)
                }
            }
        }.flatMap {
            runBlocking {
                it.await()
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

    override fun isCheckmate(board: Board, moveHistory: List<Move>, moveAs: Colour?): Boolean {
        val attackingColour = moveAs ?: moveHistory.lastOrNull()?.from?.colour ?: Colour.BLACK
        val possibleMoves = getValidMoves(board, moveHistory, true)

        val inCheck = getValidMoves(board, moveHistory, false, attackingColour).any { it.captures?.pieceType == PieceType.KING }

        if (inCheck) {
            return possibleMoves.all { move ->
                board.applyMove(move).let { foesNextTurnBoard ->
                    getValidMoves(foesNextTurnBoard, moveHistory.plus(move), false, attackingColour).any {
                        it.captures?.pieceType == PieceType.KING
                    }
                }
            }
        }
        return false
    }
}