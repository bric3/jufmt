plugins {
    `java-library`
    alias(libs.plugins.download)
}

group = "io.github.bric3.jufmt"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(libs.assertj)
    testImplementation(libs.jupiter.params)
}

val javaVersion = 11
java {
    withSourcesJar()
//    toolchain {
//        languageVersion.set(JavaLanguageVersion.of(javaVersion))
//    }
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(javaVersion)
    }

    test {
        useJUnitPlatform()
    }

    val downloadFigletFonts by registering(de.undercouch.gradle.tasks.download.Download::class) {
        src("https://github.com/xero/figlet-fonts/archive/master.zip")
        dest("$buildDir/xero-figlet-fonts-master.zip")
        onlyIfModified(true)
        useETag("all") // Use the ETag on GH
    }

    val downloadAndUnzipFigletFonts by registering(Copy::class) {
        dependsOn(downloadFigletFonts)
        from(zipTree(downloadFigletFonts.get().dest)) {
            include("**/*.tlf", "**/*.flf")
            includeEmptyDirs = false
            eachFile {
                relativePath = RelativePath(true, *relativePath.segments.drop(1).toTypedArray())
            }
        }
        into("${sourceSets.main.get().output.resourcesDir}/banana/fonts")
    }

    processResources {
        dependsOn(downloadAndUnzipFigletFonts)
    }
}