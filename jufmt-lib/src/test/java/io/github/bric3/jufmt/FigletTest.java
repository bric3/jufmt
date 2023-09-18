/*
 * jufmt
 *
 * Copyright (c) 2023, today - Brice DUTHEIL
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package io.github.bric3.jufmt;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FigletTest {

    @Test
    void render() {
        assertThat(Figlet.render("jufmt", EmbeddedFigletFonts._1_Row))
                .isEqualToNormalizingNewlines(
                        "_T |_| /= |\\/| ~|~ \n" +
                        "                   "
                );
    }
}