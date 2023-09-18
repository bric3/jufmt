/*
 * jufmt
 *
 * Copyright (c) 2023, today - Brice DUTHEIL
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package io.github.bric3.jufmt.internal.figlet;

import io.github.bric3.jufmt.Figlet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.UnaryOperator;

import static io.github.bric3.jufmt.internal.figlet.Strings.*;

/**
 * Banana is a FIGlet utility for Java.
 * FIGlet is a computer program that generates text banners,
 * in a variety of typefaces, composed of letters made up of
 * conglomerations of smaller ASCII characters.
 *
 * @author Yihleego
 */
public final class FigletRenderer {
    public static final int INVALID = 0;
    public static final int VALID = 1;
    public static final int END = 2;
    private final Map<Figlet.FontSpec, FontMetadata> cache = new ConcurrentHashMap<>();

    /**
     * Returns the FIGlet of the text with the specified font.
     *
     * @param text the original text.
     * @param font the specified font.
     * @return the FIGlet of the text.
     */
    public @NotNull String render(
            @NotNull String text,
            @NotNull Figlet.FontSpec font
    ) {
        return render(text, font, null, null, null);
    }

    /**
     * Returns the FIGlet of the text with the specified font.
     *
     * @param text             the original text.
     * @param font             the specified font.
     * @param horizontalLayout the horizontal layout.
     * @param verticalLayout   the vertical layout.
     * @return the FIGlet of the text.
     */
    public @NotNull String render(
            @NotNull String text,
            @NotNull Figlet.FontSpec font,
            @Nullable Layout horizontalLayout,
            @Nullable Layout verticalLayout,
            @Nullable UnaryOperator<String> postProcessor
    ) {
        var lines = generateFiglet(text, font, horizontalLayout, verticalLayout);
        if (lines == null || lines.length == 0) {
            return EMPTY_STRING;
        }
        var pp = Objects.requireNonNullElse(postProcessor, UnaryOperator.<String>identity());
        var sb = new StringBuilder();
        for (var s : lines) {
            sb.append(pp.apply(s)).append('\n');
        }
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * Returns the FIGlet of the text with the specified font.
     *
     * @param text             the original text.
     * @param font             the specified font.
     * @param horizontalLayout the horizontal layout.
     * @param verticalLayout   the vertical layout.
     * @return the FIGlet of the text.
     */
    @NotNull
    private String[] generateFiglet(
            @NotNull String text,
            @NotNull Figlet.FontSpec font,
            @Nullable Layout horizontalLayout,
            @Nullable Layout verticalLayout
    ) {
        var meta = getFontMetadata(font);
        var option = meta.option.withLayout(horizontalLayout, verticalLayout);
        var lines = text.split("\\r?\\n");
        var figletLines = new String[lines.length][];
        for (int i = 0; i < lines.length; i++) {
            figletLines[i] = generateFigletLine(lines[i], meta.figletMap, option);
        }
        var output = figletLines[0];
        for (int i = 1; i < figletLines.length; i++) {
            output = smushVerticalFigletLines(output, figletLines[i], option);
        }
        return output;
    }

    private @NotNull FontMetadata getFontMetadata(@NotNull Figlet.FontSpec font) {
        return cache.computeIfAbsent(font, FontMetadata::buildFontMetadata);
    }

    private static @NotNull String[] generateFigletLine(
            @NotNull String text,
            @NotNull Map<Integer, String[]> figletMap,
            @NotNull Option option
    ) {
        int height = option.height;
        var output = new String[height];
        for (int i = 0; i < height; i++) {
            output[i] = EMPTY_STRING;
        }
        for (int index = 0; index < text.length(); index++) {
            var figlet = figletMap.get((int) text.charAt(index));
            if (figlet == null) {
                continue;
            }
            int overlap = 0;
            if (option.rule.getHorizontalLayout() != Layout.FULL) {
                int length = Integer.MAX_VALUE;
                for (int i = 0; i < height; i++) {
                    length = Math.min(length, getHorizontalSmushLength(output[i], figlet[i], option));
                }
                overlap = length == Integer.MAX_VALUE ? 0 : length;
            }
            output = smushHorizontal(output, figlet, overlap, option);
        }
        for (int i = 0; i < output.length; i++) {
            output[i] = output[i].replace(option.hardBlank, BLANK_STRING);
        }
        return output;
    }

    private static @NotNull String[] smushHorizontal(
            @NotNull String[] figlet1,
            @NotNull String[] figlet2,
            int overlap,
            @NotNull Option option
    ) {
        int height = option.height;
        var rule = option.rule;
        var hardBlank = option.hardBlank;
        var output = new String[height];
        for (int i = 0; i < height; i++) {
            var text1 = figlet1[i];
            var text2 = figlet2[i];
            int len1 = text1.length();
            int len2 = text2.length();
            int overlapStart = len1 - overlap;
            var piece = new StringBuilder()
                    .append(substr(text1, 0, Math.max(0, overlapStart)));
            // Determines overlap piece.
            var seg1 = substr(text1, Math.max(0, len1 - overlap), overlap);
            var seg2 = substr(text2, 0, Math.min(overlap, len2));
            for (int j = 0; j < overlap; j++) {
                var ch1 = j < len1 ? substr(seg1, j, 1) : BLANK_STRING;
                var ch2 = j < len2 ? substr(seg2, j, 1) : BLANK_STRING;
                if (!BLANK_STRING.equals(ch1) && !BLANK_STRING.equals(ch2)) {
                    if (rule.getHorizontalLayout() == Layout.FITTED) {
                        piece.append(smushUniversal(ch1, ch2, hardBlank));
                    } else if (rule.getHorizontalLayout() == Layout.SMUSH_U) {
                        piece.append(smushUniversal(ch1, ch2, hardBlank));
                    } else {
                        var nextCh = EMPTY_STRING;
                        if (isEmpty(nextCh) && rule.isHorizontal1())
                            nextCh = smushHorizontalRule1(ch1, ch2, hardBlank);
                        if (isEmpty(nextCh) && rule.isHorizontal2())
                            nextCh = smushHorizontalRule2(ch1, ch2);
                        if (isEmpty(nextCh) && rule.isHorizontal3())
                            nextCh = smushHorizontalRule3(ch1, ch2);
                        if (isEmpty(nextCh) && rule.isHorizontal4())
                            nextCh = smushHorizontalRule4(ch1, ch2);
                        if (isEmpty(nextCh) && rule.isHorizontal5())
                            nextCh = smushHorizontalRule5(ch1, ch2);
                        if (isEmpty(nextCh) && rule.isHorizontal6())
                            nextCh = smushHorizontalRule6(ch1, ch2, hardBlank);
                        if (isEmpty(nextCh))
                            nextCh = smushUniversal(ch1, ch2, hardBlank);
                        piece.append(nextCh);
                    }
                } else {
                    piece.append(smushUniversal(ch1, ch2, hardBlank));
                }
            }
            if (overlap < len2) {
                piece.append(substr(text2, overlap, Math.max(0, len2 - overlap)));
            }
            output[i] = piece.toString();
        }
        return output;
    }

    private static int getHorizontalSmushLength(
            @NotNull String text1,
            @NotNull String text2,
            @NotNull Option option) {
        var rule = option.rule;
        var hardBlank = option.hardBlank;
        if (rule.getHorizontalLayout() == Layout.FULL) {
            return 0;
        }
        int len1 = text1.length();
        int len2 = text2.length();
        int curDist = 1;
        int maxDist = text1.length();
        boolean breakAfter = false;
        boolean validSmush = false;
        if (len1 == 0) {
            return 0;
        }
        while (curDist <= maxDist) {
            boolean skip = false;
            var seg1 = substr(text1, len1 - curDist, len1);
            var seg2 = substr(text2, 0, Math.min(curDist, len2));
            for (int i = 0; i < Math.min(curDist, len2); i++) {
                var ch1 = substr(seg1, i, 1);
                var ch2 = substr(seg2, i, 1);
                if (!BLANK_STRING.equals(ch1) && !BLANK_STRING.equals(ch2)) {
                    if (rule.getHorizontalLayout() == Layout.FITTED) {
                        curDist = curDist - 1;
                        skip = true;
                        break;
                    } else if (rule.getHorizontalLayout() == Layout.SMUSH_U) {
                        if (Objects.equals(ch1, hardBlank) || Objects.equals(ch2, hardBlank)) {
                            curDist = curDist - 1;
                        }
                        skip = true;
                        break;
                    } else {
                        breakAfter = true;
                        validSmush = false;
                        if (rule.isHorizontal1())
                            validSmush = isNotEmpty(smushHorizontalRule1(ch1, ch2, hardBlank));
                        if (!validSmush && rule.isHorizontal2())
                            validSmush = isNotEmpty(smushHorizontalRule2(ch1, ch2));
                        if (!validSmush && rule.isHorizontal3())
                            validSmush = isNotEmpty(smushHorizontalRule3(ch1, ch2));
                        if (!validSmush && rule.isHorizontal4())
                            validSmush = isNotEmpty(smushHorizontalRule4(ch1, ch2));
                        if (!validSmush && rule.isHorizontal5())
                            validSmush = isNotEmpty(smushHorizontalRule5(ch1, ch2));
                        if (!validSmush && rule.isHorizontal6())
                            validSmush = isNotEmpty(smushHorizontalRule6(ch1, ch2, hardBlank));
                        if (!validSmush) {
                            curDist = curDist - 1;
                            skip = true;
                            break;
                        }
                    }
                }
            }
            if (skip) {
                break;
            }
            if (breakAfter) {
                break;
            }
            curDist++;
        }
        return Math.min(maxDist, curDist);
    }

    private static @NotNull String[] smushVerticalFigletLines(
            @NotNull String[] figlet1,
            @NotNull String[] figlet2,
            @NotNull Option option) {
        int len1 = figlet1[0].length();
        int len2 = figlet2[0].length();
        int overlap;
        if (len1 > len2) {
            padLines(figlet2, len1 - len2);
        } else if (len2 > len1) {
            padLines(figlet1, len2 - len1);
        }
        overlap = getVerticalSmushDist(figlet1, figlet2, option);
        return smushVertical(figlet1, figlet2, overlap, option);
    }

    private static int getVerticalSmushDist(
            @NotNull String[] figlet1,
            @NotNull String[] figlet2,
            @NotNull Option option) {
        int curDist = 1;
        int maxDist = figlet1.length;
        int len1 = figlet1.length;
        String[] subLines1;
        String[] subLines2;
        while (curDist <= maxDist) {
            subLines1 = slice(figlet1, Math.max(0, len1 - curDist), len1);
            subLines2 = slice(figlet2, 0, Math.min(maxDist, curDist));
            int result = VALID;
            for (int i = 0; i < subLines2.length; i++) {
                int ret = canSmushVertical(subLines1[i], subLines2[i], option);
                if (END == ret) {
                    result = ret;
                } else if (INVALID == ret) {
                    result = ret;
                    break;
                }
            }
            if (INVALID == result) {
                curDist--;
                break;
            }
            if (END == result) {
                break;
            }
            curDist++;
        }
        return Math.min(maxDist, curDist);
    }

    /**
     * Takes in two lines of text and returns one of the following:
     * "valid" - These lines can be smushed together given the current smushing rules.
     * "end" - The lines can be smushed, but we're at a stopping point.
     * "invalid" - The two lines cannot be smushed together.
     *
     * @param line1  A line of text.
     * @param line2  A line of text.
     * @param option the option.
     */
    private static int canSmushVertical(
            @NotNull String line1,
            @NotNull String line2,
            @NotNull Option option) {
        var rule = option.rule;
        if (rule.getVerticalLayout() == Layout.FULL) {
            return INVALID;
        }
        int len = Math.min(line1.length(), line2.length());
        String ch1;
        String ch2;
        boolean endSmush = false;
        boolean validSmush;
        if (len == 0) {
            return INVALID;
        }
        for (int i = 0; i < len; i++) {
            ch1 = substr(line1, i, 1);
            ch2 = substr(line2, i, 1);
            if (!BLANK_STRING.equals(ch1) && !BLANK_STRING.equals(ch2)) {
                if (rule.getVerticalLayout() == Layout.FITTED) {
                    return INVALID;
                } else if (rule.getVerticalLayout() == Layout.SMUSH_U) {
                    return END;
                } else {
                    if (isNotEmpty(smushVerticalRule5(ch1, ch2))) {
                        continue;
                    }
                    // rule 5 allow for "super" smushing, but only if we're not already ending this smush
                    validSmush = false;
                    if (rule.isVertical1())
                        validSmush = isNotEmpty(smushVerticalRule1(ch1, ch2));
                    if (!validSmush && rule.isVertical2())
                        validSmush = isNotEmpty(smushVerticalRule2(ch1, ch2));
                    if (!validSmush && rule.isVertical3())
                        validSmush = isNotEmpty(smushVerticalRule3(ch1, ch2));
                    if (!validSmush && rule.isVertical4())
                        validSmush = isNotEmpty(smushVerticalRule4(ch1, ch2));
                    endSmush = true;
                    if (!validSmush) {
                        return INVALID;
                    }
                }
            }
        }
        if (endSmush) {
            return END;
        } else {
            return VALID;
        }
    }

    private static @NotNull String[] smushVertical(
            @NotNull String[] figlet1,
            @NotNull String[] figlet2,
            int overlap,
            @NotNull Option option
    ) {
        int len1 = figlet1.length;
        int len2 = figlet2.length;
        var piece1 = slice(figlet1, 0, Math.max(0, len1 - overlap));
        var piece21 = slice(figlet1, Math.max(0, len1 - overlap), len1);
        var piece22 = slice(figlet2, 0, Math.min(overlap, len2));
        var piece2 = new String[piece21.length];
        String line;
        for (int i = 0; i < piece21.length; i++) {
            if (i >= len2) {
                line = piece21[i];
            } else {
                line = smushVerticalLines(piece21[i], piece22[i], option);
            }
            piece2[i] = line;
        }
        var piece3 = slice(figlet2, Math.min(overlap, len2), len2);
        return flatten(piece1, piece2, piece3);
    }

    private static @NotNull String smushVerticalLines(
            @NotNull String line1,
            @NotNull String line2,
            @NotNull Option option) {
        var rule = option.rule;
        int len = Math.min(line1.length(), line2.length());
        String ch1;
        String ch2;
        var validSmush = EMPTY_STRING;
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < len; i++) {
            ch1 = substr(line1, i, 1);
            ch2 = substr(line2, i, 1);
            if (!BLANK_STRING.equals(ch1) && !BLANK_STRING.equals(ch2)) {
                if (rule.getVerticalLayout() == Layout.FITTED) {
                    result.append(smushUniversal(ch1, ch2, null));
                } else if (rule.getVerticalLayout() == Layout.SMUSH_U) {
                    result.append(smushUniversal(ch1, ch2, null));
                } else {
                    if (rule.isVertical5())
                        validSmush = smushVerticalRule5(ch1, ch2);
                    if (isEmpty(validSmush) && rule.isVertical1())
                        validSmush = smushVerticalRule1(ch1, ch2);
                    if (isEmpty(validSmush) && rule.isVertical2())
                        validSmush = smushVerticalRule2(ch1, ch2);
                    if (isEmpty(validSmush) && rule.isVertical3())
                        validSmush = smushVerticalRule3(ch1, ch2);
                    if (isEmpty(validSmush) && rule.isVertical4())
                        validSmush = smushVerticalRule4(ch1, ch2);
                    result.append(validSmush);
                }
            } else {
                result.append(smushUniversal(ch1, ch2, null));
            }
        }
        return result.toString();
    }

