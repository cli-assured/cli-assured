/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 CLI Assured contributors as indicated by the @author tags
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cliassured.test.j21.docs;

// tag::imports[]
import java.time.Duration;
import org.assertj.core.api.Assertions;
import org.cliassured.Await;
import org.cliassured.Await.LineAwait;
import org.cliassured.CliAssured;
import org.cliassured.CommandProcess;
// end::imports[]
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

public class AwaitLineTest {

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void awaitLine() {
        // @formatter:off
        // tag::snippet[]
        // Create an await condition that extracts a port number from a log line
        LineAwait<Integer> awaitPort = Await
            // Wait for a line matching this regex
            .lineMatching("listening on port: (\\d+)")
            // Map the first capturing group to an int
            .map(Integer::parseInt);

        try (CommandProcess proc = CliAssured
                .command("echo", "listening on port: 8080")
                .then()
                    .stdout()
                        // Register the await condition
                        .await(awaitPort)
                // Start the process without blocking
                .start()) {

            // Block until the condition is satisfied or the timeout is reached
            int port = awaitPort.await(Duration.ofSeconds(10));
            Assertions.assertThat(port).isEqualTo(8080);
        }
        // end::snippet[]
        // @formatter:on
    }

}
