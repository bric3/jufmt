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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class FigletTest {

    @Test
    void render() {
        assertThat(Figlet.render("jufmt", EmbeddedFigletFonts._1_Row))
                .isEqualToNormalizingNewlines(
                        "_T |_| /= |\\/| ~|~ \n" +
                        "                   "
                );
    }

    @ParameterizedTest(name = "[{index}] figlet {0}")
    @MethodSource("figletArguments")
    void supports_figlet(EmbeddedFigletFonts font, String input, String expected) {
        var result = Figlet.render(input, font);

        assertThat(result)
                .describedAs(font.name())
                .isEqualToIgnoringNewLines(expected);
    }


    private static Stream<Arguments> figletArguments() {
        return Stream.of(
                arguments(EmbeddedFigletFonts.Sub_Zero,
                        "jufmt",
                        """
                           __     __  __     ______   __    __     ______ \s
                          /\\ \\   /\\ \\/\\ \\   /\\  ___\\ /\\ "-./  \\   /\\__  _\\\s
                         _\\_\\ \\  \\ \\ \\_\\ \\  \\ \\  __\\ \\ \\ \\-./\\ \\  \\/_/\\ \\/\s
                        /\\_____\\  \\ \\_____\\  \\ \\_\\    \\ \\_\\ \\ \\_\\    \\ \\_\\\s
                        \\/_____/   \\/_____/   \\/_/     \\/_/  \\/_/     \\/_/\s
                                                                          \s
                        """),

                arguments(EmbeddedFigletFonts.Lean,
                        "jufmt",
                        """
                                                                                 \s
                                _/                _/_/                    _/     \s
                                   _/    _/    _/      _/_/_/  _/_/    _/_/_/_/  \s
                              _/  _/    _/  _/_/_/_/  _/    _/    _/    _/       \s
                             _/  _/    _/    _/      _/    _/    _/    _/        \s
                            _/    _/_/_/    _/      _/    _/    _/      _/_/     \s
                           _/                                                    \s
                        _/                                                       \s
                        """),

                arguments(EmbeddedFigletFonts.Elite,
                        "jufmt",
                        """
                         ▐▄▄▄▄• ▄▌·▄▄▄• ▌ ▄ ·. ▄▄▄▄▄
                          ·███▪██▌▐▄▄··██ ▐███▪•██ \s
                        ▪▄ ███▌▐█▌██▪ ▐█ ▌▐▌▐█· ▐█.▪
                        ▐▌▐█▌▐█▄█▌██▌.██ ██▌▐█▌ ▐█▌·
                         ▀▀▀• ▀▀▀ ▀▀▀ ▀▀  █▪▀▀▀ ▀▀▀\s
                        """),

                arguments(EmbeddedFigletFonts.Calvin_S,
                        "jufmt",
                        """
                         ┬┬ ┬┌─┐┌┬┐┌┬┐
                         ││ │├┤ │││ │\s
                        └┘└─┘└  ┴ ┴ ┴\s
                        """),

                arguments(EmbeddedFigletFonts.Bear,
                        "jufmt",
                        """
                           _     _      _     _      _     _      _     _      _     _  \s
                          (c).-.(c)    (c).-.(c)    (c).-.(c)    (c).-.(c)    (c).-.(c) \s
                           / ._. \\      / ._. \\      / ._. \\      / ._. \\      / ._. \\  \s
                         __\\( Y )/__  __\\( Y )/__  __\\( Y )/__  __\\( Y )/__  __\\( Y )/__\s
                        (_.-/'-'\\-._)(_.-/'-'\\-._)(_.-/'-'\\-._)(_.-/'-'\\-._)(_.-/'-'\\-._)
                           || J ||      || U ||      || F ||      || M ||      || T ||  \s
                         _.' `-' '._  _.' `-' '._  _.' `-' '._  _.' `-' '._  _.' `-' '._\s
                        (.-./`-'\\.-.)(.-./`-'\\.-.)(.-./`-'\\.-.)(.-./`-'\\.-.)(.-./`-'\\.-.)
                         `-'     `-'  `-'     `-'  `-'     `-'  `-'     `-'  `-'     `-'\s
                        """),

                arguments(EmbeddedFigletFonts.smpoison,
                        "jufmt",
                        """
                                                                     \s
                            @@@ @@@  @@@ @@@@@@@@ @@@@@@@@@@  @@@@@@@\s
                            @@! @@!  @@@ @@!      @@! @@! @@!   @!!  \s
                            !!@ @!@  !@! @!!!:!   @!! !!@ @!@   @!!  \s
                        .  .!!  !!:  !!! !!:      !!:     !!:   !!:  \s
                        ::.::    :.:: :   :        :      :      :   \s
                                                                     \s
                        """),

                arguments(EmbeddedFigletFonts.future,
                        "jufmt",
                        """
                         ┏┓╻ ╻┏━╸┏┳┓╺┳╸
                          ┃┃ ┃┣╸ ┃┃┃ ┃\s
                        ┗━┛┗━┛╹  ╹ ╹ ╹\s
                        """),

                arguments(EmbeddedFigletFonts.pagga,
                        "jufmt",
                        """
                        ░▀▀█░█░█░█▀▀░█▄█░▀█▀
                        ░░░█░█░█░█▀▀░█░█░░█░
                        ░▀▀░░▀▀▀░▀░░░▀░▀░░▀░
                        """),

                arguments(EmbeddedFigletFonts.bigmono9,
                        "jufmt",
                        """
                                                          \s
                                                          \s
                            █            ▒██              \s
                                         █░            █  \s
                                         █             █  \s
                          ███   █   █  █████  ██▓█▓  █████\s
                            █   █   █    █    █▒█▒█    █  \s
                            █   █   █    █    █ █ █    █  \s
                            █   █   █    █    █ █ █    █  \s
                            █   █   █    █    █ █ █    █  \s
                            █   █▒ ▓█    █    █ █ █    █░ \s
                            █   ▒██▒█    █    █ █ █    ▒██\s
                            █                             \s
                           ▒█                             \s
                          ██▒                             \s
                        """),

                arguments(EmbeddedFigletFonts.rusto,
                        "jufmt",
                        """
                          ┬┬ ┐┬─┐┌┌┐┌┐┐
                        ┌ ││ │├─ │││ │\s
                        └─┆┆─┘┆  ┘ ┆ ┆\s
                        """)
        );
    }
}