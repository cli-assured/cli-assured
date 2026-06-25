/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 CLI Assured contributors as indicated by the @author tags
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cliassured.test.j21.docs;

// tag::imports[]
import org.cliassured.CliAssured;
import org.cliassured.CommandProcess;
import org.cliassured.CommandResult;
// end::imports[]
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

public class ExecuteAndStartTest {

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void execute() {
        // @formatter:off
        // tag::execute[]
        // execute() starts the process, waits for it to terminate
        // and returns a CommandResult
        CommandResult result = CliAssured
            .command("echo", "Hello World!")
            .then()
                .stdout()
                    .hasLines("Hello World!")
            .execute();

        // Check that all assertions passed
        result.assertSuccess();
        // end::execute[]
        // @formatter:on
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void startAndAwait() {
        // @formatter:off
        // tag::start[]
        // start() returns a running CommandProcess without blocking
        CommandProcess proc = CliAssured
            .command("echo", "Hello World!")
            .then()
                .stdout()
                    .hasLines("Hello World!")
            .start();

        // awaitTermination() blocks until the process exits
        CommandResult result = proc.awaitTermination();
        result.assertSuccess();
        // end::start[]
        // @formatter:on
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void startTryWithResources() {
        // @formatter:off
        // tag::tryWithResources[]
        // CommandProcess is AutoCloseable — close() kills the process
        try (CommandProcess proc = CliAssured
                .command("echo", "Hello World!")
                .then()
                    .stdout()
                        .hasLines("Hello World!")
                .start()) {

            // Interact with the running process here if needed
            proc.awaitTermination()
                .assertSuccess();
        }
        // end::tryWithResources[]
        // @formatter:on
    }

}
