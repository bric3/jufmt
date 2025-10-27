pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version ("1.0.0")
    id("com.gradle.develocity") version "4.2.2"
}

rootProject.name = "jufmt"
include(
    "jufmt-cli",
    "jufmt-lib",
)

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

develocity {
    buildScan {
        termsOfUseUrl = "https://gradle.com/help/legal-terms-of-use"
        // termsOfUseAgree is handled by .gradle/init.d/configure-develocity.init.gradle.kts

        // Only publish on failure when NOT running on CI
        publishing {
            onlyIf {
                it.buildResult.failures.isNotEmpty() && !providers.environmentVariable("CI").isPresent
            }
        }
    }
}
