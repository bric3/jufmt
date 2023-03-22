package io.github.bric3.jufmt.internal.figlet;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.Arrays;
import java.util.zip.ZipInputStream;

public class IOUtils {
    public static final byte[] PKZIP_HEADER = new byte[]{0x50, 0x4b, 0x03, 0x04};

    static @NotNull InputStream unwrapZippedFontIfNecessary(@Nullable InputStream inputStream) throws IOException {
        if (inputStream == null) {
            throw new IOException("Failed to open file");
        }

        // detects zipped font
        var pushbackInputStream = new PushbackInputStream(inputStream, 4);
        var buf = new byte[4];
        pushbackInputStream.read(buf, 0, 4);
        pushbackInputStream.unread(buf);

        if (Arrays.equals(PKZIP_HEADER, buf)) {
            var zipInputStream = new ZipInputStream(pushbackInputStream);
            // expects a single anonymous entry
            var nextEntry = zipInputStream.getNextEntry();
            if (nextEntry == null) {
                throw new IOException("No entry in the zip file");
            }
            return zipInputStream;
        } else {
            return pushbackInputStream;
        }
    }
}
