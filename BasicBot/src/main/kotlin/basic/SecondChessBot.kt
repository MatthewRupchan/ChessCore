package basic

import bot.ChessBot
import calculator.Board
import calculator.Colour
import calculator.Move
import calculator.ValidMoveCalculatorImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.Executors

//Against martin, martin white us black, good but draw: 1. d2d4 d7d5 2. c2c3 Nb8c6 3. e2e3 e7e5 4. g2g3 Ng8f6 5. h2h4 Nf6g4 6. Qd1d3 a7a5 7. a2a3 h7h5 8. Ke1d2 Ng4xf2 9. Bf1e2 Nf2xd3 10. Be2xd3 Ra8a7 11. Bd3g6 f7xg6 12. b2b4 a5xb4 13. Ng1e2 Bc8d7 14. Ne2g1 Qd8c8 15. d4xe5 Nc6xe5 16. Ng1f3 Ne5xf3 17. Kd2e2 Bd7g4 18. Ke2f2 Qc8f5 19. c3xb4 Nf3xh4 20. Kf2g1 Nh4f3 21. Kg1f1 Bf8xb4 22. Nb1c3 Bb4xc3 23. Bc1b2 Bc3xb2 24. Rh1xh5 Rh8xh5 25. Kf1e2 Bb2xa1 26. Ke2f1 Ra7xa3 27. Kf1f2 b7b6 28. Kf2g2 Ra3xe3 29. Kg2f2 d5d4 30. Kf2f1 c7c5 31. Kf1f2 Ke8f8 32. Kf2g2 Kf8g8 33. Kg2f2 Bg4h3 34. g3g4 Qf5xg4
//now as white, against martin (black(: CHECKMATE, AND INCLUDES AN EN-PASSANT
// 1. d2d4 e7e6 2. e2e4 Qd8e7 3. Nb1c3 Nb8a6 4. Ng1f3 b7b6 5. a2a4 Ng8f6 6. h2h4 Na6c5 7. d4xc5 Qe7xc5 8. g2g3 Qc5c6 9. Ra1a2 Qc6xc3 10. b2xc3 Bf8d6 11. Ra2b2 Nf6g8 12. c3c4 Ng8e7 13. Bc1d2 Bd6a3 14. Rb2b3 g7g5 15. Rb3xa3 h7h5 16. h4xg5 c7c5 17. Ra3d3 a7a5 18. Qd1c1 Ke8d8 19. Ke1d1 Ra8b8 20. Bf1g2 Rb8b7 21. Rh1h2 Ne7g8 22. e4e5 Rb7c7 23. Bd2e3 Ng8h6 24. g5xh6 Bc8a6 25. Nf3g5 Ba6c8 26. Ng5xf7 Kd8e7 27. Nf7xh8 Bc8b7 28. Nh8g6 Ke7e8 29. Bg2h3 d7d5 30. e5xd6 Bb7f3 31. Kd1e1 Rc7d7 32. h6h7 Rd7xh7 33. Bh3xe6 Rh7h8 34. Ng6xh8 Bf3c6 35. Rh2xh5 Bc6e4 36. Rd3b3 Ke8d8 37. Rb3xb6 Be4b7 38. Rb6xb7 Kd8e8 39. Rb7b8

