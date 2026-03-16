dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
    }
}

include(":GameManager")
include(":ValidMoveCalculator")
include(":ExampleChessBot")

rootProject.name = "ChessCore"