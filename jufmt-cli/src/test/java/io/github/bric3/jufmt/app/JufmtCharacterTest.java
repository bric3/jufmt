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

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.github.bric3.jufmt.app.JufmtTestUtil.jufmt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class JufmtCharacterTest {
    @ParameterizedTest(name = "[{index}] describing \"{0}\"")
    @MethodSource("characterDescription")
    void can_describe_input_characters(String input, String expected) {
        var result = jufmt("describe", input);

        assertThat(result.err()).isEmpty();
        assertThat(result.out()).isEqualToIgnoringNewLines(expected);
    }

    private static Stream<Arguments> characterDescription() {
        return Stream.of(
                arguments(
                        "\uD83D\uDE2E\u200D\uD83D\uDCA8",
                        """
                        ---
                        Grapheme       : `😮‍💨`
                        Codepoint count: 3
                        Char count     : 5
                        | CodePoint -> Emoji  | Base | Modifier | Presentation | Component | Extended Pictographic | Name |
                        |---------------------|------|----------|--------------|-----------|-----------------------|------|
                        | `0001f62e` ->    😮 |    ❌ |        ❌ |            ✅ |         ❌ |                    ✅ | FACE WITH OPEN MOUTH |
                        | `0000200d` ->     ‍ |    ❌ |        ❌ |            ❌ |         ✅ |                    ❌ | ZERO WIDTH JOINER |
                        | `0001f4a8` ->    💨 |    ❌ |        ❌ |            ✅ |         ❌ |                    ✅ | DASH SYMBOL |
                        """
                ),
                arguments(
                        "😮‍💨",
                        """
                        ---
                        Grapheme       : `😮‍💨`
                        Codepoint count: 3
                        Char count     : 5
                        | CodePoint -> Emoji  | Base | Modifier | Presentation | Component | Extended Pictographic | Name |
                        |---------------------|------|----------|--------------|-----------|-----------------------|------|
                        | `0001f62e` ->    😮 |    ❌ |        ❌ |            ✅ |         ❌ |                    ✅ | FACE WITH OPEN MOUTH |
                        | `0000200d` ->     ‍ |    ❌ |        ❌ |            ❌ |         ✅ |                    ❌ | ZERO WIDTH JOINER |
                        | `0001f4a8` ->    💨 |    ❌ |        ❌ |            ✅ |         ❌ |                    ✅ | DASH SYMBOL |
                        """
                ),
                arguments(
                        "❌",
                        """
                        ---
                        Grapheme       : `❌`
                        Codepoint count: 1
                        Char count     : 1
                        | CodePoint -> Emoji  | Base | Modifier | Presentation | Component | Extended Pictographic | Name |
                        |---------------------|------|----------|--------------|-----------|-----------------------|------|
                        | `0000274c` ->     ❌ |    ❌ |        ❌ |            ✅ |         ❌ |                    ✅ | CROSS MARK |
                        """
                ),
                arguments(
                        "Au 😶‍🌫️ la 👨‍👩‍👧‍👦",
                        """
                        ---
                        Grapheme       : `A`
                        Codepoint count: 1
                        Char count     : 1
                        Unicode Name   : LATIN CAPITAL LETTER A
                        Character      : `00000041` -> A
                        Unicode block  : BASIC_LATIN
                        Unicode script : LATIN
                        Category       : UPPERCASE_LETTER
                        Directionality : DIRECTIONALITY_LEFT_TO_RIGHT
                        Char count     : 1
                        Lower case     : `0061` (LATIN SMALL LETTER A) -> a
                        Title case     : `0041` (LATIN CAPITAL LETTER A) -> A
                        Upper case     : `0041` (LATIN CAPITAL LETTER A) -> A
                                                
                        ---
                        Grapheme       : `u`
                        Codepoint count: 1
                        Char count     : 1
                        Unicode Name   : LATIN SMALL LETTER U
                        Character      : `00000075` -> u
                        Unicode block  : BASIC_LATIN
                        Unicode script : LATIN
                        Category       : LOWERCASE_LETTER
                        Directionality : DIRECTIONALITY_LEFT_TO_RIGHT
                        Char count     : 1
                        Lower case     : `0075` (LATIN SMALL LETTER U) -> u
                        Title case     : `0055` (LATIN CAPITAL LETTER U) -> U
                        Upper case     : `0055` (LATIN CAPITAL LETTER U) -> U
                                                
                        ---
                        Grapheme       : ` `
                        Codepoint count: 1
                        Char count     : 1
                        Unicode Name   : SPACE
                        Character      : `00000020` -> \s
                        Unicode block  : BASIC_LATIN
                        Unicode script : COMMON
                        Category       : SPACE_SEPARATOR
                        Directionality : DIRECTIONALITY_WHITESPACE
                        Char count     : 1
                                                
                        ---
                        Grapheme       : `😶‍🌫️`
                        Codepoint count: 4
                        Char count     : 6
                        | CodePoint -> Emoji  | Base | Modifier | Presentation | Component | Extended Pictographic | Name |
                        |---------------------|------|----------|--------------|-----------|-----------------------|------|
                        | `0001f636` ->    😶 |    ❌ |        ❌ |            ✅ |         ❌ |                    ✅ | FACE WITHOUT MOUTH |
                        | `0000200d` ->     ‍ |    ❌ |        ❌ |            ❌ |         ✅ |                    ❌ | ZERO WIDTH JOINER |
                        | `0001f32b` ->    🌫 |    ❌ |        ❌ |            ❌ |         ❌ |                    ✅ | FOG |
                        | `0000fe0f` ->     ️ |    ❌ |        ❌ |            ❌ |         ✅ |                    ❌ | VARIATION SELECTOR-16 |
                                                
                        ---
                        Grapheme       : ` `
                        Codepoint count: 1
                        Char count     : 1
                        Unicode Name   : SPACE
                        Character      : `00000020` -> \s
                        Unicode block  : BASIC_LATIN
                        Unicode script : COMMON
                        Category       : SPACE_SEPARATOR
                        Directionality : DIRECTIONALITY_WHITESPACE
                        Char count     : 1
                                                
                        ---
                        Grapheme       : `l`
                        Codepoint count: 1
                        Char count     : 1
                        Unicode Name   : LATIN SMALL LETTER L
                        Character      : `0000006c` -> l
                        Unicode block  : BASIC_LATIN
                        Unicode script : LATIN
                        Category       : LOWERCASE_LETTER
                        Directionality : DIRECTIONALITY_LEFT_TO_RIGHT
                        Char count     : 1
                        Lower case     : `006c` (LATIN SMALL LETTER L) -> l
                        Title case     : `004c` (LATIN CAPITAL LETTER L) -> L
                        Upper case     : `004c` (LATIN CAPITAL LETTER L) -> L
                                                
                        ---
                        Grapheme       : `a`
                        Codepoint count: 1
                        Char count     : 1
                        Unicode Name   : LATIN SMALL LETTER A
                        Character      : `00000061` -> a
                        Unicode block  : BASIC_LATIN
                        Unicode script : LATIN
                        Category       : LOWERCASE_LETTER
                        Directionality : DIRECTIONALITY_LEFT_TO_RIGHT
                        Char count     : 1
                        Lower case     : `0061` (LATIN SMALL LETTER A) -> a
                        Title case     : `0041` (LATIN CAPITAL LETTER A) -> A
                        Upper case     : `0041` (LATIN CAPITAL LETTER A) -> A
                                                
                        ---
                        Grapheme       : ` `
                        Codepoint count: 1
                        Char count     : 1
                        Unicode Name   : SPACE
                        Character      : `00000020` -> \s
                        Unicode block  : BASIC_LATIN
                        Unicode script : COMMON
                        Category       : SPACE_SEPARATOR
                        Directionality : DIRECTIONALITY_WHITESPACE
                        Char count     : 1
                                                
                        ---
                        Grapheme       : `👨‍👩‍👧‍👦`
                        Codepoint count: 7
                        Char count     : 11
                        | CodePoint -> Emoji  | Base | Modifier | Presentation | Component | Extended Pictographic | Name |
                        |---------------------|------|----------|--------------|-----------|-----------------------|------|
                        | `0001f468` ->    👨 |    ✅ |        ❌ |            ✅ |         ❌ |                    ✅ | MAN |
                        | `0000200d` ->     ‍ |    ❌ |        ❌ |            ❌ |         ✅ |                    ❌ | ZERO WIDTH JOINER |
                        | `0001f469` ->    👩 |    ✅ |        ❌ |            ✅ |         ❌ |                    ✅ | WOMAN |
                        | `0000200d` ->     ‍ |    ❌ |        ❌ |            ❌ |         ✅ |                    ❌ | ZERO WIDTH JOINER |
                        | `0001f467` ->    👧 |    ✅ |        ❌ |            ✅ |         ❌ |                    ✅ | GIRL |
                        | `0000200d` ->     ‍ |    ❌ |        ❌ |            ❌ |         ✅ |                    ❌ | ZERO WIDTH JOINER |
                        | `0001f466` ->    👦 |    ✅ |        ❌ |            ✅ |         ❌ |                    ✅ | BOY |
                        """
                ),
                arguments(
                        "11️⃣⟹0︎⃣",
                        """
                        ---
                        Grapheme       : `1`
                        Codepoint count: 1
                        Char count     : 1
                        | CodePoint -> Emoji  | Base | Modifier | Presentation | Component | Extended Pictographic | Name |
                        |---------------------|------|----------|--------------|-----------|-----------------------|------|
                        | `00000031` ->     1 |    ❌ |        ❌ |            ❌ |         ✅ |                    ❌ | DIGIT ONE |
                                                
                        ---
                        Grapheme       : `1️⃣`
                        Codepoint count: 3
                        Char count     : 3
                        | CodePoint -> Emoji  | Base | Modifier | Presentation | Component | Extended Pictographic | Name |
                        |---------------------|------|----------|--------------|-----------|-----------------------|------|
                        | `00000031` ->     1 |    ❌ |        ❌ |            ❌ |         ✅ |                    ❌ | DIGIT ONE |
                        | `0000fe0f` ->     ️ |    ❌ |        ❌ |            ❌ |         ✅ |                    ❌ | VARIATION SELECTOR-16 |
                        | `000020e3` ->     ⃣ |    ❌ |        ❌ |            ❌ |         ✅ |                    ❌ | COMBINING ENCLOSING KEYCAP |
                        
                        ---
                        Grapheme       : `⟹`
                        Codepoint count: 1
                        Char count     : 1
                        Unicode Name   : LONG RIGHTWARDS DOUBLE ARROW
                        Character      : `000027f9` -> ⟹
                        Unicode block  : SUPPLEMENTAL_ARROWS_A
                        Unicode script : COMMON
                        Category       : MATH_SYMBOL
                        Directionality : DIRECTIONALITY_OTHER_NEUTRALS
                        Char count     : 1
                                                
                        ---
                        Grapheme       : `0︎⃣`
                        Codepoint count: 3
                        Char count     : 3
                        | CodePoint -> Emoji  | Base | Modifier | Presentation | Component | Extended Pictographic | Name |
                        |---------------------|------|----------|--------------|-----------|-----------------------|------|
                        | `00000030` ->     0 |    ❌ |        ❌ |            ❌ |         ✅ |                    ❌ | DIGIT ZERO |
                        | `0000fe0e` ->     ︎ |    ❌ |        ❌ |            ❌ |         ❌ |                    ❌ | VARIATION SELECTOR-15 |
                        | `000020e3` ->     ⃣ |    ❌ |        ❌ |            ❌ |         ✅ |                    ❌ | COMBINING ENCLOSING KEYCAP |
                        """
                )
        );
    }
}