//bEAT JANJAY (us black, her white, that's 700 elo!) with a brilliant move!
// 1. e2e4 d7d5 2. e4xd5 Qd8xd5 3. Nb1c3 Qd5d6 4. Bf1b5 Nb8c6 5. Bb5xc6 Qd6xc6 6. Ng1e2 Qc6xg2 7. Ne2g3 e7e5 8. h2h4 Ng8f6 9. Ke1e2 Nf6g4 10. Ng3e4 Ng4xf2 11. Ne4xf2 a7a6 12. b2b3 b7b6 13. d2d3 c7c5 14. Nc3d5 Qg2xd5 15. Qd1g1 g7g6 16. Nf2h3 h7h6 17. Bc1b2 Ra8a7 18. Rh1h2 Ra7d7 19. Ke2f2 Bc8b7 20. Qg1c1 Ke8d8 21. Qc1e1 Kd8c8 22. a2a4 Bf8g7 23. Qe1e4 Rh8d8 24. Kf2f1 Qd5xd3 25. Qe4xd3 Rd7xd3 26. c2xd3 h6h5 27. Rh2c2 Rd8xd3 28. Bb2c3 Rd3xh3 29. Ra1a2 Rh3xh4 30. Bc3a1 Rh4f4 31. Kf1e1 a6a5 32. Rc2e2 c5c4 33. Ba1xe5 c4xb3 34. Be5xf4 b3xa2 35. Re2xa2 Bb7a8 36. Bf4g3 Bg7h8 37. Ra2c2 Kc8b7 38. Rc2c7 Kb7a6 39. Ke1d2 Ba8d5 40. Rc7a7 Ka6xa7 41. Kd2d1 Ka7a8 42. Bg3h2 h5h4 43. Bh2c7 Bh8d4 44. Bc7e5 Bd4xe5 45. Kd1c2 Ka8b8 46. Kc2d2 h4h3 47. Kd2e2 h3h2 48. Ke2d3 h2h1=Q 49. Kd3e3 Bd5e6 50. Ke3e2 Be5f6 51. Ke2e3 Kb8c8 52. Ke3e2 Qh1h2 53. Ke2d3 Be6d7 54. Kd3e4 Bd7xa4 55. Ke4e3 Ba4c6 56. Ke3d3 b6b5 57. Kd3e3 g6g5 58. Ke3d3 a5a4 59. Kd3e3 Bf6g7 60. Ke3d3 f7f5 61. Kd3e3 Kc8d7 62. Ke3d3 Qh2g2 63. Kd3e3 Bc6b7 64. Ke3d3 g5g4 65. Kd3e3 Bg7e5 66. Ke3d3 a4a3 67. Kd3e3 b5b4 68. Ke3d3 f5f4 69. Kd3c4 Qg2d2 70. Kc4b5 Qd2d4 71. Kb5a4 Qd4c4 72. Ka4a5 Kd7c6 73. Ka5a4 Bb7c8 74. Ka4a5 Qc4b5

// draw with sven. actually really well played, besides letting a pawn slip through!
// beat nelson!
// put up an alright fight against isabel!

