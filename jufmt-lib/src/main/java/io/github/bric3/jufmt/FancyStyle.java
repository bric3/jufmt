/*
 * jufmt
 *
 * Copyright (c) 2023, today - Brice DUTHEIL
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package io.github.bric3.jufmt;

import java.util.Arrays;
import java.util.stream.Collector;

public enum FancyStyle {
    strikethrough("b̶r̶i̶c̶3̶", "COMBINING LONG STROKE OVERLAY"),
    macron("b͞r͞i͞c͞3͞", "COMBINING DOUBLE MACRON"),
    macronBelow("b͟r͟i͟c͟3͟", "COMBINING DOUBLE MACRON BELOW"),
    lowline("b̲r̲i̲c̲3̲", "COMBINING LOW LINE"),
    doubleLowline("b̳r̳i̳c̳3̳", "COMBINING DOUBLE LOW LINE"),
    overline("b̅r̅i̅c̅3̅", "COMBINING OVERLINE"),
    doubleOverline("b̿r̿i̿c̿3̿", "COMBINING DOUBLE OVERLINE"),
    shadow("b̷r̷i̷c̷3̷", "COMBINING SHORT SOLIDUS OVERLAY"),
    upwardArrowBelow("b͎r͎i͎c͎3͎", "COMBINING UPWARDS ARROW BELOW"),
    hotFumes("b̾r̾i̾c̾3̾", "COMBINING VERTICAL TILDE"),
    doubleArrow("b͍r͍i͍c͍3͍", "COMBINING LEFT RIGHT ARROW BELOW"),
    electric("b͛r͛i͛c͛3͛", "COMBINING ZIGZAG ABOVE"),
    snow("b͋r͋i͋c͋3͋", "COMBINING HOMOTHETIC ABOVE"),
    smeared("b҉r҉i҉c҉3҉", "COMBINING CYRILLIC MILLIONS SIGN"),
    doubleBreve("", "COMBINING DOUBLE INVERTED BREVE", "COMBINING DOUBLE BREVE BELOW"), // see also UNDERTIE
    ;


    public final int[] combining;
    public final String example;

    FancyStyle(String example, String... codePointNames) {
        this.combining = Arrays.stream(codePointNames).mapToInt(
                Character::codePointOf
        ).toArray();
        this.example = example;
    }


    public Collector<Integer, StringBuilder, StringBuilder> collector(int length) {
        return FancyCollectors.afterCodepoints(length, this.combining);
    }


}
