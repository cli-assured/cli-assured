/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 CLI Assured contributors as indicated by the @author tags
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cliassured.test.j21.docs;

// tag::imports[]
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.cliassured.CliAssured;
// end::imports[]
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

public class GetStdOutLinesTest {

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void getStdoutLines() {
        // @formatter:off
        // tag::snippet[]
        // Redirect `stderr` to `stdout` and get all output lines
        Stream<String> lines = CliAssured.command("echo", "Line 1\nLine 2").lines();
        Assertions.assertThat(lines).containsExactly("Line 1", "Line 2");
        // end::snippet[]

        // The above is a shorthand for
        Stream<String> alsoLines = CliAssured.command("echo", "Line 1\nLine 2")
            .stderrToStdout()
            .then()
                .stdout()
                    .captureAll()
            .execute()
            .assertSuccess()
            .stdout()
            .lines();
        Assertions.assertThat(alsoLines).containsExactly("Line 1", "Line 2");
        // end::snippet[]
        // @formatter:on
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void getStdoutLinesFull() {
        // @formatter:off
        // tag::full[]
        Stream<String> lines = CliAssured.command("echo", "Line 1\nLine 2")
            .stderrToStdout()
            .then()
                .stdout()
                    .captureAll()
            .execute()
            .assertSuccess()
            .stdout()
            .lines();
        Assertions.assertThat(lines).containsExactly("Line 1", "Line 2");
        // end::full[]
        // @formatter:on
    }

}
