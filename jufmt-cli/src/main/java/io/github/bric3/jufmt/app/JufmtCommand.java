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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Normalizer;
import java.util.Arrays;

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
                To learn more about normalization: https://www.unicode.org/reports/tr15/
                                
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
            names = {"-c", "--converter"},
            description = "Converter, valid converters: ${COMPLETION-CANDIDATES}",
            paramLabel = "CONVERTER",
            defaultValue = "none"
    )
    FancyConverter converter;

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
    FancyOrnament ornament;

    @Option(
            names = {"-r", "--reversed"},
            description = "Reverse string"
    )
    boolean reversed;

    @Parameters(
            description = "The string to process",
            paramLabel = "STR",
            arity = "0..1"
    )
    String stringToProcessParam;

    @Spec
    CommandSpec spec;

    public static void main(String[] args) {
        var cmd = new CommandLine(new JufmtCommand())
                .setCaseInsensitiveEnumValuesAllowed(true);
        cmd.getHelpSectionMap().put(
                UsageMessageSpec.SECTION_KEY_HEADER,
                help -> Figlet.render("jufmt") + "\n\n"
        );
        cmd.execute(args);
    }

    public void run() {
        var stringToProcess = getStringToProcess(stringToProcessParam);

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

    @Command(
            description = "Describe codepoints and graphemes",
            mixinStandardHelpOptions = true
    )
    void describe(
            @Parameters(
                    description = "The string to describe",
                    paramLabel = "STR",
                    arity = "0..1"
            )
            String stringToProcessParam
    ) {
        var out = spec.commandLine().getOut();
        var stringToProcess = getStringToProcess(stringToProcessParam);

        CharacterDescriber.graphemes(stringToProcess)
                          .onClose(() -> out.println("--------"))
                          .forEach(codePoints -> CharacterDescriber.graphemeDetails(out, codePoints.toArray()));
    }

    @Command(
            description = "Normalize input string using the given Unicode Normalization Forms",
            mixinStandardHelpOptions = true
    )
    void normalize(
            @Option(
                    names = { "--form" },
                    description = "Available forms:  " +
                                  "NFD (Canonical decomposition), " +
                                  "NFC (Canonical decomposition, followed by canonical composition)," +
                                  "NFKD (Compatibility decomposition) or " +
                                  "NFKC (Compatibility decomposition, followed by canonical composition).",
                    paramLabel = "FORM"
            )
            Normalizer.Form normalizationForm,
            @Option(
                    names = {"--strip-diacritic-marks"},
                    description = "Strips the combining diacritical marks from the " +
                                  "string after normalization, only works with NFD or NFKD",
                    defaultValue = "false"
            )
            boolean stripDiacriticalMarks,
            @Parameters(
                    description = "The string to process",
                    paramLabel = "STR",
                    arity = "0..1"
            )
            String stringToProcessParam
    ) {
        var stringToProcess = getStringToProcess(stringToProcessParam);
        try {
            var normalized = new UnicodeNormalizer(
                    stringToProcess,
                    normalizationForm,
                    stripDiacriticalMarks
            ).normalize();
            spec.commandLine().getOut().printf("%s%n", normalized);
        } catch (UnsupportedOperationException e) {
            throw new CommandLine.ParameterException(spec.commandLine(), e.getMessage());
        }
    }

    private String getStringToProcess(String stringToProcessParam) {
        var stringToProcess = stringToProcessParam;
        checkParam(
                StdinReader.isStdinConnectedToTty && (stringToProcessParam == null || stringToProcessParam.isBlank()),
                StdinReader.isAvailable ? "Expects a non blank STR or text from stdin." : "Expects a non blank STR."
        );

        if (StdinReader.isAvailable && !StdinReader.isStdinConnectedToTty && stringToProcessParam == null) {
            var stdin = StdinReader.stdinCharSequence(System.in).toString();
            checkParam(
                    !StdinReader.isStdinConnectedToTty && stdin.isBlank(),
                    "Expects text from stdin or a non blank STR parameter."
            );
            stringToProcess = stdin;
        }
        return stringToProcess;
    }

    private void checkParam(boolean booleanExpr, String msg) {
        if (booleanExpr) {
            throw new ParameterException(spec.commandLine(), msg);
        }
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
                    paramLabel = "STR",
                    arity = "0..1"
            )
            String stringToProcessParam
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
        var stringToProcess = getStringToProcess(stringToProcessParam);
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
                checkParam(!Files.isRegularFile(fontFile) || !(fileName.endsWith(".flf") || fileName.endsWith(".tlf")),
                           "Not a regular FIGlet file: " + figletFont.fontFile);

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
            @Option(
                    names = {"-l", "--level"},
                    description = "Zalgo level: ${COMPLETION-CANDIDATES}",
                    paramLabel = "FONT",
                    defaultValue = "mini"
            ) Zalgo.Level level,
            @Option(
                    names = {"-p", "--position"},
                    description = "Zalgo positions: ${COMPLETION-CANDIDATES}",
                    split = ",",
                    defaultValue = "up,mid,down"
            ) Zalgo.Position[] positions,
            @Parameters(
                    description = "The string to process",
                    paramLabel = "STR",
                    arity = "0..1"
            ) String stringToProcessParam
    ) {
        var stringToProcess = getStringToProcess(stringToProcessParam);
        spec.commandLine().getOut().println(Zalgo.zalgo(stringToProcess, level, positions));
    }
}