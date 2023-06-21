/**
 * Internal code powering {@link io.github.bric3.jufmt.Figlet}
 *
 * Use {@link io.github.bric3.jufmt.Figlet}.
 * This is a fork of https://github.com/yihleego/banana/.
 * It is similar to version 2.1.0, but they are some changes
 * * classes have been refactored to enums, like
 *    {@code io.github.bric3.jufmt.internal.banana.Font}
 *    or {@link io.github.bric3.jufmt.internal.figlet.Ansi}.
 * * Visibility changes.
 * * Removed {@code io.github.bric3.jufmt.internal.banana.Font},
 *   replaced by {@link io.github.bric3.jufmt.EmbeddedFigletFonts}
 * * Made BananaUtils an instance rather that static singleton
 * * Split BananaUtils methods to related types, {@code buildMeta}
 *   is now in {@link io.github.bric3.jufmt.internal.figlet.FontMetadata},
 *   ansi stuff to {@link io.github.bric3.jufmt.internal.figlet.Ansi}
 * * Removes duplicate code in BananaUtils
 * * Refactored Option and Rule
 * * Extracted Ansi styling, as post processor
 * * Renamed BananaUtils to {@link io.github.bric3.jufmt.internal.figlet.FigletRenderer}
 *
 * More changes might come.
 *
 * @see io.github.bric3.jufmt.Figlet
 */
package io.github.bric3.jufmt.internal.figlet;