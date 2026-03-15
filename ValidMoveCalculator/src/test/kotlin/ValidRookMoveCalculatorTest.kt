import calculator.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ValidRookMoveCalculatorTest {
    private fun doTest(board: Board, rook: Piece, expectedMoves: List<Move>) {
        assertEquals(
            expected = expectedMoves.toSet(),
            actual = ValidRookMoveCalculator.getValidMoves(rook, board)
        )
    }

    @Test
    fun `Rook moves cardinally`() {
        val rook = Piece.whiteRook(Location(Rank(4), File.E))
        doTest(
            Board(listOf(rook)),
            rook,
            listOf(
                Move(from=rook, to=Piece.whiteRook(Location(Rank(1), File.E))),
                Move(from=rook, to=Piece.whiteRook(Location(Rank(2), File.E))),
                Move(from=rook, to=Piece.whiteRook(Location(Rank(3), File.E))),
                Move(from=rook, to=Piece.whiteRook(Location(Rank(5), File.E))),
                Move(from=rook, to=Piece.whiteRook(Location(Rank(6), File.E))),
                Move(from=rook, to=Piece.whiteRook(Location(Rank(7), File.E))),
                Move(from=rook, to=Piece.whiteRook(Location(Rank(8), File.E))),
                Move(from=rook, to=Piece.whiteRook(Location(Rank(4), File.A))),
                Move(from=rook, to=Piece.whiteRook(Location(Rank(4), File.B))),
                Move(from=rook, to=Piece.whiteRook(Location(Rank(4), File.C))),
                Move(from=rook, to=Piece.whiteRook(Location(Rank(4), File.D))),
                Move(from=rook, to=Piece.whiteRook(Location(Rank(4), File.F))),
                Move(from=rook, to=Piece.whiteRook(Location(Rank(4), File.G))),
                Move(from=rook, to=Piece.whiteRook(Location(Rank(4), File.H))),
            )
        )
    }
    @Test
    fun `Rook cannot move through friends`() {
        val rook = Piece.whiteRook(Location(Rank(4), File.E))
        val f1 = Piece.whiteKnight(Location(Rank(5), File.E))
        val f2 = Piece.whiteKnight(Location(Rank(3), File.E))
        val f3 = Piece.whiteKnight(Location(Rank(4), File.D))
        val f4 = Piece.whiteKnight(Location(Rank(4), File.F))
        doTest(
            Board(listOf(rook, f1, f2, f3, f4)),
            rook,
            listOf()
        )
    }
    @Test
    fun `Rook captures foes`() {
        val rook = Piece.blackRook(Location(Rank(4), File.E))
        val f1 = Piece.whiteKnight(Location(Rank(5), File.E))
        val f2 = Piece.whiteKnight(Location(Rank(3), File.E))
        val f3 = Piece.whiteKnight(Location(Rank(4), File.D))
        val f4 = Piece.whiteKnight(Location(Rank(4), File.F))
        doTest(
            Board(listOf(rook, f1, f2, f3, f4)),
            rook,
            listOf(
                Move(from=rook, to=Piece.blackRook(f1.location), capture = f1),
                Move(from=rook, to=Piece.blackRook(f2.location), capture = f2),
                Move(from=rook, to=Piece.blackRook(f3.location), capture = f3),
                Move(from=rook, to=Piece.blackRook(f4.location), capture = f4),
            )
        )
    }
}