package jufmt;

import java.util.Arrays;
import java.util.stream.Collector;

@SuppressWarnings("SpellCheckingInspection")
public enum FancyOrnaments {
    // wrapping chars, e.g. : 【b】【r】【i】【c】【3】, 〖b〗〖r〗〖i〗〖c〗〖3〗, ⧼b⧽⧼r⧽⧼i⧽⧼c⧽⧼3⧽, etc.
    // insert chars, e.g. : b྿r྿i྿c྿3, ፠b፠r፠i፠c፠3, b࿐r࿐i࿐c࿐3, etc.
    // lots of interesting symbols there :
    // - https://www.fileformat.info/info/unicode/block/miscellaneous_mathematical_symbols_b/images.htm
    // - https://www.fileformat.info/info/unicode/block/cjk_symbols_and_punctuation/images.htm


    curvedAngleBracket("⧼b⧽⧼r⧽⧼i⧽⧼c⧽⧼3⧽", "LEFT-POINTING CURVED ANGLE BRACKET", "RIGHT-POINTING CURVED ANGLE BRACKET") {
        @Override
        public Collector<Integer, StringBuilder, StringBuilder> collector(int length) {
            return FancyCollectors.wrapCodepoints(length, this.value[0], this.value[1]);
        }
    },

    blackLenticularBracket("【b】【r】【i】【c】【3】", "LEFT BLACK LENTICULAR BRACKET", "RIGHT BLACK LENTICULAR BRACKET") {
        @Override
        public Collector<Integer, StringBuilder, StringBuilder> collector(int length) {
            return FancyCollectors.wrapCodepoints(length, this.value[0], this.value[1]);
        }
    },
    whiteLenticularBracket("〖b〗〖r〗〖i〗〖c〗〖3〗", "LEFT WHITE LENTICULAR BRACKET", "RIGHT WHITE LENTICULAR BRACKET") {
        @Override
        public Collector<Integer, StringBuilder, StringBuilder> collector(int length) {
            return FancyCollectors.wrapCodepoints(length, this.value[0], this.value[1]);
        }
    },
    tortoiseBracket("〔b〕〔r〕〔i〕〔c〕〔3〕", "LEFT TORTOISE SHELL BRACKET", "RIGHT TORTOISE SHELL BRACKET") {
        @Override
        public Collector<Integer, StringBuilder, StringBuilder> collector(int length) {
            return FancyCollectors.wrapCodepoints(length, this.value[0], this.value[1]);
        }
    },
    whiteTortoiseBracket("〘b〙〘r〙〘i〙〘c〙〘3〙", "LEFT WHITE TORTOISE SHELL BRACKET", "RIGHT WHITE TORTOISE SHELL BRACKET") {
        @Override
        public Collector<Integer, StringBuilder, StringBuilder> collector(int length) {
            return FancyCollectors.wrapCodepoints(length, this.value[0], this.value[1]);
        }
    },
    angleBracket("〈b〉〈r〉〈i〉〈c〉〈3〉", "LEFT ANGLE BRACKET", "RIGHT ANGLE BRACKET") {
        @Override
        public Collector<Integer, StringBuilder, StringBuilder> collector(int length) {
            return FancyCollectors.wrapCodepoints(length, this.value[0], this.value[1]);
        }
    },
    doubleAngleBracket("《b》《r》《i》《c》《3》", "LEFT DOUBLE ANGLE BRACKET", "RIGHT DOUBLE ANGLE BRACKET") {
        @Override
        public Collector<Integer, StringBuilder, StringBuilder> collector(int length) {
            return FancyCollectors.wrapCodepoints(length, this.value[0], this.value[1]);
        }
    },
    cornerBracket("「b」「r」「i」「c」「3」", "LEFT CORNER BRACKET", "RIGHT CORNER BRACKET") {
        @Override
        public Collector<Integer, StringBuilder, StringBuilder> collector(int length) {
            return FancyCollectors.wrapCodepoints(length, this.value[0], this.value[1]);
        }
    },
    whiteCornerBracket("『b』『r』『i』『c』『3』", "LEFT WHITE CORNER BRACKET", "RIGHT WHITE CORNER BRACKET") {
        @Override
        public Collector<Integer, StringBuilder, StringBuilder> collector(int length) {
            return FancyCollectors.wrapCodepoints(length, this.value[0], this.value[1]);
        }
    },

    lightShade("░b░r░i░c░3░", "LIGHT SHADE") {
        @Override
        public Collector<Integer, StringBuilder, StringBuilder> collector(int length) {
            return FancyCollectors.surroundCodepoints(length, this.value);
        }
    },

    wave("࿐b࿐r࿐i࿐c࿐3࿐", "TIBETAN MARK BSKA- SHOG GI MGO RGYAN") {
        @Override
        public Collector<Integer, StringBuilder, StringBuilder> collector(int length) {
            return FancyCollectors.surroundCodepoints(length, this.value);
        }
    },

    tibetanKuRuKhaBzhiMigCan("b྿r྿i྿c྿3", "TIBETAN KU RU KHA BZHI MIG CAN") {
        @Override
        public Collector<Integer, StringBuilder, StringBuilder> collector(int length) {
            return FancyCollectors.betweenCodepoints(length, this.value);
        }
    },

    ethiopicSectionMark("b፠r፠i፠c፠3", "ETHIOPIC SECTION MARK") {
        @Override
        public Collector<Integer, StringBuilder, StringBuilder> collector(int length) {
            return FancyCollectors.betweenCodepoints(length, this.value);
        }
    },

    ;

    public final int[] value;
    public final String example;

    FancyOrnaments(String example, String... codePointName) {
        this.example = example;
        this.value = Arrays.stream(codePointName).mapToInt(Character::codePointOf).toArray();
    }

    public abstract Collector<Integer, StringBuilder, StringBuilder> collector(int length);

}
