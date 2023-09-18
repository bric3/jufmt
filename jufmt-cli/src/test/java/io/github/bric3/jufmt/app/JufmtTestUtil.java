/*
 * jufmt
 *
 * Copyright (c) 2023, today - Brice DUTHEIL
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package io.github.bric3.jufmt.app;

import picocli.CommandLine;

import java.io.PrintWriter;
import java.io.StringWriter;

class JufmtTestUtil {
    record Result(int status, String out, String err) {}

    public static Result jufmt(String... args) {
        var outWriter = new StringWriter();
        var errWriter = new StringWriter();
        int status = new CommandLine(new JufmtCommand())
                .setOut(new PrintWriter(outWriter, true))
                .setErr(new PrintWriter(errWriter, true))
                .execute(args);

        return new Result(status, outWriter.toString(), errWriter.toString());
    }
}
