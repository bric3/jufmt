package io.github.bric3.jufmt;

import io.github.bric3.jufmt.internal.figlet.Ansi;
import io.github.bric3.jufmt.internal.figlet.FigletRenderer;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

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
        return renderer.render(text, font);
    }

    /**
     * Returns the FIGlet of the text with a random font.
     *
     * @param text the original text.
     * @return the FIGlet of the text.
     */
    @NotNull
    public static String render(@NotNull String text) {
        return render(text, XeroFonts.random());
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
        String getFilename();

        @NotNull
        default Charset getCharset() {
            return StandardCharsets.UTF_8;
        }

        ;

        static FontSpec of(@NotNull String name, @NotNull String filename) {
            return new FontSpec() {
                @NotNull
                @Override
                public String getName() {
                    return name;
                }

                @NotNull
                @Override
                public String getFilename() {
                    return filename;
                }
            };
        }
    }
}