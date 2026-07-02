/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 CLI Assured contributors as indicated by the @author tags
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cliassured.test.j21.docs;

// tag::imports[]
import java.nio.file.Path;
import java.nio.file.Paths;
import org.cliassured.sdkman.Sdkman;
// end::imports[]
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

@DisabledOnOs(OS.WINDOWS)
public class SdkmanInstallationScriptUrlTest {

    static final Path sdkmanHome = Paths.get("target/sdkman-" + UUID.randomUUID())
            .toAbsolutePath().normalize();

    @Test
    void installationScriptUrl() {
        // @formatter:off
        // tag::installationScriptUrl[]
        // Install SDKMAN! using a custom local script
        Path customInstallationScript = Paths.get("target/test-classes/custom-get-sdkman.sh").toAbsolutePath().normalize();
        Sdkman.installationScriptUrl(customInstallationScript.toUri().toString())
            // end::installationScriptUrl[]
            .home(sdkmanHome)
            // tag::installationScriptUrl[]
            .installIfNeeded()
            // Get the sdk command
            .sdk()
            // Call `sdk version`
            .args("version")
            .then()
                .stdout()
                    .log()
                    .hasLines("Custom SDKMAN! version 1.2.3")
            .execute()
            .assertSuccess();
        // end::installationScriptUrl[]
        // @formatter:on
    }
}
