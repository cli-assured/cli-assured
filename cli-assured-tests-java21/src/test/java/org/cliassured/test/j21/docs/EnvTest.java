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

public class EnvTest {

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void envMultiple() {
        // @formatter:off
        // tag::multiple[]
        CliAssured
            .given()
                // Set multiple environment variables as name-value pairs
                .env("FIRST", "Hello",
                     "SECOND", "World")
            .when()
                // Use the environment variables that were set above
                .command("sh", "-c", "echo $FIRST $SECOND")
            .then()
                .stdout()
                    // Make sure the values of the variables are present in stdout
                    .hasLines("Hello World")
            .execute()
            .assertSuccess();
        // end::multiple[]
        // @formatter:on
    }

}
