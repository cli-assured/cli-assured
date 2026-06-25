/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 CLI Assured contributors as indicated by the @author tags
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cliassured.test.j21.docs;

// tag::imports[]
import org.assertj.core.api.Assertions;
import org.cliassured.CliAssured;
import org.cliassured.CommandResult;
// end::imports[]
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

public class CaptureTest {

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void captureHeadAndTail() {
        // @formatter:off
        // tag::snippet[]
        CommandResult result = CliAssured
            .command("echo", "Line 1\nLine 2\nLine 3")
            .then()
                .stdout()
                    // Capture at most 2 head and 2 tail lines
                    .capture(2, 2)
            .execute()
            .assertSuccess();

        Assertions.assertThat(result.stdout().lineCount()).isEqualTo(3);
        // end::snippet[]
        // @formatter:on
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void captureAll() {
        // @formatter:off
        // tag::captureAll[]
        CommandResult result = CliAssured
            .command("echo", "Line 1\nLine 2\nLine 3")
            .then()
                .stdout()
                    // Capture all lines in memory
                    .captureAll()
            .execute()
            .assertSuccess();

        // All lines are available via lines()
        Assertions.assertThat(result.stdout().lines())
            .containsExactly("Line 1", "Line 2", "Line 3");
        // end::captureAll[]
        // @formatter:on
    }

}
