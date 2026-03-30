plugins {
    id("buildsrc.convention.kotlin-jvm")
    alias(libs.plugins.kotlinPluginSerialization)
}

dependencies {
    implementation(libs.bundles.kotlinxEcosystem)
    implementation(libs.kotlinxCoroutines)
    implementation(project(":ValidMoveCalculator"))
    implementation(project(":ExampleChessBot"))
    testImplementation(kotlin("test"))

    implementation(libs.slf4j)
    runtimeOnly(libs.logback)
}