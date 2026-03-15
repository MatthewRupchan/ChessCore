import calculator.Board
import calculator.File
import calculator.Location
import calculator.Move
import calculator.Piece
import calculator.Rank
import calculator.ValidMoveCalculatorImpl
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ValidMoveCalculatorTest {

    private fun doTest(board: Board, expectedMoves: List<Move>, moveHistory: List<Move>) {
        assertEquals(
            expected = expectedMoves.toSet(),
            actual = ValidMoveCalculatorImpl().getValidMoves(board, moveHistory).toSet()
        )
    }

    @Test
    fun `White To Move By Default`() {
        val whitePawn = Piece.whitePawn(Location(Rank(3), File.D))
        val blackPawn = Piece.blackPawn(Location(Rank(6), File.D))
        doTest(
            Board(listOf(whitePawn, blackPawn)),
            listOf(Move(from = whitePawn, to = Piece.whitePawn(Location(Rank(4), File.D)))),
            listOf()
        )
    }

    @Test
    fun `White To Move After Black`() {
        val whitePawn = Piece.whitePawn(Location(Rank(3), File.D))
        val blackPawn = Piece.blackPawn(Location(Rank(6), File.D))
        doTest(
            Board(listOf(whitePawn, blackPawn)),
            listOf(Move(from = whitePawn, to = Piece.whitePawn(Location(Rank(4), File.D)))),
            listOf(
                Move(from = Piece.blackPawn(Location(Rank(7), File.D)), to = blackPawn)
            )
        )
    }

    @Test
    fun `Black To Move After White`() {
        val whitePawn = Piece.whitePawn(Location(Rank(3), File.D))
        val blackPawn = Piece.blackPawn(Location(Rank(6), File.D))
        doTest(
            Board(listOf(whitePawn, blackPawn)),
            listOf(Move(from = blackPawn, to = Piece.blackPawn(Location(Rank(5), File.D)))),
            listOf(
                Move(from = Piece.whitePawn(Location(Rank(2), File.D)), to = whitePawn)
            )
        )
    }
}