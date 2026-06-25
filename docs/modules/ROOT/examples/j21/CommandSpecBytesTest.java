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

public class CommandSpecBytesTest {

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void bytes() {
        // @formatter:off
        // tag::snippet[]
        // bytes() executes the command, redirects stderr to stdout,
        // captures all output, asserts success, and returns raw bytes
        byte[] bytes = CliAssured
            .command("cat")
            .stdin(stdin -> stdin.write(new byte[] {(byte) 0xc0, (byte) 0xfe, (byte) 0xba, (byte) 0xbe}))
            .bytes();

        Assertions.assertThat(bytes).asHexString().isEqualTo("C0FEBABE");
        // end::snippet[]
        // @formatter:on
    }

}
