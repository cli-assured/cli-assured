/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 CLI Assured contributors as indicated by the @author tags
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cliassured.test.j21.docs;

// tag::imports[]
import java.nio.file.Path;
import org.assertj.core.api.Assertions;
import org.cliassured.CliAssured;
// end::imports[]
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

public class RedirectToFileTest {

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void redirectToFile() throws IOException {
        // @formatter:off
        // tag::snippet[]
        Path outFile = Path.of("target/out.txt");

        CliAssured
            .command("echo", "Hello World!")
            .then()
                .stdout()
                    // Redirect stdout to a file
                    .redirect(outFile)
            .execute()
            .assertSuccess();

        // Verify the file contains the output
        Assertions.assertThat(outFile).content().contains("Hello World!");
        // end::snippet[]
        // @formatter:on
    }

}