class SecondChessBot(val scorer: BoardScorer) : ChessBot {
    private val calc = ValidMoveCalculatorImpl()
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun getNextMove(validMoves: Set<Move>, originalBoard: Board, moveHistory: List<Move>): Move {
        val newBoards = validMoves.map { it to originalBoard.applyMove(it) }
        val myColour = moveHistory.lastOrNull()?.from?.colour?.opposite() ?: Colour.WHITE

        // Any checkmates? if so, exit early and just mate the opponent.
        val bestMove = newBoards.firstOrNull { (move, board) -> calc.isCheckmate(board, moveHistory.plus(move)) }
        if (bestMove != null){
            logger.info("Choosing this move because it's checkmate")
            return bestMove.first
        }

        val movePlusResponses = newBoards.map {
            it to calc.getValidMoves(it.second, moveHistory.plus(it.first), true).map { oMove -> oMove to it.second.applyMove(oMove) }
        }


        // If I make a move, will it end up with me getting mated? if so, let's not make that move.
        // todo this isn't working right..
        val movesThatDontMateMe = movePlusResponses.filter {
            it.second.none {
                (oMove, oBoard) -> calc.isCheckmate(oBoard, moveHistory.plus(it.first.first).plus(oMove))
            }
        }
        val movesThatDontMateMeLite = movesThatDontMateMe.map { it.first.first }


        // But if I have no choice, I have no choice.
        if (movesThatDontMateMe.isEmpty()) {
            logger.info("I have no choice but to be mated")
            return validMoves.first()
        }



        val beneficialCaptures = movesThatDontMateMe.filter { isBeneficialCapture(it.first.first, originalBoard, moveHistory) }.map { it.first.first }

        val movesAllowingOpponentToCapture = movesThatDontMateMe.parallelStream().filter { (myMove, followUps) ->
            followUps.any { (m, _) ->
                isBeneficialCapture(m, myMove.second, moveHistory.plus(myMove.first))
            }
        }.toList().associate { (myMove, followUps) ->
            myMove.first to followUps.parallelStream().filter { (m, _) ->
                isBeneficialCapture(m, myMove.second, moveHistory.plus(myMove.first))
            }.toList().minOf { score(it.second, myColour) }
        }

        val movesThreateningMate = movesThatDontMateMe.filter { (myMove, _) ->
            calc.isCheckmate(myMove.second, moveHistory.plus(myMove.first), myColour)
        }.map { it.first.first }

        // If they don't have any pieces left, try to just reduce the number of moves they have, but try to offer at least 1
        if (originalBoard.pieces.count { it.colour == myColour.opposite() } == 1) {
            val result = movesThatDontMateMeLite.filter { it !in movesAllowingOpponentToCapture }.minByOrNull {
                val followUps = calc.getValidMoves(originalBoard.applyMove(it), moveHistory.plus(it), true).size
                if (followUps == 0) {
                    // avoid drawing
                    999
                } else {
                    followUps
                }
            }
            if (result != null) {
                return result
            }
        }

        var resultMove: Move? = null

        // capture scoring!
        resultMove = beneficialCaptures.maxByOrNull {
            movesAllowingOpponentToCapture[it] ?: score(originalBoard.applyMove(it), myColour)
        }
        if (resultMove != null) {
            logger.info("Beneficial capture: $resultMove")
            return resultMove
        }

        resultMove = movesThatDontMateMeLite.filter {
            it in movesThreateningMate && it !in movesAllowingOpponentToCapture
        }.maxByOrNull { score(originalBoard.applyMove(it), myColour) }
        if (resultMove != null) {
            logger.info("Threatens, and doesn't give up a piece: $resultMove")
            return resultMove
        }

        resultMove = movesThatDontMateMeLite.filter {
            it !in movesAllowingOpponentToCapture
        }.maxByOrNull { score(originalBoard.applyMove(it), myColour) }
        if (resultMove != null) {
            logger.info("Doesn't give up a piece: $resultMove")
            return resultMove
        }

        resultMove = movesThatDontMateMeLite.filter {
            it in movesThreateningMate
        }.maxByOrNull { score(originalBoard.applyMove(it), myColour) }
        if (resultMove != null) {
            logger.info("Threatens, but gives up a tradeoff: $resultMove")
            return resultMove
        }

        resultMove = movesAllowingOpponentToCapture.maxBy { it.value }.key
        logger.info("least bad, but gives up a tradeoff: $resultMove")
        return resultMove
    }

    // TODO can we add some depth to this, so it doesn't get weird when queens pin pawns to queens?
    private fun isBeneficialCaptureV1(move: Move, board: Board, moveHistory: List<Move>): Boolean {
        if (move.captures == null) {
            return false
        }

        val myColour = move.from.colour
        val originalScore = score(board, myColour)
        val nextBoard = board.applyMove(move)
        val nextHistory = moveHistory.plus(move)

        val followUps = calc.getValidMoves(nextBoard, nextHistory, true, myColour.opposite()).filter { it.captures != null }

        if (followUps.isEmpty()) {
            return true
        }
        val newScore = followUps.minOf { score(nextBoard.applyMove(it), myColour) }
        return newScore > originalScore
    }

