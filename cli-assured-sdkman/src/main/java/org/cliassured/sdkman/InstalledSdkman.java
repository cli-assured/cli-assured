/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 CLI Assured contributors as indicated by the @author tags
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cliassured.sdkman;

import java.nio.file.Path;

/**
 * SDKMAN! installed locally.
 *
 * @author <a href="https://github.com/ppalaga">Peter Palaga</a>
 * @since  0.2.0
 */
public class InstalledSdkman {

    private final Path home;
    private final Path sdkmanInitSh;

    InstalledSdkman(Path home, Path sdkmanInitSh) {
        this.home = home;
        this.sdkmanInitSh = sdkmanInitSh;
    }

    /**
     * @return a new {@link Sdk} instance
     * @since  0.2.0
     */
    public Sdk sdk() {
        return new Sdk(home, sdkmanInitSh);
    }
}
