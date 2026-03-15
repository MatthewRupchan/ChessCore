package calculator

enum class  Colour {
    WHITE,
    BLACK;

    fun opposite(): Colour {
        return when (this) {
            Colour.WHITE -> Colour.BLACK
            Colour.BLACK -> Colour.WHITE
        }
    }
}

enum class File(val value: Int) {
    A(1),
    B(2),
    C(3),
    D(4),
    E(5),
    F(6),
    G(7),
    H(8);

    fun leftOne(): File? = when (this) {
            File.A -> null
            File.B -> File.A
            File.C -> File.B
            File.D -> File.C
            File.E -> File.D
            File.F -> File.E
            File.G -> File.F
            File.H -> File.G
        }
    fun rightOne(): File? = when (this) {
        File.A -> File.B
        File.B -> File.C
        File.C -> File.D
        File.D -> File.E
        File.E -> File.F
        File.F -> File.G
        File.G -> File.H
        File.H -> null
    }

    operator fun minus(other: Int): File? {
        return File.fromValue(value - other)
    }
    operator fun plus(other: Int): File? {
        return File.fromValue(value + other)
    }
    companion object {
        fun fromValue(value: Int): File? = entries.find { it.value == value }
    }
}

data class Rank(val value: Int) {
    init {
        require(value in 1..8) { "Value must be between 1 and 8, inclusive: $value" }
    }

    operator fun minus(other: Int): Rank? {
        val new = value - other
        return if (new in 1..8) {
            Rank(new)
        } else {
            null
        }
    }
    operator fun plus(other: Int): Rank? {
        val new = value + other
        return if (new in 1..8) {
            Rank(new)
        } else {
            null
        }
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
    fun atLocation(newLocation: Location): Piece {
        return Piece(colour, pieceType, newLocation)
    }
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

data class Board(val pieces: List<Piece>) {
    fun isPieceAt(location: Location): Piece? {
        return pieces.find { piece -> piece.location == location }
    }
}

data class Move(val from: Piece, val to: Piece, val capture: Piece? = null)