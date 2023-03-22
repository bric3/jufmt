package io.github.bric3.jufmt.internal.figlet;

import io.github.bric3.jufmt.Figlet;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Yihleego
 */
class FontMetadata {
    public static final List<Integer> CODES = IntStream.concat(
                    // Adds ASCII codes.
                    IntStream.range(32, 127),
                    // Adds extra codes.
                    IntStream.of(196, 214, 220, 223, 228, 246, 252)
            )
            .boxed()
            .collect(Collectors.toUnmodifiableList());
    @NotNull
    public final Figlet.FontSpec font;
    @NotNull
    public final Option option;
    @NotNull
    public final Map<Integer, String[]> figletMap;
    @NotNull
    public final String comment;

    private FontMetadata(
            @NotNull Figlet.FontSpec font,
            @NotNull Option option,
            @NotNull Map<Integer, String[]> figletMap,
            @NotNull String comment
    ) {
        this.font = font;
        this.option = option;
        this.figletMap = Map.copyOf(figletMap);
        this.comment = comment;
    }

    static @NotNull FontMetadata buildFontMetadata(@NotNull Figlet.FontSpec font) {
        var data = readFileContent(font);
        var option = readOptions(data);

        // Reads comment.
        var comment = new StringBuilder();
        int num = 0;
        while (++num <= option.numCommentLines) {
            comment.append(data.get(num)).append("\n");
        }

        // Builds FIGlet map.
        int height = option.height;
        var figletMap = new HashMap<Integer, String[]>(CODES.size());

        // read character data
        for (int i = 0; i < CODES.size(); i++) {
            var code = CODES.get(i);
            if (i * height + num >= data.size()) {
                break;
            }
            var figlet = new String[height];
            figletMap.put(code, figlet);
            for (int j = 0; j < height; j++) {
                int row = i * height + j + num;
                if (row >= data.size()) {
                    figletMap.remove(code);
                    break;
                }
                var charRow = data.get(row);

                // From jfiglet's FigFontReader.readCharacterData
                // Starts from the line end
                int charIndex = charRow.length() - 1;

                // Skip over any whitespace characters at the end of the line
                while (charIndex >= 0 && Character.isWhitespace(charRow.charAt(charIndex))) {
                    charIndex--;
                }

                // We've found a non-whitespace character that we will interpret as an
                // end-character.
                char endChar = charRow.charAt(charIndex);

                // Skip over any end-characters.
                while (charIndex >= 0 && charRow.charAt(charIndex) == endChar) {
                    charIndex--;
                }

                // We've found the right-hand edge of the actual character data for this line.
                figlet[j] = charRow.substring(0, charIndex + 1);
            }
        }
        data.clear();

        return new FontMetadata(
                font,
                option,
                figletMap,
                comment.toString()
        );
    }

    @NotNull
    private static List<String> readFileContent(@NotNull Figlet.FontSpec font) {
        var data = new ArrayList<String>();
        var path = font.getFilename();

        try (var resourceStream = FigletRenderer.class.getClassLoader().getResourceAsStream(path);
             var inputStream = IOUtils.unwrapZippedFontIfNecessary(resourceStream)) {
            var bufferedReader = new BufferedReader(new InputStreamReader(inputStream, font.getCharset()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                data.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load font \"" + font.getName() + "\"", e);
        }
        return data;
    }

    @NotNull
    private static Option readOptions(@NotNull List<String> data) {
        // Builds option.
        var header = data.get(0).split(Strings.BLANK_STRING);
        int height = Integer.parseInt(header[1]);
        int baseline = Integer.parseInt(header[2]);
        int maxLength = Integer.parseInt(header[3]);
        int oldLayout = Integer.parseInt(header[4]);
        var hardBlank = header[0].substring(5, 6);
        int numCommentLines = Integer.parseInt(header[5]);
        int printDirection = header.length > 6 ? Integer.parseInt(header[6]) : 0;
        var fullLayout = header.length > 7 ? Integer.parseInt(header[7]) : null;
        var codeTagCount = header.length > 8 ? Integer.parseInt(header[8]) : null;

        return new Option(
                baseline,
                codeTagCount,
                Rule.getSmushRule(oldLayout, fullLayout),
                fullLayout,
                hardBlank,
                height,
                maxLength,
                numCommentLines,
                oldLayout,
                printDirection
        );
    }
}
