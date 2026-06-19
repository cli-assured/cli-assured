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

public class CdTest {

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void cd(@TempDir Path tempDir) throws IOException {
        // @formatter:off
        // tag::snippet[]
        // Create a file in a temporary directory
        Path myWorkDir = Files.createDirectory(tempDir.resolve("my-workdir"));
        Files.writeString(myWorkDir.resolve("hello.txt"), "Hello from my-workdir!");

        CliAssured
            .given()
                // Set the working directory for the command
                .cd(myWorkDir)
            .when()
                // cat resolves the file relative to the working directory we have set above
                .command("cat", "hello.txt")
            .then()
                .stdout()
                    // Ensure that the content of hello.txt is printed to stdout
                    .hasLines("Hello from my-workdir!")
            .execute()
            .assertSuccess();
        // end::snippet[]
        // @formatter:on
    }

}
