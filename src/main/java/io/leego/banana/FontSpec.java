package io.leego.banana;

import java.awt.Font;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

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
