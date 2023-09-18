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

public class JufmtCharacterTest {
    @ParameterizedTest
    @MethodSource("characterDescription")
    void can_describe_input_characters(String input, String expected) {
        var result = jufmt("--describe", input);

        assertThat(result.out()).isEqualToIgnoringNewLines(expected);
    }

    private static Stream<Arguments> characterDescription() {
        return Stream.of(
                Arguments.arguments(
                        "\uD83D\uDE2E\u200D\uD83D\uDCA8",
                        """
                        --------
                        char          : 1f62e 😮
                        char count    : 2
                        lower case    : 1f62e 😮
                        title case    : 1f62e 😮
                        upper case    : 1f62e 😮
                        char name     : FACE WITH OPEN MOUTH
                        char type     : OTHER_SYMBOL
                        char direction: 13
                        unicode block : EMOTICONS
                        unicode script: COMMON
                        --------
                        char          : 200d ‍
                        char count    : 1
                        lower case    : 200d ‍
                        title case    : 200d ‍
                        upper case    : 200d ‍
                        char name     : ZERO WIDTH JOINER
                        char type     : FORMAT
                        char direction: 9
                        unicode block : GENERAL_PUNCTUATION
                        unicode script: INHERITED
                        --------
                        char          : 1f4a8 💨
                        char count    : 2
                        lower case    : 1f4a8 💨
                        title case    : 1f4a8 💨
                        upper case    : 1f4a8 💨
                        char name     : DASH SYMBOL
                        char type     : OTHER_SYMBOL
                        char direction: 13
                        unicode block : MISCELLANEOUS_SYMBOLS_AND_PICTOGRAPHS
                        unicode script: COMMON
                        """
                ),
                Arguments.arguments(
                        "😮‍💨",
                        """
                        --------
                        char          : 1f62e 😮
                        char count    : 2
                        lower case    : 1f62e 😮
                        title case    : 1f62e 😮
                        upper case    : 1f62e 😮
                        char name     : FACE WITH OPEN MOUTH
                        char type     : OTHER_SYMBOL
                        char direction: 13
                        unicode block : EMOTICONS
                        unicode script: COMMON
                        --------
                        char          : 200d ‍
                        char count    : 1
                        lower case    : 200d ‍
                        title case    : 200d ‍
                        upper case    : 200d ‍
                        char name     : ZERO WIDTH JOINER
                        char type     : FORMAT
                        char direction: 9
                        unicode block : GENERAL_PUNCTUATION
                        unicode script: INHERITED
                        --------
                        char          : 1f4a8 💨
                        char count    : 2
                        lower case    : 1f4a8 💨
                        title case    : 1f4a8 💨
                        upper case    : 1f4a8 💨
                        char name     : DASH SYMBOL
                        char type     : OTHER_SYMBOL
                        char direction: 13
                        unicode block : MISCELLANEOUS_SYMBOLS_AND_PICTOGRAPHS
                        unicode script: COMMON
                        """
                )
        );
    }
}
