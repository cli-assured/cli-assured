/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 CLI Assured contributors as indicated by the @author tags
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cliassured.test.j21.docs;

// tag::imports[]
import org.cliassured.CliAssured;
// end::imports[]
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

public class SendStringToStdinTest {

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void sendStringToStdin() {
        // @formatter:off
        // tag::snippet[]
        CliAssured
            // cat with no arguments reads from stdin
            .command("cat")
            // Pass a string to the process' stdin using UTF-8 encoding
            .stdin("Hello from stdin!")
            .then()
                .stdout()
                    // The string appears in stdout
                    .hasLines("Hello from stdin!")
            .execute()
            .assertSuccess();
        // end::snippet[]
        // @formatter:on
    }

}
