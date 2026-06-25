/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 CLI Assured contributors as indicated by the @author tags
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cliassured.test.j21.docs;

// tag::imports[]
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import org.assertj.core.api.Assertions;
import org.cliassured.CliAssured;
// end::imports[]
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

public class RedirectToStreamTest {

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void redirectToOutputStream() {
        // @formatter:off
        // tag::snippet[]
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        CliAssured
            .command("echo", "Hello World!")
            .then()
                .stdout()
                    // Redirect stdout to an OutputStream
                    .redirect(baos)
            .execute()
            .assertSuccess();

        // CLI Assured does not close the stream; the caller is responsible
        Assertions.assertThat(baos.toString(StandardCharsets.UTF_8)).contains("Hello World!");
        // end::snippet[]
        // @formatter:on
    }

}
