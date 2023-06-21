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
                .isEqualTo("  _          _      _____ \n" +
                           " | |__  _ __(_) ___|___ / \n" +
                           " | '_ \\| '__| |/ __| |_ \\ \n" +
                           " | |_) | |  | | (__ ___) |\n" +
                           " |_.__/|_|  |_|\\___|____/ \n" +
                           "                          \n");
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
                .isEqualToNormalizingNewlines(expected);
    }

    private static Stream<Arguments> figletArguments() {
        return Stream.of(
                arguments(EmbeddedFigletFonts.Sub_Zero,
                        "jufmt",
                          "   __     __  __     ______   __    __     ______  \n" +
                          "  /\\ \\   /\\ \\/\\ \\   /\\  ___\\ /\\ \"-./  \\   /\\__  _\\ \n" +
                          " _\\_\\ \\  \\ \\ \\_\\ \\  \\ \\  __\\ \\ \\ \\-./\\ \\  \\/_/\\ \\/ \n" +
                          "/\\_____\\  \\ \\_____\\  \\ \\_\\    \\ \\_\\ \\ \\_\\    \\ \\_\\ \n" +
                          "\\/_____/   \\/_____/   \\/_/     \\/_/  \\/_/     \\/_/ \n" +
                          "                                                   \n"),

                arguments(EmbeddedFigletFonts.Lean,
                        "jufmt",
                          "                                                          \n" +
                          "        _/                _/_/                    _/      \n" +
                          "           _/    _/    _/      _/_/_/  _/_/    _/_/_/_/   \n" +
                          "      _/  _/    _/  _/_/_/_/  _/    _/    _/    _/        \n" +
                          "     _/  _/    _/    _/      _/    _/    _/    _/         \n" +
                          "    _/    _/_/_/    _/      _/    _/    _/      _/_/      \n" +
                          "   _/                                                     \n" +
                          "_/                                                        \n"),

                arguments(EmbeddedFigletFonts.Elite,
                        "jufmt",
                          " ▐▄▄▄▄• ▄▌·▄▄▄• ▌ ▄ ·. ▄▄▄▄▄\n" +
                          "  ·███▪██▌▐▄▄··██ ▐███▪•██  \n" +
                          "▪▄ ███▌▐█▌██▪ ▐█ ▌▐▌▐█· ▐█.▪\n" +
                          "▐▌▐█▌▐█▄█▌██▌.██ ██▌▐█▌ ▐█▌·\n" +
                          " ▀▀▀• ▀▀▀ ▀▀▀ ▀▀  █▪▀▀▀ ▀▀▀ \n"),

                arguments(EmbeddedFigletFonts.Calvin_S,
                        "jufmt",
                          " ┬┬ ┬┌─┐┌┬┐┌┬┐\n" +
                          " ││ │├┤ │││ │ \n" +
                          "└┘└─┘└  ┴ ┴ ┴ \n"),

                arguments(EmbeddedFigletFonts.Bear,
                        "jufmt",
                          "   _     _      _     _      _     _      _     _      _     _   \n" +
                          "  (c).-.(c)    (c).-.(c)    (c).-.(c)    (c).-.(c)    (c).-.(c)  \n" +
                          "   / ._. \\      / ._. \\      / ._. \\      / ._. \\      / ._. \\   \n" +
                          " __\\( Y )/__  __\\( Y )/__  __\\( Y )/__  __\\( Y )/__  __\\( Y )/__ \n" +
                          "(_.-/'-'\\-._)(_.-/'-'\\-._)(_.-/'-'\\-._)(_.-/'-'\\-._)(_.-/'-'\\-._)\n" +
                          "   || J ||      || U ||      || F ||      || M ||      || T ||   \n" +
                          " _.' `-' '._  _.' `-' '._  _.' `-' '._  _.' `-' '._  _.' `-' '._ \n" +
                          "(.-./`-'\\.-.)(.-./`-'\\.-.)(.-./`-'\\.-.)(.-./`-'\\.-.)(.-./`-'\\.-.)\n" +
                          " `-'     `-'  `-'     `-'  `-'     `-'  `-'     `-'  `-'     `-' \n"),

                arguments(EmbeddedFigletFonts.smpoison,
                        "jufmt",
                          "                                              \n" +
                          "    @@@ @@@  @@@ @@@@@@@@ @@@@@@@@@@  @@@@@@@ \n" +
                          "    @@! @@!  @@@ @@!      @@! @@! @@!   @!!   \n" +
                          "    !!@ @!@  !@! @!!!:!   @!! !!@ @!@   @!!   \n" +
                          ".  .!!  !!:  !!! !!:      !!:     !!:   !!:   \n" +
                          "::.::    :.:: :   :        :      :      :    \n" +
                          "                                              \n"),

                arguments(EmbeddedFigletFonts.future,
                        "jufmt",
                          " ┏┓╻ ╻┏━╸┏┳┓╺┳╸\n" +
                          "  ┃┃ ┃┣╸ ┃┃┃ ┃ \n" +
                          "┗━┛┗━┛╹  ╹ ╹ ╹ \n"),

                arguments(EmbeddedFigletFonts.pagga,
                        "jufmt",
                          "░▀▀█░█░█░█▀▀░█▄█░▀█▀\n" +
                          "░░░█░█░█░█▀▀░█░█░░█░\n" +
                          "░▀▀░░▀▀▀░▀░░░▀░▀░░▀░\n"),

                arguments(EmbeddedFigletFonts.bigmono9,
                        "jufmt",
                          "                                   \n" +
                          "                                   \n" +
                          "    █            ▒██               \n" +
                          "                 █░            █   \n" +
                          "                 █             █   \n" +
                          "  ███   █   █  █████  ██▓█▓  █████ \n" +
                          "    █   █   █    █    █▒█▒█    █   \n" +
                          "    █   █   █    █    █ █ █    █   \n" +
                          "    █   █   █    █    █ █ █    █   \n" +
                          "    █   █   █    █    █ █ █    █   \n" +
                          "    █   █▒ ▓█    █    █ █ █    █░  \n" +
                          "    █   ▒██▒█    █    █ █ █    ▒██ \n" +
                          "    █                              \n" +
                          "   ▒█                              \n" +
                          "  ██▒                              \n"),

                arguments(EmbeddedFigletFonts.rusto,
                        "jufmt",
                          "  ┬┬ ┐┬─┐┌┌┐┌┐┐\n" +
                          "┌ ││ │├─ │││ │ \n" +
                          "└─┆┆─┘┆  ┘ ┆ ┆ \n")
        );
    }
}
