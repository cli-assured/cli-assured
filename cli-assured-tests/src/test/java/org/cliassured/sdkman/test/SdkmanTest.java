/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 CLI Assured contributors as indicated by the @author tags
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cliassured.sdkman.test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
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
            InstalledCandidate mvn3911 = sdk.installCandidateIfNeeded("maven", "3.9.11");
            mvn3911
                    .bin(mvnScriptName)
                    .args("--version")
                    .stderrToStdout()
                    .then()
                    .stdout()
                    // .log()
                    .hasLines("Apache Maven 3.9.11 (3e54c93a704957b63ee3494413a2b544fd3d825b)")
                    .execute()
                    .assertSuccess();

            Assertions.assertThat(sdkSpec.home().resolve("candidates/maven/3.9.11/bin/"
                    + mvnScriptName)).isRegularFile();
            Assertions.assertThat(mvn3911.bin().resolve(mvnScriptName)).isRegularFile();
        }

        {
            InstalledCandidate mvn3912 = sdk.installCandidateIfNeeded("maven", "3.9.12");
            mvn3912
                    .bin(mvnScriptName)
                    .args("--version")
                    .stderrToStdout()
                    .then()
                    .stdout()
                    // .log()
                    .hasLines("Apache Maven 3.9.12 (848fbb4bf2d427b72bdb2471c22fced7ebd9a7a1)")
                    .execute()
                    .assertSuccess();

            Assertions.assertThat(sdkSpec.home().resolve("candidates/maven/3.9.12/bin/"
                    + mvnScriptName)).isRegularFile();
            Assertions.assertThat(mvn3912.bin().resolve(mvnScriptName)).isRegularFile();
        }

    }
}
