package calculator

interface ValidMoveCalculator {
    fun getValidMoves(board: Board, moveHistory: List<Move>, checkChecks: Boolean, moveAs: Colour? = null): Set<Move>
}