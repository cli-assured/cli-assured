/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 CLI Assured contributors as indicated by the @author tags
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cliassured.test.j21.docs;

// tag::imports[]
import org.cliassured.CliAssured;
// end::imports[]
import org.junit.jupiter.api.Test;

public class HelloWorldTest {

    @Test
    void helloWorld() {
        // @formatter:off
        // tag::snippet[]
        CliAssured
            // Call the echo command with parameter "Hello World!"
            .command("echo", "Hello World!")
            .then()
                .stdout()
                    // Fail if there is no line equal to "Hello World!" in stdout
                    .hasLines("Hello World!")
                    // Fail if there is no line matching the given regular expression
                    .hasLinesMatching("Hello .*")
                    // Fail if foo or bar occur in stdout
                    .doesNotHaveLinesContaining("foo", "bar")
                    // Fail unless stdout has exactly one line
                    .hasLineCount(1)
                    // Possibly more asserts here
                    // Log everything received from stdout
                    .log()
            // Start the echo process on the operating system
            .execute()
            // Fail if "Hello World!" was not in stdout or exit code was not 0
            .assertSuccess();
        // end::snippet[]
        // @formatter:on
    }

}
