/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 CLI Assured contributors as indicated by the @author tags
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cliassured.sdkman;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.cliassured.CliAssured;
import org.cliassured.CommandSpec;
import org.cliassured.sdkman.SdkmanSpec.ExcludeFromJacocoGeneratedReport;

/**
 * SDKMAN's {@code sdk} command.
 *
 * @author <a href="https://github.com/ppalaga">Peter Palaga</a>
 * @since  0.2.0
 */
public class Sdk {

    private final Path sdkmanInitSh;
    private final Path home;

    Sdk(Path home, Path sdkmanInitSh) {
        this.home = home;
        this.sdkmanInitSh = sdkmanInitSh;
    }

    /**
     * Calls {@code sdk install <candidate> <version>}, unless it is installed already, and returns an
     * {@link InstalledCandidate}.
     *
     * @param  candidate the package to install, such as {@code maven} or {@code java}
     * @param  version   the version (optionally with distribution name suffix) to install, e.g. {@code 25.0.3-tem} for
     *                   {@code java}, or {@code 3.9.11} for {@code maven}
     * @return           {@code $SDKMAN_DIR/candidates/<packageName>/<version>} directory
     * @since            0.2.0
     */
    public InstalledCandidate installCandidateIfNeeded(String candidate, String version) {
        Path candidateHome = home.resolve("candidates").resolve(candidate).resolve(version);
        if (Files.isDirectory(candidateHome)) {
            return new InstalledCandidate(candidateHome);
        }
        args("install", candidate, version)
                .stderrToStdout()
                .then()
                .stdout()
                .hasLinesContaining("Done installing!")
                .execute()
                .assertSuccess();
        if (!Files.isDirectory(candidateHome)) {
            throw new IllegalStateException(
                    candidateHome + " does not exist after running `sdk install " + candidate + " " + version);
        }
        return new InstalledCandidate(candidateHome);
    }

    /**
     * Set {@code sdk} command line arguments and return a new {@link CommandSpec} that can be used to execute an
     * {@code sdk} command
     * and/or define assertions on the output.
     *
     * @param  args the {@code sdk} command line arguments to set
     * @return      a new {@link CommandSpec} with its executable path and arguments set
     * @since       0.2.0
     */
    @ExcludeFromJacocoGeneratedReport
    public CommandSpec args(String... args) {
        String executable = "bash" + (System.getProperty("os.name").toLowerCase().contains("win") ? ".exe" : "");
        return CliAssured.command(executable, args)
                .env("SDKMAN_DIR", home.toString())
                .commandArrayFunction((executableSupplier, params) -> {
                    List<String> result = new ArrayList<>();
                    result.add(executableSupplier.get());
                    result.add("-c");
                    StringBuilder script = new StringBuilder("source ")
                            .append(sdkmanInitSh.toString())
                            .append(" && export sdkman_auto_answer=true && export USE=n && sdk");
                    params.stream().forEach(param -> script.append(' ').append(param));
                    result.add(script.toString());
                    return result.toArray(new String[0]);
                });
    }

}