    // actually pretty good! but very intensive, so maxed depth
    // still not perfect, but a hell of a lot better.
    private fun isBeneficialCapture(move: Move, board: Board, moveHistory: List<Move>, depth: Int = 0): Boolean {
        if (move.captures == null) {
            return false
        }
        // 6 is possible but sometimes it really bogs down
        // we need to do a pass to optimize this code!
        if (depth >= 4) {
            return true
        }

        val myColour = move.from.colour
        val originalScore = score(board, myColour) // does this need to be passed along? hmm
        val nextBoard = board.applyMove(move)
        val nextHistory = moveHistory.plus(move)

        val followUps = calc.getValidMoves(nextBoard, nextHistory, true, myColour.opposite()).filter {
            isBeneficialCapture(it, board.applyMove(it), moveHistory.plus(it), depth + 1)
        }

        if (followUps.isEmpty()) {
            return true
        }
        val newScore = followUps.minOf { score(nextBoard.applyMove(it), myColour) }
        return newScore > originalScore
    }

    private fun score(board: Board, myColour: Colour) = scorer.score(board) * if (myColour == Colour.BLACK) -1 else 1
}

/*
why does it think it can take that pawn?
my knight covers it from c6...

Opps last move: Nf3xe5 ... weird take
Pick:
dump
Piece.whitePawn(Location(Rank(2), File.A))
Piece.whitePawn(Location(Rank(2), File.B))
Piece.whitePawn(Location(Rank(2), File.C))
Piece.whitePawn(Location(Rank(2), File.F))
Piece.whitePawn(Location(Rank(2), File.G))
Piece.whitePawn(Location(Rank(2), File.H))
Piece.blackPawn(Location(Rank(7), File.A))
Piece.blackPawn(Location(Rank(7), File.B))
Piece.blackPawn(Location(Rank(7), File.F))
Piece.blackPawn(Location(Rank(7), File.G))
Piece.blackPawn(Location(Rank(7), File.H))
Piece.whiteKing(Location(Rank(1), File.E))
Piece.whiteBishop(Location(Rank(1), File.F))
Piece.whiteRook(Location(Rank(1), File.H))
Piece.blackRook(Location(Rank(8), File.H))
Piece.blackBishop(Location(Rank(6), File.E))
Piece.blackKing(Location(Rank(7), File.D))
Piece.whiteKnight(Location(Rank(8), File.A))
Piece.blackKnight(Location(Rank(6), File.C))
Piece.whiteBishop(Location(Rank(3), File.E))
Piece.blackBishop(Location(Rank(6), File.D))
Piece.whiteRook(Location(Rank(1), File.D))
Piece.blackKnight(Location(Rank(4), File.E))
Piece.whiteKnight(Location(Rank(5), File.E))
 */

/*
why does it hang its bishop? doesn't it know my rook can take it?

Opps last move: a2a3 -- should've saved bishop...
Pick:
dump
Piece.whitePawn(Location(Rank(2), File.B))
Piece.whitePawn(Location(Rank(2), File.C))
Piece.whitePawn(Location(Rank(2), File.F))
Piece.whitePawn(Location(Rank(2), File.G))
Piece.whitePawn(Location(Rank(2), File.H))
Piece.blackPawn(Location(Rank(7), File.B))
Piece.blackPawn(Location(Rank(7), File.F))
Piece.blackPawn(Location(Rank(7), File.G))
Piece.blackPawn(Location(Rank(7), File.H))
Piece.whiteKing(Location(Rank(1), File.E))
Piece.whiteBishop(Location(Rank(1), File.F))
Piece.whiteRook(Location(Rank(1), File.H))
Piece.blackBishop(Location(Rank(6), File.E))
Piece.blackKing(Location(Rank(7), File.D))
Piece.blackBishop(Location(Rank(6), File.D))
Piece.whiteRook(Location(Rank(1), File.D))
Piece.blackKnight(Location(Rank(4), File.E))
Piece.blackKnight(Location(Rank(5), File.E))
Piece.whiteBishop(Location(Rank(7), File.A))
Piece.blackRook(Location(Rank(8), File.A))
Piece.whitePawn(Location(Rank(3), File.A))
 */


