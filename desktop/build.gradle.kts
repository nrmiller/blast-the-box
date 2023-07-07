import org.apache.tools.ant.taskdefs.condition.Os

plugins {
    id("blast_the_box.kotlin-application-conventions")
    id("blast_the_box.lwjgl-conventions")
    id("edu.sc.seis.launch4j") version "3.0.3"
    kotlin("jvm")
    kotlin("kapt")

    application
}

val appName = "Blast the Box"
val _mainClassName : String = "net.nicksneurons.blastthebox.client.MainKt"

group = "net.nicksneurons"
description = ""
version = "1.0-SNAPSHOT"

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {

    implementation("io.reactivex.rxjava3:rxkotlin:3.0.1")

    implementation("com.google.dagger:dagger:2.46.1")
    kapt("com.google.dagger:dagger-compiler:2.46.1")

    implementation(project(":fractal-dungeon:Tools"))
}

application {
    // Define the main class for the application.
    mainClass.set(_mainClassName)

    if (Os.isFamily(Os.FAMILY_MAC)) {
        // If running on macOS, need the application to run on the main thread of the process.
        applicationDefaultJvmArgs = listOf("-XstartOnFirstThread")
    }
}


tasks.jar {
    archiveBaseName.set(appName)

    manifest {
        attributes["Main-Class"] = _mainClassName
    }
}

tasks.register<Jar>("fatJar") {
    archiveBaseName.set(appName)
    archiveClassifier.set("standalone")

    manifest {
        attributes["Main-Class"] = _mainClassName
    }

    from(sourceSets["main"].output)

    // Copies all files in the runtime class path into the jar
    val deps = configurations.runtimeClasspath.get().files.map { if (it.isDirectory) it else zipTree(it) }
    from(deps).apply {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }
}

launch4j {
    headerType.set("gui")
    mainClassName.set(_mainClassName)
    outfile.set("$appName.exe")
    icon.set("${projectDir}/src/main/resources/blast_the_box.ico")
    productName.set(appName)
    version.set(project.version.toString())
    textVersion.set(project.version.toString())
    companyName.set(group.toString())
    fileDescription.set(appName)
    copyright.set("Copyright 2023 - Nicholas Miller")
}

kotlin {
    jvmToolchain(19)
}

sourceSets {
    main {
        java {
            exclude("net/nicksneurons/blastthebox/tmp")
        }
    }
}
