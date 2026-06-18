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
                .command("echo", "Hello World!")   // Call the echo command with parameter "Hello World!"
                .then()
                    .stdout()
                        .hasLines("Hello World!")  // Fail if there is no line equal to "Hello World!" in stdout
                        .hasLinesMatching("Hello .*") // Fail if there is no line matching the given regular expression
                        .doesNotHaveLinesContaining("foo", "bar") // Fail if foo or bar occur in stdout
                        .hasLineCount(1)          // Fail unless stdout has exactly one line
                                                  // Possibly more asserts here
                        .log()                    // Log everything received from stdout
                .execute()                        // Start the echo process on the operating system
                .assertSuccess();                 // Fail if "Hello World!" was not in stdout or exit code was not 0
        // end::snippet[]
        // @formatter:on
    }

}
