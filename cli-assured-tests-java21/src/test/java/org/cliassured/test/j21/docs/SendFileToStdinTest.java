/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 CLI Assured contributors as indicated by the @author tags
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cliassured.test.j21.docs;

// tag::imports[]
import java.nio.file.Files;
import java.nio.file.Path;
import org.cliassured.CliAssured;
// end::imports[]
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.io.TempDir;

public class SendFileToStdinTest {

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void sendFileToStdin(@TempDir Path tempDir) throws IOException {
        // @formatter:off
        // tag::snippet[]
        // Create a file with some content
        Path inputFile = tempDir.resolve("input.txt");
        Files.writeString(inputFile, "Hello from file!");

        CliAssured
            // cat with no arguments reads from stdin
            .command("cat")
            // Pass the file contents to the process' stdin
            .stdin(inputFile)
            .then()
                .stdout()
                    // The file contents appear in stdout
                    .hasLines("Hello from file!")
            .execute()
            .assertSuccess();
        // end::snippet[]
        // @formatter:on
    }

}
