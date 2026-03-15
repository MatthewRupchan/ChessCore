import calculator.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ValidKingMoveCalculatorTest {
    private fun doTest(board: Board, king: Piece, moveHistory: List<Move>, expectedMoves: List<Move>) {
        assertEquals(
            expected = expectedMoves.toSet(),
            actual = ValidKingMoveCalculator.getValidMoves(king, board, moveHistory)
        )
    }
    
    @Test
    fun `King moves all directions`() {
        val king = Piece.whiteKing(Location(Rank(4), File.E))
        doTest(
            Board(listOf(king)),
            king,
            listOf(),
            listOf(
                Move(from=king, to=Piece.whiteKing(Location(Rank(3), File.E))),
                Move(from=king, to=Piece.whiteKing(Location(Rank(5), File.E))),
                Move(from=king, to=Piece.whiteKing(Location(Rank(3), File.D))),
                Move(from=king, to=Piece.whiteKing(Location(Rank(5), File.D))),
                Move(from=king, to=Piece.whiteKing(Location(Rank(3), File.F))),
                Move(from=king, to=Piece.whiteKing(Location(Rank(5), File.F))),
                Move(from=king, to=Piece.whiteKing(Location(Rank(4), File.D))),
                Move(from=king, to=Piece.whiteKing(Location(Rank(4), File.F))),
            )
        )
    }

    @Test
    fun `Kingside Castling`() {
        val king = Piece.whiteKing(Location(Rank(1), File.E))
        val rook = Piece.whiteRook(Location(Rank(1), File.H))
        doTest(
            Board(listOf(king, rook)),
            king,
            listOf(),
            listOf(
                Move(from=king, to=Piece.whiteKing(Location(Rank(1), File.F))),
                Move(from=king, to=Piece.whiteKing(Location(Rank(1), File.D))),
                Move(from=king, to=Piece.whiteKing(Location(Rank(2), File.F))),
                Move(from=king, to=Piece.whiteKing(Location(Rank(2), File.D))),
                Move(from=king, to=Piece.whiteKing(Location(Rank(2), File.E))),
                Move(froms=listOf(king, rook), tos=listOf(Piece.whiteKing(Location(Rank(1), File.G)), Piece.whiteRook(Location(Rank(1), File.F)))),
            )
        )
    }

    @Test
    fun `Queenside Castling`() {
        val king = Piece.whiteKing(Location(Rank(1), File.E))
        val rook = Piece.whiteRook(Location(Rank(1), File.A))
        doTest(
            Board(listOf(king, rook)),
            king,
            listOf(),
            listOf(
                Move(from=king, to=Piece.whiteKing(Location(Rank(1), File.F))),
                Move(from=king, to=Piece.whiteKing(Location(Rank(1), File.D))),
                Move(from=king, to=Piece.whiteKing(Location(Rank(2), File.F))),
                Move(from=king, to=Piece.whiteKing(Location(Rank(2), File.D))),
                Move(from=king, to=Piece.whiteKing(Location(Rank(2), File.E))),
                Move(froms=listOf(king, rook), tos=listOf(Piece.whiteKing(Location(Rank(1), File.C)), Piece.whiteRook(Location(Rank(1), File.D)))),
            )
        )
    }

    @Test
    fun `King cannot walk through friends`() {
        val king = Piece.whiteKing(Location(Rank(4), File.E))
        val knight1 = Piece.whiteKnight(Location(Rank(3), File.E))
        val knight2 = Piece.whiteKnight(Location(Rank(5), File.E))
        val knight3 = Piece.whiteKnight(Location(Rank(3), File.D))
        val knight4 = Piece.whiteKnight(Location(Rank(5), File.D))
        val knight5 = Piece.whiteKnight(Location(Rank(3), File.F))
        val knight6 = Piece.whiteKnight(Location(Rank(5), File.F))
        val knight7 = Piece.whiteKnight(Location(Rank(4), File.D))
        val knight8 = Piece.whiteKnight(Location(Rank(4), File.F))
        doTest(
            Board(listOf(king, knight1, knight2, knight3, knight4, knight5, knight6, knight7, knight8)),
            king,
            listOf(),
            listOf()
        )
    }

    @Test
    fun `Cannot Kingside Castle Through Friend`() {
        val king = Piece.whiteKing(Location(Rank(1), File.E))
        val rook = Piece.whiteRook(Location(Rank(1), File.H))
        val bishop = Piece.whiteBishop(Location(Rank(1), File.G))
        doTest(
            Board(listOf(king, rook, bishop)),
            king,
            listOf(),
            listOf(
                Move(from=king, to=Piece.whiteKing(Location(Rank(1), File.F))),
                Move(from=king, to=Piece.whiteKing(Location(Rank(1), File.D))),
                Move(from=king, to=Piece.whiteKing(Location(Rank(2), File.F))),
                Move(from=king, to=Piece.whiteKing(Location(Rank(2), File.D))),
                Move(from=king, to=Piece.whiteKing(Location(Rank(2), File.E))),
            )
        )
    }

    @Test
    fun `Cannot Castle Queenside Through Friend`() {
        val king = Piece.whiteKing(Location(Rank(1), File.E))
        val rook = Piece.whiteRook(Location(Rank(1), File.A))
        val bishop = Piece.whiteBishop(Location(Rank(1), File.C))
        doTest(
            Board(listOf(king, rook, bishop)),
            king,
            listOf(),
            listOf(
                Move(from=king, to=Piece.whiteKing(Location(Rank(1), File.F))),
                Move(from=king, to=Piece.whiteKing(Location(Rank(1), File.D))),
                Move(from=king, to=Piece.whiteKing(Location(Rank(2), File.F))),
                Move(from=king, to=Piece.whiteKing(Location(Rank(2), File.D))),
                Move(from=king, to=Piece.whiteKing(Location(Rank(2), File.E))),
            )
        )
    }

    @Test
    fun `King cannot castle if moved previously`() {
        val king = Piece.whiteKing(Location(Rank(1), File.E))
        val rook = Piece.whiteRook(Location(Rank(1), File.H))
        doTest(
            Board(listOf(king, rook)),
            king,
            listOf(
                Move(from=king, to=Piece.whiteKing(Location(Rank(1), File.F))),
                Move(from=Piece.whiteKing(Location(Rank(1), File.F)), to=king),
            ),
            listOf(
                Move(from=king, to=Piece.whiteKing(Location(Rank(1), File.F))),
                Move(from=king, to=Piece.whiteKing(Location(Rank(1), File.D))),
                Move(from=king, to=Piece.whiteKing(Location(Rank(2), File.F))),
                Move(from=king, to=Piece.whiteKing(Location(Rank(2), File.D))),
                Move(from=king, to=Piece.whiteKing(Location(Rank(2), File.E))),
            )
        )
    }

    @Test
    fun `King cannot castle if rook moved previously`() {
        val king = Piece.whiteKing(Location(Rank(1), File.E))
        val rook = Piece.whiteRook(Location(Rank(1), File.H))
        doTest(
            Board(listOf(king, rook)),
            king,
            listOf(
                Move(from=rook, to=Piece.whiteRook(Location(Rank(1), File.F))),
                Move(from=Piece.whiteRook(Location(Rank(1), File.F)), to=rook),
            ),
            listOf(
                Move(from=king, to=Piece.whiteKing(Location(Rank(1), File.F))),
                Move(from=king, to=Piece.whiteKing(Location(Rank(1), File.D))),
                Move(from=king, to=Piece.whiteKing(Location(Rank(2), File.F))),
                Move(from=king, to=Piece.whiteKing(Location(Rank(2), File.D))),
                Move(from=king, to=Piece.whiteKing(Location(Rank(2), File.E))),
            )
        )
    }

    @Test
    fun `King must move out of check`() {
        val king = Piece.whiteKing(Location(Rank(1), File.E))
        val blackRook = Piece.blackRook(Location(Rank(1), File.H))
        doTest(
            Board(listOf(king, blackRook)),
            king,
            listOf(),
            listOf(
                Move(from=king, to=Piece.whiteKing(Location(Rank(2), File.F))),
                Move(from=king, to=Piece.whiteKing(Location(Rank(2), File.D))),
                Move(from=king, to=Piece.whiteKing(Location(Rank(2), File.E))),
            )
        )
    }

    @Test
    fun `Cannot Kingside Castle When Opponent Controls Squares`() {
        val king = Piece.whiteKing(Location(Rank(1), File.E))
        val rook = Piece.whiteRook(Location(Rank(1), File.H))
        val foe = Piece.blackKnight(Location(Rank(3), File.H))
        doTest(
            Board(listOf(king, rook, foe)),
            king,
            listOf(),
            listOf(
                Move(from=king, to=Piece.whiteKing(Location(Rank(1), File.F))),
                Move(from=king, to=Piece.whiteKing(Location(Rank(1), File.D))),
                Move(from=king, to=Piece.whiteKing(Location(Rank(2), File.D))),
                Move(from=king, to=Piece.whiteKing(Location(Rank(2), File.E))),
            )
        )
    }

    @Test
    fun `Cannot Queenside Castle When Opponent Controls Squares`() {
        val king = Piece.whiteKing(Location(Rank(1), File.E))
        val rook = Piece.whiteRook(Location(Rank(1), File.A))
        val foe = Piece.blackKnight(Location(Rank(3), File.A))
        doTest(
            Board(listOf(king, rook, foe)),
            king,
            listOf(),
            listOf(
                Move(from=king, to=Piece.whiteKing(Location(Rank(1), File.F))),
                Move(from=king, to=Piece.whiteKing(Location(Rank(1), File.D))),
                Move(from=king, to=Piece.whiteKing(Location(Rank(2), File.F))),
                Move(from=king, to=Piece.whiteKing(Location(Rank(2), File.D))),
                Move(from=king, to=Piece.whiteKing(Location(Rank(2), File.E))),
            )
        )
    }

    @Test
    fun `King Can Capture Enemy Pieces`() {
        val king = Piece.blackKing(Location(Rank(8), File.H))
        val foe = Piece.whiteRook(Location(Rank(7), File.H))
        doTest(
            Board(listOf(king, foe)),
            king,
            listOf(),
            listOf(
                Move(from=king, to=Piece.blackKing(Location(Rank(8), File.G))),
                Move(from=king, to=Piece.blackKing(foe.location), capture = foe),
            )
        )
    }

    @Test
    fun `King Cannot Capture Defended Enemy Pieces`() {
        val king = Piece.blackKing(Location(Rank(8), File.H))
        val foe1 = Piece.whiteRook(Location(Rank(7), File.H))
        val foe2 = Piece.whiteRook(Location(Rank(6), File.H))
        doTest(
            Board(listOf(king, foe1, foe2)),
            king,
            listOf(),
            listOf(
                Move(from=king, to=Piece.blackKing(Location(Rank(8), File.G))),
            )
        )
    }
}