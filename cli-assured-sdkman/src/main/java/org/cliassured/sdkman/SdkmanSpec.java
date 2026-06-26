/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 CLI Assured contributors as indicated by the @author tags
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cliassured.sdkman;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.cliassured.CliAssured;
import org.slf4j.LoggerFactory;

/**
 * A specification of an SDKMAN! installation
 *
 * @author <a href="https://github.com/ppalaga">Peter Palaga</a>
 * @since  0.2.0
 */
public class SdkmanSpec {
    private static final int BUFFER_SIZE = 8192;
    private static final String GET_SDKMAN_IO = "https://get.sdkman.io";
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(SdkmanSpec.class);

    private final Path home;
    private final String installationScriptUrl;

    SdkmanSpec() {
        this.home = Paths.get(System.getProperty("user.home")).resolve(".sdkman");
        this.installationScriptUrl = GET_SDKMAN_IO;
    }

    SdkmanSpec(Path home, String installationScriptUrl) {
        this.home = home;
        this.installationScriptUrl = installationScriptUrl;
    }

    /**
     * Set {@code SDKMAN_DIR} directory where SDKMAN! scripts and candidates will be installed. Typically {@code ~/.sdkman}
     *
     * @param  home SDKMAN! home directory
     * @return      an adjusted copy of this {@link SdkmanSpec}
     * @since       0.2.0
     */
    public SdkmanSpec home(Path home) {
        return new SdkmanSpec(home, installationScriptUrl);
    }

    /**
     * @return {@code SDKMAN_DIR} directory where SDKMAN! scripts and packages will be installed. Typically
     *         {@code ~/.sdkman}
     * @since  0.2.0
     */
    public Path home() {
        return home;
    }

    /**
     * Set the installation script URL from which {@link SdkmanSpec} can be installed instead of the default
     * {@value #GET_SDKMAN_IO}.
     *
     * @param  installationScriptUrl the distribution URL
     * @return                       an adjusted copy of this {@link SdkmanSpec}
     * @since                        0.2.0
     */
    public SdkmanSpec installationScriptUrl(String installationScriptUrl) {
        return new SdkmanSpec(home, installationScriptUrl);
    }

    /**
     * @return the installation script URL
     * @since  0.2.0
     */
    public String installationScriptUrl() {
        return installationScriptUrl;
    }

    /**
     * @return the path to {@code sdkman-init.sh}
     * @since  0.2.0
     */
    public Path sdkmanInitSh() {
        return home.resolve("bin/sdkman-init.sh");
    }

    /**
     * Install SDKMAN! from {@link #installationScriptUrl()} or throw an {@link AssertionError} if SDKMAN! is installed in {@link #home()} already.
     * <p>
     * You may want to prefer {@link #installIfNeeded()} if you cannot guarantee that SDKMAN! was installed already
     *
     * @return                       a new {@link InstalledSdkman} instance
     * @throws AssertionError if this SDKMAN! version is installed in {@link #home()} already
     * @since                        0.2.0
     */
    @ExcludeFromJacocoGeneratedReport
    public InstalledSdkman install() {
        final Path sdkmanInitSh = sdkmanInitSh();
        if (Files.exists(sdkmanInitSh)) {
            throw new AssertionError(
                    "Cannot install SDKMAN! to " + home + " because " + sdkmanInitSh + " exists already");
        }
        log.info("Installing SDKMAN! to " + home);

        CliAssured
                .command("bash")
                .env("SDKMAN_DIR", home.toString())
                .stdin(stdin -> {
                    try (InputStream in = new URI(installationScriptUrl).toURL().openStream()) {
                        final byte[] buff = new byte[BUFFER_SIZE];
                        int bytesRead;
                        while ((bytesRead = in.read(buff)) >= 0) {
                            stdin.write(buff, 0, bytesRead);
                        }
                    } catch (IOException e) {
                        throw new UncheckedIOException("Could not get SDKMAN installation script from " + installationScriptUrl,
                                e);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException("Could not get SDKMAN installation script from " + installationScriptUrl, e);
                    }
                })
                .stderrToStdout()
                .then()
                .stdout()
                .hasLines("All done!")
                .execute()
                .assertSuccess();

        return new InstalledSdkman(home, sdkmanInitSh);
    }

    /**
     * Throw an {@link IllegalStateException} unless SDKMAN is installed in directory returned by {@link #home()}.
     *
     * @return                       a new {@link InstalledSdkman} instance
     * @throws AssertionError if SDKMAN! is not installed in directory returned by {@link #home()}
     * @since                 0.2.0
     */
    @ExcludeFromJacocoGeneratedReport
    public InstalledSdkman assertInstalled() {
        if (!isInstalled()) {
            throw new AssertionError(
                    "SDKMAN! is not installed in " + home + ". You may want to call Sdk.installIfNeeded()");
        }
        return new InstalledSdkman(home, sdkmanInitSh());
    }

    /**
     * @return {@code true} if SDKMAN! can be found in {@link #home()}; {@code false} otherwise
     * @since  0.2.0
     */
    @ExcludeFromJacocoGeneratedReport
    public boolean isInstalled() {
        final Path sdkmanInitSh = sdkmanInitSh();
        return Files.isRegularFile(sdkmanInitSh);
    }

    /**
     * Install SDKMAN! from {@link #installationScriptUrl()} to {@link #home()} unless it is installed already.
     *
     * @return a new {@link InstalledSdkman} instance
     * @since  0.2.0
     */
    public InstalledSdkman installIfNeeded() {
        if (!isInstalled()) {
            return install();
        }
        return new InstalledSdkman(home, sdkmanInitSh());
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.CONSTRUCTOR, ElementType.METHOD })
    @interface ExcludeFromJacocoGeneratedReport {
    }
}
