package jufmt;


import picocli.CommandLine;
import picocli.CommandLine.*;
import picocli.CommandLine.Model.CommandSpec;

import java.lang.Character.UnicodeBlock;
import java.lang.Character.UnicodeScript;
import java.util.Arrays;

@Command(name = "jufmt", description = "Format input latin string with fancy unicode chars",
         mixinStandardHelpOptions = true)
public class JufmtCommand implements Runnable {
    @Option(names = {"-l", "--list-styles"}, description = "List available styles")
    boolean listStyles;

    @Option(names = {"-s", "--style"},
            description = "Letter style, valid styles: ${COMPLETION-CANDIDATES}",
            paramLabel = "STYLE")
    FancyCharSets style;

    @Option(names = {"-r", "--reversed"},
            description = "Reverse string")
    boolean reversed;

    @Option(names = {"-d", "--describe"},
            description = "Describe chararacters, or more precisely codepoints")
    boolean describeStyle;

    @Parameters(description = "The string to process",
                paramLabel = "STR",
                arity = "0..1")
    String stringToProcess;

    @Spec
    CommandSpec spec;

    public static void main(String[] args) {
        new CommandLine(JufmtCommand.class).execute(args);
    }

    private static void charDetails(int c) {
        System.out.println("--------");
        System.out.printf("char          : %04x %s%n", c, Character.toString(c));
        System.out.printf("char count    : %d%n", Character.charCount(c));
        System.out.printf("lower case    : %04x %s%n", Character.toLowerCase(c), Character.toString(Character.toLowerCase(c)));
        System.out.printf("title case    : %04x %s%n", Character.toTitleCase(c), Character.toString(Character.toTitleCase(c)));
        System.out.printf("upper case    : %04x %s%n", Character.toUpperCase(c), Character.toString(Character.toUpperCase(c)));
        System.out.printf("char name     : %s%n", Character.getName(c));
        System.out.printf("char type     : %d%n", Character.getType(c));
        System.out.printf("unicode block : %s%n", UnicodeBlock.of(c));
        System.out.printf("unicode script: %s%n", UnicodeScript.of(c));
    }

    public void run() {
        if (listStyles) {
            Arrays.stream(FancyCharSets.values()).forEach(System.out::println);
            return;
        }

        if (describeStyle) {
            if (stringToProcess != null && !stringToProcess.isBlank()) {
                stringToProcess.codePoints()
                               .forEach(JufmtCommand::charDetails);
                return;
            }
            if (style != null) {
                style.chars.codePoints()
                           .onClose(() -> System.out.println("--------"))
                           .forEach(JufmtCommand::charDetails);
                return;
            }
        }

        if (stringToProcess == null || stringToProcess.isBlank()) {
            throw new ParameterException(spec.commandLine(), "Expects a non blank STR.");
        }

        var result = style == null ?
                     new StringBuilder(stringToProcess) :
                     stringToProcess.codePoints()
                                    .map(style::translateChar)
                                    .collect(StringBuilder::new,
                                             StringBuilder::appendCodePoint,
                                             StringBuilder::append);

        if (reversed) {
            result.reverse();
        }

        System.out.printf("%s%n", result);
    }
}
