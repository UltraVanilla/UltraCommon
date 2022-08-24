import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    java
    id("net.minecrell.plugin-yml.bukkit") version "0.5.2"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "ultravanilla.common"
version = "1.0.0"
description = "Common utilities for UV plugin collection"

repositories {
    mavenCentral()
    maven { url = uri("https://papermc.io/repo/repository/maven-public/") }
}

dependencies {
    implementation("org.rocksdb", "rocksdbjni", "6.6.4")
    implementation("com.zaxxer", "HikariCP", "5.0.1")
    compileOnly("io.papermc.paper:paper-api:1.19.2-R0.1-SNAPSHOT")
    library("com.google.code.gson", "gson", "2.8.7")
    bukkitLibrary("com.google.code.gson", "gson", "2.8.7")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }

    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name()
    }

    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("UltraCommon")
        mergeServiceFiles()
        manifest {
            attributes(mapOf("Main-Class" to "ultravanilla.common.UltraCommon"))
        }
    }

    build {
        dependsOn(shadowJar)
    }
}

bukkit {
    author = "lordpipe"
    main = "ultravanilla.common.UltraCommon"
    apiVersion = "1.19"

    load = BukkitPluginDescription.PluginLoadOrder.STARTUP
}
