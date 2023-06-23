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

import java.util.stream.Collector;

public enum FancyStyle {
    strikethrough("COMBINING LONG STROKE OVERLAY", "b̶r̶i̶c̶3̶"),
    macron("COMBINING DOUBLE MACRON", "b͞r͞i͞c͞3͞"),
    macronBelow("COMBINING DOUBLE MACRON BELOW", "b͟r͟i͟c͟3͟"),
    lowline("COMBINING LOW LINE", "b̲r̲i̲c̲3̲"),
    doubleLowline("COMBINING DOUBLE LOW LINE", "b̳r̳i̳c̳3̳"),
    overline("COMBINING OVERLINE", "b̅r̅i̅c̅3̅"),
    doubleOverline("COMBINING DOUBLE OVERLINE", "b̿r̿i̿c̿3̿"),
    shadow("COMBINING SHORT SOLIDUS OVERLAY", "b̷r̷i̷c̷3̷"),
    upwardArrowBelow("COMBINING UPWARDS ARROW BELOW", "b͎r͎i͎c͎3͎"),
    hotFumes("COMBINING VERTICAL TILDE", "b̾r̾i̾c̾3̾"),
    doubleArrow("COMBINING LEFT RIGHT ARROW BELOW", "b͍r͍i͍c͍3͍"),
    electric("COMBINING ZIGZAG ABOVE", "b͛r͛i͛c͛3͛"),
    snow("COMBINING HOMOTHETIC ABOVE", "b͋r͋i͋c͋3͋"),
    smeared("COMBINING CYRILLIC MILLIONS SIGN", "b҉r҉i҉c҉3҉"),
    ;



    public final int value;
    public final String example;

    FancyStyle(String codePointName, String example) {
        this.value = Character.codePointOf(codePointName);
        this.example = example;
    }


    public Collector<Integer, StringBuilder, StringBuilder> collector(int length) {
        return FancyCollectors.afterCodepoints(length, this.value);
    }


}
