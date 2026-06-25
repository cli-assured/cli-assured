/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 CLI Assured contributors as indicated by the @author tags
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cliassured.test.j21.docs;

// tag::imports[]
import org.assertj.core.api.Assertions;
import org.cliassured.CliAssured;
// end::imports[]
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

public class AssertJLinesTest {

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void assertJOnIndividualLines() {
        // @formatter:off
        // tag::snippet[]
        CliAssured
            .command("echo", "Hello World!")
            .then()
                .stdout()
                    // Use lines() to assert each individual line with AssertJ
                    .lines(line -> Assertions.assertThat(line).doesNotContain("ERROR"))
            .execute()
            .assertSuccess();
        // end::snippet[]
        // @formatter:on
    }

}
