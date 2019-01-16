import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.11"
    // Check if we are using JDK 11+ and apply plugin for loading JavaFX if needed
    if(JavaVersion.current().isJava11Compatible) {
        id("org.openjfx.javafxplugin") version "0.0.6"
    }
}

group = "ru.mipt.npm"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compile(kotlin("stdlib-jdk8"))
    compile("no.tornado:tornadofx:1.7.17")
    compile(group= "com.github.javafaker", name= "javafaker", version = "0.16")
}

// Check if we are using JDK 11+ and apply plugin configuration if needed
if(JavaVersion.current().isJava11Compatible) {
    apply(from = "build-jdk11.gradle")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}