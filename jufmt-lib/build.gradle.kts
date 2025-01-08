/*
 * jufmt
 *
 * Copyright (c) 2023, today - Brice DUTHEIL
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
import net.thauvin.erik.urlencoder.UrlEncoderUtil
import org.jsoup.Jsoup
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

plugins {
    `java-library`
    alias(libs.plugins.download)
    alias(libs.plugins.testLogger)
}

buildscript {
    dependencies {
        classpath(libs.jsoup)
        classpath(libs.urlencoder)
    }
}

group = "io.github.bric3.jufmt"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.jetbrains.annotations)

    testImplementation(libs.assertj)
    testImplementation(libs.bundles.junit.jupiter)
}

val shippedJavaVersion = 11
val toolchainJavaVersion = 21

java {
    withSourcesJar()
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(toolchainJavaVersion))
        // Note this one doesn't enforce graalvm
    }
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
        options.release.set(shippedJavaVersion)
    }

    jar {
        from("${rootDir}/LICENSE") {
            rename { "${it}_${base.archivesName}" }
        }
    }

    test {
        useJUnitPlatform()
    }

    val downloadXeroFigletFonts by registeringDownload("https://github.com/xero/figlet-fonts")
    val downloadGangshitFigletFonts by registeringDownload("https://github.com/thugcrowd/gangshit")

    val downloadAndUnzipFigletFonts by registering(Copy::class) {
        dependsOn(
            downloadXeroFigletFonts,
            downloadGangshitFigletFonts,
        )

        dependsOn.filterIsInstance<TaskProvider<de.undercouch.gradle.tasks.download.Download>>().forEach {
            from(zipTree(it.get().dest)) {
                include("**/*.tlf", "**/*.flf")
                includeEmptyDirs = false
                eachFile {
                    relativePath = RelativePath(true, *relativePath.segments.drop(1).toTypedArray())
                }
            }
        }
        into("${sourceSets.main.get().output.resourcesDir}/jufmt/figlet-fonts")
    }

    val scrapeAolFonts by registering(AolFontScrapper::class) {
        dest.set("${sourceSets.main.get().output.resourcesDir}/jufmt/aol-fonts")
    }

    processResources {
        dependsOn(
            downloadAndUnzipFigletFonts,
            scrapeAolFonts
        )
    }
}

fun TaskContainer.registeringDownload(src: String) = registering(de.undercouch.gradle.tasks.download.Download::class) {
        src(
            when {
                src.endsWith("archive/master.zip") -> src
                else -> "${src.removeSuffix("/")}/archive/master.zip"
            }
        )
        dest(src.let {
            val repo = it.removePrefix("https://github.com/")
                .removeSuffix("/archive/master.zip")
                .replace("/", "-")
            project.layout.buildDirectory.file("$repo-figlet-fonts-master.zip")
        })
        onlyIfModified(true)
        useETag("all") // Use the ETag on GH
    }


abstract class AolFontScrapper @Inject constructor(
    private val project: Project,
) : DefaultTask() {
    @get:OutputDirectory
    abstract val dest: Property<String>

    @TaskAction
    fun scrape() {
        val aolFontFileNames = Jsoup.connect("https://patorjk.com/software/taag/").get()
            .body()
            // <select id="fontList">
            .select("select[id=fontList]")
            // <option value="Abraxis-Big.aol">Abraxis-Big</option><
            .select("option")
            .map { it.attr("value") }
            .filter { it.endsWith(".aol") }
            .toList()

        project.download.run {
            src(aolFontFileNames.map { "https://patorjk.com/software/taag/fonts/${it}".encodeFontName() })
            dest(project.layout.buildDirectory.dir("aol-fonts"))
            eachFile {
                name = URLDecoder.decode(name, StandardCharsets.UTF_8)
            }
            compress(true)
            tempAndMove(true)
            onlyIfNewer(true)
            onlyIfModified(true)
        }

        project.copy {
            from(project.layout.buildDirectory.dir("aol-fonts")) {
                include("*.aol")
            }
            into(dest)
        }
    }

    private fun String.encodeFontName() = this.replaceAfterLast('/', UrlEncoderUtil.encode(this.substringAfterLast('/')) )
}