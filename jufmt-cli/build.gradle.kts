plugins {
    application
    alias(libs.plugins.asciidoctor)
    alias(libs.plugins.graalvmBuildtoolsNative)
    alias(libs.plugins.shadow)
}

group = "io.github.bric3.jufmt.app"

repositories {
    mavenCentral()
}

dependencies {
    nativeImageCompileOnly(libs.graalvm.nativeimage.svm)
    annotationProcessor(libs.picocli.codegen)
    implementation(libs.picocli)
    implementation(projects.jufmtLib)

    testImplementation(libs.assertj)
    testImplementation(libs.jupiter.params)
}

application {
    mainClass.set("io.github.bric3.jufmt.app.JufmtCommand")
}

val javaVersion = 17
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
