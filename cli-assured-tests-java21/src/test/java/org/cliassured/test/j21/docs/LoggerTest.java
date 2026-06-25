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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerTest {

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void logCustom() {
        // @formatter:off
        // tag::custom[]
        Logger log = LoggerFactory.getLogger("my.custom.logger");

        CliAssured
            .command("sh", "-c",
                     "echo 'Hello from stdout'; echo 'Hello from stderr' 1>&2")
            .then()
                .stdout()
                    // Log stdout lines on INFO level
                    .log(log::info)
                .stderr()
                    // Log stderr lines on ERROR level
                    .log(log::error)
            .execute()
            .assertSuccess();
        // end::custom[]
        // @formatter:on
    }

}
