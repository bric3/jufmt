package jufmt;

import java.util.Arrays;
import java.util.stream.Collector;

public enum FancyDecoration {
    // wrapping chars, e.g. : 【b】【r】【i】【c】【3】, 〖b〗〖r〗〖i〗〖c〗〖3〗, ⧼b⧽⧼r⧽⧼i⧽⧼c⧽⧼3⧽, etc.
    // insert chars, e.g. : b྿r྿i྿c྿3, ፠b፠r፠i፠c፠3, b࿐r࿐i࿐c࿐3, etc.
    // lots of interesting symbols there :
    // - https://www.fileformat.info/info/unicode/block/miscellaneous_mathematical_symbols_b/images.htm
    // - https://www.fileformat.info/info/unicode/block/cjk_symbols_and_punctuation/images.htm


//    blackLenticularBracket("") {
//        "LEFT-POINTING CURVED ANGLE BRACKET" c "RIGHT-POINTING CURVED ANGLE BRACKET"
//    },
//
//
//    blackLenticularBracket("") {
//        "LEFT BLACK LENTICULAR BRACKET" c "RIGHT BLACK LENTICULAR BRACKET"
//    },
//    whiteLenticularBracket("") {
//        "LEFT WHITE LENTICULAR BRACKET" c "RIGHT WHITE LENTICULAR BRACKET"
//    },
//    tortoiseBracket("") {
//        "LEFT TORTOISE SHELL BRACKET" c "RIGHT TORTOISE SHELL BRACKET"
//    },
//    whiteTortoiseBracket("") {
//        "LEFT WHITE TORTOISE SHELL BRACKET" c "RIGHT WHITE TORTOISE SHELL BRACKET"
//    },
//
//    angleBracket("") {
//        "LEFT ANGLE BRACKET" c "RIGHT ANGLE BRACKET"
//    },
//    doubleAngleBracket("") {
//        "LEFT DOUBLE ANGLE BRACKET" c "RIGHT DOUBLE ANGLE BRACKET"
//    },
//
//    cornerBracket("") {
//        "LEFT CORNER BRACKET" c "RIGHT CORNER BRACKET"
//    },
//    whiteCornerBracket("") {
//        "LEFT WHITE CORNER BRACKET" c "RIGHT WHITE CORNER BRACKET"
//    },

    wave("TIBETAN MARK BSKA- SHOG GI MGO RGYAN") {  // ࿐
        @Override
        public Collector<Integer, StringBuilder, StringBuilder> collector(int length) {
            return FancyCollectors.surroundCodepoints(length, this.value);
        }
    },

    tibetanKuRuKhaBzhiMigCan("TIBETAN KU RU KHA BZHI MIG CAN") { // ྿
        @Override
        public Collector<Integer, StringBuilder, StringBuilder> collector(int length) {
            return FancyCollectors.betweenCodepoints(length, this.value);
        }
    },

    ethiopicSectionMark("ETHIOPIC SECTION MARK") { // ፠
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
