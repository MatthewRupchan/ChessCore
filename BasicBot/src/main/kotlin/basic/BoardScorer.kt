package basic

import calculator.Board
import calculator.Colour
import calculator.File
import calculator.Location
import calculator.Piece
import calculator.PieceType

interface BoardScorer {
    fun score(board: Board): Int
}


class BoardScorer1 : BoardScorer {
    override fun score(board: Board): Int {
        val (white, black) = board.pieces.partition { it.colour == Colour.WHITE }
        return score(white, Colour.WHITE) - score(black, Colour.BLACK)
    }

    private fun score(pieces: List<Piece>, colour: Colour)  = pieces.sumOf {
        when(it.pieceType) {
            PieceType.PAWN -> scorePawn(it.location, colour)
            PieceType.ROOK -> 12
            PieceType.KNIGHT -> scoreKnight(it.location, colour)
            PieceType.BISHOP -> scoreBishop(it.location, pieces)
            PieceType.KING -> 500
            PieceType.QUEEN -> 21
        }
    }

    private val pawnScoreMatrix = mapOf(
        File.A to listOf(0, 2, 2, 2, 3, 4, 6, 0),
        File.B to listOf(0, 2, 2, 1, 3, 4, 5, 0),
        File.C to listOf(0, 2, 1, 2, 3, 4, 5, 0),
        File.D to listOf(0, 1, 2, 3, 3, 4, 5, 0),
        File.E to listOf(0, 1, 2, 3, 3, 4, 5, 0),
        File.F to listOf(0, 2, 1, 1, 3, 4, 5, 0),
        File.G to listOf(0, 2, 2, 1, 3, 4, 5, 0),
        File.H to listOf(0, 2, 2, 2, 3, 4, 6, 0),
    )

    private fun scorePawn(location: Location, colour: Colour): Int {
        val indexRank = if (colour == Colour.BLACK) {
            8 - location.rank.value
        } else {
            location.rank.value - 1
        }
        return pawnScoreMatrix[location.file]!![indexRank]
    }

    private fun scoreKnight(location: Location, colour: Colour): Int {
        var score  = 6
        if (location.file == File.H || location.file == File.A || location.rank.value == 1 || location.rank.value == 8) {
                score -= 1
        }
        if (colour == Colour.WHITE && location.rank.value >= 3) {
            return score + 1
        }
        if (colour == Colour.BLACK && location.rank.value <= 5) {
            return score + 1
        }
        return score
    }

    private fun scoreBishop(location: Location, myPieces: List<Piece>): Int {
        val myColour = location.squareColour()
        val myPawnsOnMyColour =
            myPieces.filter { it.pieceType == PieceType.PAWN }.count { it.location.squareColour() == myColour }

        var score = 6
        if (myPieces.count { it.pieceType == PieceType.BISHOP } >= 2) {
            score += 1
        }
        if (myPawnsOnMyColour >= 5) {
            score -= 1
        } else {
            score += 1
        }
        return score
    }

    private fun Location.squareColour(): Colour = if((rank.value + file.value) % 2 == 0) {
        Colour.BLACK
    } else {
        Colour.WHITE
    }
}


class BoardScorer2 : BoardScorer {
    override fun score(board: Board): Int {
        val (white, black) = board.pieces.partition { it.colour == Colour.WHITE }
        return score(white) - score(black)
    }

    private fun score(pieces: List<Piece>)  = pieces.sumOf {
        when(it.pieceType) {
            PieceType.PAWN -> 1
            PieceType.ROOK -> 5
            PieceType.KNIGHT -> 3
            PieceType.BISHOP -> 3
            PieceType.KING -> 100
            PieceType.QUEEN -> 9
        }
    }
}



class BoardScorer3 : BoardScorer {
    override fun score(board: Board): Int {
        val (white, black) = board.pieces.partition { it.colour == Colour.WHITE }
        return score(white, Colour.WHITE) - score(black, Colour.BLACK)
    }

