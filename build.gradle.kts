plugins {
    application
    alias(libs.plugins.asciidoctor)
    alias(libs.plugins.graalvmBuildtoolsNative)
    alias(libs.plugins.shadow)
    alias(libs.plugins.download)
}

group = "io.github.bric3.jufmt"

repositories {
    mavenCentral()
}

dependencies {
    nativeImageCompileOnly(libs.graalvm.nativeimage.svm)
    annotationProcessor(libs.picocli.codegen)
    implementation(libs.picocli)

    testImplementation(libs.assertj)
    testImplementation(libs.jupiter.params)
}

application {
    mainClass.set("jufmt.JufmtCommand")
}

val javaVersion = 17
graalvmNative {
    metadataRepository {
        enabled.set(true)
    }
    binaries {
        named("main") {
            javaLauncher.set(javaToolchains.launcherFor {
                languageVersion.set(JavaLanguageVersion.of(javaVersion))
                vendor.set(JvmVendorSpec.matching("GraalVM Community"))
            })
            buildArgs.addAll(
                    "--native-image-info",
                    "-H:IncludeResources=banana/fonts/.*.[tf]lf\$",
                    // https://medium.com/graalvm/making-sense-of-native-image-contents-741a688dab4d
                    // https://www.graalvm.org/docs/tools/dashboard/?ojr=dashboard
                    "-H:DashboardDump=jufmt-native-image-dashboard",
                    "-H:+DashboardAll"
            )
            verbose.set(true)
            debug.set(false) // to play with native debugger in IJ
        }
    }
}

tasks {
    distZip { enabled = false }
    distTar { enabled = false }
    shadowDistZip { enabled = false }
    shadowDistTar { enabled = false }

    val downloadFigletFonts by registering(de.undercouch.gradle.tasks.download.Download::class) {
        src("https://github.com/xero/figlet-fonts/archive/master.zip")
        dest("$buildDir/xero-figlet-fonts-master.zip")
        onlyIfModified(true)
        useETag(true) // Use the ETag on GH
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

    compileJava {
        finalizedBy(downloadAndUnzipFigletFonts)
    }

    jar {
        dependsOn(downloadAndUnzipFigletFonts)
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(javaVersion)
    }
}

asciidoctorj {
    setVersion(libs.versions.asciidoctorj.get())
    modules {
        diagram.setVersion(libs.versions.asciidoctorjDiagram.get())
    }

    options(mapOf(
        "doctype" to "book",
        "ruby" to "erubis"
    ))

    attributes(mapOf(
        "sourcedir" to "src/docs/asciidoc",
        "source-highlighter" to "coderay",
        "toc" to "left",
        "idprefix" to "",
        "idseparator" to "-",
        "icons" to "font",
        "setanchors" to "",
        "listing-caption" to "",
        "imagesdir" to "images",
        "project-version" to "$project.version",
        "revnumber" to "$project.version"
    ))
}
