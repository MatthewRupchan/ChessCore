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
    QUEEN;
}

data class Location(val rank: Rank, val file: File) {
    operator fun plus(other: Pair<Int, Int>): Location? {
        return (rank + other.first)?.let { r ->
            (file + other.second)?.let { f ->
                Location(r, f)
            }
        }
    }

    fun getNotation(): String {
        return "${file.name.lowercase()}${rank.value}"
    }
}

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

    fun applyMove(move: Move): Board {
        val pieces = pieces.toMutableList()
        pieces.remove(move.from)
        move.captures?.also { pieces.remove(it) }
        pieces.add(move.to)
        return Board(pieces.toList())
    }
}

data class Move(val froms: List<Piece>, val tos: List<Piece>, val captures: Piece? = null) {
    constructor(from: Piece, to: Piece, capture: Piece? = null) : this(listOf(from), listOf(to), capture)

    val from: Piece
        get() {
            return froms.firstOrNull { it.pieceType == PieceType.KING } ?: froms.first()
        }
    val to: Piece
        get() {
            return tos.firstOrNull { it.pieceType == PieceType.KING } ?: tos.first()
        }

    fun getNotation(): String {
        if (froms.size > 1) {
            if (froms.first { it.pieceType == PieceType.ROOK }.location.file == File.H) {
                return "0-0"
            }
            return "0-0-0"
        }

        val detailedNotation = from.location.getNotation() + (captures?.let { "x" } ?: "") + to.location.getNotation()

        if (from.pieceType != to.pieceType) {
            return when(to.pieceType) {
                PieceType.ROOK -> "$detailedNotation=R"
                PieceType.KNIGHT -> "$detailedNotation=N"
                PieceType.BISHOP -> "$detailedNotation=B"
                PieceType.QUEEN -> "$detailedNotation=Q"
                else -> throw IllegalStateException("Pawns cannot promote like that $this")
            }
        }

        return when(from.pieceType) {
            PieceType.PAWN -> detailedNotation
            PieceType.ROOK -> "R$detailedNotation"
            PieceType.KNIGHT -> "N$detailedNotation"
            PieceType.BISHOP -> "B$detailedNotation"
            PieceType.KING -> "K$detailedNotation"
            PieceType.QUEEN -> "Q$detailedNotation"
        }
    }
}