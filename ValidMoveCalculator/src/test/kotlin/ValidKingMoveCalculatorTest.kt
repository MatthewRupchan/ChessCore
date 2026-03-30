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

    @Test
    fun `Kingside Castling even after opponent king moves`() {
        val king = Piece.whiteKing(Location(Rank(1), File.E))
        val rook = Piece.whiteRook(Location(Rank(1), File.H))
        doTest(
            Board(listOf(king, rook)),
            king,
            listOf(Move(from= Piece.blackKing(Location(Rank(8), File.E)), to= Piece.blackKing(Location(Rank(7), File.E)))),
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
    fun `Kingside Castling even after other rook moves`() {
        val king = Piece.whiteKing(Location(Rank(1), File.E))
        val rook1 = Piece.whiteRook(Location(Rank(1), File.H))
        val rook2 = Piece.whiteRook(Location(Rank(1), File.A))
        doTest(
            Board(listOf(king, rook1, rook2)),
            king,
            listOf(
                Move(from= rook2, to=Piece.whiteRook(Location(Rank(2), File.A))),
                Move(from= Piece.whiteRook(Location(Rank(2), File.A)), to=rook2),
            ),
            listOf(
                Move(from=king, to=Piece.whiteKing(Location(Rank(1), File.F))),
                Move(from=king, to=Piece.whiteKing(Location(Rank(1), File.D))),
                Move(from=king, to=Piece.whiteKing(Location(Rank(2), File.F))),
                Move(from=king, to=Piece.whiteKing(Location(Rank(2), File.D))),
                Move(from=king, to=Piece.whiteKing(Location(Rank(2), File.E))),
                Move(froms=listOf(king, rook1), tos=listOf(Piece.whiteKing(Location(Rank(1), File.G)), Piece.whiteRook(Location(Rank(1), File.F)))),
            )
        )
    }

    @Test
    fun `Situation found in game where black should be able to queenside castle`() {
        val king = Piece.blackKing(Location(Rank(8), File.E))
        val castleableRook =  Piece.blackRook(Location(Rank(8), File.A))
        doTest(
            Board(listOf(
                Piece.whitePawn(Location(Rank(2), File.B)),
                Piece.whitePawn(Location(Rank(2), File.C)),
                Piece.whitePawn(Location(Rank(2), File.F)),
                Piece.whitePawn(Location(Rank(2), File.G)),
                Piece.blackPawn(Location(Rank(7), File.A)),
                Piece.blackPawn(Location(Rank(7), File.B)),
                Piece.blackPawn(Location(Rank(7), File.F)),
                Piece.blackPawn(Location(Rank(7), File.G)),
                Piece.blackPawn(Location(Rank(7), File.H)),
                Piece.whiteRook(Location(Rank(1), File.A)),
                Piece.whiteBishop(Location(Rank(1), File.C)),
                Piece.whiteKing(Location(Rank(1), File.E)),
                Piece.whiteBishop(Location(Rank(1), File.F)),
                Piece.whiteRook(Location(Rank(1), File.H)),
                castleableRook,
                king,
                Piece.blackBishop(Location(Rank(8), File.F)),
                Piece.blackKnight(Location(Rank(8), File.G)),
                Piece.blackRook(Location(Rank(8), File.H)),
                Piece.whiteKnight(Location(Rank(3), File.C)),
                Piece.blackPawn(Location(Rank(5), File.E)),
                Piece.whiteKnight(Location(Rank(3), File.F)),
                Piece.blackQueen(Location(Rank(5), File.C)),
                Piece.whitePawn(Location(Rank(4), File.E)),
                Piece.blackKnight(Location(Rank(6), File.C)),
                Piece.whitePawn(Location(Rank(3), File.A)),
                Piece.blackBishop(Location(Rank(4), File.G)),
                Piece.whitePawn(Location(Rank(3), File.H)),
            )),
            king,
            listOf(), // 1. d2d4 d7d5 2. Nb1c3 c7c5 3. d4xc5 e7e5 4. Qd1xd5 Qd8xd5 5. Ng1f3 Qd5xc5 6. e2e4 Nb8c6 7. a2a3 Bc8g4 8. h2h3
            listOf(
                Move(from=king, to=Piece.blackKing(Location(Rank(7), File.E))),
                Move(from=king, to=Piece.blackKing(Location(Rank(7), File.D))),
                Move(from=king, to=Piece.blackKing(Location(Rank(8), File.D))),
                Move(froms=listOf(king, castleableRook), tos=listOf(Piece.blackKing(Location(Rank(8), File.C)), Piece.blackRook(Location(Rank(8), File.D)))),
            )
        )
    }

    @Test
    fun `Castling Disallowed when you are in check`() {
        val king = Piece.blackKing(Location(Rank(8), File.E))
        val rook = Piece.blackRook(Location(Rank(8), File.A))
        val knight = Piece.whiteKnight(Location(Rank(7), File.C))
        doTest(
            Board(listOf(king, rook, knight)),
            king,
            listOf(),
            listOf(
                Move(from=king, to=Piece.blackKing(Location(Rank(8), File.D))),
                Move(from=king, to=Piece.blackKing(Location(Rank(7), File.D))),
                Move(from=king, to=Piece.blackKing(Location(Rank(7), File.E))),
                Move(from=king, to=Piece.blackKing(Location(Rank(7), File.F))),
                Move(from=king, to=Piece.blackKing(Location(Rank(8), File.F))),
            )
        )
    }
}