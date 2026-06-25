/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 CLI Assured contributors as indicated by the @author tags
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cliassured.test.j21.docs;

// tag::imports[]
import java.time.Duration;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.cliassured.CliAssured;
import org.cliassured.CommandResult;
// end::imports[]
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

public class CommandResultTest {

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void assertSuccess() {
        // @formatter:off
        // tag::assertSuccess[]
        // assertSuccess() checks that no exceptions occurred
        // and all assertions passed
        CliAssured
            .command("echo", "Hello")
            .then()
                .stdout()
                    .hasLines("Hello")
            .execute()
            .assertSuccess();
        // end::assertSuccess[]
        // @formatter:on
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void assertTimeout() {
        // @formatter:off
        // tag::assertTimeout[]
        CliAssured
            .command("sleep", "60")
            .then()
                .stdout()
                    .isEmpty()
            // Wait at most 100 ms
            .execute(Duration.ofMillis(100))
            // Assert that the process was killed due to timeout
            .assertTimeout();
        // end::assertTimeout[]
        // @formatter:on
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void exitCode() {
        // @formatter:off
        // tag::exitCode[]
        CommandResult result = CliAssured
            .command("true")
            .execute();

        // Access the exit code of the terminated process
        int exitCode = result.exitCode();
        Assertions.assertThat(exitCode).isZero();
        // end::exitCode[]
        // @formatter:on
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void duration() {
        // @formatter:off
        // tag::duration[]
        CommandResult result = CliAssured
            .command("sleep", "0.01")
            .execute()
            .assertSuccess();

        // Access the wall-clock duration of the command execution
        Duration duration = result.duration();
        Assertions.assertThat(duration).isPositive();
        // end::duration[]
        // @formatter:on
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void stdoutLines() {
        // @formatter:off
        // tag::stdoutLines[]
        CommandResult result = CliAssured
            .command("echo", "Hello\nWorld")
            .then()
                .stdout()
                    // captureAll() is required for lines() and bytes()
                    .captureAll()
            .execute()
            .assertSuccess();

        // Access captured stdout lines as a Stream
        Stream<String> lines = result.stdout().lines();
        Assertions.assertThat(lines).containsExactly("Hello", "World");
        // end::stdoutLines[]
        // @formatter:on
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void stdoutBytes() {
        // @formatter:off
        // tag::stdoutBytes[]
        CommandResult result = CliAssured
            .command("echo", "Hello")
            .then()
                .stdout()
                    // captureAll() is required for lines() and bytes()
                    .captureAll()
            .execute()
            .assertSuccess();

        // Access captured stdout output as raw bytes
        byte[] bytes = result.stdout().bytes();
        Assertions.assertThat(new String(bytes)).contains("Hello");
        // end::stdoutBytes[]
        // @formatter:on
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void lineCountAndByteCount() {
        // @formatter:off
        // tag::counts[]
        CommandResult result = CliAssured
            .command("echo", "Hello\nWorld")
            .then()
                .stdout()
                    .hasLinesContaining("Hello")
            .execute()
            .assertSuccess();

        // lineCount() and byteCount() are always available
        // without captureAll()
        int lineCount = result.stdout().lineCount();
        long byteCount = result.stdout().byteCount();

        Assertions.assertThat(lineCount).isEqualTo(2);
        Assertions.assertThat(byteCount).isGreaterThan(0);
        // end::counts[]
        // @formatter:on
    }

}
