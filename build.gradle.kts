plugins {
    kotlin("jvm") version "2.2.20"
    application
}

group = "org.alloyreduce"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("org.alloyreduce.MainKt")
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.alloytools:org.alloytools.alloy.dist:6.2.0")
    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.6")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
