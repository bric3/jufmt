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

import java.io.PrintWriter;
import java.text.BreakIterator;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CharacterDescriber {
    public static Stream<IntStream> graphemes(String string) {
        var cBreakIterator = BreakIterator.getCharacterInstance();
        cBreakIterator.setText(string);
        int start = cBreakIterator.first();
        var builder = Stream.<IntStream>builder();
        for (
                int end = cBreakIterator.next();
                end != BreakIterator.DONE;
                start = end, end = cBreakIterator.next()
        ) {
            builder.add(string.substring(start, end).codePoints());
        }
        return builder.build();
    }


    public static void graphemeDetails(PrintWriter out, int[] codePoints) {
        out.println("---");

        out.printf("Grapheme       : `%s`%n", new String(codePoints, 0, codePoints.length));
        out.printf("Codepoint count: %s%n", codePoints.length);
        out.printf("Char count     : %s%n", Arrays.stream(codePoints).map(Character::charCount).sum());

        if (Character.isEmoji(codePoints[0])) {
            emojiDetails(out, codePoints);
        } else {
            charDetails(out, codePoints[0]);
        }
    }

    public static void charDetails(PrintWriter out, int codePoint) {
        out.printf("Unicode Name   : %s%n", Character.getName(codePoint));
        out.printf("Character      : `%08x` -> %s%n", codePoint, Character.toString(codePoint));
        out.printf("Unicode block  : %s%n", Character.UnicodeBlock.of(codePoint));
        out.printf("Unicode script : %s%n", Character.UnicodeScript.of(codePoint));
        out.printf("Category       : %s%n", getCharacterCategoryName(codePoint));
        out.printf("Directionality : %s%n", getDirectionalityName(codePoint));
        out.printf("Char count     : %s%n", Character.charCount(codePoint));

        if (Character.isLetterOrDigit(codePoint)) {
            int lowerCase = Character.toLowerCase(codePoint);
            out.printf("Lower case     : `%04x` (%s) -> %s%n",
                       lowerCase,
                       Character.getName(lowerCase),
                       Character.toString(lowerCase)
            );
            int titleCase = Character.toTitleCase(codePoint);
            out.printf("Title case     : `%04x` (%s) -> %s%n",
                       titleCase,
                       Character.getName(titleCase),
                       Character.toString(titleCase)
            );
            int upperCase = Character.toUpperCase(codePoint);
            out.printf("Upper case     : `%04x` (%s) -> %s%n",
                       upperCase,
                       Character.getName(upperCase),
                       Character.toString(upperCase)
            );
        }

        emojiDetails(out, codePoint);
        out.println("");
    }

    private static void emojiDetails(PrintWriter out, int... codePoints) {
        // JDK 21
        if (!Character.isEmoji(codePoints[0])) {
            return;
        }

        out.println("| CodePoint -> Emoji  | Base | Modifier | Presentation | Component | Extended Pictographic | Name |");
        out.println("|---------------------|------|----------|--------------|-----------|-----------------------|------|");
        Arrays.stream(codePoints)
              .forEach(codePoint -> System.out.printf(
                      "| `%08x` -> %5s | %4c | %8c | %12c | %9c | %20c | %s |%n",
                      codePoint,
                      Character.toString(codePoint),
                      yesNo(Character.isEmojiModifierBase(codePoint)),
                      yesNo(Character.isEmojiModifier(codePoint)),
                      yesNo(Character.isEmojiPresentation(codePoint)),
                      yesNo(Character.isEmojiComponent(codePoint)),
                      yesNo(Character.isExtendedPictographic(codePoint)),
                      Character.getName(codePoint)
              ));
        out.println();
    }

    public static char yesNo(boolean value) {
        return value ? '✅' : '❌';
    }
    // Unfortunately, character class doesn't give access to category **name**.

    private static String getCharacterCategoryName(int codePoint) {
        return switch (Character.getType(codePoint)) {
            case Character.COMBINING_SPACING_MARK -> "COMBINING_SPACING_MARK";
            case Character.CONNECTOR_PUNCTUATION -> "CONNECTOR_PUNCTUATION";
            case Character.CONTROL -> "CONTROL";
            case Character.CURRENCY_SYMBOL -> "CURRENCY_SYMBOL";
            case Character.DASH_PUNCTUATION -> "DASH_PUNCTUATION";
            case Character.DECIMAL_DIGIT_NUMBER -> "DECIMAL_DIGIT_NUMBER";
            case Character.ENCLOSING_MARK -> "ENCLOSING_MARK";
            case Character.END_PUNCTUATION -> "END_PUNCTUATION";
            case Character.FINAL_QUOTE_PUNCTUATION -> "FINAL_QUOTE_PUNCTUATION";
            case Character.FORMAT -> "FORMAT";
            case Character.INITIAL_QUOTE_PUNCTUATION -> "INITIAL_QUOTE_PUNCTUATION";
            case Character.LETTER_NUMBER -> "LETTER_NUMBER";
            case Character.LINE_SEPARATOR -> "LINE_SEPARATOR";
            case Character.LOWERCASE_LETTER -> "LOWERCASE_LETTER";
            case Character.MATH_SYMBOL -> "MATH_SYMBOL";
            case Character.MODIFIER_LETTER -> "MODIFIER_LETTER";
            case Character.MODIFIER_SYMBOL -> "MODIFIER_SYMBOL";
            case Character.NON_SPACING_MARK -> "NON_SPACING_MARK";
            case Character.OTHER_LETTER -> "OTHER_LETTER";
            case Character.OTHER_NUMBER -> "OTHER_NUMBER";
            case Character.OTHER_PUNCTUATION -> "OTHER_PUNCTUATION";
            case Character.OTHER_SYMBOL -> "OTHER_SYMBOL";
            case Character.PARAGRAPH_SEPARATOR -> "PARAGRAPH_SEPARATOR";
            case Character.PRIVATE_USE -> "PRIVATE_USE";
            case Character.SPACE_SEPARATOR -> "SPACE_SEPARATOR";
            case Character.START_PUNCTUATION -> "START_PUNCTUATION";
            case Character.SURROGATE -> "SURROGATE";
            case Character.TITLECASE_LETTER -> "TITLECASE_LETTER";
            case Character.UNASSIGNED -> "UNASSIGNED";
            case Character.UPPERCASE_LETTER -> "UPPERCASE_LETTER";
            default -> "UNKNOWN";
        };
    }

    private static String getDirectionalityName(int codePoint) {
        return switch (Character.getDirectionality(codePoint)) {
            case Character.DIRECTIONALITY_UNDEFINED -> "DIRECTIONALITY_UNDEFINED";
            case Character.DIRECTIONALITY_LEFT_TO_RIGHT -> "DIRECTIONALITY_LEFT_TO_RIGHT";
            case Character.DIRECTIONALITY_RIGHT_TO_LEFT -> "DIRECTIONALITY_RIGHT_TO_LEFT";
            case Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC -> "DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC";
            case Character.DIRECTIONALITY_EUROPEAN_NUMBER -> "DIRECTIONALITY_EUROPEAN_NUMBER";
            case Character.DIRECTIONALITY_EUROPEAN_NUMBER_SEPARATOR -> "DIRECTIONALITY_EUROPEAN_NUMBER_SEPARATOR";
            case Character.DIRECTIONALITY_EUROPEAN_NUMBER_TERMINATOR -> "DIRECTIONALITY_EUROPEAN_NUMBER_TERMINATOR";
            case Character.DIRECTIONALITY_ARABIC_NUMBER -> "DIRECTIONALITY_ARABIC_NUMBER";
            case Character.DIRECTIONALITY_COMMON_NUMBER_SEPARATOR -> "DIRECTIONALITY_COMMON_NUMBER_SEPARATOR";
            case Character.DIRECTIONALITY_NONSPACING_MARK -> "DIRECTIONALITY_NONSPACING_MARK";
            case Character.DIRECTIONALITY_BOUNDARY_NEUTRAL -> "DIRECTIONALITY_BOUNDARY_NEUTRAL";
            case Character.DIRECTIONALITY_PARAGRAPH_SEPARATOR -> "DIRECTIONALITY_PARAGRAPH_SEPARATOR";
            case Character.DIRECTIONALITY_SEGMENT_SEPARATOR -> "DIRECTIONALITY_SEGMENT_SEPARATOR";
            case Character.DIRECTIONALITY_WHITESPACE -> "DIRECTIONALITY_WHITESPACE";
            case Character.DIRECTIONALITY_OTHER_NEUTRALS -> "DIRECTIONALITY_OTHER_NEUTRALS";
            case Character.DIRECTIONALITY_LEFT_TO_RIGHT_EMBEDDING -> "DIRECTIONALITY_LEFT_TO_RIGHT_EMBEDDING";
            case Character.DIRECTIONALITY_LEFT_TO_RIGHT_OVERRIDE -> "DIRECTIONALITY_LEFT_TO_RIGHT_OVERRIDE";
            case Character.DIRECTIONALITY_RIGHT_TO_LEFT_EMBEDDING -> "DIRECTIONALITY_RIGHT_TO_LEFT_EMBEDDING";
            case Character.DIRECTIONALITY_RIGHT_TO_LEFT_OVERRIDE -> "DIRECTIONALITY_RIGHT_TO_LEFT_OVERRIDE";
            case Character.DIRECTIONALITY_POP_DIRECTIONAL_FORMAT -> "DIRECTIONALITY_POP_DIRECTIONAL_FORMAT";
            case Character.DIRECTIONALITY_LEFT_TO_RIGHT_ISOLATE -> "DIRECTIONALITY_LEFT_TO_RIGHT_ISOLATE";
            case Character.DIRECTIONALITY_RIGHT_TO_LEFT_ISOLATE -> "DIRECTIONALITY_RIGHT_TO_LEFT_ISOLATE";
            case Character.DIRECTIONALITY_FIRST_STRONG_ISOLATE -> "DIRECTIONALITY_FIRST_STRONG_ISOLATE";
            case Character.DIRECTIONALITY_POP_DIRECTIONAL_ISOLATE -> "DIRECTIONALITY_POP_DIRECTIONAL_ISOLATE";
            default -> "UNKNOWN";
        };
    }
}