// 1. d2d4 d7d5 2. Nb1c3 Nb8c6 3. e2e4 d5xe4 4. Nc3xe4 Nc6xd4 5. Ng1f3 e7e6 6. Qd1xd4 Ng8f6 7. Ne4xf6 g7xf6 8. a2a3 Rh8g8 9. b2b3 Rg8xg2 10. Bf1xg2 e6e5 11. Qd4d3 h7h5 12. Nf3xe5 f6xe5 13. Bg2xb7 Bc8xb7 14. h2h3 Bb7xh1 15. Ra1a2 Ra8b8 16. Bc1d2 a7a6 17. Qd3xa6 c7c5 18. Ke1e2 Qd8d7 19. h3h4 Rb8d8 20. c2c4 Ke8e7 21. a3a4 Bf8g7 22. a4a5 e5e4 23. Ra2c2 Bh1g2 24. Bd2e3 Qd7d1
// we make a bishop move at the end that gives up mate in 1.. we have guards against that I thought?
// we also don't defend a handing rook. and we sack a rook for no freaking reason!!


// I FIGURED OUT SOMETHING

// Lets say we have
/*
---QK
--N-
---p
----
---QK

It thinks that If we opponent take their knight, they will take our queen.
so it thinks that we can't claim their pieces
BUT that's not true :O

it's surprisingly good board vision
BUT
capture depth leaves something to be lacking..
that's a serious blind spot...


 */

