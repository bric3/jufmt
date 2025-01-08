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
    alias(libs.plugins.yumi.licenser)
}

allprojects {
    apply(plugin=rootProject.libs.plugins.yumi.licenser.get().pluginId)

    license {
        rule(rootProject.file("HEADER"))

        include(
            "**/*.java",
            "**/*.kt",
            "**/*.kts",
            "**/*.properties",
            "**/*.xml",
        )

        exclude("**/internal/figlet/**")
    }
    tasks {
        withType<JavaCompile>() {
            dependsOn(applyLicenses)
        }
    }
}

tasks {
    // About Daemon JVM Criteria: https://docs.gradle.org/current/userguide/gradle_daemon.html#sec:daemon_jvm_criteria
    updateDaemonJvm {
        jvmVersion = JavaLanguageVersion.of(21)
    }
}