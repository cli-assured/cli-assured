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

public class LineCountTest {

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void hasLineCount() {
        // @formatter:off
        // tag::snippet[]
        CliAssured
            .command("echo", "Line 1\nLine 2\nLine 3")
            .then()
                .stdout()
                    // Fail unless stdout has exactly 3 lines
                    .hasLineCount(3)
            .execute()
            .assertSuccess();
        // end::snippet[]
        // @formatter:on
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void hasLineCountPredicate() {
        // @formatter:off
        // tag::predicate[]
        CliAssured
            .command("echo", "Line 1\nLine 2\nLine 3")
            .then()
                .stdout()
                    // Assert using a custom predicate
                    .hasLineCount(
                        count -> count >= 2,
                        "Expected at least 2 lines in ${stream} but found ${actual} lines")
            .execute()
            .assertSuccess();
        // end::predicate[]
        // @formatter:on
    }

}
