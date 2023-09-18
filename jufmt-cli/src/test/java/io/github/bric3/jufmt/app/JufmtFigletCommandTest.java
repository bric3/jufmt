/*
 * jufmt
 *
 * Copyright (c) 2023, today - Brice DUTHEIL
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package io.github.bric3.jufmt.app;

import io.github.bric3.jufmt.EmbeddedFigletFonts;
import io.github.bric3.jufmt.Figlet;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.regex.Pattern;

import static io.github.bric3.jufmt.app.JufmtTestUtil.jufmt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

public class JufmtFigletCommandTest {
    @Test
    void no_option_means_random() {
        try (var mockedStatic = Mockito.mockStatic(Figlet.class)) {
            mockedStatic.when(() -> Figlet.render(eq("bric3")))
                    .thenReturn("A rendered string by figlet");

            var result = jufmt("figlet", "bric3");

            assertThat(result.out()).isEqualToIgnoringNewLines("A rendered string by figlet");
        }
    }

    @Test
    void can_pick_random_font() {
        try (var mockedStatic = Mockito.mockStatic(Figlet.class)) {
            mockedStatic.when(() -> Figlet.render(eq("bric3")))
                    .thenReturn("A rendered string by figlet");

            var result = jufmt("figlet", "--random", "bric3");

            assertThat(result.out()).isEqualToIgnoringNewLines("A rendered string by figlet");
        }
    }

    @Test
    void can_render_all_font() {
        try (var mockedStatic = Mockito.mockStatic(Figlet.class)) {
            mockedStatic.when(() -> Figlet.render(eq("bric3"), any(EmbeddedFigletFonts.class)))
                    .thenReturn("A rendered string by figlet");

            var result = jufmt("figlet", "--all", "bric3");

            assertThat(result.out()).contains(Arrays.stream(EmbeddedFigletFonts.values()).map(Enum::name).toList());
            assertThat(
                    Pattern.compile("A rendered string by figlet")
                            .matcher(result.out())
                            .results().count()
            ).isEqualTo(EmbeddedFigletFonts.values().length);
        }
    }

    @Test
    void supports_passing_font() {
        try (var mockedStatic = Mockito.mockStatic(Figlet.class)) {
            mockedStatic.when(() -> Figlet.render("jufmt", EmbeddedFigletFonts._3d))
                    .thenReturn("A rendered string by figlet");

            var result = jufmt("figlet", "--font", EmbeddedFigletFonts._3d.name(), "jufmt");

            assertThat(result.out()).isEqualToIgnoringNewLines("A rendered string by figlet");
        }
    }
}
