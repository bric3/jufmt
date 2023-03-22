pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "jufmt"
include(
    "jufmt-cli",
    "jufmt-lib",
)

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")