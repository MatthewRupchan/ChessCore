package basic

import bot.ChessBot
import calculator.Board
import calculator.Colour
import calculator.Move
import calculator.ValidMoveCalculatorImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.Executors

class FirstChessBot : ChessBot {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    private val calc = ValidMoveCalculatorImpl()

    private val scope = CoroutineScope(Executors.newFixedThreadPool(16).asCoroutineDispatcher())

    override fun getNextMove(validMoves: Set<Move>, board: Board, moveHistory: List<Move>): Move {
        // logger.info("MMM getting move on $board")
        val myColour = validMoves.first().from.colour
        // logger.info("MMM I'm $myColour")
        return getNextMoveRecursive(board, validMoves, moveHistory, 0, myColour).first
    }

    private fun getNextMoveRecursive(board: Board, validMoves: Set<Move>, moveHistory: List<Move>, depth: Int, myColour: Colour): Pair<Move, Int?> {
        val maxDepth = when {
            board.pieces.size == 32 -> 2
            board.pieces.size < 10 -> 0
            else -> 3
        }
        val ourMovesToLookAt = when {
            board.pieces.size == 32 -> 9
            board.pieces.size < 10 -> 9
            else -> 4
        }
        val theirMovesToLookAt = when {
            board.pieces.size == 32 -> 1
            board.pieces.size < 10 -> 2
            else -> 2
        }
        // logger.info("MMM depth $depth, for max: $maxDepth ours: $ourMovesToLookAt theirs: $theirMovesToLookAt")


        val bestMoves1 = validMoves.sortedByDescending { score(board.applyMove(it), myColour) }.take(ourMovesToLookAt)
        

        // logger.info("Best moves: $bestMoves1")

        return bestMoves1.associateWith { move ->
                val newBoard = board.applyMove(move)
                val newHistory = moveHistory.plus(move)
                val oppsValidMoves = calc.getValidMoves(newBoard, newHistory, true).sortedBy {
                    score(newBoard.applyMove(it), myColour)
                }
                if (oppsValidMoves.isEmpty()) {
                   9999
                } else {
                    // logger.info("$move maps to $oppsValidMoves")
                    oppsValidMoves.take(theirMovesToLookAt).map {
                        val finalBoard = newBoard.applyMove(it)
                        val finalHistory = newHistory.plus(it)
                        if (depth < maxDepth) {
                            // logger.info("do recursion")
                            val validMoves = calc.getValidMoves(finalBoard,finalHistory, true)
                            if (validMoves.isEmpty()) {
                                -9999
                            } else {
                                getNextMoveRecursive(
                                    finalBoard,
                                    validMoves,
                                    finalHistory,
                                    depth + 1, myColour
                                ).second 
                            }
                        } else {
                            // logger.info("end recursion")
                            score(finalBoard, myColour)
                        }
                    }.maxByOrNull { it ?: -9999 }
                }
        }
            .map { it.key to it.value }
            .maxBy { it.second ?: -9999 }
    }

    private fun score(board: Board, myColour: Colour) = BoardScorer1().score(board) * if (myColour == Colour.BLACK) -1 else 1
}

