/*
 * jufmt
 *
 * Copyright (c) 2023, today - Brice DUTHEIL
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package io.github.bric3.jufmt;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;

import static java.util.stream.Collectors.toMap;

@SuppressWarnings("SpellCheckingInspection")
public enum FancyConverter {
    none("", "") {
        @Override
        public StringBuilder convert(String stringToProcess) {
            return new StringBuilder(stringToProcess);
        }
    },

    normal("bric3", "\"\\ !#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[]^_`abcdefghijklmnopqrstuvwxyz{|}~"),
    sans("𝖻𝗋𝗂𝖼𝟥", "\"\\ !#$%&'()*+,-./𝟢𝟣𝟤𝟥𝟦𝟧𝟨𝟩𝟪𝟫:;<=>?@𝖠𝖡𝖢𝖣𝖤𝖥𝖦𝖧𝖨𝖩𝖪𝖫𝖬𝖭𝖮𝖯𝖰𝖱𝖲𝖳𝖴𝖵𝖶𝖷𝖸𝖹[]^_`𝖺𝖻𝖼𝖽𝖾𝖿𝗀𝗁𝗂𝗃𝗄𝗅𝗆𝗇𝗈𝗉𝗊𝗋𝗌𝗍𝗎𝗏𝗐𝗑𝗒𝗓{|}~"),
    sansBold("𝗯𝗿𝗶𝗰𝟯", "\"\\ !#$%&'()*+,-./𝟬𝟭𝟮𝟯𝟰𝟱𝟲𝟳𝟴𝟵:;<=>?@𝗔𝗕𝗖𝗗𝗘𝗙𝗚𝗛𝗜𝗝𝗞𝗟𝗠𝗡𝗢𝗣𝗤𝗥𝗦𝗧𝗨𝗩𝗪𝗫𝗬𝗭[]^_`𝗮𝗯𝗰𝗱𝗲𝗳𝗴𝗵𝗶𝗷𝗸𝗹𝗺𝗻𝗼𝗽𝗾𝗿𝘀𝘁𝘂𝘃𝘄𝘅𝘆𝘇{|}~"),
    sansItalic("𝘣𝘳𝘪𝘤3", "\"\\ !#$%&'()*+,-./0123456789:;<=>?@𝘈𝘉𝘊𝘋𝘌𝘍𝘎𝘏𝘐𝘑𝘒𝘓𝘔𝘕𝘖𝘗𝘘𝘙𝘚𝘛𝘜𝘝𝘞𝘟𝘠𝘡[]^_`𝘢𝘣𝘤𝘥𝘦𝘧𝘨𝘩𝘪𝘫𝘬𝘭𝘮𝘯𝘰𝘱𝘲𝘳𝘴𝘵𝘶𝘷𝘸𝘹𝘺𝘻{|}~"),
    sansBoldItalic("𝙗𝙧𝙞𝙘𝟑", "\"\\ !#$%&'()*+,-./𝟎𝟏𝟐𝟑𝟒𝟓𝟔𝟕𝟖𝟗:;<=>?@𝘼𝘽𝘾𝘿𝙀𝙁𝙂𝙃𝙄𝙅𝙆𝙇𝙈𝙉𝙊𝙋𝙌𝙍𝙎𝙏𝙐𝙑𝙒𝙓𝙔𝙕[]^_`𝙖𝙗𝙘𝙙𝙚𝙛𝙜𝙝𝙞𝙟𝙠𝙡𝙢𝙣𝙤𝙥𝙦𝙧𝙨𝙩𝙪𝙫𝙬𝙭𝙮𝙯{|}~"),
    monospace("𝚋𝚛𝚒𝚌𝟹", "\"\\ !#$%&'()*+,-./𝟶𝟷𝟸𝟹𝟺𝟻𝟼𝟽𝟾𝟿:;<=>?@𝙰𝙱𝙲𝙳𝙴𝙵𝙶𝙷𝙸𝙹𝙺𝙻𝙼𝙽𝙾𝙿𝚀𝚁𝚂𝚃𝚄𝚅𝚆𝚇𝚈𝚉[]^_`𝚊𝚋𝚌𝚍𝚎𝚏𝚐𝚑𝚒𝚓𝚔𝚕𝚖𝚗𝚘𝚙𝚚𝚛𝚜𝚝𝚞𝚟𝚠𝚡𝚢𝚣{|}~"),
    fullwidth("ｂｒｉｃ３", "\"＼ ！＃＄％＆＇（）＊＋，－．／０１２３４５６７８９：；＜＝＞？＠ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ［］＾＿｀ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ｛｜｝～"),
    fraktur("𝔟𝔯𝔦𝔠3", "\"\\ !#$%&'()*+,-./0123456789:;<=>?@𝔄𝔅ℭ𝔇𝔈𝔉𝔊ℌℑ𝔍𝔎𝔏𝔐𝔑𝔒𝔓𝔔ℜ𝔖𝔗𝔘𝔙𝔚𝔛𝔜ℨ[]^_`𝔞𝔟𝔠𝔡𝔢𝔣𝔤𝔥𝔦𝔧𝔨𝔩𝔪𝔫𝔬𝔭𝔮𝔯𝔰𝔱𝔲𝔳𝔴𝔵𝔶𝔷{|}~"),
    boldFraktur("𝖇𝖗𝖎𝖈𝟑", "\"\\ !#$%&'()*+,-./𝟎𝟏𝟐𝟑𝟒𝟓𝟔𝟕𝟖𝟗:;<=>?@𝕬𝕭𝕮𝕯𝕰𝕱𝕲𝕳𝕴𝕵𝕶𝕷𝕸𝕹𝕺𝕻𝕼𝕽𝕾𝕿𝖀𝖁𝖂𝖃𝖄𝖅[]^_`𝖆𝖇𝖈𝖉𝖊𝖋𝖌𝖍𝖎𝖏𝖐𝖑𝖒𝖓𝖔𝖕𝖖𝖗𝖘𝖙𝖚𝖛𝖜𝖝𝖞𝖟{|}~"),
    serifBold("𝐛𝐫𝐢𝐜𝟑", "\"\\ !#$%&'()*+,-./𝟎𝟏𝟐𝟑𝟒𝟓𝟔𝟕𝟖𝟗:;<=>?@𝐀𝐁𝐂𝐃𝐄𝐅𝐆𝐇𝐈𝐉𝐊𝐋𝐌𝐍𝐎𝐏𝐐𝐑𝐒𝐓𝐔𝐕𝐖𝐗𝐘𝐙[]^_`𝐚𝐛𝐜𝐝𝐞𝐟𝐠𝐡𝐢𝐣𝐤𝐥𝐦𝐧𝐨𝐩𝐪𝐫𝐬𝐭𝐮𝐯𝐰𝐱𝐲𝐳{|}~"),
    serifItalic("𝑏𝑟𝑖𝑐3", "\"\\ !#$%&'()*+,-./0123456789:;<=>?@𝐴𝐵𝐶𝐷𝐸𝐹𝐺𝐻𝐼𝐽𝐾𝐿𝑀𝑁𝑂𝑃𝑄𝑅𝑆𝑇𝑈𝑉𝑊𝑋𝑌𝑍[]^_`𝑎𝑏𝑐𝑑𝑒𝑓𝑔ℎ𝑖𝑗𝑘𝑙𝑚𝑛𝑜𝑝𝑞𝑟𝑠𝑡𝑢𝑣𝑤𝑥𝑦𝑧{|}~"),
    serifBoldItalic("𝒃𝒓𝒊𝒄𝟑", "\"\\ !#$%&'()*+,-./𝟎𝟏𝟐𝟑𝟒𝟓𝟔𝟕𝟖𝟗:;<=>?@𝑨𝑩𝑪𝑫𝑬𝑭𝑮𝑯𝑰𝑱𝑲𝑳𝑴𝑵𝑶𝑷𝑸𝑹𝑺𝑻𝑼𝑽𝑾𝑿𝒀𝒁[]^_`𝒂𝒃𝒄𝒅𝒆𝒇𝒈𝒉𝒊𝒋𝒌𝒍𝒎𝒏𝒐𝒑𝒒𝒓𝒔𝒕𝒖𝒗𝒘𝒙𝒚𝒛{|}~"),
    doubleStruck("𝕓𝕣𝕚𝕔𝟛", "\"\\ !#$%&'()*+,-./𝟘𝟙𝟚𝟛𝟜𝟝𝟞𝟟𝟠𝟡:;<=>?@𝔸𝔹ℂ𝔻𝔼𝔽𝔾ℍ𝕀𝕁𝕂𝕃𝕄ℕ𝕆ℙℚℝ𝕊𝕋𝕌𝕍𝕎𝕏𝕐ℤ[]^_`𝕒𝕓𝕔𝕕𝕖𝕗𝕘𝕙𝕚𝕛𝕜𝕝𝕞𝕟𝕠𝕡𝕢𝕣𝕤𝕥𝕦𝕧𝕨𝕩𝕪𝕫{|}~"),
    script("𝒷𝓇𝒾𝒸3", "\"\\ !#$%&'()*+,-./0123456789:;<=>?@𝒜ℬ𝒞𝒟ℰℱ𝒢ℋℐ𝒥𝒦ℒℳ𝒩𝒪𝒫𝒬ℛ𝒮𝒯𝒰𝒱𝒲𝒳𝒴𝒵[]^_`𝒶𝒷𝒸𝒹ℯ𝒻ℊ𝒽𝒾𝒿𝓀𝓁𝓂𝓃ℴ𝓅𝓆𝓇𝓈𝓉𝓊𝓋𝓌𝓍𝓎𝓏{|}~"),
    boldScript("𝓫𝓻𝓲𝓬3", "\"\\ !#$%&'()*+,-./0123456789:;<=>?@𝓐𝓑𝓒𝓓𝓔𝓕𝓖𝓗𝓘𝓙𝓚𝓛𝓜𝓝𝓞𝓟𝓠𝓡𝓢𝓣𝓤𝓥𝓦𝓧𝓨𝓩[]^_`𝓪𝓫𝓬𝓭𝓮𝓯𝓰𝓱𝓲𝓳𝓴𝓵𝓶𝓷𝓸𝓹𝓺𝓻𝓼𝓽𝓾𝓿𝔀𝔁𝔂𝔃{|}~"),
    circled("ⓑⓡⓘⓒ③", "\"⦸ !#$%&'()⊛⊕,⊖⨀⊘⓪①②③④⑤⑥⑦⑧⑨:;⧀⊜⧁?@ⒶⒷⒸⒹⒺⒻⒼⒽⒾⒿⓀⓁⓂⓃⓄⓅⓆⓇⓈⓉⓊⓋⓌⓍⓎⓏ[]^_`ⓐⓑⓒⓓⓔⓕⓖⓗⓘⓙⓚⓛⓜⓝⓞⓟⓠⓡⓢⓣⓤⓥⓦⓧⓨⓩ{⦶}~"),
    circledNegative("🅑🅡🅘🅒❸", "\"\\ !#$%&'()*+,-./⓿❶❷❸❹❺❻❼❽❾:;<=>?@🅐🅑🅒🅓🅔🅕🅖🅗🅘🅙🅚🅛🅜🅝🅞🅟🅠🅡🅢🅣🅤🅥🅦🅧🅨🅩[]^_`🅐🅑🅒🅓🅔🅕🅖🅗🅘🅙🅚🅛🅜🅝🅞🅟🅠🅡🅢🅣🅤🅥🅦🅧🅨🅩{|}~"),
    squared("🄱🅁🄸🄲3", "\"⧅ !#$%&'()⧆⊞,⊟⊡⧄0123456789:;<=>?@🄰🄱🄲🄳🄴🄵🄶🄷🄸🄹🄺🄻🄼🄽🄾🄿🅀🅁🅂🅃🅄🅅🅆🅇🅈🅉[]^_`🄰🄱🄲🄳🄴🄵🄶🄷🄸🄹🄺🄻🄼🄽🄾🄿🅀🅁🅂🅃🅄🅅🅆🅇🅈🅉{|}~"),
    squaredNegative("🅱🆁🅸🅲3", "\"\\ !#$%&'()*+,-./0123456789:;<=>?@🅰🅱🅲🅳🅴🅵🅶🅷🅸🅹🅺🅻🅼🅽🅾🅿🆀🆁🆂🆃🆄🆅🆆🆇🆈🆉[]^_`🅰🅱🅲🅳🅴🅵🅶🅷🅸🅹🅺🅻🅼🅽🅾🅿🆀🆁🆂🆃🆄🆅🆆🆇🆈🆉{|}~"),
    keycap("b⃣r⃣i⃣c⃣3⃣", "") {
        final int COMBINING_ENCLOSING_KEYCAP = Character.codePointOf("COMBINING ENCLOSING KEYCAP");
        @Override
        public StringBuilder convert(String stringToProcess) {
            return stringToProcess.codePoints()
                                  .boxed()
                                  .collect(
                                          FancyCollectors.afterCodepoints(stringToProcess.length() * 2, COMBINING_ENCLOSING_KEYCAP)
                                  );
        }
    },
    parenthesized("⒝⒭⒤⒞⑶", "\"\\ !#$%&'()*+,-./0⑴⑵⑶⑷⑸⑹⑺⑻⑼:;<=>?@🄐🄑🄒🄓🄔🄕🄖🄗🄘🄙🄚🄛🄜🄝🄞🄟🄠🄡🄢🄣🄤🄥🄦🄧🄨🄩[]^_`⒜⒝⒞⒟⒠⒡⒢⒣⒤⒥⒦⒧⒨⒩⒪⒫⒬⒭⒮⒯⒰⒱⒲⒳⒴⒵{|}~"),
    smallCaps("ʙʀɪᴄ3", "\"\\ !#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[]^_`ᴀʙᴄᴅᴇғɢʜɪᴊᴋʟᴍɴᴏᴘǫʀsᴛᴜᴠᴡxʏᴢ{|}~"),
    subscript("ᵦᵣᵢ𝒸₃", "\"\\ !#$%&'₍₎*₊,₋./₀₁₂₃₄₅₆₇₈₉:;<₌>?@ᴀʙᴄᴅᴇꜰɢʜɪᴊᴋʟᴍɴᴏᴘ🇶ʀꜱᴛᴜᴠᴡxʏᴢ[]^_`ₐᵦ𝒸𝒹ₑ𝒻𝓰ₕᵢⱼₖₗₘₙₒₚᵩᵣₛₜᵤᵥ𝓌ₓᵧ𝓏{|}~"),
    superscript("ᵇʳⁱᶜ³", "\"\\ !#$%&'⁽⁾*⁺,⁻./⁰¹²³⁴⁵⁶⁷⁸⁹:;<⁼>?@ᴬᴮᶜᴰᴱᶠᴳᴴᴵᴶᴷᴸᴹᴺᴼᴾᵠᴿˢᵀᵁⱽᵂˣʸᶻ[]^_`ᵃᵇᶜᵈᵉᶠᵍʰⁱʲᵏˡᵐⁿᵒᵖᵠʳˢᵗᵘᵛʷˣʸᶻ{|}~"),
    inverted("qɹıɔƐ", "„\\ ¡#$%⅋,)(*+‘-˙/0ƖՇƐᔭϛ9𝘓86:;<=>¿@∀ꓭↃꓷƎℲ⅁HIſꓘ⅂WNOԀῸꓤS⊥∩ꓥMX⅄Z][^‾`ɐqɔpǝɟƃɥıɾʞןɯuodbɹsʇnʌʍxʎz}|{~"),
    mirrored("dᴙiↄƐ", "\"/ !#$%&')(*+,-.\\0߁ςƐ߂टმ٢8୧:;<=>⸮@AꓭↃꓷƎꟻӘHIႱꓘ⅃MИOꟼϘЯꙄTUVWXYZ][^_`ɒdↄbɘʇϱʜiįʞlmᴎoqpᴙꙅɈυvwxγz}|{~"),
    rounded("ᗷᖇIᑕ3", "\"\\ !#$%&'()*+,-./0123456789:;<=>?@ᗩᗷᑕᗪEᖴGᕼIᒍKᒪᗰᑎOᑭᑫᖇᔕTᑌᐯᗯ᙭Yᘔ[]^_`ᗩᗷᑕᗪEᖴGᕼIᒍKᒪᗰᑎOᑭᑫᖇᔕTᑌᐯᗯ᙭Yᘔ{|}~"),
    greek("BЯIᑕ3", "\"\\ !#$%&'()*+,-./0123456789:;<=>?@ΛBᑕDΣFGΉIJKᒪMПӨPQЯƧƬЦVЩXYZ[]^_`ΛBᑕDΣFGΉIJKᒪMПӨPQЯƧƬЦVЩXYZ{|}~"),
    japanese("乃尺丨匚3", "\"\\ !#$%&'()*+,-./0123456789:;<=>?@卂乃匚ᗪ乇千Ꮆ卄丨ﾌҜㄥ爪几ㄖ卩Ɋ尺丂ㄒㄩᐯ山乂ㄚ乙[]^_`卂乃匚ᗪ乇千Ꮆ卄丨ﾌҜㄥ爪几ㄖ卩Ɋ尺丂ㄒㄩᐯ山乂ㄚ乙{|}~"),
    fauxEthiopian("ጌዪጎር3", "\"\\ !#$%&'()*+,-./0123456789:;<=>?@ልጌርዕቿቻኗዘጎጋጕረጠክዐየዒዪነፕሁሀሠሸሃጊ[]^_`ልጌርዕቿቻኗዘጎጋጕረጠክዐየዒዪነፕሁሀሠሸሃጊ{|}~"),
    /**
     * Based on Ogham letters
     * <p>
     * See the wikipedia page on the <a href="https://en.wikipedia.org/wiki/Ogham">Ogham alphabet</a>.
     * These latin letters don't have equivalent ogham letters : c, f, h, q, v, x, y, z
     * <ul>
     *     <li><code>c</code> &rArr; <code>ᚙ</code></li>
     *     <li><code>f</code> &rArr; <code>ᚘ</code></li>
     *     <li><code>h</code> &rArr; <code>ᚍ</code></li>
     *     <li><code>q</code> &rArr; <code>ᚊ</code></li>
     *     <li><code>v</code> &rArr; <code>ᚗ</code></li>
     *     <li><code>x</code> &rArr; <code>ᚕ</code></li>
     *     <li><code>y</code> &rArr; <code>ᚖ</code></li>
     *     <li><code>z</code> &rArr; <code>ᚎ</code></li>
     * </ul>
     * <p>
     *
     * Note that chars without a corresponding ogham character are dropped.
     */
    ogham("᚛ᚁᚏᚔᚙ᚜", "\0\0 \0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0ᚐᚁᚙᚇᚓᚘᚌᚍᚔᚆᚉᚂᚋᚅᚑᚚᚊᚏᚄᚈᚒᚗᚃᚕᚖᚎ\0\0\0\0\0ᚐᚁᚙᚇᚓᚘᚌᚍᚔᚆᚉᚂᚋᚅᚑᚚᚊᚏᚄᚈᚒᚗᚃᚕᚖᚎ\0\0\0\0") {
        @Override
        public StringBuilder convert(String stringToProcess) {
            return stringToProcess.codePoints()
                                  .map(this::translateChar)
                                  .filter(c -> c != 0)
                                  .boxed()
                                  .collect(FancyCollectors.toStringBuilder(stringToProcess.length()))
                                  .insert(0, "᚛")
                                  .append("᚜");
        }
    },

    /**
     * Ascii Braille.
     * <p>
     * Braille system is not unique, it is declined in various language to adapt to their unique feature:
     * <ul>
     * <li>https://en.wikipedia.org/wiki/Braille</li>
     * <li>https://en.wikipedia.org/wiki/English_Braille</li>
     * <li>https://en.wikipedia.org/wiki/Russian_Braille</li>
     * <li>https://en.wikipedia.org/wiki/Braille_ASCII</li>
     * </ul>
     *
     * <p>Note that `, {, |, and } are not used and their Braille ASCII rendition is not defined, they are replaced by a space.</p>
     *
     * <p><em>Moon type</em> system, is another writing systems for blind people, however it doesn't seem to be much used</p>
     */
    asciiBrailleGrade1("⠃⠗⠊⠉⠒", "⠐⠳ ⠮⠼⠫⠩⠯⠄⠷⠾⠡⠬⠠⠤⠨⠌⠴⠂⠆⠒⠲⠢⠖⠶⠦⠔⠱⠰⠣⠿⠜⠹⠈⠁⠃⠉⠙⠑⠋⠛⠓⠊⠚⠅⠇⠍⠝⠕⠏⠟⠗⠎⠞⠥⠧⠺⠭⠽⠵⠪⠻⠘⠸ ⠁⠃⠉⠙⠑⠋⠛⠓⠊⠚⠅⠇⠍⠝⠕⠏⠟⠗⠎⠞⠥⠧⠺⠭⠽⠵    "),

    // TODO
    //  - hieroglyphs  𓆐𓆏𓄚𓄇𓃻𓃷𓃠     goes from \u13000 to \u1342F
    //  - cuneiforms  𒐪 𒐫 𒐬 𒁏 𒁐 𒀰 𒀱 𒀲 goes from   \u12000 ot \u123FF, punctuation \u12400 to \u1247F

    morse(ITUMorseConverter.example, "") {
        @Override
        public StringBuilder convert(String stringToProcess) {
            return new ITUMorseConverter().convert(stringToProcess);
        }
    },
    ;

    private static final Map<Integer, Integer> codepointIndex;

    static {
        String normalSet = normal.chars;

        codepointIndex = normalSet.codePoints()
                                  .boxed()
                                  .collect(toMap(
                                          Function.identity(),
                                          ToCodepointIndex.instance()
                                  ));


    }

    public final String example;
    public String chars;
    private final Map<Integer, Integer> indexToCodepoint;

    FancyConverter(String example, String chars) {
        this.example = example;
        this.chars = chars;
        this.indexToCodepoint = chars.codePoints()
                                     .boxed()
                                     .collect(toMap(ToCodepointIndex.instance(),
                                                    Function.identity()));
    }

    protected int translateChar(int codepoint) {
        var codepointAt = codepointIndex.get(codepoint);

        return indexToCodepoint.get(codepointAt);
    }

    public StringBuilder convert(String stringToProcess) {
        return stringToProcess.codePoints()
                              .map(this::translateChar)
                              .boxed()
                              .collect(FancyCollectors.toStringBuilder(stringToProcess.length()));
    }

    private static class ToCodepointIndex implements Function<Integer, Integer> {
        int movingOffset = 0;

        public static ToCodepointIndex instance() {
            return new ToCodepointIndex();
        }

        @Override
        public Integer apply(Integer c) {
//            int currentOffset = movingOffset;
//            movingOffset += Character.charCount(c);
//            return currentOffset;
            return movingOffset++;
        }
    }
}
