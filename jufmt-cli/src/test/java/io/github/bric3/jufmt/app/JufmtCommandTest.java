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


import io.github.bric3.jufmt.FancyConverter;
import io.github.bric3.jufmt.FancyOrnament;
import io.github.bric3.jufmt.FancyStyle;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.EnumSource.Mode;
import org.junit.jupiter.params.provider.MethodSource;
import picocli.CommandLine;

import java.text.Normalizer;
import java.util.stream.Stream;

import static io.github.bric3.jufmt.app.JufmtTestUtil.jufmt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;


@SuppressWarnings("SpellCheckingInspection")
public class JufmtCommandTest {

    @ParameterizedTest
    @EnumSource
    public void check_style(FancyStyle style) {
        var result = jufmt("-s", style.name(), "bric3");

        assertThat(result.out())
                .describedAs(style.name())
                .isEqualToIgnoringNewLines(style.example);

        System.out.printf("%s: %s", style.name(), result.out());

    }

    @ParameterizedTest
    @EnumSource(mode = Mode.EXCLUDE, names = {"none"})
    public void check_charset(FancyConverter converter) {
        var result = jufmt("-c", converter.name(), "bric3");

        assertThat(result.out())
                .describedAs(converter.name())
                .isEqualToIgnoringNewLines(converter.example);

        System.out.printf("%s: %s", converter.name(), result.out());
    }

    @ParameterizedTest
    @EnumSource(FancyOrnament.class)
    public void check_ornament(FancyOrnament ornaments) {
        var result = jufmt("-o", ornaments.name(), "bric3");

        assertThat(result.out())
                .describedAs(ornaments.name())
                .isEqualToIgnoringNewLines(ornaments.example);

        System.out.printf("%s: %s", ornaments.name(), result.out());
    }

    @Test
    public void check_reversed() {
        var result = jufmt("-r", "bric3");

        assertThat(result.out())
                .describedAs("reversed")
                .isEqualToIgnoringNewLines("3cirb");

//        System.out.printf("reversed: %s", stringWriter.toString());
    }

    @Nested
    class Normalization {
        @ParameterizedTest
        @MethodSource("normalizationArguments")
        void check_normalizer(Normalizer.Form form, String input, String expected) {
            var result = jufmt("-n", form.name(), input);

//            System.out.printf("Normalizer.Form.%s:%n%s%n%s", form.name(), input, stringWriter.toString());

            assertThat(result.out())
                    .describedAs(form.name())
                    .isEqualToIgnoringNewLines(expected);
        }

