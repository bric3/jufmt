plugins {
    `application`
    id("org.graalvm.buildtools.native") version "0.9.15"
    id("org.asciidoctor.jvm.convert") version "3.3.2"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("de.undercouch.download") version "5.3.0"
}

group = "io.github.bric3.jufmt"

apply(from="gradle/asciidoc.gradle")
repositories {
    mavenCentral()
}

graalvmNative {
    metadataRepository {
        enabled.set(true)
    }
    binaries {
        named("main") {
            javaLauncher.set(javaToolchains.launcherFor {
                languageVersion.set(JavaLanguageVersion.of(17))
                vendor.set(JvmVendorSpec.matching("GraalVM Community"))
            })
            buildArgs.addAll(
                    // "--no-fallback",
                    "--native-image-info",
                    "--verbose",
                    "-H:IncludeResources=java/lang/uniName.dat\$" /* https://github.com/oracle/graal/issues/3133 */,
                    "-H:IncludeResources=banana/fonts/.*.[tf]lf\$",
                    // https://medium.com/graalvm/making-sense-of-native-image-contents-741a688dab4d
                    // https://www.graalvm.org/docs/tools/dashboard/?ojr=dashboard
                    "-H:DashboardDump=jufmt-native-image-dashboard",
                    "-H:+DashboardAll"
            )
        }
    }
}

dependencies {
    compileOnly("org.graalvm.nativeimage:svm:22.2.0.1")

    annotationProcessor("info.picocli:picocli-codegen:4.6.3")
    implementation("info.picocli:picocli:4.6.3")

    testImplementation("org.assertj:assertj-core:3.23.1")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.1")
}


application {
    mainClass.set("jufmt.JufmtCommand")
}

tasks {
    distZip { enabled = false }
    distTar { enabled = false }
    shadowDistZip { enabled = false }
    shadowDistTar { enabled = false }

    val downloadFigletFonts by registering(de.undercouch.gradle.tasks.download.Download::class) {
        src("https://github.com/xero/figlet-fonts/archive/master.zip")
        dest("$buildDir/xero-figlet-fonts-master.zip")
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

    named("compileJava") {
        finalizedBy(downloadAndUnzipFigletFonts)
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(17)
    }
}

