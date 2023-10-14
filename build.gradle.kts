import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.0"
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

group = "space.rimgro.jabkiBoxes"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://jitpack.io") // Репозиторий для CommandAPI
    maven("https://repo.xenondevs.xyz/releases")
}

dependencies {
    testImplementation(kotlin("test"))
    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")
    implementation("dev.jorel:commandapi-bukkit-shade:9.0.3")
    val jacksonVersion = "2.14.3"
    implementation("com.fasterxml.jackson.core:jackson-databind:${jacksonVersion}")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:${jacksonVersion}")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:${jacksonVersion}")
    implementation("org.apache.commons:commons-text:1.10.0")
    implementation("xyz.xenondevs.invui:invui:1.19")
    implementation("xyz.xenondevs.invui:invui-kotlin:1.19")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks.shadowJar {
    relocate("dev.jorel.commandapi", "space.rimgro.jabkiBoxes.commandapi")
    relocate("xyz.xenondevs.invui", "space.rimgro.jabkiBoxes.invui")
    archiveFileName.set("JabkiBoxes-${rootProject.version}.jar")
    destinationDirectory.set(file("C:\\Users\\rimgro\\test_servers\\paper1.20.1\\plugins\\"))
}

tasks.assemble {
    dependsOn(tasks.shadowJar)
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}