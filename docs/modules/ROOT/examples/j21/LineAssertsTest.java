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

public class LineAssertsTest {

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void hasLines() {
        // @formatter:off
        // tag::hasLines[]
        CliAssured
            .command("echo", "Hello\nWorld")
            .then()
                .stdout()
                    // Fail unless both lines are present (any order)
                    .hasLines("Hello", "World")
            .execute()
            .assertSuccess();
        // end::hasLines[]
        // @formatter:on
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void doesNotHaveLines() {
        // @formatter:off
        // tag::doesNotHaveLines[]
        CliAssured
            .command("echo", "Hello")
            .then()
                .stdout()
                    // Fail if the line "Goodbye" is present
                    .doesNotHaveLines("Goodbye")
            .execute()
            .assertSuccess();
        // end::doesNotHaveLines[]
        // @formatter:on
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void hasLinesContaining() {
        // @formatter:off
        // tag::hasLinesContaining[]
        CliAssured
            .command("echo", "Hello World!")
            .then()
                .stdout()
                    // Fail unless some line contains "World"
                    .hasLinesContaining("World")
            .execute()
            .assertSuccess();
        // end::hasLinesContaining[]
        // @formatter:on
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void doesNotHaveLinesContaining() {
        // @formatter:off
        // tag::doesNotHaveLinesContaining[]
        CliAssured
            .command("echo", "Hello World!")
            .then()
                .stdout()
                    // Fail if any line contains "foo" or "bar"
                    .doesNotHaveLinesContaining("foo", "bar")
            .execute()
            .assertSuccess();
        // end::doesNotHaveLinesContaining[]
        // @formatter:on
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void hasLinesContainingCaseInsensitive() {
        // @formatter:off
        // tag::hasLinesContainingCaseInsensitive[]
        CliAssured
            .command("echo", "Hello World!")
            .then()
                .stdout()
                    // Case-insensitive substring match
                    .hasLinesContainingCaseInsensitive("hello")
            .execute()
            .assertSuccess();
        // end::hasLinesContainingCaseInsensitive[]
        // @formatter:on
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void hasLinesMatching() {
        // @formatter:off
        // tag::hasLinesMatching[]
        CliAssured
            .command("echo", "Hello World!")
            .then()
                .stdout()
                    // Fail unless some line matches the regex
                    .hasLinesMatching("Hello .*")
            .execute()
            .assertSuccess();
        // end::hasLinesMatching[]
        // @formatter:on
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void doesNotHaveLinesMatching() {
        // @formatter:off
        // tag::doesNotHaveLinesMatching[]
        CliAssured
            .command("echo", "Hello World!")
            .then()
                .stdout()
                    // Fail if any line matches the regex
                    .doesNotHaveLinesMatching("Error.*")
            .execute()
            .assertSuccess();
        // end::doesNotHaveLinesMatching[]
        // @formatter:on
    }

}
