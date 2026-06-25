/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 CLI Assured contributors as indicated by the @author tags
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cliassured.test.j21.docs;

// tag::imports[]
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.cliassured.CliAssured;
// end::imports[]
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

public class LogTest {

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void log() {
        // @formatter:off
        // tag::snippet[]
        CliAssured
            .command("echo", "Hello World!")
            .then()
                .stdout()
                    // Log each line at INFO level via org.cliassured.stdout logger
                    .log()
            .execute()
            .assertSuccess();
        // end::snippet[]
        // @formatter:on
    }

}
