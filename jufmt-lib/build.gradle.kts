/*
 * jufmt
 *
 * Copyright (c) 2023, today - Brice DUTHEIL
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
import de.undercouch.gradle.tasks.download.Download
import net.thauvin.erik.urlencoder.UrlEncoderUtil
import org.gradle.kotlin.dsl.support.zipTo
import org.jsoup.Jsoup
import org.spdx.sbom.gradle.SpdxSbomTask
import java.io.ByteArrayOutputStream
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

plugins {
    `java-library`
    alias(libs.plugins.download)
    alias(libs.plugins.testLogger)
    alias(libs.plugins.spdx)
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

spdxSbom {
    targets {
        create("jufmtLibRelease") {
            configurations.set(listOf("compileClasspath")) // compilationClasspath
            scm {
                tool.set("git")
                uri.set("https://github.com/bric3/jufmt.git")
                revision.set(
                    providers.environmentVariable("GITHUB_SHA")
                        .orElse(providers.of(GitHeadSource::class) {})
                )
            }
            document {
                name.set("jufmt-lib")
                creator.set("Person: Brice Dutheil")
                packageSupplier.set("Person: Brice Dutheil")
                // NOTE: The URI does not have to be accessible. It is only intended to provide a unique ID.
                // In many cases, the URI will point to a Web accessible document, but this should not be assumed to be the case.
                // This property is the URI that should give this object a universally unique name. Although this property looks like a HTTP URL, it is in fact not. Technically speaking, a URL defined a Location, where as a URI defines an Identifier (i.e. the name by which something is known)
                namespace.set("https://github.com/bric3/jufmt/${UUID.randomUUID()}")
            }
        }
    }
}

val sbomCfg = configurations.maybeCreate("sbomForJufmtLibRelease").apply {
    isCanBeResolved = false
    isCanBeConsumed = true
}
val spdxSbomTaskTaskProvider = tasks.named<SpdxSbomTask>("spdxSbomForJufmtLibRelease")
val sbomArtifact = artifacts.add(sbomCfg.name, spdxSbomTaskTaskProvider) {
    type = "sbom"
    extension = "spdx.json"
    builtBy(spdxSbomTaskTaskProvider)
}

tasks.withType<AbstractPublishToMaven> {
    dependsOn(tasks.spdxSbom)
}
tasks.withType<Sign>().configureEach {
    mustRunAfter(tasks.spdxSbom)
}
tasks.withType<PublishToMavenLocal>().configureEach {
    publication.artifact(sbomArtifact)
}


abstract class GitHeadSource : ValueSource<String, ValueSourceParameters.None> {
    @get:Inject
    abstract val execOps : ExecOperations
    override fun obtain(): String {
        return try {
            val baos = ByteArrayOutputStream()
            execOps.exec {
                commandLine("git", "rev-parse", "HEAD")
                standardOutput = baos
            }
            baos.toString().trim()
        } catch (e: GradleException) {
            ""
        }
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

    val downloadXeroFigletFonts by registeringMasterDownload("https://github.com/xero/figlet-fonts", "figlet-fonts")
    val downloadGangshitFigletFonts by registeringMasterDownload(
        "https://github.com/thugcrowd/gangshit",
        "figlet-fonts"
    )
    val downloadAndUnzipFigletFonts by registering(Copy::class) {
        dependsOn(
            downloadXeroFigletFonts,
            downloadGangshitFigletFonts,
        )

        dependsOn.filterIsInstance<TaskProvider<Download>>().forEach { downloadTask ->
            from(zipTree(downloadTask.map { it.dest })) {
                include("**/*.tlf", "**/*.flf")
                includeEmptyDirs = false
                eachFile {
                    relativePath = RelativePath(true, *relativePath.segments.drop(1).toTypedArray())
                }
            }
        }
        into("${sourceSets.main.get().output.resourcesDir}/jufmt/figlet-fonts")
    }

    val downloadAolFonts by registering(Download::class) {
        val aolFontsRelease = project.property("aol-fonts-release").toString()
        src("https://github.com/bric3/jufmt/releases/download/$aolFontsRelease")
        dest(project.layout.buildDirectory.file(aolFontsRelease.substringAfterLast('/')))
        onlyIfModified(true)
        useETag("all") // Use the ETag on GH
    }

    val downloadAndUnzipAolFonts by registering(Copy::class) {
        dependsOn(downloadAolFonts)

        from(zipTree(downloadAolFonts.map { it.dest }))
        into("${sourceSets.main.get().output.resourcesDir}/aol-fonts")
    }

    /**
     * Used to scrap the font and create an archive.
     */
    val scrapeAolFonts by registering(AolFontScrapper::class) {
        dest.set(project.layout.buildDirectory.map { it.dir("aol-fonts-scrape") })
    }

    val patchPropertiesIfAolFontsChanged by registering {
        dependsOn(scrapeAolFonts)
        val propFile = rootProject.file("gradle.properties")
        outputs.files(propFile)

        val aolFontsReleaseList = project.property("aol-fonts-release").toString().replace(".zip", "-list.txt")
        val currentListFile = project.layout.buildDirectory
            .file("tmp/$name/${aolFontsReleaseList.substringAfterLast('/')}")
        doLast {
            download.run() {
                src("https://github.com/bric3/jufmt/releases/download/$aolFontsReleaseList")
                dest(currentListFile)
            }

            val currentAolFonts = currentListFile.get().asFile.readLines().toSet()

            val scraped = scrapeAolFonts.get().fontList.get().asFile.readLines().toSet()

            if (currentAolFonts.containsAll(scraped)) {
                throw GradleException("Same fonts")
            } else {
                propFile.run {
                    val patched = readText().replace(
                        Regex("aol-fonts-release\\s*=\\s*.+.zip"),
                        "aol-fonts-release=aol-fonts-${scrapeAolFonts.get().month}/${scrapeAolFonts.get().fontsArchiveFileName}"
                    )
                    writeText(patched)
                }

                logger.lifecycle("Scraped Aol Fonts are different, patched `gradle.properties`")
            }
        }
    }

    processResources {
        dependsOn(
            downloadAndUnzipFigletFonts,
            downloadAndUnzipAolFonts,
        )
    }
}

