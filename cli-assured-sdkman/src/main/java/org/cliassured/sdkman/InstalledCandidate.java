/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 CLI Assured contributors as indicated by the @author tags
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cliassured.sdkman;

import java.nio.file.Files;
import java.nio.file.Path;
import org.cliassured.CliAssured;
import org.cliassured.CommandSpec;

/**
 * An installed SDKMAN! candidate, such as Maven 3.9.11 or Java 25.0.3-tem.
 *
 * @author <a href="https://github.com/ppalaga">Peter Palaga</a>
 * @since  0.2.0
 */
public class InstalledCandidate {
    private final Path home;
    private final Path bin;

    InstalledCandidate(Path home) {
        this.home = home;
        this.bin = home.resolve("bin");
    }

    /**
     * Resolves {@code binaryName} against {@link #home()} and if that file exists, returns a new {@link CommandSpec} having
     * the resolved executable set.
     * <p>
     * You may need to append {@code .exe} or {@code .cmd} to the plain {@code binaryName} on Windows.
     *
     * @param  binaryName            name of a file under this {@link InstalledCandidate}'s {@code bin} directory
     * @return                       a new {@link CommandSpec} having executable set to an absolute path of the specified
     *                               {@code binaryName}
     * @throws IllegalStateException if the specified {@code binaryName} does not exist under {@link #home()}
     * @since                        0.2.0
     */
    public CommandSpec bin(String binaryName) {
        final Path executable = bin.resolve(binaryName);
        if (Files.isRegularFile(executable)) {
            return CliAssured.command(executable.toString());
        }
        throw new IllegalStateException("The requested binary " + executable + " does not exist");
    }

    /**
     * @return home directory of this {@link InstalledCandidate}, typically {@code $SDKMAN_DIR/<candidate>/<version>}
     * @since  0.2.0
     */
    public Path home() {
        return home;
    }

    /**
     * @return bin directory of this {@link InstalledCandidate}, typically {@code $SDKMAN_DIR/<candidate>/<version>/bin}
     * @since  0.2.0
     */
    public Path bin() {
        return bin;
    }
}
