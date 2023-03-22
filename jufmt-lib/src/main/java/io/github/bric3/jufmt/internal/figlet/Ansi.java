package io.github.bric3.jufmt.internal.figlet;

import org.jetbrains.annotations.NotNull;

/**
 * @author Yihleego
 */
public enum Ansi {
    /**
     * Font-Colors.
     */
    BLACK("30"),
    RED("31"),
    GREEN("32"),
    YELLOW("33"),
    BLUE("34"),
    PURPLE("35"),
    CYAN("36"),
    WHITE("37"),
    /**
     * Background-Colors.
     */
    BG_BLACK("40"),
    BG_RED("41"),
    BG_GREEN("42"),
    BG_YELLOW("43"),
    BG_BLUE("44"),
    BG_PURPLE("45"),
    BG_CYAN("46"),
    BG_WHITE("47"),
    /**
     * Others.
     */
    NORMAL("0"),
    BOLD("1"),
    FAINT("2"),
    ITALIC("3"),
    UNDERLINE("4"),
    SLOW_BLINK("5"),
    RAPID_BLINK("6"),
    REVERSE_VIDEO("7"),
    CONCEAL("8"),
    CROSSED_OUT("9"),
    PRIMARY("10");

    public final String code;

    Ansi(String code) {
        this.code = code;
    }

    public String getAnsi() {
        return "\033[" + code + "m";
    }

    static void applyStyles(StringBuilder sb, Ansi... styles) {
        sb.append("\033[");
        for (var style : styles) {
            sb.append(style.code).append(";");
        }
        sb.deleteCharAt(sb.length() - 1).append("m");
    }

    public static @NotNull String apply(@NotNull String text, @NotNull Ansi... styles) {
        if ("".equals(text) || styles == null || styles.length == 0) {
            return text;
        }
        var sb = new StringBuilder();
        applyStyles(sb, styles);
        sb.append(text);
        applyStyles(sb, NORMAL);
        return sb.toString();
    }
}
