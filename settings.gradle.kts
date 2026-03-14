dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
    }
}

include(":GameManager")
include(":ValidMoveCalculator")

rootProject.name = "ChessCore"