fun TaskContainer.registeringMasterDownload(
    src: String,
    typeTag: String? = null
) = registering(Download::class) {
    src(
        when {
            src.endsWith("archive/master.zip") -> src
            else -> "${src.removeSuffix("/")}/archive/master.zip"
        }
    )
    dest(
        src.removePrefix("https://github.com/")
            .removeSuffix("/archive/master.zip")
            .replace("/", "-")
            .let { repo ->
                project.layout.buildDirectory.file("$repo${typeTag?.let { "-${it}" } ?: ""}${if (src.endsWith("archive/master.zip")) "-master" else ""}.zip")
            }
    )
    onlyIfModified(true)
    useETag("all") // Use the ETag on GH
}


abstract class AolFontScrapper @Inject constructor(
    private val project: Project,
) : DefaultTask() {
    @get:OutputDirectory
    abstract val dest: DirectoryProperty

    @get:Internal
    val fontList: Provider<RegularFile>
        get() = dest.map { it.file(fontListsFileName) }

    @get:Internal
    val fontsArchive: Provider<RegularFile>
        get() = dest.map { it.file(fontsArchiveFileName) }

    @Internal
    val month = DateTimeFormatter.ofPattern("yyyy-MM").format(ZonedDateTime.now())
    @Internal
    val fontsArchiveFileName = "$month-aol-fonts.zip"
    @Internal
    val fontListsFileName = "$month-aol-fonts-list.txt"

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

        val tempDownloadDir = project.layout.buildDirectory.dir("tmp/$name/downloaded")
        project.download.run {
            src(aolFontFileNames.map { "https://patorjk.com/software/taag/fonts/${it}".encodeFontName() })
            dest(tempDownloadDir)
            eachFile {
                name = URLDecoder.decode(name, StandardCharsets.UTF_8)
            }
            compress(true)
            tempAndMove(true)
            onlyIfNewer(true)
            onlyIfModified(true)
        }

        // gen archive

        val aolFontsArchive = tempDownloadDir.map { it.file("../$fontsArchiveFileName") }
        zipTo(
            aolFontsArchive.get().asFile,
            tempDownloadDir.get().asFile
        )
        val listFile = tempDownloadDir.map { it.file("../$fontListsFileName") }
        project.file(listFile.get().asFile)
            .writeText(project.fileTree(dest).files.sorted().joinToString("\n") { it.name })

        dest.get().file("foo")

        // https://github.com/gradle/gradle/issues/31627
        project.copy {
            from(listFile, aolFontsArchive)
            into(dest)
        }
    }

    private fun String.encodeFontName() =
        this.replaceAfterLast('/', UrlEncoderUtil.encode(this.substringAfterLast('/')))
}