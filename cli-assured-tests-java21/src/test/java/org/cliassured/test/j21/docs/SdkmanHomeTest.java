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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

@DisabledOnOs(OS.WINDOWS)
public class SdkmanHomeTest {

    @Test
    void installSdkmanToCustomHome() {
        // @formatter:off
        // tag::installToCustomHome[]
        Path customHome = Paths.get("target/sdkman").toAbsolutePath().normalize();
        // Install SDKMAN! to customHome
        Sdkman.home(customHome)
            // Install SDKMAN! if needed
            .installIfNeeded()
            // Get the sdk command
            .sdk()
            // Call `sdk version`
            .args("version")
            .then()
                .stdout()
                    .log()
                    .hasLinesContaining(
                            "SDKMAN!",
                            "script:",
                            "native:")
            .execute()
            .assertSuccess();
        // end::installToCustomHome[]
        // @formatter:on
    }
}
