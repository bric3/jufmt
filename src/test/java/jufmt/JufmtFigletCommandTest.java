package jufmt;

import io.leego.banana.Font;
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


        assertThat(stringWriter.toString().lines().count()).isGreaterThanOrEqualTo(2);
    }

    @ParameterizedTest
    @MethodSource("figletArguments")
    void supports_figlet(Font font, String input, String expected) {
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
                arguments(Font.SUB_ZERO,
                          "jufmt",
                          "   __     __  __     ______   __    __     ______  \n" +
                          "  /\\ \\   /\\ \\/\\ \\   /\\  ___\\ /\\ \"-./  \\   /\\__  _\\ \n" +
                          " _\\_\\ \\  \\ \\ \\_\\ \\  \\ \\  __\\ \\ \\ \\-./\\ \\  \\/_/\\ \\/ \n" +
                          "/\\_____\\  \\ \\_____\\  \\ \\_\\    \\ \\_\\ \\ \\_\\    \\ \\_\\ \n" +
                          "\\/_____/   \\/_____/   \\/_/     \\/_/  \\/_/     \\/_/ \n" +
                          "                                                   \n"),

                arguments(Font.LEAN,
                          "jufmt",
                          "                                                          \n" +
                          "        _/                _/_/                    _/      \n" +
                          "           _/    _/    _/      _/_/_/  _/_/    _/_/_/_/   \n" +
                          "      _/  _/    _/  _/_/_/_/  _/    _/    _/    _/        \n" +
                          "     _/  _/    _/    _/      _/    _/    _/    _/         \n" +
                          "    _/    _/_/_/    _/      _/    _/    _/      _/_/      \n" +
                          "   _/                                                     \n" +
                          "_/                                                        \n"),

                arguments(Font.ELITE,
                          "jufmt",
                          " ▐▄▄▄▄• ▄▌·▄▄▄• ▌ ▄ ·. ▄▄▄▄▄\n" +
                          "  ·███▪██▌▐▄▄··██ ▐███▪•██  \n" +
                          "▪▄ ███▌▐█▌██▪ ▐█ ▌▐▌▐█· ▐█.▪\n" +
                          "▐▌▐█▌▐█▄█▌██▌.██ ██▌▐█▌ ▐█▌·\n" +
                          " ▀▀▀• ▀▀▀ ▀▀▀ ▀▀  █▪▀▀▀ ▀▀▀ \n"),

                arguments(Font.CALVIN_S,
                          "jufmt",
                          " ┬┬ ┬┌─┐┌┬┐┌┬┐\n" +
                          " ││ │├┤ │││ │ \n" +
                          "└┘└─┘└  ┴ ┴ ┴ \n")
        );
    }
}
