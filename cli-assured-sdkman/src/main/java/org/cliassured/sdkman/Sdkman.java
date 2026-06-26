/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 CLI Assured contributors as indicated by the @author tags
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cliassured.sdkman;

import java.nio.file.Path;
import org.cliassured.sdkman.SdkmanSpec.ExcludeFromJacocoGeneratedReport;

// @formatter:off
/**
 * Entry methods for installing and invoking SDKMAN!
 *
 * @author <a href="https://github.com/ppalaga">Peter Palaga</a>
 * @since  0.2.0
 */
// @formatter:on
public class Sdkman {

    private Sdkman() {
    }

    /**
     * @return a new {@link SdkmanSpec} with default SDKMAN home directory and installation script URL
     * @since  0.2.0
     */
    public static SdkmanSpec given() {
        return new SdkmanSpec();
    }

    /**
     * @param  home SDKMAN! home directory
     * @return      a new {@link SdkmanSpec} with default installation script URL and the specified SDKMAN home directory
     *              set
     * @since       0.2.0
     */
    public static SdkmanSpec home(Path home) {
        return new SdkmanSpec().home(home);
    }

    /**
     * @param  installationScriptUrl the distribution URL
     * @return                       a new {@link SdkmanSpec} with default SDKMAN home directory and the given installation
     *                               script URL set
     * @since                        0.2.0
     */
    public static SdkmanSpec installationScriptUrl(String installationScriptUrl) {
        return new SdkmanSpec().installationScriptUrl(installationScriptUrl);
    }

    /**
     * Install SDKMAN! from default installation script URL to default SDKMAN home directory or throw an
     * {@link AssertionError} if SDKMAN! is installed there already.
     *
     * @return                       a new {@link InstalledSdkman} instance
     * @throws AssertionError if this SDKMAN! version is installed in default SDKMAN home directory already
     * @since                        0.2.0
     */
    @ExcludeFromJacocoGeneratedReport
    public static InstalledSdkman install() {
        return new SdkmanSpec().install();
    }

    /**
     * Throw an {@link AssertionError} unless SDKMAN is installed in default SDKMAN home directory.
     * <p>
     * You may want to prefer {@link #installIfNeeded()} if you cannot guarantee that SDKMAN! was installed already
     *
     * @return                       a new {@link InstalledSdkman} instance
     * @throws AssertionError if SDKMAN! is not installed in default SDKMAN home directory
     * @since                 0.2.0
     */
    @ExcludeFromJacocoGeneratedReport
    public static InstalledSdkman assertInstalled() {
        return new SdkmanSpec().assertInstalled();
    }

    /**
     * Install SDKMAN! from default installation script URL to default SDKMAN home directory
     * unless it is installed already.
     *
     * @return a new {@link InstalledSdkman} instance
     * @since  0.2.0
     */
    public static InstalledSdkman installIfNeeded() {
        return new SdkmanSpec().installIfNeeded();
    }

}
