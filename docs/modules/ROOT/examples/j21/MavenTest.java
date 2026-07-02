/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 CLI Assured contributors as indicated by the @author tags
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cliassured.test.j21.docs;

// tag::imports[]
import org.cliassured.maven.Maven;
// end::imports[]
import org.junit.jupiter.api.Test;

public class MavenTest {

    @Test
    void versionLiteral() {
        // @formatter:off
        // tag::snippet[]
        // Use Maven version 3.9.11 as available in ~/.m2/wrapper/dists/apache-maven-3.9.11/a2d47e15
        Maven.version("3.9.11")
            // If ~/.m2/wrapper/dists/apache-maven-3.9.11/a2d47e15 does not exist,
            // download it from
            // https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.11/apache-maven-3.9.11-bin.zip
            // much like Maven Wrapper would do.
            .installIfNeeded()
            // Obtain a CommandSpec with bin/mvn[.cmd] set as executable
            .mvn()
            // Call `mvn[.cmd] --version`
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
        // under the nearest ancestor of the current directory,
        // extract the distribution URL from there
        // find Maven version from the distribution URL
        // and use that information to create a new Mvn instance
        Maven.fromMvnw()
            .installIfNeeded()
            // Obtain a CommandSpec with bin/mvn[.cmd] set as executable
            .mvn()
            // Call `mvn[.cmd] --version`
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
