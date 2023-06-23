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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import picocli.CommandLine;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class JufmtFigletCommandTest {

    @Test
    @Disabled("No more default font, only random")
    void choose_same_default_font() {
        var stringWriter = new StringWriter();
        new CommandLine(new JufmtCommand())
                .setOut(new PrintWriter(stringWriter))
                .execute("figlet", "bric3");

        assertThat(stringWriter.toString())
                .isEqualToIgnoringNewLines("""
                             _          _      _____\s
                            | |__  _ __(_) ___|___ /\s
                            | '_ \\| '__| |/ __| |_ \\\s
                            | |_) | |  | | (__ ___) |
                            |_.__/|_|  |_|\\___|____/\s
                                                    \s
                           """);
    }

    @Test
    void can_pick_random_font() {
        var stringWriter = new StringWriter();
        new CommandLine(new JufmtCommand())
                .setOut(new PrintWriter(stringWriter))
                .execute("figlet", "-r", "bric3");


        assertThat(stringWriter.toString()).isNotBlank();
    }

    @ParameterizedTest
    @MethodSource("figletArguments")
    void supports_figlet(EmbeddedFigletFonts font, String input, String expected) {
        var stringWriter = new StringWriter();
        new CommandLine(new JufmtCommand())
                .setOut(new PrintWriter(stringWriter))
                .execute("figlet", "-f", font.name(), input);

        assertThat(stringWriter.toString())
                .describedAs(font.toString())
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
