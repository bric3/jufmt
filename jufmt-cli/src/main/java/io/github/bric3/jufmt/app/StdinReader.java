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
import java.io.InputStreamReader;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.ValueLayout;
import java.util.stream.Collectors;

public class StdinReader {
    private static final int STDIN_FILENO = 0;

    private static Linker linker;
    static {
        try {
            linker = Linker.nativeLinker();
        } catch (Error t) {
            // Unsupported on GraalVM because aarch64 is not supported yet
        }
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
        var isatty = lookup
                .find("isatty")
                .orElseThrow();
        var isatty_MH = linker.downcallHandle(isatty, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT));
        try {
            return (int) isatty_MH.invokeExact(fd) > 0;
        } catch (Throwable t) {
            System.getLogger(StdinReader.class.getName()).log(System.Logger.Level.ERROR, "Error invoking isatty", t);
            return true;
        }
    }

    public static boolean isStdinConnectedToTty = isatty(STDIN_FILENO);

    public static CharSequence stdinCharSequence() {
        var reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.lines().collect(Collectors.joining("\n"));
    }

    public static void main(String[] args) {
        System.out.println("Is stdin connected to a tty? " + isStdinConnectedToTty);

        if (!isStdinConnectedToTty) {
            System.out.println(stdinCharSequence());
        }
    }
}
