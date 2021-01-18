package jufmt;

import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.stream.Collector;
import java.util.stream.IntStream;

public class FancyCollectors {
    public static Collector<Integer, StringBuilder, StringBuilder> afterCodepoints(int length, int... codepoints) {
        return Collector.of(() -> new StringBuilder(length),
                            (sb, codePoint) -> {
                                sb.appendCodePoint(codePoint);
                                IntStream.of(codepoints).forEach(sb::appendCodePoint);
                            },
                            StringBuilder::append);
    }

    public static Collector<Integer, StringBuilder, StringBuilder> wrapCodepoints(int length, int... codepoints) {
        return Collector.of(() -> new StringBuilder(length),
                            (sb, codePoint) -> {
                                IntStream.of(codepoints).forEach(sb::appendCodePoint);
                                sb.appendCodePoint(codePoint);
                            },
                            StringBuilder::append,
                            sb -> {
                                IntStream.of(codepoints).forEach(sb::appendCodePoint);
                                return sb;
                            });
    }

    public static Collector<Integer, StringBuilder, StringBuilder> betweenCodepoints(int length, int... codepoints) {
        return Collector.of(() -> new StringBuilder(length),
                            (sb, codePoint) -> {
                                IntStream.of(codepoints).forEach(sb::appendCodePoint);
                                sb.appendCodePoint(codePoint);
                            },
                            StringBuilder::append,
                            sb -> sb.delete(0, IntStream.of(codepoints).map(Character::charCount).sum()));
    }
}
