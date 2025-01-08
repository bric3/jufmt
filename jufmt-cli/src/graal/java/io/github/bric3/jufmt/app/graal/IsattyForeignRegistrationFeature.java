/*
 * jufmt
 *
 * Copyright (c) 2022, today - Brice DUTHEIL
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package io.github.bric3.jufmt.app.graal;

import org.graalvm.nativeimage.hosted.Feature;
import org.graalvm.nativeimage.hosted.RuntimeForeignAccess;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.ValueLayout;

// Not yet available for arm64
// https://github.com/oracle/graal/blob/d5c7b8aab207e8d6ad0eef494e316bdfdd5a4ebf/substratevm/src/com.oracle.svm.hosted.foreign/src/com/oracle/svm/hosted/foreign/ForeignFunctionsFeature.java#L221
public class IsattyForeignRegistrationFeature implements Feature {
    @Override
    public void duringSetup(DuringSetupAccess access) {
        RuntimeForeignAccess.registerForDowncall(FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT));
    }
}
