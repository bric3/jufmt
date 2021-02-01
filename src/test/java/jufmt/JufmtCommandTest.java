package jufmt;


import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.EnumSource.Mode;
import org.junit.jupiter.params.provider.MethodSource;
import picocli.CommandLine;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.Normalizer;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;


@SuppressWarnings("SpellCheckingInspection")
public class JufmtCommandTest {

    @ParameterizedTest
    @EnumSource
    public void check_style(FancyStyle style) {
        var stringWriter = new StringWriter();
        new CommandLine(new JufmtCommand())
                .setOut(new PrintWriter(stringWriter))
                .execute("-s", style.name(), "bric3");

        assertThat(stringWriter.toString())
                .describedAs(style.name())
                .isEqualToIgnoringNewLines(style.example);

        System.out.printf("%s: %s", style.name(), stringWriter.toString());

    }

    @ParameterizedTest
    @EnumSource(mode = Mode.EXCLUDE, names = {"none"})
    public void check_charset(FancyConverters converter) {
        var stringWriter = new StringWriter();
        new CommandLine(new JufmtCommand())
                .setOut(new PrintWriter(stringWriter))
                .execute("-c", converter.name(), "bric3");

        assertThat(stringWriter.toString())
                .describedAs(converter.name())
                .isEqualToIgnoringNewLines(converter.example);

        System.out.printf("%s: %s", converter.name(), stringWriter.toString());
    }

    @ParameterizedTest
    @EnumSource(FancyOrnaments.class)
    public void check_ornament(FancyOrnaments ornaments) {
        var stringWriter = new StringWriter();
        new CommandLine(new JufmtCommand())
                .setOut(new PrintWriter(stringWriter))
                .execute("-o", ornaments.name(), "bric3");

        assertThat(stringWriter.toString())
                .describedAs(ornaments.name())
                .isEqualToIgnoringNewLines(ornaments.example);

        System.out.printf("%s: %s", ornaments.name(), stringWriter.toString());
    }

    @Test
    public void check_reversed() {
        var stringWriter = new StringWriter();
        new CommandLine(new JufmtCommand())
                .setOut(new PrintWriter(stringWriter))
                .execute("-r", "bric3");

        assertThat(stringWriter.toString())
                .describedAs("reversed")
                .isEqualToIgnoringNewLines("3cirb");

//        System.out.printf("reversed: %s", stringWriter.toString());
    }

    @Nested
    static class Normalization {
        @ParameterizedTest
        @MethodSource("normalizationArguments")
        void check_normalizer(Normalizer.Form form, String input, String expected) {
            var cmd = new CommandLine(new JufmtCommand());

            var stringWriter = new StringWriter();
            cmd.setOut(new PrintWriter(stringWriter));
            cmd.execute("-n", form.name(), input);

//            System.out.printf("Normalizer.Form.%s:%n%s%n%s", form.name(), input, stringWriter.toString());

            assertThat(stringWriter.toString())
                    .describedAs(form.name())
                    .isEqualToIgnoringNewLines(expected);
        }

        private static Stream<Arguments> normalizationArguments() {
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

            var stringWriter = new StringWriter();
            new CommandLine(new JufmtCommand())
                    .setOut(new PrintWriter(stringWriter))
                    .execute("-n", form.name(), "--strip-diacritic-marks", input);

            System.out.printf("%s: %s", form.name(), stringWriter);

            assertThat(stringWriter.toString())
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

            var stdout = new StringWriter();
            var stderr = new StringWriter();
            var status = new CommandLine(new JufmtCommand())
                    .setOut(new PrintWriter(stdout))
                    .setErr(new PrintWriter(stderr))
                    .execute("-n", form.name(), "--strip-diacritic-marks", "ignored");

            assertThat(status).isEqualTo(CommandLine.ExitCode.USAGE);
            assertThat(stderr.toString())
                    .describedAs(form.name())
                    .containsIgnoringCase("diacritical mark stripping")
                    .containsIgnoringCase("NFD")
                    .containsIgnoringCase("NFKD");
        }
    }
}
