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
    implementation(libs.jetbrains.annotations)

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

    val downloadXeroFigletFonts by registering(DownloadGHMaster::class) {
        sourceUrl.set("https://github.com/xero/figlet-fonts")
    }
    val downloadGangshitFigletFonts by registering(DownloadGHMaster::class) {
        sourceUrl.set("https://github.com/thugcrowd/gangshit")
    }

    val downloadAndUnzipFigletFonts by registering(Copy::class) {
        dependsOn(
            downloadXeroFigletFonts,
            downloadGangshitFigletFonts,
        )

        dependsOn.filterIsInstance<TaskProvider<DownloadGHMaster>>().forEach {
            from(zipTree(it.get().dest)) {
                include("**/*.tlf", "**/*.flf")
                includeEmptyDirs = false
                eachFile {
                    relativePath = RelativePath(true, *relativePath.segments.drop(1).toTypedArray())
                }
            }
        }
        into("${sourceSets.main.get().output.resourcesDir}/banana/fonts")
    }

    processResources {
        dependsOn(downloadAndUnzipFigletFonts)
    }
}

@Suppress("LeakingThis")
abstract class DownloadGHMaster @Inject constructor(project: Project) : de.undercouch.gradle.tasks.download.Download() {
    @get:Input
    abstract val sourceUrl: Property<String>

    init {
        this.description = "Download github repo archive"

        val repoArchiveUrl =
            sourceUrl.map {
                when {
                    it.endsWith("archive/master.zip") -> it
                    else -> "${it.removeSuffix("/")}/archive/master.zip"
                }
            }

        val repoArchiveFile =
            sourceUrl.map {
                val repo = it.removePrefix("https://github.com/")
                    .removeSuffix("/archive/master.zip")
                    .replace("/", "-")
                "${project.buildDir}/$repo-figlet-fonts-master.zip"
            }

        src(repoArchiveUrl)
        dest(repoArchiveFile)
        onlyIfModified(true)
        useETag("all") // Use the ETag on GH
    }
}