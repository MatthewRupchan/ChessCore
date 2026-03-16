plugins {
    id("buildsrc.convention.kotlin-jvm")
    alias(libs.plugins.kotlinPluginSerialization)
}

dependencies {
    implementation(libs.bundles.kotlinxEcosystem)
    implementation(project(":ValidMoveCalculator"))
    testImplementation(kotlin("test"))

    implementation(libs.slf4j)
    runtimeOnly(libs.logback)
}