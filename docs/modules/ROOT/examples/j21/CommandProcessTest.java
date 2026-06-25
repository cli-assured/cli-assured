/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 CLI Assured contributors as indicated by the @author tags
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cliassured.test.j21.docs;

// tag::imports[]
import java.time.Duration;
import java.util.stream.LongStream;
import org.assertj.core.api.Assertions;
import org.cliassured.CliAssured;
import org.cliassured.CommandProcess;
// end::imports[]
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

public class CommandProcessTest {

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void kill() {
        // @formatter:off
        // tag::kill[]
        // Start a long-running process
        CommandProcess proc = CliAssured
            .command("sleep", "60")
            .then()
                // SIGTERM yields exit code 143, SIGKILL yields 137
                .exitCodeIsAnyOf(143, 137)
            .start();

        // Kill the process gracefully (SIGTERM), without killing descendants
        proc.kill(false, false);

        // Await termination after the kill signal
        proc.awaitTermination()
            .assertSuccess();
        // end::kill[]
        // @formatter:on
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void killForcibly() {
        // @formatter:off
        // tag::killForcibly[]
        CommandProcess proc = CliAssured
            .command("sleep", "60")
            .then()
                .exitCodeIsAnyOf(137)
            .start();

        // Kill the process forcibly (SIGKILL) including all descendant processes
        proc.kill(true, true);

        proc.awaitTermination()
            .assertSuccess();
        // end::killForcibly[]
        // @formatter:on
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void pid() {
        // @formatter:off
        // tag::pid[]
        try (CommandProcess proc = CliAssured
                .command("sleep", "5")
                .then()
                    .exitCodeIsAnyOf(143, 137)
                .start()) {

            // Get the OS process ID
            long pid = proc.pid();
            Assertions.assertThat(pid).isPositive();
        }
        // end::pid[]
        // @formatter:on
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void childrenAndDescendants() {
        // @formatter:off
        // tag::children[]
        // Start a shell that spawns a child process
        try (CommandProcess proc = CliAssured
                .command("bash", "-c", "sleep 30 & wait")
                // Allow time for the process to terminate after closing
                .autoCloseTimeout(Duration.ofSeconds(5))
                .then()
                    .exitCodeIsAnyOf(0, 143, 137)
                .start()) {

            // Wait briefly for the child process to spawn
            try { Thread.sleep(500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

            // children() returns PIDs of direct child processes
            LongStream children = proc.children();
            Assertions.assertThat(children.toArray()).isNotEmpty();

            // descendants() returns PIDs of all direct and indirect child processes
            LongStream descendants = proc.descendants();
            Assertions.assertThat(descendants.toArray()).isNotEmpty();
        }
        // end::children[]
        // @formatter:on
    }

}
