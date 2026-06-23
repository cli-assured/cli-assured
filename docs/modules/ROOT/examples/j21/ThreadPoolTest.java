/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 CLI Assured contributors as indicated by the @author tags
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cliassured.test.j21.docs;

// tag::imports[]
import java.time.Duration;
import org.cliassured.CliAssured;
// end::imports[]
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

public class ThreadPoolTest {

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void localThreadPool() {
        // @formatter:off
        // tag::local[]
        CliAssured
            .command("echo", "Hello!")
            .threadPool()                         // Use a per-command thread pool
                .coreSize(2)                      // 2 core threads
                .maxSize(4)                       // Up to 4 threads
                .keepAlive(Duration.ofSeconds(30)) // Idle threads removed after 30s
            .execute()
            .assertSuccess();
        // end::local[]
        // @formatter:on
    }

}
