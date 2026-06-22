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

public class StderrToStdoutTest {

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void stderrToStdout() {
        // @formatter:off
        // tag::snippet[]
        CliAssured
            // Run a command that writes to stderr
            .command("sh", "-c", "echo 'error message' >&2")
            // Redirect stderr to stdout so it can be asserted via stdout()
            .stderrToStdout()
            .then()
                .stdout()
                    // The stderr output is now available through stdout
                    .hasLines("error message")
            .execute()
            .assertSuccess();
        // end::snippet[]
        // @formatter:on
    }

}
