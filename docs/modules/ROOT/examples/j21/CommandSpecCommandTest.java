/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 CLI Assured contributors as indicated by the @author tags
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cliassured.test.j21.docs;

// tag::imports[]
import org.cliassured.CliAssured;
import org.cliassured.CommandSpec;
// end::imports[]
import org.junit.jupiter.api.Test;

public class CommandSpecCommandTest {

    @Test
    void command() {
        // @formatter:off
        // tag::command[]
        CliAssured
            // Call the echo command with two parameters "Hello" and "World!"
            .command("echo", "Hello", "World!")
            .execute()
            .assertSuccess();
        // end::command[]
        // @formatter:on
    }

    @Test
    void executableArgs() {
        // @formatter:off
        // tag::executableArgs[]

        // Set the executable echo
        CommandSpec echo = CliAssured.given().executable("echo");

        // Add arguments 1, 2 and 3
        for (int i = 1; i < 4; i++) {
            echo = echo.arg(String.valueOf(i));
            // note that all CLI Assured API methods return an adjusted copy,
            // so we need to assign the result of echo.arg(...) back to echo
        }

        echo
            .then()
                .stdout()
                    .log()
                    // ensure 1 2 3 is printed
                    .hasLines("1 2 3")
                    // ... and nothing else
                    .hasLineCount(1)
                .execute()
                .assertSuccess();
        // end::executableArgs[]
        // @formatter:on
    }

}
