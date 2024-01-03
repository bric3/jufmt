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

import java.util.EnumSet;
import java.util.Objects;

public class UnicodeNormalizer {
    private String stringToProcess = "";
    private java.text.Normalizer.Form normalizationForm;
    private boolean stripDiacriticalMarks = false;

    public UnicodeNormalizer(String stringToProcess, java.text.Normalizer.Form normalizationForm, boolean stripDiacriticalMarks) {
        this.stringToProcess = stringToProcess;
        this.normalizationForm = normalizationForm;
        this.stripDiacriticalMarks = stripDiacriticalMarks;
    }

    public void setStringToProcess(String stringToProcess) {
        this.stringToProcess = stringToProcess;
    }

    public void setNormalizationForm(java.text.Normalizer.Form normalizationForm) {
        this.normalizationForm = normalizationForm;
    }

    public void setStripDiacriticalMarks(boolean stripDiacriticalMarks) {
        this.stripDiacriticalMarks = stripDiacriticalMarks;
    }

    public String normalize() {
        Objects.requireNonNull(normalizationForm);
        Objects.requireNonNull(stringToProcess);

        // https://unicode.org/reports/tr15/#Norm_Forms
        // https://towardsdatascience.com/difference-between-nfd-nfc-nfkd-and-nfkc-explained-with-python-code-e2631f96ae6c
        // - Normalization Form D (NFD) : Canonical Decomposition
        // - Normalization Form C (NFC) : Canonical Decomposition, followed by Canonical Composition
        // - Normalization Form KD (NFKD) : Compatibility Decomposition
        // - Normalization Form KC (NFKC) : Compatibility Decomposition, followed by Canonical Composition
        //
        // In compatibility mode (K), the length can change,
        //    because a character can be decomposed for "compatibility",
        //    e.g. '…' -> '...'
        // In decomposition mode (D), the length can change,
        //    because a character can be decomposed by main char and combining mark,
        //    e.g. 'ポ'(U+30DD) -> 'ホ'(U+30DB) + '  ゚'(U+309A)
        // If followed by composition (C), then some separated chars are composed back together
        var normalized = java.text.Normalizer.normalize(stringToProcess, normalizationForm);
        if (stripDiacriticalMarks) {
            if (EnumSet.of(java.text.Normalizer.Form.NFC, java.text.Normalizer.Form.NFKC).contains(normalizationForm)) {
                throw new UnsupportedOperationException(
                        "Diacritical mark stripping only works without canonical composition, e.g. only NFD and NFKD"
                );
            }
            normalized = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}", "");
        }
        return normalized;
    }
}
