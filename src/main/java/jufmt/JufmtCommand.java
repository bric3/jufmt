package jufmt;


import io.leego.banana.BananaUtils;
import io.leego.banana.Font;
import picocli.CommandLine;
import picocli.CommandLine.*;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Model.UsageMessageSpec;

import java.lang.Character.UnicodeBlock;
import java.lang.Character.UnicodeScript;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Random;

@Command(name = "jufmt",
         header = {
                 "░░▒█░█▒█▒█▀░█▄▒▄█░▀█▀",
                 "░▀▄█░▀▄█░█▀░█▒▀▒█░▒█▒",
         },
         description = "Format input latin string with fancy unicode chars",
         mixinStandardHelpOptions = true)
public class JufmtCommand implements Runnable {
    @Option(names = {"-n", "--normalize"},
            description = "Normalize input string using the given strategy, " +
                          "possible forms: ${COMPLETION-CANDIDATES}",
            paramLabel = "FORM")
    Normalizer.Form normalizationForm;

    @Option(names = {"--strip-diacritic-marks"},
            description = "Strips the combining diacritical marks from the " +
                          "string after normalization, only works with NFD or NFKD")
    boolean stripDiacriticalMarks;

    @Option(names = {"-c", "--converter"},
            description = "Converter, valid converters: ${COMPLETION-CANDIDATES}",
            paramLabel = "CONVERTER",
            defaultValue = "none")
    FancyConverters converter;

    @Option(names = {"-s", "--style"},
            description = "Styles, valid styles: ${COMPLETION-CANDIDATES}",
            paramLabel = "STYLE")
    FancyStyle style;

    @Option(names = {"-o", "--ornament"},
            description = "Ornaments, valid ornaments: ${COMPLETION-CANDIDATES}",
            paramLabel = "ORNAMENT")
    FancyOrnaments ornament;

    @Option(names = {"-r", "--reversed"},
            description = "Reverse string")
    boolean reversed;

    @Option(names = {"-d", "--describe"},
            description = "Describe characters, or more precisely codepoints")
    boolean describe;

    @Parameters(description = "The string to process",
                paramLabel = "STR",
                arity = "0..1")
    String stringToProcess;

    @Spec
    CommandSpec spec;

    public static void main(String[] args) {
        var cmd = new CommandLine(JufmtCommand.class).setCaseInsensitiveEnumValuesAllowed(true);
        cmd.getHelpSectionMap().put(UsageMessageSpec.SECTION_KEY_HEADER,
                                    help -> BananaUtils.bananaify("jufmt", randomEnum(Font.class)) + "\n\n");
        cmd.execute(args);
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
            //    because a character can be decomposed for "compatibility",
            //    e.g. '…' -> '...'
            // In decomposition mode (D), the length can change,
            //    because a character can be decomposed by main char and combining mark,
            //    e.g. 'ポ'(U+30DD) -> 'ホ'(U+30DB) + '  ゚'(U+309A)
            // If followed by composition (C), then separated chars are composed back together
            var normalized = Normalizer.normalize(stringToProcess, normalizationForm);
            if (stripDiacriticalMarks) {
                if (EnumSet.of(Normalizer.Form.NFC, Normalizer.Form.NFKC).contains(normalizationForm)) {
                    throw new ParameterException(
                            spec.commandLine(),
                            "Diacritical mark stripping only works without canonical composition, e.g. only NFD and NFKD");
                }
                normalized = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}", "");
            }


//            spec.commandLine().getOut().printf(
//                    "input length: %d, output length %d, %s%n",
//                    stringToProcess.codePointCount(0, stringToProcess.length() - 1),
//                    normalized.codePointCount(0, normalized.length() - 1),
//                    normalized
//            );

            spec.commandLine().getOut().printf("%s%n", normalized);
            return;
        }

        if (describe) {
            if (stringToProcess != null && !stringToProcess.isBlank()) {
                stringToProcess.codePoints()
                               .onClose(() -> System.out.println("--------"))
                               .forEach(JufmtCommand::charDetails);
                return;
            }
            if (converter != FancyConverters.none) {
                converter.chars.codePoints()
                               .onClose(() -> System.out.println("--------"))
                               .forEach(JufmtCommand::charDetails);
                return;
            }
        }

        if (stringToProcess == null || stringToProcess.isBlank()) {
            throw new ParameterException(spec.commandLine(), "Expects a non blank STR.");
        }

        var result = converter.convert(stringToProcess);

        if (reversed) {
            // use StringBuilder reverse as it treats surrogate pairs as single character
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

    @Command(description = "Renders input string as a text banner (FIGlet)",
             mixinStandardHelpOptions = true)
    void figlet(
            @Option(names = {"-f", "--font"},
                    description = "Specify a FIGlet font among: ${COMPLETION-CANDIDATES}",
                    paramLabel = "FONT"
            ) XeroFonts font,
            @Option(names = {"-r", "--random"},
                    description = "Render with a random font"
            ) boolean random,
            @Option(names = {"-a", "--all"},
                    description = "Render renderAll font"
            ) boolean renderAll,
            @Parameters(description = "The string to process",
                        paramLabel = "STR",
                        arity = "0..1"
            ) String stringToProcess
    ) {
        /*
         * https://en.wikipedia.org/wiki/FIGlet
         * https://github.com/lalyos/jfiglet (old, font non included)
         * https://github.com/ColOfAbRiX/figlet4s (scala, recent, lots of feature)
         * https://github.com/dtmo/jfiglet (no deps, lots of feature, recent, some fonts included (can load others))
         * https://github.com/yihleego/banana (no deps, lots of feature, recent, many fonts)
         *
         * https://github.com/vzvz4/jfiglol
         *
         * TODO expose layout options
         */

        var out = spec.commandLine().getOut();
        if (random) {
            out.println(BananaUtils.bananaify(stringToProcess, randomEnum(XeroFonts.class)));
            return;
        }
        if (renderAll) {
            Arrays.stream(XeroFonts.values())
                  .forEach(f -> {
                      out.printf("%s:%n", f);
                      out.println();
                      out.println(BananaUtils.bananaify(stringToProcess, f));
                      out.println();
                  });
            return;
        }

        var rendered = BananaUtils.bananaify(stringToProcess, font);
        out.println(rendered);
    }

    public static <T extends Enum<?>> T randomEnum(Class<T> clazz) {
        var enumConstants = clazz.getEnumConstants();
        return enumConstants[new Random().nextInt(enumConstants.length)];
    }
}
