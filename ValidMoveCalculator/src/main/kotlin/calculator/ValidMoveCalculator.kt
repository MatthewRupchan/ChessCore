package calculator

interface ValidMoveCalculator {
    fun getValidMoves(board: Board, moveHistory: List<Move>): Set<Move>
}