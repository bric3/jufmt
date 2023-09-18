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


import io.github.bric3.jufmt.*;
import picocli.CommandLine;
import picocli.CommandLine.*;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Model.UsageMessageSpec;

import java.io.IOException;
import java.lang.Character.UnicodeBlock;
import java.lang.Character.UnicodeScript;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.EnumSet;

@Command(
        name = "jufmt",
        header = {
                """
                        ░░▒█░█▒█▒█▀░█▄▒▄█░▀█▀
                        ░▀▄█░▀▄█░█▀░█▒▀▒█░▒█▒
                        """,
        },
        description = "Format input latin string with fancy unicode chars",
        footer = {
                """
                                        
                        Fonts are provided by:                             ╱|、
                         - https://github.com/xero/figlet-fonts          (˚ˎ 。7
                         - https://github.com/thugcrowd/gangshit          |、˜〵
                                                                          じしˍ,)ノ
                        """,
        },
        mixinStandardHelpOptions = true
)
public class JufmtCommand implements Runnable {
    @Option(
            names = {"-n", "--normalize"},
            description = "Normalize input string using the given strategy, " +
                          "possible forms: ${COMPLETION-CANDIDATES}",
            paramLabel = "FORM"
    )
    Normalizer.Form normalizationForm;

    @Option(
            names = {"--strip-diacritic-marks"},
            description = "Strips the combining diacritical marks from the " +
                          "string after normalization, only works with NFD or NFKD"
    )
    boolean stripDiacriticalMarks;

    @Option(
            names = {"-c", "--converter"},
            description = "Converter, valid converters: ${COMPLETION-CANDIDATES}",
            paramLabel = "CONVERTER",
            defaultValue = "none"
    )
    FancyConverters converter;

    @Option(
            names = {"-s", "--style"},
            description = "Styles, valid styles: ${COMPLETION-CANDIDATES}",
            paramLabel = "STYLE"
    )
    FancyStyle style;

    @Option(
            names = {"-o", "--ornament"},
            description = "Ornaments, valid ornaments: ${COMPLETION-CANDIDATES}",
            paramLabel = "ORNAMENT"
    )
    FancyOrnaments ornament;

    @Option(
            names = {"-r", "--reversed"},
            description = "Reverse string"
    )
    boolean reversed;

    @Option(
            names = {"-d", "--describe"},
            description = "Describe characters, or more precisely codepoints"
    )
    boolean describe;

    @Parameters(
            description = "The string to process",
            paramLabel = "STR",
            arity = "0..1"
    )
    String stringToProcess;

    @Spec
    CommandSpec spec;

    public static void main(String[] args) {
        var cmd = new CommandLine(JufmtCommand.class).setCaseInsensitiveEnumValuesAllowed(true);
        cmd.getHelpSectionMap().put(
                UsageMessageSpec.SECTION_KEY_HEADER,
                help -> Figlet.render("jufmt") + "\n\n"
        );
        cmd.execute(args);
    }

    private void charDetails(int codePoint) {
        var out = spec.commandLine().getOut();
        out.println("--------");
        out.printf("char          : %04x %s%n", codePoint, Character.toString(codePoint));
        out.printf("char count    : %d%n", Character.charCount(codePoint));
        out.printf("lower case    : %04x %s%n", Character.toLowerCase(codePoint), Character.toString(Character.toLowerCase(codePoint)));
        out.printf("title case    : %04x %s%n", Character.toTitleCase(codePoint), Character.toString(Character.toTitleCase(codePoint)));
        out.printf("upper case    : %04x %s%n", Character.toUpperCase(codePoint), Character.toString(Character.toUpperCase(codePoint)));
        out.printf("char name     : %s%n", Character.getName(codePoint));
        out.printf("char type     : %s%n", getCharacterCategoryName(codePoint));
        out.printf("char direction: %d%n", Character.getDirectionality(codePoint));
        out.printf("unicode block : %s%n", UnicodeBlock.of(codePoint));
        out.printf("unicode script: %s%n", UnicodeScript.of(codePoint));
    }

