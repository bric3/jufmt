package io.github.bric3.jufmt.internal.figlet;

import org.jetbrains.annotations.NotNull;

abstract class Strings {
    static final String BLANK_STRING = " ";
    static final String EMPTY_STRING = "";

    /**
     * Flatten string array.
     * @param texts the tests.
     * @return the flattened string array.
     */
    static @NotNull String[] flatten(@NotNull String @NotNull []... texts) {
        if (texts.length == 0) {
            return new String[0];
        }

        int count = 0;
        for (var group : texts) {
            count += group.length;
        }

        var result = new String[count];
        int i = 0;
        for (var group : texts) {
            for (var element : group) {
                result[i++] = element;
            }
        }
        return result;
    }

    /**
     * Returns a string that is a substring of this string.
     * @param s      the string.
     * @param start  the beginning index.
     * @param length the length of specified substring.
     * @return the specified substring.
     */
    static @NotNull String substr(String s, int start, int length) {
        if (isEmpty(s)) {
            return EMPTY_STRING;
        }
        return s.substring(start, Math.min(start + length, s.length()));
    }

    /**
     * Returns a section of an array.
     * @param array the array.
     * @param start the beginning of the specified portion of the array.
     * @param end   the end of the specified portion of the array.
     */
    static @NotNull String[] slice(@NotNull String[] array, int start, int end) {
        var result = new String[end - start];
        int index = 0;
        for (int i = start; i < end; i++) {
            result[index++] = array[i];
        }
        return result;
    }

    /**
     * Checks whether the given {@code String} is empty.
     * @param s the candidate String.
     * @return {@code true} if the {@code String} is empty.
     */
    static boolean isEmpty(String s) {
        return null == s || EMPTY_STRING.equals(s);
    }

    /**
     * Checks that the given {@code String} is neither {@code null} nor of length 0.
     * @param s the string.
     * @return <code>true</code> if the String is not empty and not null.
     */
    static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }

    static void padLines(@NotNull String[] lines, int numSpaces) {
        var padded = BLANK_STRING.repeat(Math.max(0, numSpaces));
        for (int i = 0; i < lines.length; i++) {
            lines[i] += padded;
        }
    }
}
