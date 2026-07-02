/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 CLI Assured contributors as indicated by the @author tags
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cliassured.sdkman;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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

    InstalledCandidate(Path home) {
        this.home = home;
    }

    /**
     * Resolves {@code binaryName} and {@code altBinaryNames} against {@code home() + "/bin"},
     * selects the first of those files that exists and returns a new {@link CommandSpec} having
     * the file set as {@link executable CommandSpec#executable(String)}.
     * <p>
     * {@code altBinaryNames} may come it handy when covering Windows and Unix-like platforms.
     * E.g. to resolve {@code java} on Linux and {@code java.exe} on Windows you would call
     * {@code bin("java", "java.exe")}.
     *
     * @param  binaryName            name of a file under this {@link InstalledCandidate}'s {@code bin} directory
     * @param  altBinaryNames        alternative names of files under this {@link InstalledCandidate}'s {@code bin}
     *                               directory
     * @return                       a new {@link CommandSpec} having executable set to an absolute path of the specified
     *                               {@code binaryName}
     * @throws IllegalStateException if the specified {@code binaryName} does not exist under {@link #home()}
     * @since                        0.2.0
     */
    public CommandSpec bin(String binaryName, String... altBinaryNames) {
        final Path bin = home.resolve("bin");
        {
            final Path executable = bin.resolve(binaryName);
            if (Files.isRegularFile(executable)) {
                return CliAssured.command(executable.toString());
            }
        }
        if (altBinaryNames.length > 0) {
            for (String altBinaryName : altBinaryNames) {
                final Path executable = bin.resolve(altBinaryName);
                if (Files.isRegularFile(executable)) {
                    return CliAssured.command(executable.toString());
                }
            }
            throw new IllegalStateException(
                    "None of the requested binary "
                            + Stream.concat(Stream.of(binaryName), Stream.of(altBinaryNames))
                                    .map(bin::resolve)
                                    .map(Path::toString)
                                    .collect(Collectors.joining(", "))
                            + " exists");
        }
        throw new IllegalStateException("The requested binary " + bin.resolve(binaryName) + " does not exist");
    }

    /**
     * @return home directory of this {@link InstalledCandidate}, typically {@code $SDKMAN_DIR/<candidate>/<version>}
     * @since  0.2.0
     */
    public Path home() {
        return home;
    }

}
