package jufmt;

import java.util.stream.Collector;

public enum FancyStyle {
    strikethrough("COMBINING LONG STROKE OVERLAY") {
        @Override
        public Collector<Integer, StringBuilder, StringBuilder> collector(int length) {
            return FancyStyleCollectors.afterCodepoint(length, this);
        }
    },
//    underline("COMBINING DOUBLE MACRON"),
    underline("COMBINING DOUBLE MACRON BELOW") {
        @Override
        public Collector<Integer, StringBuilder, StringBuilder> collector(int length) {
            return FancyStyleCollectors.afterCodepoint(length, this);
        }
    },
    lowline("COMBINING LOW LINE") {
        @Override
        public Collector<Integer, StringBuilder, StringBuilder> collector(int length) {
            return FancyStyleCollectors.afterCodepoint(length, this);
        }
    },
    doubleLowline("COMBINING DOUBLE LOW LINE") {
        @Override
        public Collector<Integer, StringBuilder, StringBuilder> collector(int length) {
            return FancyStyleCollectors.afterCodepoint(length, this);
        }
    },
    overline("COMBINING OVERLINE") {
        @Override
        public Collector<Integer, StringBuilder, StringBuilder> collector(int length) {
            return FancyStyleCollectors.afterCodepoint(length, this);
        }
    },
    doubleOverline("COMBINING DOUBLE OVERLINE") {
        @Override
        public Collector<Integer, StringBuilder, StringBuilder> collector(int length) {
            return FancyStyleCollectors.afterCodepoint(length, this);
        }
    },
    solidus("COMBINING SHORT SOLIDUS OVERLAY") {
        @Override
        public Collector<Integer, StringBuilder, StringBuilder> collector(int length) {
            return FancyStyleCollectors.afterCodepoint(length, this);
        }
    },
    ethiopicSectionMark("ETHIOPIC SECTION MARK") {
        @Override
        public Collector<Integer, StringBuilder, StringBuilder> collector(int length) {
            return FancyStyleCollectors.wrapCodepoint(length, this);
        }
    },
    upwardArrowBelow("COMBINING UPWARDS ARROW BELOW") {
        @Override
        public Collector<Integer, StringBuilder, StringBuilder> collector(int length) {
            return FancyStyleCollectors.afterCodepoint(length, this);
        }
    },
    ;



    public final int value;

    FancyStyle(String codePointName) {
        value = Character.codePointOf(codePointName);
    }


    public abstract Collector<Integer, StringBuilder, StringBuilder> collector(int length);


}
