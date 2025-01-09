/*
 * jufmt
 *
 * Copyright (c) 2022, today - Brice DUTHEIL
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package io.github.bric3.jufmt.app;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.ValueLayout;
import java.util.stream.Collectors;

public class StdinReader {
    private static final int STDIN_FILENO = 0;

    private static Linker linker;
    static {
        try {
            linker = Linker.nativeLinker();
        } catch (Throwable t) {
            // Unsupported on GraalVM because aarch64 is not supported yet
            linker = null;
        }
        System.out.println("Linker is " + linker);
    }

    public static boolean isAvailable = linker != null;

    /**
     * One time use of isatty, after this can be thrown away.
     *
     * @param fd The file descriptor number
     * @return true if the file descriptor is a tty, false otherwise
     */
    @SuppressWarnings("SameParameterValue")
    private static boolean isatty(int fd) {
        if (linker == null) {
            // Unsupported on GraalVM because aarch64 is not supported yet
            return true;
        }

        var lookup = linker.defaultLookup();

        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");
        var isattySymbolName = isWindows ? "_isatty" : "isatty";

        var isatty = lookup
                .find(isattySymbolName)
                .orElse(null);

        if (isatty == null) {
            System.getLogger(StdinReader.class.getName()).log(System.Logger.Level.ERROR, isattySymbolName + " not found");
            return true;
        }

        try {
            var isatty_MH = linker.downcallHandle(isatty, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT));
            return (int) isatty_MH.invokeExact(fd) > 0;
        } catch (Throwable t) {
            System.getLogger(StdinReader.class.getName()).log(System.Logger.Level.ERROR, "Error invoking isatty", t);
            return true;
        }
    }

    public static boolean isStdinConnectedToTty = isatty(STDIN_FILENO);

    /**
     * Returns stdin as a CharSequence.
     *
     * Blocks if nothing is on the input.
     *
     * @param stdin Should be System.in, parameter is available for testing
     * @return The content of the stdin as a CharSequence
     */
    public static CharSequence stdinCharSequence(InputStream stdin) {
        var reader = new BufferedReader(new InputStreamReader(stdin));
        return reader.lines().collect(Collectors.joining("\n"));
    }

    public static void main(String[] args) {
        System.out.println("Is stdin connected to a tty? " + isStdinConnectedToTty);

        if (!isStdinConnectedToTty) {
            System.out.println(stdinCharSequence(System.in));
        }
    }
}
