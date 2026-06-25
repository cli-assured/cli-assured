/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 CLI Assured contributors as indicated by the @author tags
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cliassured.test.j21.docs;

// tag::imports[]
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.cliassured.CliAssured;
// end::imports[]
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

public class ReadFromStdoutTest {

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void readFromStdout() {
        // @formatter:off
        // tag::snippet[]
        // List.add() will be called in a different thread, so use a thread safe List
        List<String> collected = Collections.synchronizedList(new ArrayList<>());

        CliAssured
            .command("echo", "Hello\nWorld")
            .then()
                .stdout()
                    // Collect each line into a list
                    .lines(collected::add)
            .execute()
            .assertSuccess();

        Assertions.assertThat(collected).containsExactly("Hello", "World");
        // end::snippet[]
        // @formatter:on
    }

}