    private fun score(pieces: List<Piece>, colour: Colour)  = pieces.sumOf {
        when(it.pieceType) {
            PieceType.PAWN -> scorePawn(it.location, colour, pieces)
            PieceType.ROOK -> scoreRook(it.location, pieces)
            PieceType.KNIGHT -> scoreKnight(it.location, colour, pieces)
            PieceType.BISHOP -> scoreBishop(it.location, pieces)
            PieceType.KING -> scoreKing(it.location, pieces)
            PieceType.QUEEN -> 24
        }
    } // 23. Rh1h2 Ke8f7

    private val pawnScoreMatrix = mapOf(
        File.A to listOf(0, 2, 2, 2, 2, 3, 4, 0),
        File.B to listOf(0, 2, 2, 1, 2, 3, 4, 0),
        File.C to listOf(0, 2, 1, 2, 2, 3, 4, 0),
        File.D to listOf(0, 1, 2, 3, 3, 3, 4, 0),
        File.E to listOf(0, 1, 2, 3, 3, 3, 4, 0),
        File.F to listOf(0, 2, 1, 1, 2, 3, 4, 0),
        File.G to listOf(0, 2, 2, 1, 2, 3, 4, 0),
        File.H to listOf(0, 2, 2, 2, 2, 3, 4, 0),
    )

    private fun scoreKing(location: Location, pieces: List<Piece>): Int {
        var score = 500
        if (pieces.count() > 7) {
            if (location.file in listOf(File.B, File.C, File.G)) {
                score += 1
            }
            if (location.rank.value == 1 || location.rank.value == 8) {
                score += 1
            }
            return score
        }
        // guard our pawns
        if (pieces.filter { it.pieceType == PieceType.PAWN }.any { it.location - location == 1 }) {
            score += 1
        }
        return score
    }

    private fun scorePawn(location: Location, colour: Colour, pieces: List<Piece>): Int {
        val indexRank = if (colour == Colour.BLACK) {
            8 - location.rank.value
        } else {
            location.rank.value - 1
        }
        if (pieces.count() < 8) {
            return indexRank
        }
        return pawnScoreMatrix[location.file]!![indexRank]
    }

    private fun scoreRook(location: Location, pieces: List<Piece>): Int {
        var score = 13
        if (location.file == File.D || location.file == File.E || location.file == File.F) {
            score += 1
        }
        if (
            // take open files in the early game
            pieces.none { it.pieceType == PieceType.PAWN && it.location.file == location.file }
            // support our pawn promos in the lategame
            && pieces.count { it.pieceType == PieceType.PAWN } > 3
            ) {
            score += 2
        }

        if (
            // support our pawn promos in the lategame
            pieces.any { it.pieceType == PieceType.PAWN && it.location.file == location.file }
            && pieces.count { it.pieceType == PieceType.PAWN } <= 3
        ) {
            score += 1
        }

        val connetables = pieces.filter { it.pieceType == PieceType.ROOK || it.pieceType == PieceType.QUEEN }
        if (connetables.filter { it.location.file == location.file || it.location.rank == location.rank}.count() >= 2) {
            // connecting the rooks / with the queen too, is beneficial
            score += 1
        }

        return score
    }

    private fun scoreKnight(location: Location, colour: Colour, pieces: List<Piece>): Int {
        var score  = 8
        if (location.file == File.H || location.file == File.A || location.rank.value == 1 || location.rank.value == 8) {
            score -= 1
        }
        if (pieces.count() > 7) {
            if (colour == Colour.WHITE && location.rank.value >= 3) {
                return score + 1
            }
            if (colour == Colour.BLACK && location.rank.value <= 5) {
                return score + 1
            }
        }
        return score
    }

    private fun scoreBishop(location: Location, myPieces: List<Piece>): Int {
        val myColour = location.squareColour()
        val myPawnsOnMyColour =
            myPieces.filter { it.pieceType == PieceType.PAWN }.count { it.location.squareColour() == myColour }

        var score = 8
        if (myPieces.count { it.pieceType == PieceType.BISHOP } >= 2) {
            score += 1
        }
        if (myPawnsOnMyColour >= 5) {
            score -= 1
        } else {
            score += 1
        }

        if (location.rank.value == 1 || location.rank.value == 8) {
            score -= 1
        }

        return score
    }

    private fun Location.squareColour(): Colour = if((rank.value + file.value) % 2 == 0) {
        Colour.BLACK
    } else {
        Colour.WHITE
    }
}
