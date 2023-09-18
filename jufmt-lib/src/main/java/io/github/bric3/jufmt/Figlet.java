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

import io.github.bric3.jufmt.internal.figlet.Ansi;
import io.github.bric3.jufmt.internal.figlet.FigletRenderer;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class Figlet {
    private static final FigletRenderer renderer = new FigletRenderer();

    /**
     * Returns the FIGlet of the text with the specified font.
     *
     * @param text the original text.
     * @param font the specified font.
     * @return the FIGlet of the text.
     */
    @NotNull
    public static String render(@NotNull String text, @NotNull FontSpec font) {
        String render = renderer.render(text, font);
        System.out.println("Render for " + text + " with " + font.getName() +
                           "\n" + render + "\n");
        return render;
    }

    /**
     * Returns the FIGlet of the text with a random font.
     *
     * @param text the original text.
     * @return the FIGlet of the text.
     */
    @NotNull
    public static String render(@NotNull String text) {
        return render(text, EmbeddedFigletFonts.random());
    }

    /**
     * Returns the FIGlet of the text with the specified font and styles.
     *
     * @param text   the original text.
     * @param font   the specified font.
     * @param styles the specified styles.
     * @return the FIGlet of the text.
     */
    public @NotNull String render(
            @NotNull String text,
            @NotNull Figlet.FontSpec font,
            @NotNull Ansi... styles
    ) {
        return renderer.render(
                text, font, null, null,
                s -> Ansi.apply(s, styles)
        );
    }

    public interface FontSpec {

        @NotNull
        String getName();

        @NotNull
        Path getPath();

        @NotNull
        default Charset getCharset() {
            return StandardCharsets.UTF_8;
        }

        ;

        static FontSpec of(@NotNull String name, @NotNull Path filename) {
            return new FontSpec() {
                @NotNull
                @Override
                public String getName() {
                    return name;
                }

                @NotNull
                @Override
                public Path getPath() {
                    return filename;
                }
            };
        }
    }
}
