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
import jdk.dynalink.StandardNamespace;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import picocli.CommandLine;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class JufmtFigletCommandTest {
    private final StringWriter outWriter = new StringWriter();
    private final StringWriter errWriter = new StringWriter();

    @Test
    @Disabled("No more default font, only random")
    void choose_same_default_font() {
        jufmt("figlet", "bric3");

        assertThat(outWriter.toString())
                .isEqualToNormalizingNewlines("""
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
        jufmt("figlet", "-r", "bric3");

        assertThat(outWriter.toString()).isNotBlank();
    }

    @ParameterizedTest
    @MethodSource("figletArguments")
    void supports_figlet(EmbeddedFigletFonts font, String input, String expected) {
        jufmt("figlet", "-f", font.name(), input);

        assertThat(outWriter.toString())
                .describedAs(font.toString())
                .isEqualToNormalizingNewlines(expected);
    }

    @AfterEach
    void checks_err_is_empty() {
        assertThat(errWriter.toString()).isEmpty();
    }

    private int jufmt(String... args) {
        var printOut = new PrintWriter(outWriter, true);
        var printErr = new PrintWriter(errWriter, true);

        return new CommandLine(new JufmtCommand())
                .setOut(printOut)
                .setErr(printErr)
                .setColorScheme(CommandLine.Help.defaultColorScheme(CommandLine.Help.Ansi.OFF))
                .execute(args);
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
