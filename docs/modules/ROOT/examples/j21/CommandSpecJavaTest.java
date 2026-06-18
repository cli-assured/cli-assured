/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 CLI Assured contributors as indicated by the @author tags
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cliassured.test.j21.docs;

// tag::imports[]
import org.cliassured.CliAssured;
// end::imports[]
import org.junit.jupiter.api.Test;

public class CommandSpecJavaTest {

    @Test
    void javaVersion() {
        // @formatter:off
        // tag::javaVersion[]
        CliAssured
            .java()               // Use the java executable of the current JVM
                .args("-version")
            .then()
                .stderr()         // java -version prints to stderr
                    .hasLinesContaining(System.getProperty("java.version"))
            .execute()
            .assertSuccess();
        // end::javaVersion[]
        // @formatter:on
    }

}
