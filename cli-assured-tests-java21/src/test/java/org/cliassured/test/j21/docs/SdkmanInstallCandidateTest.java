/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 CLI Assured contributors as indicated by the @author tags
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cliassured.test.j21.docs;

// tag::imports[]
import org.cliassured.sdkman.InstalledCandidate;
import org.cliassured.sdkman.Sdk;
import org.cliassured.sdkman.Sdkman;
// end::imports[]
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

@DisabledOnOs(OS.WINDOWS)
public class SdkmanInstallCandidateTest {

    static final Path sdkmanHome = Paths.get("target/sdkman-" + UUID.randomUUID())
            .toAbsolutePath().normalize();

    @Test
    void installCandidate() {
        // @formatter:off
        // tag::installCandidate[]
        // Install a specific version of a candidate
        InstalledCandidate maven_3_9_11 = Sdkman
            // end::installCandidate[]
            .home(sdkmanHome)
            // tag::installCandidate[]
            .installIfNeeded()
            // calls `sdk install maven 3.9.11`
            .sdk().installCandidateIfNeeded("maven", "3.9.11");

        // Invoke the mvn binary from the installed candidate's bin folder
        maven_3_9_11
            .bin("mvn") // You may need to use "mvn.cmd" on Windows
            .args("--version")
            .stderrToStdout()
            .then()
                .stdout()
                    .hasLines("Apache Maven 3.9.11 (3e54c93a704957b63ee3494413a2b544fd3d825b)")
            .execute()
            .assertSuccess();
        // end::installCandidate[]
        // @formatter:on
    }

}
