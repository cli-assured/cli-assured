/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 CLI Assured contributors as indicated by the @author tags
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cliassured.test.j21.docs;

// tag::imports[]
import java.time.Duration;
import org.cliassured.CliAssured;
import org.cliassured.CommandProcess;
// end::imports[]
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

public class AutoCloseForciblyTest {

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void autoCloseForcibly() {
        // @formatter:off
        // tag::snippet[]
        // Start a long-running process using try-with-resources
        try (CommandProcess proc = CliAssured
                .command("sleep", "60")
                // Use forcible kill (SIGKILL) when closing instead of the default graceful kill (SIGTERM)
                .autoCloseForcibly()
                // Wait up to 5 seconds for the process to terminate after closing
                .autoCloseTimeout(Duration.ofSeconds(5))
                .then()
                    .exitCodeIsAnyOf(137, 143) // SIGKILL=137, SIGTERM=143
                .start()) {

            // Do some work while the process is running...

        } // The process is forcibly killed here when the try block exits
        // end::snippet[]
        // @formatter:on
    }

}
