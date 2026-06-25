/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 CLI Assured contributors as indicated by the @author tags
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cliassured.test.j21.docs;

// tag::imports[]
import java.util.concurrent.Executors;
import org.cliassured.CliAssured;
// end::imports[]
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

public class ThreadPoolCustomTest {

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void customThreadPool() {
        // @formatter:off
        // tag::custom[]
        CliAssured
            .command("echo", "Hello!")
            // Supply a custom ExecutorService
            .threadPool(Executors::newCachedThreadPool)
            .execute()
            .assertSuccess();
        // end::custom[]
        // @formatter:on
    }

}
