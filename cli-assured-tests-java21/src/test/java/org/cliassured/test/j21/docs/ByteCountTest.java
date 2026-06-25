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

public class ByteCountTest {

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void hasByteCount() {
        // @formatter:off
        // tag::snippet[]
        CliAssured
            .command("echo", "Hi")
            .then()
                .stdout()
                    // Assert using a custom predicate
                    .hasByteCount(
                        count -> count > 0,
                        "Expected more than 0 bytes but found %d bytes")
            .execute()
            .assertSuccess();
        // end::snippet[]
        // @formatter:on
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void isEmpty() {
        // @formatter:off
        // tag::isEmpty[]
        CliAssured
            // The true command produces no output
            .command("true")
            .then()
                .stdout()
                    // Fail unless stdout is empty (0 bytes)
                    .isEmpty()
            .execute()
            .assertSuccess();
        // end::isEmpty[]
        // @formatter:on
    }

}
