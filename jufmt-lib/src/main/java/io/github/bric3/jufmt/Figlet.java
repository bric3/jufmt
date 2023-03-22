package io.github.bric3.jufmt;

import io.github.bric3.jufmt.internal.banana.BananaUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class Figlet {
    public static String render(String text, FontSpec font) {
        return BananaUtils.bananaify(text, font);
    }

    public static String render(String text) {
        return render(text, randomEnum(XeroFonts.class));
    }

    private static <T extends Enum<T>> T randomEnum(Class<T> clazz) {
        var enumConstants = clazz.getEnumConstants();
        return enumConstants[new Random().nextInt(enumConstants.length)];
    }

    public interface FontSpec {

        String getName();

        String getFilename();

        default Charset getCharset() {
            return StandardCharsets.UTF_8;
        };

        static FontSpec of(String name, String filename) {
            return new FontSpec() {
                @Override
                public String getName() {
                    return name;
                }

                @Override
                public String getFilename() {
                    return filename;
                }
            };
        }
    }
}
