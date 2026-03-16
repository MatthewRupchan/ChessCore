package manager.flow

import bot.ChessBot
import calculator.Board
import calculator.Colour
import calculator.File
import calculator.Location
import calculator.Move
import calculator.Piece
import calculator.PieceType
import calculator.Rank
import calculator.ValidMoveCalculator
import example.ExampleChessBot
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class GameFlow(val validMoves: ValidMoveCalculator) {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    private val possibleBots = mapOf<String, ChessBot>("Example Chess Bot" to ExampleChessBot())

    fun startGame() {
        logger.info("Setting up game.")

        var white = ""
        do {
            println("Select a bot to play as white (${possibleBots.keys.joinToString(", ")}): ")
            white = readln()
        } while(white !in possibleBots.keys)

        var black = ""
        do {
            println("Select a bot to play as black  (${possibleBots.keys.joinToString(", ")}): ")
            black = readln()
        } while(black !in possibleBots.keys)

        logger.info("Initiating game between $white and $black")
        runGame(possibleBots[white]!!, possibleBots[black]!!)
    }

    private fun runGame(white: ChessBot, black: ChessBot) {
        var board = getStartingBoard()
        val moves = mutableListOf<Move>()

        while(true) {
            val lastFifty = moves.takeLast(100)
            if (lastFifty.size >= 100 && lastFifty.all { it.captures == null }) {
                logger.info("Stalemate")
                break
            }

            val possibleMoves = validMoves.getValidMoves(board, moves, true)
            if (isCheckmate(board, possibleMoves, moves)) {
                logger.info("Checkmate")
                break
            }

            if (possibleMoves.isEmpty()) {
                logger.info("Stalemate")
                break
            }

            val nextToMove = moves.lastOrNull()?.from?.colour?.opposite() ?: Colour.WHITE
            val nextMove = when(nextToMove) {
                Colour.WHITE -> white.getNextMove(possibleMoves, board, moves)
                Colour.BLACK -> black.getNextMove(possibleMoves, board, moves)
            }

            logger.info("$nextToMove: ${nextMove.getNotation()}")
            moves.add(nextMove)
            board = board.applyMove(nextMove)

            if (moves.size > 200) {
                logger.info("Temp Exit Point")
                break
            }
        }

        logger.info("PEN: ${moves.map { it.getNotation() }.chunked(2).mapIndexed { num, moves -> "${num + 1}. ${moves.joinToString(" ")}" }.joinToString(" ")}")
    }

    private fun getStartingBoard(): Board {
        return Board(buildList {
            addAll(File.entries.map { Piece.whitePawn(Location(Rank(2), it)) })
            addAll(File.entries.map { Piece.blackPawn(Location(Rank(7), it)) })

            add(Piece.whiteRook(Location(Rank(1), File.A)))
            add(Piece.whiteKnight(Location(Rank(1), File.B)))
            add(Piece.whiteBishop(Location(Rank(1), File.C)))
            add(Piece.whiteQueen(Location(Rank(1), File.D)))
            add(Piece.whiteKing(Location(Rank(1), File.E)))
            add(Piece.whiteBishop(Location(Rank(1), File.F)))
            add(Piece.whiteKnight(Location(Rank(1), File.G)))
            add(Piece.whiteRook(Location(Rank(1), File.H)))

            add(Piece.blackRook(Location(Rank(8), File.A)))
            add(Piece.blackKnight(Location(Rank(8), File.B)))
            add(Piece.blackBishop(Location(Rank(8), File.C)))
            add(Piece.blackQueen(Location(Rank(8), File.D)))
            add(Piece.blackKing(Location(Rank(8), File.E)))
            add(Piece.blackBishop(Location(Rank(8), File.F)))
            add(Piece.blackKnight(Location(Rank(8), File.G)))
            add(Piece.blackRook(Location(Rank(8), File.H)))
        })
    }

    private fun isCheckmate(board: Board, possibleMoves: Set<Move>, moveHistory: List<Move>): Boolean {
        val attackingColour = moveHistory.lastOrNull()?.from?.colour ?: Colour.BLACK

        val inCheck = validMoves.getValidMoves(board, moveHistory, false, attackingColour).any { it.captures?.pieceType == PieceType.KING }

        if (inCheck) {
            return possibleMoves.all { move ->
                board.applyMove(move).let { foesNextTurnBoard ->
                    validMoves.getValidMoves(foesNextTurnBoard, moveHistory.plus(move), false, attackingColour).any {
                        it.captures?.pieceType == PieceType.KING
                    }
                }
            }
        }
        return false
    }
}