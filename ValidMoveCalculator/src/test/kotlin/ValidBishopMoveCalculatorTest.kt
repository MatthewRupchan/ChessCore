import calculator.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ValidBishopMoveCalculatorTest {
    private fun doTest(board: Board, bishop: Piece, expectedMoves: List<Move>) {
        assertEquals(
            expected = expectedMoves.toSet(),
            actual = ValidBishopMoveCalculator.getValidMoves(bishop, board)
        )
    }

    @Test
    fun `Bishop moves diagonally`() {
        val bishop = Piece.whiteBishop(Location(Rank(4), File.D))
        doTest(
            Board(listOf(bishop)),
            bishop,
            listOf(
                Move(from=bishop, to=Piece.whiteBishop(Location(Rank(1), File.A))),
                Move(from=bishop, to=Piece.whiteBishop(Location(Rank(2), File.B))),
                Move(from=bishop, to=Piece.whiteBishop(Location(Rank(3), File.C))),
                Move(from=bishop, to=Piece.whiteBishop(Location(Rank(5), File.E))),
                Move(from=bishop, to=Piece.whiteBishop(Location(Rank(6), File.F))),
                Move(from=bishop, to=Piece.whiteBishop(Location(Rank(7), File.G))),
                Move(from=bishop, to=Piece.whiteBishop(Location(Rank(8), File.H))),
                Move(from=bishop, to=Piece.whiteBishop(Location(Rank(1), File.G))),
                Move(from=bishop, to=Piece.whiteBishop(Location(Rank(2), File.F))),
                Move(from=bishop, to=Piece.whiteBishop(Location(Rank(3), File.E))),
                Move(from=bishop, to=Piece.whiteBishop(Location(Rank(5), File.C))),
                Move(from=bishop, to=Piece.whiteBishop(Location(Rank(6), File.B))),
                Move(from=bishop, to=Piece.whiteBishop(Location(Rank(7), File.A))),
            )
        )
    }
    @Test
    fun `Bishop cannot move through friends`() {
        val bishop = Piece.whiteBishop(Location(Rank(4), File.E))
        val f1 = Piece.whiteKnight(Location(Rank(3), File.F))
        val f2 = Piece.whiteKnight(Location(Rank(3), File.D))
        val f3 = Piece.whiteKnight(Location(Rank(5), File.F))
        val f4 = Piece.whiteKnight(Location(Rank(5), File.D))
        doTest(
            Board(listOf(bishop, f1, f2, f3, f4)),
            bishop,
            listOf()
        )
    }
    @Test
    fun `Bishop captures foes`() {
        val bishop = Piece.blackBishop(Location(Rank(4), File.E))
        val f1 = Piece.whiteKnight(Location(Rank(3), File.F))
        val f2 = Piece.whiteKnight(Location(Rank(3), File.D))
        val f3 = Piece.whiteKnight(Location(Rank(5), File.F))
        val f4 = Piece.whiteKnight(Location(Rank(5), File.D))
        doTest(
            Board(listOf(bishop, f1, f2, f3, f4)),
            bishop,
            listOf(
                Move(from=bishop, to=Piece.blackBishop(f1.location), capture = f1),
                Move(from=bishop, to=Piece.blackBishop(f2.location), capture = f2),
                Move(from=bishop, to=Piece.blackBishop(f3.location), capture = f3),
                Move(from=bishop, to=Piece.blackBishop(f4.location), capture = f4),
            )
        )
    }
}