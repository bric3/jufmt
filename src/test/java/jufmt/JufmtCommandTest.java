package jufmt;


import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import picocli.CommandLine;

import static org.assertj.core.api.Assertions.assertThat;


public class JufmtCommandTest {

    @ParameterizedTest
    @EnumSource(FancyStyle.class)
    public void check_style(FancyStyle style) {
        var cmd = new CommandLine(new JufmtCommand());
        var stringWriter = new StringWriter();
        cmd.setOut(new PrintWriter(stringWriter));

        
        cmd.execute("-s", style.name(), "bric3");

        assertThat(stringWriter.toString())
                .describedAs(style.name())
                .isEqualToIgnoringNewLines(style.example);

        System.out.printf("%s: %s", style.name(), stringWriter.toString());

    }

    @ParameterizedTest
    @EnumSource(FancyCharsets.class)
    public void check_charset(FancyCharsets charset) {
        var cmd = new CommandLine(new JufmtCommand());
        var stringWriter = new StringWriter();
        cmd.setOut(new PrintWriter(stringWriter));

        cmd.execute("-c", charset.name(), "bric3");

        assertThat(stringWriter.toString())
                .describedAs(charset.name())
                .isEqualToIgnoringNewLines(charset.example);

        System.out.printf("%s: %s", charset.name(), stringWriter.toString());
    }

    @ParameterizedTest
    @EnumSource(FancyOrnaments.class)
    public void check_ornament(FancyOrnaments ornaments) {
        var cmd = new CommandLine(new JufmtCommand());
        var stringWriter = new StringWriter();
        cmd.setOut(new PrintWriter(stringWriter));

        cmd.execute("-o", ornaments.name(), "bric3");

        assertThat(stringWriter.toString())
                .describedAs(ornaments.name())
                .isEqualToIgnoringNewLines(ornaments.example);

        System.out.printf("%s: %s", ornaments.name(), stringWriter.toString());
    }

    @Test
    public void check_reversed() {
        var cmd = new CommandLine(new JufmtCommand());
        var stringWriter = new StringWriter();
        cmd.setOut(new PrintWriter(stringWriter));


        cmd.execute("-r", "bric3");

        assertThat(stringWriter.toString())
                .describedAs("reversed")
                .isEqualToIgnoringNewLines("3cirb");

        System.out.printf("reversed: %s", stringWriter.toString());
    }
}
