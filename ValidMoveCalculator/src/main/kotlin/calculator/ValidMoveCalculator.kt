package calculator

interface ValidMoveCalculator {
    fun getValidMoves(board: Board, lastMove: Move?): Set<Move>
}