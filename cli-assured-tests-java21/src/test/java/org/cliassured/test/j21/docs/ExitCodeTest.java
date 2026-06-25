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

public class ExitCodeTest {

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void exitCodeIs() {
        // @formatter:off
        // tag::exitCodeIs[]
        CliAssured
            // The true command exits with code 0
            .command("true")
            .then()
                // Fail unless exit code is exactly 0
                .exitCodeIs(0)
            .execute()
            .assertSuccess();
        // end::exitCodeIs[]
        // @formatter:on
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void exitCodeIsAnyOf() {
        // @formatter:off
        // tag::exitCodeIsAnyOf[]
        CliAssured
            // The false command exits with code 1
            .command("false")
            .then()
                // Fail unless exit code is 0 or 1
                .exitCodeIsAnyOf(0, 1)
            .execute()
            .assertSuccess();
        // end::exitCodeIsAnyOf[]
        // @formatter:on
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void exitCodeSatisfies() {
        // @formatter:off
        // tag::exitCodeSatisfies[]
        CliAssured
            // The false command exits with code 1
            .command("false")
            .then()
                // Assert exit code with a custom predicate
                .exitCodeSatisfies(
                    code -> code >= 0,
                    "Expected non-negative exit code but got ${actual}")
            .execute()
            .assertSuccess();
        // end::exitCodeSatisfies[]
        // @formatter:on
    }

}
