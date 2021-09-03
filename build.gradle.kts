import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI

group = "me.oska"
version = ""

val kotlinVersion = "1.5.10"

plugins {
    java
    kotlin("jvm") version "1.5.10"
    id("com.github.johnrengelman.shadow") version "5.0.0"
}

java {
    sourceCompatibility = JavaVersion.VERSION_16
    targetCompatibility = JavaVersion.VERSION_16
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.freeCompilerArgs = listOf("-Xjvm-default=compatibility")
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://oss.sonatype.org/content/repositories/central")
}

dependencies {
    compileOnly(kotlin("stdlib-jdk8", kotlinVersion))
    compileOnly(fileTree("src/main/libs"))
    implementation(fileTree("src/main/shaded"))

    compileOnly("org.spigotmc:spigot-api:1.17.1-R0.1-SNAPSHOT")
}

val sourcesJar by tasks.registering(Jar::class) {
    dependsOn(JavaPlugin.CLASSES_TASK_NAME)
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
    manifest {
        attributes(mapOf("Main-Class" to "$group/SurvivalTool"))
    }
}

val shadowJar = (tasks["shadowJar"] as ShadowJar).apply {
    exclude("kotlin/**")
}

val deployPath: String by project
val deployPlugin by tasks.registering(Copy::class) {
    dependsOn(shadowJar)

    System.getenv("PLUGIN_DEPLOY_PATH")?.let {
        from(shadowJar)
        into(it)
    }
}

val build = (tasks["build"] as Task).apply {
    arrayOf(
        sourcesJar
        , shadowJar
        , deployPlugin
    ).forEach { dependsOn(it) }
}