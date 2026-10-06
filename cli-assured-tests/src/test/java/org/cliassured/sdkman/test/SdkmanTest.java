/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 CLI Assured contributors as indicated by the @author tags
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cliassured.sdkman.test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.regex.Pattern;
import org.assertj.core.api.Assertions;
import org.cliassured.sdkman.InstalledCandidate;
import org.cliassured.sdkman.Sdk;
import org.cliassured.sdkman.Sdkman;
import org.cliassured.sdkman.SdkmanSpec;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

public class SdkmanTest {

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void e2e() {
        final Path baseDir = Paths.get(".").toAbsolutePath().normalize();
        final Path sdkmanHome = baseDir.resolve("target/sdkman-" + UUID.randomUUID());
        SdkmanSpec sdkSpec = Sdkman.given().home(sdkmanHome);

        Assertions.assertThat(sdkSpec.isInstalled()).isFalse();

        Sdk sdk = sdkSpec.installIfNeeded().sdk();
        Assertions.assertThat(sdkSpec.isInstalled()).isTrue();
        Assertions.assertThat(sdkSpec.sdkmanInitSh()).isRegularFile();

        sdk
                .args("version")
                .stderrToStdout()
                .then()
                .stdout()
                // .log()
                .hasLinesContaining("SDKMAN!", "script:", "native:")
                .execute()
                .assertSuccess();

        final String mvnScriptName = "mvn" + (System.getProperty("os.name").toLowerCase().contains("win") ? ".cmd" : "");
        {
            final String version = "3.9.16";
            InstalledCandidate maven = sdk.installCandidateIfNeeded("maven", version);
            maven
                    .bin(mvnScriptName)
                    .args("--version")
                    .stderrToStdout()
                    .then()
                    .stdout()
                    // .log()
                    .hasLinesMatching("Apache Maven\\s+" + Pattern.quote(version) + "(?:\\s|$)")
                    .execute()
                    .assertSuccess();

            Assertions.assertThat(sdkSpec.home().resolve("candidates/maven/" + version + "/bin/"
                    + mvnScriptName)).isRegularFile();
        }

        {
            final String version = "3.9.12";
            InstalledCandidate maven = sdk.installCandidateIfNeeded("maven", version);
            maven
                    .bin(mvnScriptName)
                    .args("--version")
                    .stderrToStdout()
                    .then()
                    .stdout()
                    // .log()
                    .hasLinesMatching("Apache Maven\\s+" + Pattern.quote(version) + "(?:\\s|$)")
                    .execute()
                    .assertSuccess();

            Assertions.assertThat(sdkSpec.home().resolve("candidates/maven/" + version + "/bin/"
                    + mvnScriptName)).isRegularFile();
        }

    }
}