    /**
     * Rule 1: EQUAL CHARACTER SMUSHING (code value 1)
     * Two sub-characters are smushed into a single sub-character
     * if they are the same.  This rule does not smush hardblanks.
     * (See rule 6 on hardblanks below)
     */
    private static String smushHorizontalRule1(String s1, String s2, String hardBlank) {
        if (Objects.equals(s1, s2)) {
            if (!Objects.equals(s1, hardBlank)) {
                return s1;
            }
        }
        return EMPTY_STRING;
    }

    /**
     * Rule 2: UNDERSCORE SMUSHING (code value 2)
     * An underscore ("_") will be replaced by any of:
     * "|", "/", "\", "[", "]", "{", "}", "(", ")", "<" or ">".
     */
    private static String smushHorizontalRule2(String s1, String s2) {
        String rule = "|/\\[]{}()<>";
        if (Objects.equals("_", s1)) {
            if (rule.contains(s2)) {
                return s2;
            }
        } else {
            if (Objects.equals("_", s2)) {
                if (rule.contains(s1)) {
                    return s1;
                }
            }
        }
        return EMPTY_STRING;
    }

    /**
     * Rule 3: HIERARCHY SMUSHING (code value 4)
     * A hierarchy of six classes is used: "|", "/\", "[]", "{}", "()", and "<>".
     * When two smushing sub-characters are from different classes,
     * the one from the latter class will be used.
     */
    private static String smushHorizontalRule3(String s1, String s2) {
        String rule = "| /\\ [] {} () <>";
        int pos1 = rule.indexOf(s1);
        int pos2 = rule.indexOf(s2);
        if (pos1 != -1 && pos2 != -1 && pos1 != pos2 && Math.abs(pos1 - pos2) != 1) {
            return substr(rule, Math.max(pos1, pos2), 1);
        }
        return EMPTY_STRING;
    }

