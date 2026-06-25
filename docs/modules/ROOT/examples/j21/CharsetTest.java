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

public class CharsetTest {

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void charset() {
        // @formatter:off
        // tag::snippet[]
        CliAssured
            .command("echo", "Hello World!")
            .then()
                .stdout()
                    // Read stdout using ISO-8859-1 encoding
                    .charset(StandardCharsets.ISO_8859_1)
                    .hasLinesContaining("Hello")
            .execute()
            .assertSuccess();
        // end::snippet[]
        // @formatter:on
    }

}
