package io.github.bric3.jufmt;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class FancyConverterTest {
    @ParameterizedTest
    @MethodSource("fancyConverterArguments")
    void name(FancyConverter converter, String input, String expected) {
        var result = converter.convert(input);

        assertThat(result.toString())
                .describedAs(converter.name())
                .isEqualTo(expected);
    }

    public static Stream<Arguments> fancyConverterArguments() {
        return Stream.of(
                arguments(
                        FancyConverter.none,
                        "bric3",
                        "bric3"
                ),
                arguments(
                        FancyConverter.normal,
                        "bric3",
                        "bric3"
                ),
                arguments(
                        FancyConverter.sans,
                        "bric3",
                        "𝖻𝗋𝗂𝖼𝟥"
                ),
                arguments(
                        FancyConverter.sansBold,
                        "bric3",
                        "𝗯𝗿𝗶𝗰𝟯"
                ),
                arguments(
                        FancyConverter.sansItalic,
                        "bric3",
                        "𝘣𝘳𝘪𝘤3"
                ),
                arguments(
                        FancyConverter.sansBoldItalic,
                        "bric3",
                        "𝙗𝙧𝙞𝙘3"
                ),
                arguments(
                        FancyConverter.monospace,
                        "bric3",
                        "𝚋𝚛𝚒𝚌𝟹"
                ),
                arguments(
                        FancyConverter.fullwidth,
                        "bric3",
                        "ｂｒｉｃ３"
                ),
                arguments(
                        FancyConverter.fraktur,
                        "bric3",
                        "𝔟𝔯𝔦𝔠3"
                ),
                arguments(
                        FancyConverter.boldFraktur,
                        "bric3",
                        "𝖇𝖗𝖎𝖈3"
                ),
                arguments(
                        FancyConverter.serifBold,
                        "bric3",
                        "𝐛𝐫𝐢𝐜𝟑"
                ),
                arguments(
                        FancyConverter.serifItalic,
                        "bric3",
                        "𝑏𝑟𝑖𝑐3"
                ),
                arguments(
                        FancyConverter.serifBoldItalic,
                        "bric3",
                        "𝒃𝒓𝒊𝒄3"
                ),
                arguments(
                        FancyConverter.doubleStruck,
                        "bric3",
                        "𝕓𝕣𝕚𝕔𝟛"
                ),
                arguments(
                        FancyConverter.script,
                        "bric3",
                        "𝒷𝓇𝒾𝒸3"
                ),
                arguments(
                        FancyConverter.boldScript,
                        "bric3",
                        "𝓫𝓻𝓲𝓬3"
                ),
                arguments(
                        FancyConverter.circled,
                        "bric3",
                        "ⓑⓡⓘⓒ③"
                ),
                arguments(
                        FancyConverter.circledNegative,
                        "bric3",
                        "🅑🅡🅘🅒❸"
                ),
                arguments(
                        FancyConverter.squared,
                        "bric3",
                        "🄱🅁🄸🄲3"
                ),
                arguments(
                        FancyConverter.squaredNegative,
                        "bric3",
                        "🅱🆁🅸🅲3"
                ),
                arguments(
                        FancyConverter.parenthesized,
                        "bric3",
                        "⒝⒭⒤⒞⑶"
                ),
                arguments(
                        FancyConverter.smallCaps,
                        "bric3",
                        "ʙʀɪᴄ3"
                ),
                arguments(
                        FancyConverter.subscript,
                        "bric3",
                        "ᵦᵣᵢ𝒸₃"
                ),
                arguments(
                        FancyConverter.superscript,
                        "bric3",
                        "ᵇʳⁱᶜ³"
                ),
                arguments(
                        FancyConverter.inverted,
                        "bric3",
                        "qɹıɔƐ"
                ),
                arguments(
                        FancyConverter.mirrored,
                        "bric3",
                        "dᴙiↄƐ"
                ),
                arguments(
                        FancyConverter.rounded,
                        "bric3",
                        "ᗷᖇIᑕ3"
                ),
                arguments(
                        FancyConverter.greek,
                        "bric3",
                        "BЯIᑕ3"
                ),
                arguments(
                        FancyConverter.japanese,
                        "bric3",
                        "乃尺丨匚3"
                ),
                arguments(
                        FancyConverter.fauxEthiopian,
                        "bric3",
                        "ጌዪጎር3"
                ),
                arguments(
                        FancyConverter.ogham,
                        "bric3",
                        "᚛ᚁᚏᚔᚙ3᚜"
                )
        );
    }
}