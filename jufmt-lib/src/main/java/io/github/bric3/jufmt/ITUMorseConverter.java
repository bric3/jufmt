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

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This converter implements basic ITU morse conversion
 *
 * That is accented letters are not available. In general unknown characters
 * are translated as an empty string.
 *
 * https://en.wikipedia.org/wiki/Morse_code
 */
public class ITUMorseConverter {
    public static final String example = "_... ._. .. _._. ...__";

    @SuppressWarnings("RedundantTypeArguments") // to avoid compiler and IDE slowdown
    private final Map<Integer, String> translation = Map.<Integer, String>ofEntries(
            Map.entry((int) 'a', "._"),
            Map.entry((int) 'b', "_..."),
            Map.entry((int) 'c', "_._."),
            Map.entry((int) 'd', "_.."),
            Map.entry((int) 'e', "."),
            Map.entry((int) 'f', ".._."),
            Map.entry((int) 'g', "__."),
            Map.entry((int) 'h', "...."),
            Map.entry((int) 'i', ".."),
            Map.entry((int) 'j', ".____"),
            Map.entry((int) 'k', "_._"),
            Map.entry((int) 'l', "._.."),
            Map.entry((int) 'm', "__"),
            Map.entry((int) 'n', "_."),
            Map.entry((int) 'o', "___"),
            Map.entry((int) 'p', ".__."),
            Map.entry((int) 'q', "__._"),
            Map.entry((int) 'r', "._."),
            Map.entry((int) 's', "..."),
            Map.entry((int) 't', "_"),
            Map.entry((int) 'u', ".._"),
            Map.entry((int) 'v', "..._"),
            Map.entry((int) 'w', ".__"),
            Map.entry((int) 'x', "_.._"),
            Map.entry((int) 'y', "_.__"),
            Map.entry((int) 'z', "__.."),

            Map.entry((int) '0', "_____"),
            Map.entry((int) '1', ".____"),
            Map.entry((int) '2', "..___"),
            Map.entry((int) '3', "...__"),
            Map.entry((int) '4', "...._"),
            Map.entry((int) '5', "....."),
            Map.entry((int) '6', "_...."),
            Map.entry((int) '7', "__..."),
            Map.entry((int) '8', "___.."),
            Map.entry((int) '9', "____."),

            Map.entry((int) '.', "._._._"),
            Map.entry((int) ',', "__..__"),
            Map.entry((int) '\'', ".____."),
            Map.entry((int) '\"', "._.._."),
            Map.entry((int) '_', "..__._"),
            Map.entry((int) ':', "___..."),
            Map.entry((int) ';', "_._._."),
            Map.entry((int) '?', "..__.."),
            Map.entry((int) '!', "_._.__"),
            Map.entry((int) '-', "_...._"),
            Map.entry((int) '+', "._._."),
            Map.entry((int) '/', "_.._."),
            Map.entry((int) '(', "_.__."),
            Map.entry((int) ')', "_.__._"),
            Map.entry((int) '=', "_..._"),
            Map.entry((int) '@', ".__._.")
    );

    private static boolean notCombiningMark(int cp) {
        // Here's a bitwise integer OR for fun, as there is no real performance
        // impact on jufmt.

        // It is equivalent to the following code :
        // return Character.getType(cp) != Character.COMBINING_SPACING_MARK
        //        || Character.getType(cp) != Character.NON_SPACING_MARK;
        //
        // This work because currently Unicode types defined in Character class
        // goes up to 30 (FINAL_QUOTE_PUNCTUATION), if it ever cross 31, then the
        // bitwise operation may not work well, then there's two choices, either
        // go back to readable code, or to keep the fun a bit longer one can make
        // use long instead of int, eg (1L << shift distance)

        return (1<<Character.getType(cp) & (1 << Character.COMBINING_SPACING_MARK | 1 << Character.NON_SPACING_MARK)) == 0;
    }


    public StringBuilder convert(String stringToProcess) {
        return Normalizer.normalize(stringToProcess, Form.NFKD)
                         .codePoints()
                         .filter(ITUMorseConverter::notCombiningMark)
                         .boxed()
                         .map(Character::toLowerCase)
                         .map(cp -> translation.getOrDefault(cp, ""))
                         .collect(Collectors.collectingAndThen(Collectors.joining(" "), StringBuilder::new));
    }
}


