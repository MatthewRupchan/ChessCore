import calculator.Board
import calculator.File
import calculator.Location
import calculator.Move
import calculator.Piece
import calculator.Rank
import calculator.ValidKnightMoveCalculator
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ValidKnightMoveCalculatorTest {
    private fun doTest(board: Board, knight: Piece, expectedMoves: List<Move>, lastMove: Move? = null) {
        assertEquals(
            expected = expectedMoves.toSet(),
            actual = ValidKnightMoveCalculator.getValidMoves(knight, board, lastMove)
        )
    }

    @Test
    fun `Knight has 8 moves`() {
        val knight = Piece.whiteKnight(Location(Rank(4), File.E))
        doTest(
            Board(listOf(knight)),
            knight,
            listOf(
                Move(from=knight, to=Piece.whiteKnight(Location(Rank(6), File.D))),
                Move(from=knight, to=Piece.whiteKnight(Location(Rank(6), File.F))),
                Move(from=knight, to=Piece.whiteKnight(Location(Rank(5), File.C))),
                Move(from=knight, to=Piece.whiteKnight(Location(Rank(5), File.G))),
                Move(from=knight, to=Piece.whiteKnight(Location(Rank(3), File.C))),
                Move(from=knight, to=Piece.whiteKnight(Location(Rank(3), File.G))),
                Move(from=knight, to=Piece.whiteKnight(Location(Rank(2), File.D))),
                Move(from=knight, to=Piece.whiteKnight(Location(Rank(2), File.F))),
            )
        )
    }
    @Test
    fun `Knight does not jump off top left corner`() {
        val knight = Piece.whiteKnight(Location(Rank(1), File.A))
        doTest(
            Board(listOf(knight)),
            knight,
            listOf(
                Move(from=knight, to=Piece.whiteKnight(Location(Rank(2), File.C))),
                Move(from=knight, to=Piece.whiteKnight(Location(Rank(3), File.B))),
            )
        )
    }
    @Test
    fun `Knight does not jump off bottom right corner`() {
        val knight = Piece.blackKnight(Location(Rank(8), File.H))
        doTest(
            Board(listOf(knight)),
            knight,
            listOf(
                Move(from=knight, to=Piece.blackKnight(Location(Rank(6), File.G))),
                Move(from=knight, to=Piece.blackKnight(Location(Rank(7), File.F))),
            )
        )
    }
    @Test
    fun `Knight Captures Foes`() {
        val knight = Piece.whiteKnight(Location(Rank(1), File.H))
        val q1 = Piece.blackQueen(Location(Rank(2), File.F))
        val q2 = Piece.blackQueen(Location(Rank(3), File.G))
        doTest(
            Board(listOf(knight, q1, q2)),
            knight,
            listOf(
                Move(from=knight, to=Piece.whiteKnight(Location(Rank(2), File.F)), capture = q1),
                Move(from=knight, to=Piece.whiteKnight(Location(Rank(3), File.G)), capture = q2),
            )
        )
    }

    @Test
    fun `Knight Cannot Stand On Friends`() {
        val knight = Piece.whiteKnight(Location(Rank(8), File.A))
        val q1 = Piece.whiteQueen(Location(Rank(7), File.C))
        val q2 = Piece.whiteQueen(Location(Rank(6), File.B))
        doTest(
            Board(listOf(knight, q1, q2)),
            knight,
            listOf()
        )
    }
}