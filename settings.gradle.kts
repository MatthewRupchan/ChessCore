dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
    }
}

include(":GameManager")
include(":ValidMoveCalculator")
include(":ExampleChessBot")
include(":BasicBot")

rootProject.name = "ChessCore"