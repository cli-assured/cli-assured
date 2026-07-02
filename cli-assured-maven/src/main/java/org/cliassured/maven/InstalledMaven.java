/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 CLI Assured contributors as indicated by the @author tags
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cliassured.maven;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.cliassured.CliAssured;
import org.cliassured.CommandSpec;
import org.cliassured.maven.MavenSpec.ExcludeFromJacocoGeneratedReport;

/**
 * Maven installed locally.
 *
 * @author <a href="https://github.com/ppalaga">Peter Palaga</a>
 * @since  0.2.0
 */
public class InstalledMaven {

    private final Path home;
    private final String version;

    InstalledMaven(String version, Path home) {
        this.version = Objects.requireNonNull(version, "version");
        this.home = Objects.requireNonNull(home, "home");
    }

    /**
     * @return the path pointing at the {@code mvn} or {@code mvn.cmd} executable of this {@link InstalledMaven}; the path
     *         is
     *         guaranteed to exist only after calling {@link #assertInstalled()}, {@link #installIfNeeded()} or ensuring
     *         that {@link #isInstalled()} returns {@code true}
     * @since  0.2.0
     */
    @ExcludeFromJacocoGeneratedReport
    Path mvnPath() {
        return home
                .resolve("bin/mvn"
                        + (System.getProperty("os.name").toLowerCase().contains("win") ? ".cmd" : ""));
    }

    /**
     * @return the Maven home directory of this {@link InstalledMaven} - the one containing {@code bin/mvn[.cmd]}
     * @since  0.0.1
     */
    public Path home() {
        return home;
    }

    /**
     * @return the Maven version of this {@link InstalledMaven}
     * @since  0.0.1
     */
    public String version() {
        return version;
    }

    /**
     * Returns a new {@link CommandSpec} that can be used to execute a Maven command
     * and/or define assertions on the output.
     *
     * @return a new {@link CommandSpec} with its executable path and arguments set
     * @since  0.2.0
     */
    @ExcludeFromJacocoGeneratedReport
    public CommandSpec mvn() {
        final String exe = mvnPath().toString();
        if (exe.endsWith(".cmd")) {
            /* Windows call via cmd.exe */
            return CliAssured.command("cmd.exe")
                    .args("/c", exe);
        } else {
            /* Linux or Mac - call mvn directly */
            return CliAssured.command(exe);
        }
    }

    /**
     * Resolves {@code binaryName} and {@code altBinaryNames} against {@code home() + "/bin"},
     * selects the first of those files that exists and returns a new {@link CommandSpec} having
     * the file set as {@link executable CommandSpec#executable(String)}.
     * <p>
     * {@code altBinaryNames} may come it handy when covering Windows and Unix-like platforms.
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
        {
            final Path executable = home.resolve("bin/" + binaryName);
            if (Files.isRegularFile(executable)) {
                return CliAssured.command(executable.toString());
            }
        }
        if (altBinaryNames.length > 0) {
            for (String altBinaryName : altBinaryNames) {
                final Path executable = home.resolve("bin/" + altBinaryName);
                if (Files.isRegularFile(executable)) {
                    return CliAssured.command(executable.toString());
                }
            }
            throw new IllegalStateException(
                    "None of the requested binaries "
                            + Stream.concat(Stream.of(binaryName), Stream.of(altBinaryNames))
                                    .map(name -> home.resolve("bin/" + name))
                                    .map(Path::toString)
                                    .collect(Collectors.joining(", "))
                            + " exists");
        }
        throw new IllegalStateException("The requested binary " + home.resolve("bin/" + binaryName) + " does not exist");
    }
}
