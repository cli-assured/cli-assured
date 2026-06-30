/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 CLI Assured contributors as indicated by the @author tags
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cliassured.test.j21.docs;

// tag::imports[]
import org.cliassured.mvn.Mvn;
// end::imports[]
import org.junit.jupiter.api.Test;

public class MvnTest {

    @Test
    void versionLiteral() {
        // @formatter:off
        // tag::snippet[]
        // Use Maven version 3.9.11 as available in ~/.m2/wrapper/dists/apache-maven-3.9.11/a2d47e15
        Mvn.version("3.9.11")
            // If ~/.m2/wrapper/dists/apache-maven-3.9.11/a2d47e15 does not exist,
            // download it from
            // https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.11/apache-maven-3.9.11-bin.zip
            // much like Maven Wrapper would do.
            // You can omit this, if you are sure the specified version is already installed
            // by running mvnw or by invoking installIfNeeded() or install().
            .installIfNeeded()
            // Mvn.version(String) and installIfNeeded() methods return a CommandSpec,
            // so the rest is a standard cli-assured code
            .args("--version")
            .then()
                .stdout()
                    .hasLines("Apache Maven 3.9.11 (3e54c93a704957b63ee3494413a2b544fd3d825b)")
            .execute()
            .assertSuccess();
        // end::snippet[]
        // @formatter:on
    }

    @Test
    void fromMvnw() {
        // @formatter:off
        // tag::fromMvnw[]
        // Find .mvn/wrapper/maven-wrapper.properties
        // under the nearest ancestor,
        // extract the distribution URL from there
        // find Maven version from the distribution URL
        // and use all of that to create a new Mvn instance
        Mvn.fromMvnw()
            // You can omit this, if you are sure mvnw was run before
            .installIfNeeded()
            // Mvn.fromMvnw() and installIfNeeded() methods return a CommandSpec,
            // so the rest is a standard cli-assured code
            .args("--version")
            .then()
                .stdout()
                    .hasLines("Apache Maven 3.9.11 (3e54c93a704957b63ee3494413a2b544fd3d825b)")
            .execute()
            .assertSuccess();
        // end::fromMvnw[]
        // @formatter:on
    }

}
