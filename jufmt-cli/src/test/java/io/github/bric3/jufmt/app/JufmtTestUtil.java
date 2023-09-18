package io.github.bric3.jufmt.app;

import io.github.bric3.jufmt.app.JufmtCommand;
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
