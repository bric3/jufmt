pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version ("0.9.0")
    id("com.gradle.develocity") version "4.0"
}

develocity {
    buildScan {
        termsOfUseUrl = "https://gradle.com/terms-of-service"
        termsOfUseAgree = "yes"
        // publish on failure only when NOT running on CI
        publishing {
            onlyIf {
                it.buildResult.failures.isNotEmpty() && !providers.environmentVariable("CI").isPresent
            }
        }
    }
}

rootProject.name = "jufmt"
include(
    "jufmt-cli",
    "jufmt-lib",
)

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")