    /**
     * Rule 4: OPPOSITE PAIR SMUSHING (code value 8)
     * Smushes opposing brackets ("[]" or "]["), braces ("{}" or "}{")
     * and parentheses ("()" or ")(") together, replacing
     * any such pair with a vertical bar ("|").
     */
    private static String smushHorizontalRule4(String s1, String s2) {
        String rule = "[] {} ()";
        int pos1 = rule.indexOf(s1);
        int pos2 = rule.indexOf(s2);
        if (pos1 != -1 && pos2 != -1 && Math.abs(pos1 - pos2) <= 1) {
            return "|";
        }
        return EMPTY_STRING;
    }

    /**
     * Rule 5: BIG X SMUSHING (code value 16)
     * Smushes "/\" into "|", "\/" into "Y", and "><" into "X".
     * Note that "<>" is not smushed in any way by this rule.
     * The name "BIG X" is historical; originally all three pairs
     * were smushed into "X".
     */
    private static String smushHorizontalRule5(String s1, String s2) {
        String rule = "/\\ \\/ ><";
        int pos1 = rule.indexOf(s1);
        int pos2 = rule.indexOf(s2);
        if (pos1 != -1 && pos2 != -1 && pos2 - pos1 == 1) {
            if (pos1 == 0) {
                return "|";
            } else if (pos1 == 3) {
                return "Y";
            } else if (pos1 == 6) {
                return "X";
            }
        }
        return EMPTY_STRING;
    }

