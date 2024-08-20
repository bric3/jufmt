pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("com.gradle.enterprise") version "3.18"
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
        // publish on failure only when NOT running on CI
        publishOnFailureIf(System.getenv("CI").isNullOrEmpty())
    }
}

rootProject.name = "jufmt"
include(
    "jufmt-cli",
    "jufmt-lib",
)

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")