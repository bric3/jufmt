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
        into("${sourceSets.main.get().output.resourcesDir}/banana/fonts")
    }

    processResources {
        dependsOn(downloadAndUnzipFigletFonts)
    }
}

fun <C : PolymorphicDomainObjectContainer<Task>> C.registeringDownload(
    src: String,
): RegisteringDomainObjectDelegateProviderWithTypeAndAction<out C, de.undercouch.gradle.tasks.download.Download> =
    RegisteringDomainObjectDelegateProviderWithTypeAndAction.of(
        this,
        de.undercouch.gradle.tasks.download.Download::class
    ) {
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
            "${project.buildDir}/$repo-figlet-fonts-master.zip"
        })
        onlyIfModified(true)
        useETag("all") // Use the ETag on GH
    }