    /**
     * Rule 6: HARDBLANK SMUSHING (code value 32)
     * Smushes two hardblanks together, replacing them with a single hardblank.
     * (See "Hardblanks" below.)
     */
    private static @NotNull String smushHorizontalRule6(@NotNull String s1, @NotNull String s2, String hardBlank) {
        if (Objects.equals(s1, hardBlank)) {
            if (Objects.equals(s2, hardBlank)) {
                return hardBlank;
            }
        }
        return EMPTY_STRING;
    }

    /**
     * Rule 1: EQUAL CHARACTER SMUSHING (code value 256)
     * Same as horizontal smushing rule 1.
     */
    private static @NotNull String smushVerticalRule1(@NotNull String s1, @NotNull String s2) {
        if (Objects.equals(s1, s2)) {
            return s1;
        }
        return EMPTY_STRING;
    }

    /**
     * Rule 2: UNDERSCORE SMUSHING (code value 512)
     * Same as horizontal smushing rule 2.
     */
    private static @NotNull String smushVerticalRule2(@NotNull String s1, @NotNull String s2) {
        return smushHorizontalRule2(s1, s2);
    }

    /**
     * Rule 3: HIERARCHY SMUSHING (code value 1024)
     * Same as horizontal smushing rule 3.
     */
    private static @NotNull String smushVerticalRule3(@NotNull String s1, @NotNull String s2) {
        return smushHorizontalRule3(s1, s2);
    }

