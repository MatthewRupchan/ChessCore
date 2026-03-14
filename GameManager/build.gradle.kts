plugins {
    id("buildsrc.convention.kotlin-jvm")
    application
}

dependencies {
    implementation(project(":ValidMoveCalculator"))
}

application {
    mainClass = "manager.application.MainKt"
}