        private static Stream<Arguments> normalizationArguments() {
            //noinspection UnnecessaryUnicodeEscape
            return Stream.of(
                    arguments(Normalizer.Form.NFD,
                            "𝓗𝓸𝔀 𝓽𝓸 𝓻𝓮𝓶𝓸𝓿𝓮 𝓽𝓱𝓲𝓼 𝓯𝓸𝓷𝓽 𝓯𝓻𝓸𝓶 𝓪 𝓼𝓽𝓻𝓲𝓷𝓰?",
                            "𝓗𝓸𝔀 𝓽𝓸 𝓻𝓮𝓶𝓸𝓿𝓮 𝓽𝓱𝓲𝓼 𝓯𝓸𝓷𝓽 𝓯𝓻𝓸𝓶 𝓪 𝓼𝓽𝓻𝓲𝓷𝓰?"),
                    arguments(Normalizer.Form.NFD,
                            "𝐻𝑜𝓌 𝓉𝑜 𝓇𝑒𝓂𝑜𝓋𝑒 𝓉𝒽𝒾𝓈 𝒻𝑜𝓃𝓉 𝒻𝓇𝑜𝓂 𝒶 𝓈𝓉𝓇𝒾𝓃𝑔?",
                            "𝐻𝑜𝓌 𝓉𝑜 𝓇𝑒𝓂𝑜𝓋𝑒 𝓉𝒽𝒾𝓈 𝒻𝑜𝓃𝓉 𝒻𝓇𝑜𝓂 𝒶 𝓈𝓉𝓇𝒾𝓃𝑔?"),
                    arguments(Normalizer.Form.NFD,
                            "нσω тσ яємσνє тнιѕ ƒσηт ƒяσм α ѕтяιηg?",
                            "нσω тσ яємσνє тнιѕ ƒσηт ƒяσм α ѕтяιηg?"),
                    arguments(Normalizer.Form.NFD,
                            "𝗔𝗕𝗖𝗗𝗘𝗙𝗚𝗛𝗜𝗝𝗞𝗟𝗠𝗡𝗢𝗣𝗤𝗥𝗦𝗧𝗨𝗩𝗪𝗫𝗬𝗭𝗮𝗯𝗰𝗱𝗲𝗳𝗴𝗵𝗶𝗷𝗸𝗹𝗺𝗻𝗼𝗽𝗾𝗿𝘀𝘁𝘂𝘃𝘄𝘅𝘆𝘇",
                            "𝗔𝗕𝗖𝗗𝗘𝗙𝗚𝗛𝗜𝗝𝗞𝗟𝗠𝗡𝗢𝗣𝗤𝗥𝗦𝗧𝗨𝗩𝗪𝗫𝗬𝗭𝗮𝗯𝗰𝗱𝗲𝗳𝗴𝗵𝗶𝗷𝗸𝗹𝗺𝗻𝗼𝗽𝗾𝗿𝘀𝘁𝘂𝘃𝘄𝘅𝘆𝘇"),
                    arguments(Normalizer.Form.NFD,
                            "🅰🅱🅲🅳🅴🅵🅶🅷🅸🅹🅺🅻🅼🅽🅾🅿🆀🆁🆂🆃🆄🆅🆆🆇🆈🆉",
                            "🅰🅱🅲🅳🅴🅵🅶🅷🅸🅹🅺🅻🅼🅽🅾🅿🆀🆁🆂🆃🆄🆅🆆🆇🆈🆉"),
                    arguments(Normalizer.Form.NFD,
                            "⒜⒝⒞⒟⒠⒡⒢⒣⒤⒥⒦⒧⒨⒩⒪⒫⒬⒭⒮⒯⒰⒱⒲⒳⒴⒵",
                            "⒜⒝⒞⒟⒠⒡⒢⒣⒤⒥⒦⒧⒨⒩⒪⒫⒬⒭⒮⒯⒰⒱⒲⒳⒴⒵"),
                    arguments(Normalizer.Form.NFD,
                            "\"＼　！＃＄％＆＇（）＊＋，－．／０１２３４５６７８９：；<＝>？＠ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ［］＾＿｀ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ｛｜｝～",
                            "\"＼　！＃＄％＆＇（）＊＋，－．／０１２３４５６７８９：；<＝>？＠ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ［］＾＿｀ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ｛｜｝～"),
                    arguments(Normalizer.Form.NFD,
                            "株式会社ＫＡＤＯＫＡＷＡ Ｆｕｔｕｒｅ Ｐｕｂｌｉｓｈｉｎｇ",
                            "株式会社ＫＡＤＯＫＡＷＡ Ｆｕｔｕｒｅ Ｐｕｂｌｉｓｈｉｎｇ"),
                    arguments(Normalizer.Form.NFD,
                            "ポé",
                            "ポé"),
                    arguments(Normalizer.Form.NFD,
                            "パピプペポ",
                            "パピプペポ"),
                    arguments(Normalizer.Form.NFD,
                            "\u0069\u0307",
                            "i̇"),

                    arguments(Normalizer.Form.NFC,
                            "𝓗𝓸𝔀 𝓽𝓸 𝓻𝓮𝓶𝓸𝓿𝓮 𝓽𝓱𝓲𝓼 𝓯𝓸𝓷𝓽 𝓯𝓻𝓸𝓶 𝓪 𝓼𝓽𝓻𝓲𝓷𝓰?",
                            "𝓗𝓸𝔀 𝓽𝓸 𝓻𝓮𝓶𝓸𝓿𝓮 𝓽𝓱𝓲𝓼 𝓯𝓸𝓷𝓽 𝓯𝓻𝓸𝓶 𝓪 𝓼𝓽𝓻𝓲𝓷𝓰?"),
                    arguments(Normalizer.Form.NFC,
                            "𝐻𝑜𝓌 𝓉𝑜 𝓇𝑒𝓂𝑜𝓋𝑒 𝓉𝒽𝒾𝓈 𝒻𝑜𝓃𝓉 𝒻𝓇𝑜𝓂 𝒶 𝓈𝓉𝓇𝒾𝓃𝑔?",
                            "𝐻𝑜𝓌 𝓉𝑜 𝓇𝑒𝓂𝑜𝓋𝑒 𝓉𝒽𝒾𝓈 𝒻𝑜𝓃𝓉 𝒻𝓇𝑜𝓂 𝒶 𝓈𝓉𝓇𝒾𝓃𝑔?"),
                    arguments(Normalizer.Form.NFC,
                            "нσω тσ яємσνє тнιѕ ƒσηт ƒяσм α ѕтяιηg?",
                            "нσω тσ яємσνє тнιѕ ƒσηт ƒяσм α ѕтяιηg?"),
                    arguments(Normalizer.Form.NFC,
                            "𝗔𝗕𝗖𝗗𝗘𝗙𝗚𝗛𝗜𝗝𝗞𝗟𝗠𝗡𝗢𝗣𝗤𝗥𝗦𝗧𝗨𝗩𝗪𝗫𝗬𝗭𝗮𝗯𝗰𝗱𝗲𝗳𝗴𝗵𝗶𝗷𝗸𝗹𝗺𝗻𝗼𝗽𝗾𝗿𝘀𝘁𝘂𝘃𝘄𝘅𝘆𝘇",
                            "𝗔𝗕𝗖𝗗𝗘𝗙𝗚𝗛𝗜𝗝𝗞𝗟𝗠𝗡𝗢𝗣𝗤𝗥𝗦𝗧𝗨𝗩𝗪𝗫𝗬𝗭𝗮𝗯𝗰𝗱𝗲𝗳𝗴𝗵𝗶𝗷𝗸𝗹𝗺𝗻𝗼𝗽𝗾𝗿𝘀𝘁𝘂𝘃𝘄𝘅𝘆𝘇"),
                    arguments(Normalizer.Form.NFC,
                            "🅰🅱🅲🅳🅴🅵🅶🅷🅸🅹🅺🅻🅼🅽🅾🅿🆀🆁🆂🆃🆄🆅🆆🆇🆈🆉",
                            "🅰🅱🅲🅳🅴🅵🅶🅷🅸🅹🅺🅻🅼🅽🅾🅿🆀🆁🆂🆃🆄🆅🆆🆇🆈🆉"),
                    arguments(Normalizer.Form.NFC,
                            "⒜⒝⒞⒟⒠⒡⒢⒣⒤⒥⒦⒧⒨⒩⒪⒫⒬⒭⒮⒯⒰⒱⒲⒳⒴⒵",
                            "⒜⒝⒞⒟⒠⒡⒢⒣⒤⒥⒦⒧⒨⒩⒪⒫⒬⒭⒮⒯⒰⒱⒲⒳⒴⒵"),
                    arguments(Normalizer.Form.NFC,
                            "\"＼　！＃＄％＆＇（）＊＋，－．／０１２３４５６７８９：；<＝>？＠ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ［］＾＿｀ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ｛｜｝～",
                            "\"＼　！＃＄％＆＇（）＊＋，－．／０１２３４５６７８９：；<＝>？＠ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ［］＾＿｀ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ｛｜｝～"),
                    arguments(Normalizer.Form.NFC,
                            "株式会社ＫＡＤＯＫＡＷＡ Ｆｕｔｕｒｅ Ｐｕｂｌｉｓｈｉｎｇ",
                            "株式会社ＫＡＤＯＫＡＷＡ Ｆｕｔｕｒｅ Ｐｕｂｌｉｓｈｉｎｇ"),
                    arguments(Normalizer.Form.NFC,
                            "ポé",
                            "ポé"),
                    arguments(Normalizer.Form.NFC,
                            "パピプペポ",
                            "パピプペポ"),
                    arguments(Normalizer.Form.NFC,
                            "\u0069\u0307",
                            "i̇"),

                    arguments(Normalizer.Form.NFKD,
                            "𝓗𝓸𝔀 𝓽𝓸 𝓻𝓮𝓶𝓸𝓿𝓮 𝓽𝓱𝓲𝓼 𝓯𝓸𝓷𝓽 𝓯𝓻𝓸𝓶 𝓪 𝓼𝓽𝓻𝓲𝓷𝓰?",
                            "How to remove this font from a string?"),
                    arguments(Normalizer.Form.NFKD,
                            "𝐻𝑜𝓌 𝓉𝑜 𝓇𝑒𝓂𝑜𝓋𝑒 𝓉𝒽𝒾𝓈 𝒻𝑜𝓃𝓉 𝒻𝓇𝑜𝓂 𝒶 𝓈𝓉𝓇𝒾𝓃𝑔?",
                            "How to remove this font from a string?"),
                    arguments(Normalizer.Form.NFKD,
                            "нσω тσ яємσνє тнιѕ ƒσηт ƒяσм α ѕтяιηg?",
                            "нσω тσ яємσνє тнιѕ ƒσηт ƒяσм α ѕтяιηg?"),
                    arguments(Normalizer.Form.NFKD,
                            "𝗔𝗕𝗖𝗗𝗘𝗙𝗚𝗛𝗜𝗝𝗞𝗟𝗠𝗡𝗢𝗣𝗤𝗥𝗦𝗧𝗨𝗩𝗪𝗫𝗬𝗭𝗮𝗯𝗰𝗱𝗲𝗳𝗴𝗵𝗶𝗷𝗸𝗹𝗺𝗻𝗼𝗽𝗾𝗿𝘀𝘁𝘂𝘃𝘄𝘅𝘆𝘇",
                            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"),
                    arguments(Normalizer.Form.NFKD,
                            "🅰🅱🅲🅳🅴🅵🅶🅷🅸🅹🅺🅻🅼🅽🅾🅿🆀🆁🆂🆃🆄🆅🆆🆇🆈🆉",
                            "🅰🅱🅲🅳🅴🅵🅶🅷🅸🅹🅺🅻🅼🅽🅾🅿🆀🆁🆂🆃🆄🆅🆆🆇🆈🆉"),
                    arguments(Normalizer.Form.NFKD,
                            "⒜⒝⒞⒟⒠⒡⒢⒣⒤⒥⒦⒧⒨⒩⒪⒫⒬⒭⒮⒯⒰⒱⒲⒳⒴⒵",
                            "(a)(b)(c)(d)(e)(f)(g)(h)(i)(j)(k)(l)(m)(n)(o)(p)(q)(r)(s)(t)(u)(v)(w)(x)(y)(z)"),
                    arguments(Normalizer.Form.NFKD,
                            "\"＼　！＃＄％＆＇（）＊＋，－．／０１２３４５６７８９：；<＝>？＠ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ［］＾＿｀ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ｛｜｝～",
                            "\"\\ !#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[]^_`abcdefghijklmnopqrstuvwxyz{|}~"),
                    arguments(Normalizer.Form.NFKD,
                            "株式会社ＫＡＤＯＫＡＷＡ Ｆｕｔｕｒｅ Ｐｕｂｌｉｓｈｉｎｇ",
                            "株式会社KADOKAWA Future Publishing"),
                    arguments(Normalizer.Form.NFKD,
                            "ポé",
                            "ポé"),
                    arguments(Normalizer.Form.NFKD,
                            "パピプペポ",
                            "パピプペポ"),
                    arguments(Normalizer.Form.NFKD,
                            "\u0069\u0307",
                            "i̇"),

                    arguments(Normalizer.Form.NFKC,
                            "𝓗𝓸𝔀 𝓽𝓸 𝓻𝓮𝓶𝓸𝓿𝓮 𝓽𝓱𝓲𝓼 𝓯𝓸𝓷𝓽 𝓯𝓻𝓸𝓶 𝓪 𝓼𝓽𝓻𝓲𝓷𝓰?",
                            "How to remove this font from a string?"),
                    arguments(Normalizer.Form.NFKC,
                            "𝐻𝑜𝓌 𝓉𝑜 𝓇𝑒𝓂𝑜𝓋𝑒 𝓉𝒽𝒾𝓈 𝒻𝑜𝓃𝓉 𝒻𝓇𝑜𝓂 𝒶 𝓈𝓉𝓇𝒾𝓃𝑔?",
                            "How to remove this font from a string?"),
                    arguments(Normalizer.Form.NFKC,
                            "нσω тσ яємσνє тнιѕ ƒσηт ƒяσм α ѕтяιηg?",
                            "нσω тσ яємσνє тнιѕ ƒσηт ƒяσм α ѕтяιηg?"),
                    arguments(Normalizer.Form.NFKC,
                            "𝗔𝗕𝗖𝗗𝗘𝗙𝗚𝗛𝗜𝗝𝗞𝗟𝗠𝗡𝗢𝗣𝗤𝗥𝗦𝗧𝗨𝗩𝗪𝗫𝗬𝗭𝗮𝗯𝗰𝗱𝗲𝗳𝗴𝗵𝗶𝗷𝗸𝗹𝗺𝗻𝗼𝗽𝗾𝗿𝘀𝘁𝘂𝘃𝘄𝘅𝘆𝘇",
                            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"),
                    arguments(Normalizer.Form.NFKC,
                            "🅰🅱🅲🅳🅴🅵🅶🅷🅸🅹🅺🅻🅼🅽🅾🅿🆀🆁🆂🆃🆄🆅🆆🆇🆈🆉",
                            "🅰🅱🅲🅳🅴🅵🅶🅷🅸🅹🅺🅻🅼🅽🅾🅿🆀🆁🆂🆃🆄🆅🆆🆇🆈🆉"),
                    arguments(Normalizer.Form.NFKC,
                            "⒜⒝⒞⒟⒠⒡⒢⒣⒤⒥⒦⒧⒨⒩⒪⒫⒬⒭⒮⒯⒰⒱⒲⒳⒴⒵",
                            "(a)(b)(c)(d)(e)(f)(g)(h)(i)(j)(k)(l)(m)(n)(o)(p)(q)(r)(s)(t)(u)(v)(w)(x)(y)(z)"),
                    arguments(Normalizer.Form.NFKC,
                            "\"＼　！＃＄％＆＇（）＊＋，－．／０１２３４５６７８９：；<＝>？＠ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ［］＾＿｀ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ｛｜｝～",
                            "\"\\ !#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[]^_`abcdefghijklmnopqrstuvwxyz{|}~"),
                    arguments(Normalizer.Form.NFKC,
                            "株式会社ＫＡＤＯＫＡＷＡ Ｆｕｔｕｒｅ Ｐｕｂｌｉｓｈｉｎｇ",
                            "株式会社KADOKAWA Future Publishing"),
                    arguments(Normalizer.Form.NFKC,
                            "ポé",
                            "ポé"),
                    arguments(Normalizer.Form.NFKC,
                            "パピプペポ",
                            "パピプペポ"),
                    arguments(Normalizer.Form.NFKC,
                            "\u0069\u0307",
                            "i̇")
            );
        }


        @ParameterizedTest
        @MethodSource("diacriticalMarkStrippingArguments")
        void check_normalizer_with_diacritical_mark_stripping(Normalizer.Form form, String input, String expected) {
            var result = jufmt("-n", form.name(), "--strip-diacritic-marks", input);

            System.out.printf("%s: %s", form.name(), result.out());

            assertThat(result.out())
                    .describedAs(form.name())
                    .isEqualToIgnoringNewLines(expected);
        }

        private static Stream<Arguments> diacriticalMarkStrippingArguments() {
            return Stream.of(
                    arguments(Normalizer.Form.NFD, "Tĥïŝ ĩš â fůňķŷ Šťŕĭńġ", "This is a funky String"),
                    arguments(Normalizer.Form.NFKD, "Tĥïŝ ĩš â fůňķŷ Šťŕĭńġ", "This is a funky String")
            );
        }

        @ParameterizedTest
        @EnumSource(mode = Mode.INCLUDE, names = {"NFC", "NFKC"})
        void check_incompatible_normalizer_with_diacritical_mark_stripping(Normalizer.Form form) {
            var result = jufmt("-n", form.name(), "--strip-diacritic-marks", "ignored");

            assertThat(result.status()).isEqualTo(CommandLine.ExitCode.USAGE);
            assertThat(result.err())
                    .describedAs(form.name())
                    .containsIgnoringCase("diacritical mark stripping")
                    .containsIgnoringCase("NFD")
                    .containsIgnoringCase("NFKD");
        }
    }
}
