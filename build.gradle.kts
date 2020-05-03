import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.72"
    // Check if we are using JDK 11+ and apply plugin for loading JavaFX if needed
    if(JavaVersion.current().isJava11Compatible) {
        id("org.openjfx.javafxplugin") version "0.0.8"
    }
}

group = "ru.mipt.npm"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("no.tornado:tornadofx:1.7.19")
    implementation(group= "com.github.javafaker", name= "javafaker", version = "0.16")
}

// Check if we are using JDK 11+ and apply plugin configuration if needed
if(JavaVersion.current().isJava11Compatible) {
    configure<org.openjfx.gradle.JavaFXOptions>{
        modules("javafx.controls")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

task("runDSLDemo", JavaExec::class) {
    group = "run"
    main = "DSLDemoApp"
    classpath = sourceSets["main"].runtimeClasspath
    if(JavaVersion.current().isJava11Compatible) {
        jvmArgs = listOf(
            "--module-path", files(configurations.compileClasspath).asPath,
            "--add-modules", "ALL-MODULE-PATH"
        )
    }
}

task("runTableDemo", JavaExec::class) {
    group = "run"
    main = "TableDemoApp"
    classpath = sourceSets["main"].runtimeClasspath
    if(JavaVersion.current().isJava11Compatible) {
        jvmArgs = listOf(
            "--module-path", files(configurations.compileClasspath).asPath,
            "--add-modules", "ALL-MODULE-PATH"
        )
    }
}