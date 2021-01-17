package jufmt;

public enum FancyStyle {
    strikethrough("COMBINING LONG STROKE OVERLAY"),
//    underline("COMBINING DOUBLE MACRON"),
    underline("COMBINING DOUBLE MACRON BELOW"),
    lowline("COMBINING LOW LINE"),
    doubleLowline("COMBINING DOUBLE LOW LINE"),
    overline("COMBINING OVERLINE"),
    doubleOverline("COMBINING DOUBLE OVERLINE"),
    solidus("COMBINING SHORT SOLIDUS OVERLAY"),
    ethiopicSectionMark("ETHIOPIC SECTION MARK"),
    upwardArrowBelow("COMBINING UPWARDS ARROW BELOW"),
    ;



    public final int value;

    FancyStyle(String codePointName) {
        value = Character.codePointOf(codePointName);
    }
}
