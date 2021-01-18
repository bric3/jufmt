package jufmt;


import picocli.CommandLine;
import picocli.CommandLine.*;
import picocli.CommandLine.Model.CommandSpec;

import java.lang.Character.UnicodeBlock;
import java.lang.Character.UnicodeScript;
import java.util.Arrays;

@Command(name = "jufmt",
         description = "Format input latin string with fancy unicode chars",
         mixinStandardHelpOptions = true)
public class JufmtCommand implements Runnable {
    @Option(names = {"-l", "--list-charsets"}, description = "List available fancy charsets")
    boolean listCharsets;

    @Option(names = {"-c", "--charset"},
            description = "Charset, valid charsets: ${COMPLETION-CANDIDATES}",
            paramLabel = "CHARSET")
    FancyCharsets charset;

    @Option(names = {"-s", "--style"},
            description = "Charset, valid charsets: ${COMPLETION-CANDIDATES}",
            paramLabel = "STYLE")
    FancyStyle style;

    @Option(names = {"-r", "--reversed"},
            description = "Reverse string")
    boolean reversed;

    @Option(names = {"-d", "--describe"},
            description = "Describe characters, or more precisely codepoints")
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
        System.out.printf("char direction: %d%n", Character.getDirectionality(c));
        System.out.printf("unicode block : %s%n", UnicodeBlock.of(c));
        System.out.printf("unicode script: %s%n", UnicodeScript.of(c));
    }

    public void run() {
        if (listCharsets) {
            Arrays.stream(FancyCharsets.values()).forEach(System.out::println);
            return;
        }

        if (describeStyle) {
            if (stringToProcess != null && !stringToProcess.isBlank()) {
                stringToProcess.codePoints()
                               .onClose(() -> System.out.println("--------"))
                               .forEach(JufmtCommand::charDetails);
                return;
            }
            if (charset != null) {
                charset.chars.codePoints()
                             .onClose(() -> System.out.println("--------"))
                             .forEach(JufmtCommand::charDetails);
                return;
            }
        }

        if (stringToProcess == null || stringToProcess.isBlank()) {
            throw new ParameterException(spec.commandLine(), "Expects a non blank STR.");
        }

        var result = charset == null ?
                     new StringBuilder(stringToProcess) :
                     stringToProcess.codePoints()
                                    .map(charset::translateChar)
                                    .boxed()
                                    .collect(() -> new StringBuilder(stringToProcess.length()),
                                             StringBuilder::appendCodePoint,
                                             StringBuilder::append);

        if (reversed) {
            result.reverse();
        }

        if (style != null) {
            result = result.codePoints()
                           .boxed()
                           .collect(style.collector(result.length()));
        }

//        var decoration = FancyDecoration.ethiopicSectionMark;
//        if (decoration != null) {
//            result = result.codePoints()
//                           .boxed()
//                           .collect(decoration.collector(result.length()));
//        }

        spec.commandLine().getOut().printf("%s%n", result);
    }
}