    /**
     * Rule 4: HORIZONTAL LINE SMUSHING (code value 2048)
     * Smushes stacked pairs of "-" and "_", replacing them with
     * a single "=" sub-character.  It does not matter which is
     * found above the other.  Note that vertical smushing rule 1
     * will smush IDENTICAL pairs of horizontal lines, while this
     * rule smushes horizontal lines consisting of DIFFERENT
     * sub-characters.
     */
    private static @NotNull String smushVerticalRule4(@NotNull String s1, @NotNull String s2) {
        if ((Objects.equals("-", s1) && Objects.equals("_", s2)) || (Objects.equals("_", s1) && Objects.equals("-", s2))) {
            return "=";
        }
        return EMPTY_STRING;
    }

    /**
     * Rule 5: VERTICAL LINE SUPERSMUSHING (code value 4096)
     * This one rule is different from all others, in that it
     * "supersmushes" vertical lines consisting of several
     * vertical bars ("|").  This creates the illusion that
     * FIGcharacters have slid vertically against each other.
     * Supersmushing continues until any sub-characters other
     * than "|" would have to be smushed.  Supersmushing can
     * produce impressive results, but it is seldom possible,
     * since other sub-characters would usually have to be
     * considered for smushing as soon as any such stacked
     * vertical lines are encountered.
     */
    private static @NotNull String smushVerticalRule5(@NotNull String s1, @NotNull String s2) {
        if (Objects.equals("|", s1)) {
            if (Objects.equals("|", s2)) {
                return "|";
            }
        }
        return EMPTY_STRING;
    }

    /**
     * Universal smushing simply overrides the sub-character from the
     * earlier FIGcharacter with the sub-character from the later
     * FIGcharacter.  This produces an "overlapping" effect with some
     * FIGfonts, wherin the latter FIGcharacter may appear to be "in front".
     */
    private static @NotNull String smushUniversal(@NotNull String s1, @NotNull String s2, @Nullable String hardBlank) {
        if (Objects.equals(BLANK_STRING, s2) || Objects.equals(EMPTY_STRING, s2)) {
            return s1;
        } else {
            if (Objects.equals(s2, hardBlank) && !Objects.equals(BLANK_STRING, s1)) {
                return s1;
            } else {
                return s2;
            }
        }
    }
}
