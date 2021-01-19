package jufmt;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.Normalizer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import picocli.CommandLine;

import static org.assertj.core.api.Assertions.assertThat;


public class JufmtCommandTest {

    @ParameterizedTest
    @EnumSource(FancyStyle.class)
    public void check_style(FancyStyle style) {
        var cmd = new CommandLine(new JufmtCommand());
        var stringWriter = new StringWriter();
        cmd.setOut(new PrintWriter(stringWriter));

        
        cmd.execute("-s", style.name(), "bric3");

        assertThat(stringWriter.toString())
                .describedAs(style.name())
                .isEqualToIgnoringNewLines(style.example);

        System.out.printf("%s: %s", style.name(), stringWriter.toString());

    }

    @ParameterizedTest
    @EnumSource(FancyCharsets.class)
    public void check_charset(FancyCharsets charset) {
        var cmd = new CommandLine(new JufmtCommand());
        var stringWriter = new StringWriter();
        cmd.setOut(new PrintWriter(stringWriter));

        cmd.execute("-c", charset.name(), "bric3");

        assertThat(stringWriter.toString())
                .describedAs(charset.name())
                .isEqualToIgnoringNewLines(charset.example);

        System.out.printf("%s: %s", charset.name(), stringWriter.toString());
    }

    @ParameterizedTest
    @EnumSource(FancyOrnaments.class)
    public void check_ornament(FancyOrnaments ornaments) {
        var cmd = new CommandLine(new JufmtCommand());
        var stringWriter = new StringWriter();
        cmd.setOut(new PrintWriter(stringWriter));

        cmd.execute("-o", ornaments.name(), "bric3");

        assertThat(stringWriter.toString())
                .describedAs(ornaments.name())
                .isEqualToIgnoringNewLines(ornaments.example);

        System.out.printf("%s: %s", ornaments.name(), stringWriter.toString());
    }

    @Test
    public void check_reversed() {
        var cmd = new CommandLine(new JufmtCommand());
        var stringWriter = new StringWriter();
        cmd.setOut(new PrintWriter(stringWriter));


        cmd.execute("-r", "bric3");

        assertThat(stringWriter.toString())
                .describedAs("reversed")
                .isEqualToIgnoringNewLines("3cirb");

        System.out.printf("reversed: %s", stringWriter.toString());
    }

    @ParameterizedTest
    @EnumSource(Normalizer.Form.class)
    void check_normalizer(Normalizer.Form form) {
        var cmd = new CommandLine(new JufmtCommand());

        var stringWriter = new StringWriter();
        cmd.setOut(new PrintWriter(stringWriter));
        cmd.execute("-n", form.name(), "𝓗𝓸𝔀 𝓽𝓸 𝓻𝓮𝓶𝓸𝓿𝓮 𝓽𝓱𝓲𝓼 𝓯𝓸𝓷𝓽 𝓯𝓻𝓸𝓶 𝓪 𝓼𝓽𝓻𝓲𝓷𝓰?");
        cmd.execute("-n", form.name(), "𝐻𝑜𝓌 𝓉𝑜 𝓇𝑒𝓂𝑜𝓋𝑒 𝓉𝒽𝒾𝓈 𝒻𝑜𝓃𝓉 𝒻𝓇𝑜𝓂 𝒶 𝓈𝓉𝓇𝒾𝓃𝑔?");
        cmd.execute("-n", form.name(), "нσω тσ яємσνє тнιѕ ƒσηт ƒяσм α ѕтяιηg?");
        cmd.execute("-n", form.name(), "𝗔𝗕𝗖𝗗𝗘𝗙𝗚𝗛𝗜𝗝𝗞𝗟𝗠𝗡𝗢𝗣𝗤𝗥𝗦𝗧𝗨𝗩𝗪𝗫𝗬𝗭𝗮𝗯𝗰𝗱𝗲𝗳𝗴𝗵𝗶𝗷𝗸𝗹𝗺𝗻𝗼𝗽𝗾𝗿𝘀𝘁𝘂𝘃𝘄𝘅𝘆𝘇");
        cmd.execute("-n", form.name(), "🅰🅱🅲🅳🅴🅵🅶🅷🅸🅹🅺🅻🅼🅽🅾🅿🆀🆁🆂🆃🆄🆅🆆🆇🆈🆉");
        cmd.execute("-n", form.name(), "⒜⒝⒞⒟⒠⒡⒢⒣⒤⒥⒦⒧⒨⒩⒪⒫⒬⒭⒮⒯⒰⒱⒲⒳⒴⒵");
        cmd.execute("-n", form.name(), "\"＼　！＃＄％＆＇（）＊＋，－．／０１２３４５６７８９：；<＝>？＠ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ［］＾＿｀ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ｛｜｝～");
        cmd.execute("-n", form.name(), "株式会社ＫＡＤＯＫＡＷＡ Ｆｕｔｕｒｅ Ｐｕｂｌｉｓｈｉｎｇ");
        cmd.execute("-n", form.name(), "ポé");
        cmd.execute("-n", form.name(), "パピプペポ");
        cmd.execute("-n", form.name(), "\u0069\u0307");


        System.out.printf("%s: %s", form.name(), stringWriter.toString());

//        assertThat(stringWriter.toString())
//                .describedAs(form.name())
//                .isEqualToIgnoringNewLines(form.example);


    }

    @ParameterizedTest
    @EnumSource(Normalizer.Form.class)
    void check_normalizer_with_diacritical_mark_stripping(Normalizer.Form form) {
        var cmd = new CommandLine(new JufmtCommand());

        var stringWriter = new StringWriter();
        cmd.setOut(new PrintWriter(stringWriter));
        cmd.execute( "-n", form.name(), "--strip-diacritic-marks", "Tĥïŝ ĩš â fůňķŷ Šťŕĭńġ");

        System.out.printf("%s: %s", form.name(), stringWriter.toString());

//        assertThat(stringWriter.toString())
//                .describedAs(form.name())
//                .isEqualToIgnoringNewLines(form.example);


    }
}