    private String getCharacterCategoryName(int codePoint) {
        // Unfortunately, character class doesn't give access to category name.
        return switch (Character.getType(codePoint)) {
            case Character.UNASSIGNED -> "UNASSIGNED";
            case Character.UPPERCASE_LETTER -> "UPPERCASE_LETTER";
            case Character.LOWERCASE_LETTER -> "LOWERCASE_LETTER";
            case Character.TITLECASE_LETTER -> "TITLECASE_LETTER";
            case Character.MODIFIER_LETTER -> "MODIFIER_LETTER";
            case Character.OTHER_LETTER -> "OTHER_LETTER";
            case Character.NON_SPACING_MARK -> "NON_SPACING_MARK";
            case Character.ENCLOSING_MARK -> "ENCLOSING_MARK";
            case Character.COMBINING_SPACING_MARK -> "COMBINING_SPACING_MARK";
            case Character.DECIMAL_DIGIT_NUMBER -> "DECIMAL_DIGIT_NUMBER";
            case Character.LETTER_NUMBER -> "LETTER_NUMBER";
            case Character.OTHER_NUMBER -> "OTHER_NUMBER";
            case Character.SPACE_SEPARATOR -> "SPACE_SEPARATOR";
            case Character.LINE_SEPARATOR -> "LINE_SEPARATOR";
            case Character.PARAGRAPH_SEPARATOR -> "PARAGRAPH_SEPARATOR";
            case Character.CONTROL -> "CONTROL";
            case Character.FORMAT -> "FORMAT";
            case Character.PRIVATE_USE -> "PRIVATE_USE";
            case Character.SURROGATE -> "SURROGATE";
            case Character.DASH_PUNCTUATION -> "DASH_PUNCTUATION";
            case Character.START_PUNCTUATION -> "START_PUNCTUATION";
            case Character.END_PUNCTUATION -> "END_PUNCTUATION";
            case Character.CONNECTOR_PUNCTUATION -> "CONNECTOR_PUNCTUATION";
            case Character.OTHER_PUNCTUATION -> "OTHER_PUNCTUATION";
            case Character.MATH_SYMBOL -> "MATH_SYMBOL";
            case Character.CURRENCY_SYMBOL -> "CURRENCY_SYMBOL";
            case Character.MODIFIER_SYMBOL -> "MODIFIER_SYMBOL";
            case Character.OTHER_SYMBOL -> "OTHER_SYMBOL";
            case Character.INITIAL_QUOTE_PUNCTUATION -> "INITIAL_QUOTE_PUNCTUATION";
            case Character.FINAL_QUOTE_PUNCTUATION -> "FINAL_QUOTE_PUNCTUATION";
            default -> "UNKNOWN";
        };
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
                        .onClose(() -> spec.commandLine().getOut().println("--------"))
                        .forEach(this::charDetails);
                return;
            }
            if (converter != FancyConverters.none) {
                converter.chars.codePoints()
                        .onClose(() -> spec.commandLine().getOut().println("--------"))
                        .forEach(this::charDetails);
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

    static class FigletFont {
        @Option(names = {"-f", "--font"},
                description = "Specify a FIGlet font among: ${COMPLETION-CANDIDATES}",
                paramLabel = "FONT"
        )
        EmbeddedFigletFonts font;
        @Option(names = {"--font-file"},
                description = "Specify a FIGlet or Toilet font file",
                paramLabel = "FILE"
        )
        Path fontFile;
    }

    @Command(
            description = "Renders input string as a text banner (FIGlet)",
            mixinStandardHelpOptions = true
    )
    void figlet(
            @ArgGroup(exclusive = true)
            FigletFont figletFont,
            @Option(
                    names = {"-r", "--random"},
                    description = "Render with a random font"
            )
            boolean random,
            @Option(
                    names = {"-a", "--all"},
                    description = "Render renderAll font"
            )
            boolean renderAll,
            @Parameters(
                    description = "The string to process",
                    paramLabel = "STR"
            )
            String stringToProcess
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
        if (random || figletFont == null && !renderAll) {
            out.println(Figlet.render(stringToProcess));
            return;
        }
        if (renderAll) {
            Arrays.stream(EmbeddedFigletFonts.values())
                    .forEach(f -> {
                        out.printf("%s:%n", f);
                        out.println();
                        out.println(Figlet.render(stringToProcess, f));
                        out.println();
                    });
            return;
        }

        Figlet.FontSpec font;
        if (figletFont.fontFile != null) {
            try {
                var fontFile = Path.of(".").toRealPath().resolve(figletFont.fontFile);
                var fileName = fontFile.getFileName().toString();
                if (!Files.isRegularFile(fontFile) || !(fileName.endsWith(".flf") || fileName.endsWith(".tlf"))) {
                    throw new ParameterException(
                            spec.commandLine(),
                            "Not a regular FIGlet file: " + figletFont.fontFile
                    );
                }

                font = Figlet.FontSpec.of(
                        fileName.substring(0, fileName.lastIndexOf('.')),
                        fontFile
                );
            } catch (IOException e) {
                throw new ParameterException(
                        spec.commandLine(),
                        "Invalid file: " + figletFont.fontFile
                );
            }
        } else {
            font = figletFont.font;
        }

        out.println(Figlet.render(stringToProcess, font));
    }

    @Command(
            description = "Renders input with Zalgo",
            mixinStandardHelpOptions = true
    )
    void zalgo(
            @Option(names = {"-l", "--level"},
                    description = "Zalgo level: ${COMPLETION-CANDIDATES}",
                    paramLabel = "FONT",
                    defaultValue = "mini"
            ) Zalgo.Level level,
            @Option(names = {"-p", "--position"},
                    description = "Zalgo positions: ${COMPLETION-CANDIDATES}",
                    split = ",",
                    defaultValue = "up,mid,down"
            ) Zalgo.Position[] positions,
            @Parameters(description = "The string to process",
                    paramLabel = "STR"
            ) String stringToProcess
    ) {
        spec.commandLine().getOut().println(Zalgo.zalgo(stringToProcess, level, positions));
    }
}