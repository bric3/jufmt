pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("com.gradle.develocity") version "3.19"
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