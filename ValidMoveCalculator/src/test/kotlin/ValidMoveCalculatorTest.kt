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

    internal class PawnTest {
        private fun doTest(board: Board, pawn: Piece, expectedMoves: List<Move>, lastMove: Move? = null) {
            assertEquals(
                expected = expectedMoves.toSet(),
                actual = ValidMoveCalculatorImpl().getValidMoves(board, lastMove).filter {
                    it.from == pawn
                }.toSet()
            )
        }

        @Test
        fun `Move White Pawn`() {
            val pawn = Piece.whitePawn(Location(Rank(4), File.F))
            doTest(
                Board(listOf(pawn)),
                pawn,
                listOf(
                    Move(from=pawn, to=Piece.whitePawn(Location(Rank(5), File.F))),
                )
            )
        }
        @Test
        fun `Move Black Pawn`() {
            val pawn = Piece.blackPawn(Location(Rank(4), File.F))
            doTest(
                Board(listOf(pawn)),
                pawn,
                listOf(
                    Move(from=pawn, to=Piece.blackPawn(Location(Rank(3), File.F))),
                )
            )
        }
        @Test
        fun `White Pawn on Home Square`() {
            val pawn = Piece.whitePawn(Location(Rank(2), File.F))
            doTest(
                Board(listOf(pawn)),
                pawn,
                listOf(
                    Move(from=pawn, to=Piece.whitePawn(Location(Rank(3), File.F))),
                    Move(from=pawn, to=Piece.whitePawn(Location(Rank(4), File.F))),
                )
            )
        }
        @Test
        fun `Black Pawn on Home Square`() {
            val pawn = Piece.blackPawn(Location(Rank(7), File.F))
            doTest(
                Board(listOf(pawn)),
                pawn,
                listOf(
                    Move(from=pawn, to=Piece.blackPawn(Location(Rank(6), File.F))),
                    Move(from=pawn, to=Piece.blackPawn(Location(Rank(5), File.F))),
                )
            )
        }
        @Test
        fun `White Pawn En Passant`() {
            val pawn = Piece.whitePawn(Location(Rank(5), File.F))
            val lastMove = Move(
                from=Piece.blackPawn(Location(Rank(7), File.G)),
                to=Piece.blackPawn(Location(Rank(5), File.G))
            )
            doTest(
                Board(listOf(pawn, lastMove.to)),
                pawn,
                listOf(
                    Move(from=pawn, to=Piece.whitePawn(Location(Rank(6), File.F))),
                    Move(from=pawn, to=Piece.whitePawn(Location(Rank(6), File.G)), capture = lastMove.to),
                ),
                lastMove = lastMove
            )
        }
        @Test
        fun `Black Pawn En Passant`() {
            val pawn = Piece.blackPawn(Location(Rank(4), File.F))
            val lastMove = Move(
                from=Piece.whitePawn(Location(Rank(2), File.G)),
                to=Piece.whitePawn(Location(Rank(4), File.G))
            )
            doTest(
                Board(listOf(pawn, lastMove.to)),
                pawn,
                listOf(
                    Move(from=pawn, to=Piece.blackPawn(Location(Rank(3), File.F))),
                    Move(from=pawn, to=Piece.blackPawn(Location(Rank(3), File.G)), capture = lastMove.to),
                ),
                lastMove = lastMove
            )
        }
        @Test
        fun `White Pawn No En Passant Too Far Away`() {
            val pawn = Piece.whitePawn(Location(Rank(5), File.F))
            val lastMove = Move(
                from=Piece.blackPawn(Location(Rank(7), File.A)),
                to=Piece.blackPawn(Location(Rank(5), File.A))
            )
            doTest(
                Board(listOf(pawn, lastMove.to)),
                pawn,
                listOf(
                    Move(from=pawn, to=Piece.whitePawn(Location(Rank(6), File.F))),
                ),
                lastMove = lastMove
            )
        }
        @Test
        fun `Black Pawn No En Passant Too Far Away`() {
            val pawn = Piece.blackPawn(Location(Rank(4), File.F))
            val lastMove = Move(
                from=Piece.whitePawn(Location(Rank(2), File.A)),
                to=Piece.whitePawn(Location(Rank(4), File.A))
            )
            doTest(
                Board(listOf(pawn, lastMove.to)),
                pawn,
                listOf(
                    Move(from=pawn, to=Piece.blackPawn(Location(Rank(3), File.F))),
                ),
                lastMove = lastMove
            )
        }
        @Test
        fun `White Pawn No En Passant No Jump`() {
            val pawn = Piece.whitePawn(Location(Rank(5), File.F))
            val lastMove = Move(
                from=Piece.blackPawn(Location(Rank(6), File.E)),
                to=Piece.blackPawn(Location(Rank(5), File.E))
            )
            doTest(
                Board(listOf(pawn, lastMove.to)),
                pawn,
                listOf(
                    Move(from=pawn, to=Piece.whitePawn(Location(Rank(6), File.F))),
                ),
                lastMove = lastMove
            )
        }
        @Test
        fun `Black Pawn No En Passant Not Last Move`() {
            val pawn = Piece.blackPawn(Location(Rank(4), File.F))
            doTest(
                Board(listOf(pawn, Piece.whitePawn(Location(Rank(4), File.E)))),
                pawn,
                listOf(
                    Move(from=pawn, to=Piece.blackPawn(Location(Rank(3), File.F))),
                )
            )
        }
        @Test
        fun `White Pawn Promoting`() {
            val pawn = Piece.whitePawn(Location(Rank(7), File.B))
            doTest(
                Board(listOf(pawn)),
                pawn,
                listOf(
                    Move(from=pawn, to=Piece.whiteRook(Location(Rank(8), File.B))),
                    Move(from=pawn, to=Piece.whiteQueen(Location(Rank(8), File.B))),
                    Move(from=pawn, to=Piece.whiteKnight(Location(Rank(8), File.B))),
                    Move(from=pawn, to=Piece.whiteBishop(Location(Rank(8), File.B))),
                ),
            )
        }
        @Test
        fun `Black Pawn Promoting`() {
            val pawn = Piece.blackPawn(Location(Rank(2), File.B))
            doTest(
                Board(listOf(pawn)),
                pawn,
                listOf(
                    Move(from=pawn, to=Piece.blackRook(Location(Rank(1), File.B))),
                    Move(from=pawn, to=Piece.blackQueen(Location(Rank(1), File.B))),
                    Move(from=pawn, to=Piece.blackKnight(Location(Rank(1), File.B))),
                    Move(from=pawn, to=Piece.blackBishop(Location(Rank(1), File.B))),
                ),
            )
        }
        @Test
        fun `White Pawn Promoting Through Capture`() {
            val pawn = Piece.whitePawn(Location(Rank(7), File.B))
            val blackPiece1 = Piece.blackRook(Location(Rank(8), File.A))
            val blackPiece2 = Piece.blackBishop(Location(Rank(8), File.C))
            doTest(
                Board(listOf(pawn, blackPiece1, blackPiece2)),
                pawn,
                listOf(
                    Move(from=pawn, to=Piece.whiteRook(Location(Rank(8), File.B))),
                    Move(from=pawn, to=Piece.whiteQueen(Location(Rank(8), File.B))),
                    Move(from=pawn, to=Piece.whiteKnight(Location(Rank(8), File.B))),
                    Move(from=pawn, to=Piece.whiteBishop(Location(Rank(8), File.B))),

                    Move(from=pawn, to=Piece.whiteRook(Location(Rank(8), blackPiece1.location.file)), capture = blackPiece1),
                    Move(from=pawn, to=Piece.whiteQueen(Location(Rank(8), blackPiece1.location.file)), capture = blackPiece1),
                    Move(from=pawn, to=Piece.whiteKnight(Location(Rank(8), blackPiece1.location.file)), capture = blackPiece1),
                    Move(from=pawn, to=Piece.whiteBishop(Location(Rank(8), blackPiece1.location.file)), capture = blackPiece1),

                    Move(from=pawn, to=Piece.whiteRook(Location(Rank(8), blackPiece2.location.file)), capture = blackPiece2),
                    Move(from=pawn, to=Piece.whiteQueen(Location(Rank(8), blackPiece2.location.file)), capture = blackPiece2),
                    Move(from=pawn, to=Piece.whiteKnight(Location(Rank(8), blackPiece2.location.file)), capture = blackPiece2),
                    Move(from=pawn, to=Piece.whiteBishop(Location(Rank(8), blackPiece2.location.file)), capture = blackPiece2),
                ),
            )
        }
        @Test
        fun `Black Pawn Promoting Through Capture`() {
            val pawn = Piece.blackPawn(Location(Rank(2), File.B))
            val whitePiece1 = Piece.whiteRook(Location(Rank(1), File.A))
            val whitePiece2 = Piece.whiteBishop(Location(Rank(1), File.C))
            doTest(
                Board(listOf(pawn, whitePiece1, whitePiece2)),
                pawn,
                listOf(
                    Move(from=pawn, to=Piece.blackRook(Location(Rank(1), File.B))),
                    Move(from=pawn, to=Piece.blackQueen(Location(Rank(1), File.B))),
                    Move(from=pawn, to=Piece.blackKnight(Location(Rank(1), File.B))),
                    Move(from=pawn, to=Piece.blackBishop(Location(Rank(1), File.B))),

                    Move(from=pawn, to=Piece.blackRook(Location(Rank(1), whitePiece1.location.file)), capture = whitePiece1),
                    Move(from=pawn, to=Piece.blackQueen(Location(Rank(1), whitePiece1.location.file)), capture = whitePiece1),
                    Move(from=pawn, to=Piece.blackKnight(Location(Rank(1), whitePiece1.location.file)), capture = whitePiece1),
                    Move(from=pawn, to=Piece.blackBishop(Location(Rank(1), whitePiece1.location.file)), capture = whitePiece1),

                    Move(from=pawn, to=Piece.blackRook(Location(Rank(1), whitePiece2.location.file)), capture = whitePiece2),
                    Move(from=pawn, to=Piece.blackQueen(Location(Rank(1), whitePiece2.location.file)), capture = whitePiece2),
                    Move(from=pawn, to=Piece.blackKnight(Location(Rank(1), whitePiece2.location.file)), capture = whitePiece2),
                    Move(from=pawn, to=Piece.blackBishop(Location(Rank(1), whitePiece2.location.file)), capture = whitePiece2),
                ),
            )
        }
        @Test
        fun `Black Pawn Captures Enemy`() {
            val pawn = Piece.blackPawn(Location(Rank(7), File.B))
            val whitePiece1 = Piece.whiteKnight(Location(Rank(6), File.A))
            val whitePiece2 = Piece.whiteKnight(Location(Rank(6), File.B))
            val whitePiece3 = Piece.whiteKnight(Location(Rank(6), File.C))
            doTest(
                Board(listOf(pawn, whitePiece1, whitePiece2, whitePiece3)),
                pawn,
                listOf(
                    Move(from=pawn, to=Piece.blackPawn(Location(Rank(6), File.A)), capture = whitePiece1),
                    Move(from=pawn, to=Piece.blackPawn(Location(Rank(6), File.C)), capture = whitePiece3),
                ),
            )
        }
        @Test
        fun `White Pawn Captures Enemy`() {
            val pawn = Piece.whitePawn(Location(Rank(2), File.B))
            val blackPiece1 = Piece.blackKnight(Location(Rank(3), File.A))
            val blackPiece2 = Piece.blackKnight(Location(Rank(3), File.B))
            val blackPiece3 = Piece.blackKnight(Location(Rank(3), File.C))
            doTest(
                Board(listOf(pawn, blackPiece1, blackPiece2, blackPiece3)),
                pawn,
                listOf(
                    Move(from=pawn, to=Piece.whitePawn(Location(Rank(3), File.A)), capture = blackPiece1),
                    Move(from=pawn, to=Piece.whitePawn(Location(Rank(3), File.C)), capture = blackPiece3),
                ),
            )
        }
        @Test
        fun `Black Pawn Does Not Capture or Move Through Friends`() {
            val pawn = Piece.blackPawn(Location(Rank(7), File.B))
            val blackPiece1 = Piece.blackKnight(Location(Rank(6), File.A))
            val blackPiece2 = Piece.blackKnight(Location(Rank(6), File.B))
            val blackPiece3 = Piece.blackKnight(Location(Rank(6), File.C))
            doTest(
                Board(listOf(pawn, blackPiece1, blackPiece2, blackPiece3)),
                pawn,
                listOf(),
            )
        }
        @Test
        fun `White Pawn Does Not Capture or Move Through  Friends`() {
            val pawn = Piece.whitePawn(Location(Rank(2), File.B))
            val whitePiece1 = Piece.whiteKnight(Location(Rank(3), File.A))
            val whitePiece2 = Piece.whiteKnight(Location(Rank(3), File.B))
            val whitePiece3 = Piece.whiteKnight(Location(Rank(3), File.C))
            doTest(
                Board(listOf(pawn, whitePiece1, whitePiece2, whitePiece3)),
                pawn,
                listOf(),
            )
        }
    }

}