/*
 * jufmt
 *
 * Copyright (c) 2023, today - Brice DUTHEIL
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package io.github.bric3.jufmt;

import java.util.stream.Collector;
import java.util.stream.IntStream;

public class FancyCollectors {

    public static Collector<Integer, StringBuilder, StringBuilder> afterCodepoints(int length, int... codepoints) {
        return Collector.of(() -> new StringBuilder(length),
                            (sb, codePoint) -> {
                                sb.appendCodePoint(codePoint);
                                for (int codepoint : codepoints) {
                                    sb.appendCodePoint(codepoint);
                                }
                            },
                            StringBuilder::append);
    }

    public static Collector<Integer, StringBuilder, StringBuilder> surroundCodepoints(int length, int... codepoints) {
        return Collector.of(() -> new StringBuilder(length),
                            (sb, codePoint) -> {
                                for (int codepoint : codepoints) {
                                    sb.appendCodePoint(codepoint);
                                }
                                sb.appendCodePoint(codePoint);
                            },
                            StringBuilder::append,
                            sb -> {
                                for (int codepoint : codepoints) {
                                    sb.appendCodePoint(codepoint);
                                }
                                return sb;
                            });
    }

    public static Collector<Integer, StringBuilder, StringBuilder> betweenCodepoints(int length, int... codepoints) {
        return Collector.of(() -> new StringBuilder(length),
                            (sb, codePoint) -> {
                                for (int codepoint : codepoints) {
                                    sb.appendCodePoint(codepoint);
                                }
                                sb.appendCodePoint(codePoint);
                            },
                            StringBuilder::append,
                            sb -> sb.delete(0, IntStream.of(codepoints).map(Character::charCount).sum()));
    }

    public static Collector<Integer, StringBuilder, StringBuilder> wrapCodepoints(int length, int leftCodepoint, int rightCodepoint) {
        return Collector.of(() -> new StringBuilder(length),
                            (sb, codePoint) -> sb.appendCodePoint(leftCodepoint)
                                                 .appendCodePoint(codePoint)
                                                 .appendCodePoint(rightCodepoint),
                            StringBuilder::append);
    }

    public static Collector<Integer, StringBuilder, StringBuilder> toStringBuilder(int length) {
        return Collector.of(() -> new StringBuilder(length),
                            StringBuilder::appendCodePoint,
                            StringBuilder::append);
    }
}
