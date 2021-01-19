package jufmt;


import picocli.CommandLine;
import picocli.CommandLine.*;
import picocli.CommandLine.Model.CommandSpec;

import java.lang.Character.UnicodeBlock;
import java.lang.Character.UnicodeScript;
import java.text.Normalizer;

@Command(name = "jufmt",
         header = {
                 "░░▒█░█▒█▒█▀░█▄▒▄█░▀█▀",
                 "░▀▄█░▀▄█░█▀░█▒▀▒█░▒█▒",
         },
         description = "Format input latin string with fancy unicode chars",
         mixinStandardHelpOptions = true)
public class JufmtCommand implements Runnable {
    @Option(names = {"-n", "--normalize"},
            description = "Normalize inout string using the given strategy, " +
                          "possible forms: ${COMPLETION-CANDIDATES}",
            paramLabel = "FORM")
    Normalizer.Form normalizationForm;

    @Option(names = {"--strip-diacritic-marks"},
            description = "Strips the combining diacritical marks from the " +
                          "string after normalization, only works with NFD or NFKD")
    boolean stripDiacriticalMarks;

    @Option(names = {"-c", "--charset"},
            description = "Charset, valid charsets: ${COMPLETION-CANDIDATES}",
            paramLabel = "CHARSET")
    FancyCharsets charset;

    @Option(names = {"-s", "--style"},
            description = "Styles, valid charsets: ${COMPLETION-CANDIDATES}",
            paramLabel = "STYLE")
    FancyStyle style;

    @Option(names = {"-o", "--ornament"},
            description = "Ornaments, valid charsets: ${COMPLETION-CANDIDATES}",
            paramLabel = "ORNAMENT")
    FancyOrnaments ornament;

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
        if (normalizationForm != null) {
            // https://unicode.org/reports/tr15/#Norm_Forms
            // https://towardsdatascience.com/difference-between-nfd-nfc-nfkd-and-nfkc-explained-with-python-code-e2631f96ae6c
            // - Normalization Form D (NFD) : Canonical Decomposition
            // - Normalization Form C (NFC) : Canonical Decomposition, followed by Canonical Composition
            // - Normalization Form KD (NFKD) : Compatibility Decomposition
            // - Normalization Form KC (NFKC) : Compatibility Decomposition, followed by Canonical Composition
            //
            // In compatibility mode (K), the length can change,
            //    because a character can be decomposed for "compability",
            //    e.g. '…' -> '...'
            // In decomposition mode (D), the length can change,
            //    because a character can be decomposed by main char and combining mark,
            //    e.g. 'ポ'(U+30DD) -> 'ホ'(U+30DB) + '  ゚'(U+309A)
            // If followed by composition (C), then separated chars are composed back together
            var normalized = Normalizer.normalize(stringToProcess, normalizationForm);
            if (stripDiacriticalMarks) {
                normalized = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}", "");
            }

            spec.commandLine().getOut().printf(
                    "input length: %d, output length %d, %s%n",
                    stringToProcess.codePointCount(0, stringToProcess.length() - 1),
                    normalized.codePointCount(0, normalized.length() - 1),
                    normalized
            );

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

        if (ornament != null) {
            result = result.codePoints()
                           .boxed()
                           .collect(ornament.collector(result.length()));
        }

        spec.commandLine().getOut().printf("%s%n", result);
    }
}
