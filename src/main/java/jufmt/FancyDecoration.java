package jufmt;

import java.util.Arrays;
import java.util.stream.Collector;

public enum FancyDecoration {
    ethiopicSectionMark("ETHIOPIC SECTION MARK") {
        @Override
        public Collector<Integer, StringBuilder, StringBuilder> collector(int length) {
            return FancyCollectors.betweenCodepoints(length, this.value);
        }
    },
    ;

    public final int[] value;

    FancyDecoration(String... codePointName) {
        value = Arrays.stream(codePointName).mapToInt(Character::codePointOf).toArray();
    }

    public abstract Collector<Integer, StringBuilder, StringBuilder> collector(int length);

}
