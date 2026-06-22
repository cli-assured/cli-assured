/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 CLI Assured contributors as indicated by the @author tags
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cliassured.test.j21.docs;

// tag::imports[]
import java.nio.charset.StandardCharsets;
import org.cliassured.CliAssured;
// end::imports[]
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

public class WriteToStdinTest {

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void writeToStdin() {
        // @formatter:off
        // tag::snippet[]
        CliAssured
            // cat with no arguments reads from stdin
            .command("cat")
            // Write bytes to the process' stdin
            .stdin(out -> {
                out.write("Hello from stdin!".getBytes(StandardCharsets.UTF_8));
            })
            .then()
                .stdout()
                    // The data written to stdin appears in stdout
                    .hasLines("Hello from stdin!")
            .execute()
            .assertSuccess();
        // end::snippet[]
        // @formatter:on
    }

}
