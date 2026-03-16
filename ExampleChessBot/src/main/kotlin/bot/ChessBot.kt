package bot

import calculator.Board
import calculator.Move

interface ChessBot {
    fun getNextMove(validMoves: Set<Move>, board: Board, moveHistory: List<Move>): Move
}