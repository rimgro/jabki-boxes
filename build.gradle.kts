plugins {
    kotlin("jvm") version "1.9.0"
    id("com.github.johnrengelman.shadow") version "7.1.0"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
}

group = "space.rimgro.jabkibox"
version = "1.0-SNAPSHOT"
description = "жабкибоксы да"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") // Paper
    maven("https://jitpack.io") // Oraxen
    maven("https://repo.xenondevs.xyz/releases") // InvUI
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")
    compileOnly("dev.jorel:commandapi-bukkit-core:9.0.3")
    implementation("xyz.xenondevs.invui:invui:1.19")
    implementation("xyz.xenondevs.invui:invui-kotlin:1.19")
    compileOnly("com.github.oraxen:oraxen:1.162.0")
    implementation(files("libs/OutbreakLib-1.0-SNAPSHOT.jar"))
}

bukkit {
    version = rootProject.version.toString()
    name = rootProject.name
    main = "${rootProject.group}.JabkiBoxPlugin"
    apiVersion = "1.20"
    authors = listOf("OUTBREAK")
    description = rootProject.description
    softDepend = listOf("Oraxen")
}

kotlin {
    jvmToolchain(17)
}

tasks.shadowJar {
    relocate("xyz.xenondevs.invui", "${rootProject.group}.invui")

    archiveFileName.set("JabkiBox-${rootProject.version}.jar")
//    destinationDirectory.set(file("C:\\Users\\rimgro\\test_servers\\paper1.20.1\\plugins\\"))
}

tasks.assemble {
    dependsOn(tasks.shadowJar)
}