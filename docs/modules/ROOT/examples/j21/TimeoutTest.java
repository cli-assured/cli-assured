/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 CLI Assured contributors as indicated by the @author tags
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cliassured.test.j21.docs;

// tag::imports[]
import java.time.Duration;
import org.cliassured.CliAssured;
// end::imports[]
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

public class TimeoutTest {

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void executeWithDuration() {
        // @formatter:off
        // tag::snippet[]
        CliAssured
            .command("echo", "Hello World!")
            .then()
                .stdout()
                    .hasLinesContaining("Hello")
            // Wait at most 10 seconds for the process to terminate
            .execute(Duration.ofSeconds(10))
            .assertSuccess();
        // end::snippet[]
        // @formatter:on
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void executeWithMillis() {
        // @formatter:off
        // tag::millis[]
        CliAssured
            .command("echo", "Hello World!")
            .then()
                .stdout()
                    .hasLinesContaining("Hello")
            // Wait at most 10000 milliseconds
            .execute(10_000)
            .assertSuccess();
        // end::millis[]
        // @formatter:on
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void assertTimeout() {
        // @formatter:off
        // tag::assertTimeout[]
        CliAssured
            // A command that would run for 60 seconds
            .command("sleep", "60")
            .then()
                .stdout()
                    .isEmpty()
            // Wait at most 100 ms
            .execute(Duration.ofMillis(100))
            // Assert that the process timed out
            .assertTimeout();
        // end::assertTimeout[]
        // @formatter:on
    }

}
