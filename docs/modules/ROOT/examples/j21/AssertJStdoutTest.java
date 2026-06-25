/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 CLI Assured contributors as indicated by the @author tags
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cliassured.test.j21.docs;

// tag::imports[]
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.cliassured.CliAssured;
import org.cliassured.CommandResult;
// end::imports[]
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

public class AssertJStdoutTest {

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void assertJOnStdout() {
        // @formatter:off
        // tag::snippet[]
        // Capture all lines and use AssertJ for assertions
        CommandResult result = CliAssured
            .command("echo", "Hello\nWorld")
            .then()
                .stdout()
                    // Capture all stdout lines in memory
                    .captureAll()
            .execute()
            .assertSuccess();

        // Access the captured lines and assert with AssertJ
        Stream<String> lines = result.stdout().lines();
        Assertions.assertThat(lines)
            .containsExactly("Hello", "World");
        // end::snippet[]
        // @formatter:on
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void assertJOnBytes() {
        // @formatter:off
        // tag::bytes[]
        CommandResult result = CliAssured
            .command("echo", "Hello")
            .then()
                .stdout()
                    // Capture all stdout output in memory
                    .captureAll()
            .execute()
            .assertSuccess();

        // Access the raw bytes
        byte[] bytes = result.stdout().bytes();
        Assertions.assertThat(new String(bytes)).contains("Hello");
        // end::bytes[]
        // @formatter:on
    }

}
