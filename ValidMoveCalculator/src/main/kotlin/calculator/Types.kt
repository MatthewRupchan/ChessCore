package calculator

enum class  Colour {
    WHITE,
    BLACK
}

enum class File(val value: Int) {
    A(1),
    B(2),
    C(3),
    D(4),
    E(5),
    F(6),
    G(7),
    H(8)
}

data class Rank(val value: Int) {
    init {
        require(value in 1..8) { "Value must be between 1 and 8, inclusive: $value" }
    }
}

enum class PieceType {
    PAWN,
    ROOK,
    KNIGHT,
    BISHOP,
    KING,
    QUEEN,
}

data class Location(val rank: Rank, val file: File)

data class Piece(val colour: Colour, val pieceType: PieceType, val location: Location) {
    companion object {
        fun whitePawn(location: Location): Piece {
            return Piece(Colour.WHITE, PieceType.PAWN, location)
        }
        fun blackPawn(location: Location): Piece {
            return Piece(Colour.BLACK, PieceType.PAWN, location)
        }
        fun whiteKnight(location: Location): Piece {
            return Piece(Colour.WHITE, PieceType.KNIGHT, location)
        }
        fun blackKnight(location: Location): Piece {
            return Piece(Colour.BLACK, PieceType.KNIGHT, location)
        }
        fun whiteBishop(location: Location): Piece {
            return Piece(Colour.WHITE, PieceType.BISHOP, location)
        }
        fun blackBishop(location: Location): Piece {
            return Piece(Colour.BLACK, PieceType.BISHOP, location)
        }
        fun whiteRook(location: Location): Piece {
            return Piece(Colour.WHITE, PieceType.ROOK, location)
        }
        fun blackRook(location: Location): Piece {
            return Piece(Colour.BLACK, PieceType.ROOK, location)
        }
        fun whiteQueen(location: Location): Piece {
            return Piece(Colour.WHITE, PieceType.QUEEN, location)
        }
        fun blackQueen(location: Location): Piece {
            return Piece(Colour.BLACK, PieceType.QUEEN, location)
        }
        fun whiteKing(location: Location): Piece {
            return Piece(Colour.WHITE, PieceType.KING, location)
        }
        fun blackKing(location: Location): Piece {
            return Piece(Colour.BLACK, PieceType.KING, location)
        }
    }
}

data class Board(val pieces: List<Piece>)

data class Move(val from: Piece, val to: Piece, val capture: Piece? = null)