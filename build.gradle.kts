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
    alias(libs.plugins.quilt.licenser)
}

allprojects {
    apply(plugin=rootProject.libs.plugins.quilt.licenser.get().pluginId)
    license {
        rule(file("${rootDir}/HEADER"))
        include(
            "**/*.java",
            "**/*.properties",
            "**/*.yaml",
            "**/*.kt",
            "*.kts",
        )
        exclude("**/internal/figlet/**")
    }
}
