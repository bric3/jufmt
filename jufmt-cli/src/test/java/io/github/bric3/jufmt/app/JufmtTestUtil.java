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

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import static java.nio.charset.StandardCharsets.UTF_8;

class JufmtTestUtil {
    record Result(int status, String out, String err) {}

    public static Result jufmt(String... args) {
        var outWriter = new StringWriter();
        var errWriter = new StringWriter();
        try (
            var out = new PrintWriter(outWriter, true);
            var err = new PrintWriter(errWriter, true);
        ) {
            int status = new CommandLine(new JufmtCommand())
                    .setOut(out)
                    .setErr(err)
                    .execute(args);

            return new Result(
                    status,
                    outWriter.toString(),
                    errWriter.toString()
            );
        }
    }
}
