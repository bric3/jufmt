/*
 * jufmt
 *
 * Copyright (c) 2023, today - Brice DUTHEIL
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

plugins {
    application
    alias(libs.plugins.asciidoctor)
    alias(libs.plugins.graalvmBuildtoolsNative)
    alias(libs.plugins.shadow)
    alias(libs.plugins.testLogger)
}

group = "io.github.bric3.jufmt.app"

repositories {
    mavenCentral()
}

dependencies {
    annotationProcessor(libs.picocli.codegen)
    implementation(libs.picocli)
    implementation(projects.jufmtLib)

    testImplementation(libs.assertj)
    testImplementation(libs.bundles.junit.jupiter)
}

application {
    mainClass.set("io.github.bric3.jufmt.app.JufmtCommand")
}

val javaVersion = 20

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(javaVersion))
        // Note this one doesn't enforce graalvm
    }
}

// Workaround for https://github.com/gradle/gradle/issues/18426
val javaToolchainLauncher = javaToolchains.launcherFor(java.toolchain)
val javaToolchainCompiler = javaToolchains.compilerFor(java.toolchain)

graalvmNative {
    metadataRepository {
        enabled.set(true)
    }
    binaries {
        named("main") {
            imageName.set("jufmt")
            javaLauncher.set(javaToolchains.launcherFor {
                languageVersion.set(JavaLanguageVersion.of(javaVersion))
                vendor.set(JvmVendorSpec.GRAAL_VM)
            })
            buildArgs.addAll(
                "--native-image-info",
                // Log resources
                "-H:Log=registerResource:3",
                // https://medium.com/graalvm/making-sense-of-native-image-contents-741a688dab4d
                // https://www.graalvm.org/docs/tools/dashboard/?ojr=dashboard
                "-H:DashboardDump=jufmt-native-image-dashboard",
                "-H:+DashboardAll",

                // https://www.graalvm.org/latest/reference-manual/native-image/guides/create-heap-dump/
                // https://www.graalvm.org/latest/reference-manual/native-image/guides/build-and-run-native-executable-with-jfr/
                // https://github.com/oracle/graal/blob/master/docs/reference-manual/native-image/JFR.md
                // Use with `./jufmt {args} -XX:+FlightRecorder -XX:StartFlightRecording="filename=recording.jfr`
                "--enable-monitoring=jfr,heapdump,jvmstat,jmxclient,jmxserver",

                // Not available in GraalVM community edition
                // "--enable-sbom=cyclonedx",
            )
            verbose.set(true)
            debug.set(false) // to play with native debugger in IJ

            runtimeArgs.addAll(
                "-XX:StartFlightRecording=filename=recording.jfr"
            )
        }
    }
}

tasks {
    distZip { enabled = false }
    distTar { enabled = false }
    shadowDistZip { enabled = false }
    shadowDistTar { enabled = false }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(javaVersion)
    }

    withType<JavaExec> {
        javaLauncher.set(javaToolchainLauncher)
    }

    test {
        dependsOn(":jufmt-lib:test")
        useJUnitPlatform()
    }
}

asciidoctorj {
    setVersion(libs.versions.asciidoctorj.get())
    modules {
        diagram.setVersion(libs.versions.asciidoctorjDiagram.get())
    }

    options(
        mapOf(
            "doctype" to "book",
            "ruby" to "erubis"
        )
    )

    attributes(
        mapOf(
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
        )
    )
}