/*
todo! figure out this mayhem


WE SACKED A QUEEN TO TAKE A KNIGHT WTF
Piece.whitePawn(Location(Rank(2), File.B))
Piece.whitePawn(Location(Rank(2), File.G))
Piece.whitePawn(Location(Rank(2), File.H))
Piece.blackPawn(Location(Rank(7), File.B))
Piece.blackPawn(Location(Rank(7), File.C))
Piece.blackPawn(Location(Rank(7), File.F))
Piece.blackPawn(Location(Rank(7), File.G))
Piece.whiteRook(Location(Rank(1), File.A))
Piece.whiteBishop(Location(Rank(1), File.C))
Piece.blackRook(Location(Rank(8), File.A))
Piece.blackKing(Location(Rank(8), File.E))
Piece.blackBishop(Location(Rank(8), File.F))
Piece.blackRook(Location(Rank(8), File.H))
Piece.whitePawn(Location(Rank(4), File.D))
Piece.whitePawn(Location(Rank(4), File.F))
Piece.blackBishop(Location(Rank(7), File.D))
Piece.whitePawn(Location(Rank(4), File.A))
Piece.blackKnight(Location(Rank(4), File.B))
Piece.blackPawn(Location(Rank(5), File.A))
Piece.whitePawn(Location(Rank(5), File.C))
Piece.blackPawn(Location(Rank(4), File.C))
Piece.whiteKing(Location(Rank(1), File.G))
Piece.blackPawn(Location(Rank(5), File.H))
Piece.whiteRook(Location(Rank(1), File.D))
Piece.blackPawn(Location(Rank(5), File.E))
Piece.whiteKnight(Location(Rank(3), File.F))
Piece.blackQueen(Location(Rank(5), File.G))

AND AGAIN, TAKING BISHOP WITH THE QUEEN! WTF!
IS IT BECAUSE WE CAN TAKE THE PAWN??

Piece.whitePawn(Location(Rank(2), File.A))
Piece.whitePawn(Location(Rank(2), File.B))
Piece.whitePawn(Location(Rank(2), File.C))
Piece.whitePawn(Location(Rank(2), File.F))
Piece.whitePawn(Location(Rank(2), File.G))
Piece.whitePawn(Location(Rank(2), File.H))
Piece.blackPawn(Location(Rank(7), File.A))
Piece.blackPawn(Location(Rank(7), File.B))
Piece.blackPawn(Location(Rank(7), File.C))
Piece.blackPawn(Location(Rank(7), File.E))
Piece.blackPawn(Location(Rank(7), File.F))
Piece.blackPawn(Location(Rank(7), File.G))
Piece.blackPawn(Location(Rank(7), File.H))
Piece.whiteRook(Location(Rank(1), File.A))
Piece.whiteKing(Location(Rank(1), File.E))
Piece.whiteBishop(Location(Rank(1), File.F))
Piece.whiteKnight(Location(Rank(1), File.G))
Piece.whiteRook(Location(Rank(1), File.H))
Piece.blackRook(Location(Rank(8), File.A))
Piece.blackKnight(Location(Rank(8), File.B))
Piece.blackQueen(Location(Rank(8), File.D))
Piece.blackKing(Location(Rank(8), File.E))
Piece.blackBishop(Location(Rank(8), File.F))
Piece.blackRook(Location(Rank(8), File.H))
Piece.whitePawn(Location(Rank(4), File.D))
Piece.blackKnight(Location(Rank(6), File.F))
Piece.whiteKnight(Location(Rank(3), File.C))
Piece.blackPawn(Location(Rank(4), File.E))
Piece.whiteBishop(Location(Rank(3), File.E))
Piece.whiteQueen(Location(Rank(4), File.G))


here we hang a bishop...
it's because we also hang a pawn.. but we decide that by taking the pawn, that's the least bad
NO! BAD!
IT'S THE SCORE OF THE FINAL RESULT THAT MATTERS, NOT THE IMMEDIATE NEXT STEP AUGHHH

Piece.whitePawn(Location(Rank(2), File.A))
Piece.whitePawn(Location(Rank(2), File.B))
Piece.whitePawn(Location(Rank(2), File.C))
Piece.whitePawn(Location(Rank(2), File.F))
Piece.whitePawn(Location(Rank(2), File.G))
Piece.whitePawn(Location(Rank(2), File.H))
Piece.blackPawn(Location(Rank(7), File.A))
Piece.blackPawn(Location(Rank(7), File.B))
Piece.blackPawn(Location(Rank(7), File.F))
Piece.blackPawn(Location(Rank(7), File.G))
Piece.blackPawn(Location(Rank(7), File.H))
Piece.whiteRook(Location(Rank(1), File.A))
Piece.whiteKing(Location(Rank(1), File.E))
Piece.whiteKnight(Location(Rank(1), File.G))
Piece.whiteRook(Location(Rank(1), File.H))
Piece.blackRook(Location(Rank(8), File.A))
Piece.blackKnight(Location(Rank(8), File.B))
Piece.blackQueen(Location(Rank(8), File.D))
Piece.blackKing(Location(Rank(8), File.E))
Piece.blackBishop(Location(Rank(8), File.F))
Piece.blackRook(Location(Rank(8), File.H))
Piece.whiteBishop(Location(Rank(3), File.E))
Piece.blackKnight(Location(Rank(4), File.G))
Piece.whiteKnight(Location(Rank(4), File.E))
Piece.whiteBishop(Location(Rank(5), File.B))
Piece.blackPawn(Location(Rank(6), File.C))
Piece.whitePawn(Location(Rank(5), File.E))


Nd4xb5
WHY
Piece.whitePawn(Location(Rank(2), File.A))
Piece.whitePawn(Location(Rank(2), File.F))
Piece.blackPawn(Location(Rank(7), File.B))
Piece.blackPawn(Location(Rank(7), File.H))
Piece.blackKing(Location(Rank(8), File.E))
Piece.blackRook(Location(Rank(8), File.H))
Piece.whitePawn(Location(Rank(3), File.C))
Piece.blackPawn(Location(Rank(6), File.F))
Piece.whiteRook(Location(Rank(2), File.G))
Piece.whitePawn(Location(Rank(3), File.B))
Piece.blackRook(Location(Rank(1), File.C))
Piece.whiteKing(Location(Rank(3), File.D))
Piece.blackPawn(Location(Rank(6), File.A))
Piece.whitePawn(Location(Rank(3), File.H))
Piece.blackBishop(Location(Rank(4), File.F))
Piece.whiteKnight(Location(Rank(5), File.B))

 */