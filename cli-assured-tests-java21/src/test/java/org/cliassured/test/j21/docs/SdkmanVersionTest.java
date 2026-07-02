/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 CLI Assured contributors as indicated by the @author tags
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cliassured.test.j21.docs;

// tag::imports[]
import java.nio.file.Path;
import java.nio.file.Paths;
import org.cliassured.sdkman.InstalledSdkman;
import org.cliassured.sdkman.Sdkman;
// end::imports[]
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

@DisabledOnOs(OS.WINDOWS)
public class SdkmanVersionTest {

    static final Path sdkmanHome = Paths.get("target/sdkman-" + UUID.randomUUID())
            .toAbsolutePath().normalize();

    @Test
    void version() {
        // @formatter:off
        // tag::version[]
        Sdkman
            // end::version[]
            .home(sdkmanHome)
            // tag::version[]
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
        // end::version[]
        // @formatter:on
    }
}
