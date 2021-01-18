package jufmt;

import java.util.stream.Collector;

public class FancyStyleCollectors {
    public static Collector<Integer, StringBuilder, StringBuilder> afterCodepoint(int length, FancyStyle style) {
        return Collector.of(() -> new StringBuilder(length),
                            (sb, codePoint) -> sb.appendCodePoint(codePoint).appendCodePoint(style.value),
                            StringBuilder::append);
    }

    public static Collector<Integer, StringBuilder, StringBuilder> wrapCodepoint(int length, FancyStyle style) {
        return Collector.of(() -> new StringBuilder(length),
                            (sb, codePoint) -> sb.appendCodePoint(style.value).appendCodePoint(codePoint),
                            StringBuilder::append,
                            stringBuilder -> stringBuilder.appendCodePoint(style.value));
    }
}
