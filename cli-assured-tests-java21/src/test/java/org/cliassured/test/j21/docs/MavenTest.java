/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 CLI Assured contributors as indicated by the @author tags
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cliassured.test.j21.docs;

// tag::imports[]
import java.util.regex.Pattern;
import org.cliassured.maven.InstalledMaven;
import org.cliassured.maven.Maven;
// end::imports[]
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.regex.Matcher;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class MavenTest {

    @Test
    void versionLiteral() {
        // @formatter:off
        // tag::snippet[]
        final String version = "3.9.16";
        Maven.version(version)
            // Reuse the installation under ~/.m2/wrapper/dists,
            // or download the requested version from Maven Central.
            .installIfNeeded()
            // Obtain a CommandSpec with bin/mvn[.cmd] set as executable
            .mvn()
            // Call `mvn[.cmd] --version`
            .args("--version")
            .then()
                .stdout()
                    .hasLinesMatching("Apache Maven\\s+" + Pattern.quote(version) + "(?:\\s|$)")
            .execute()
            .assertSuccess();
        // end::snippet[]
        // @formatter:on
    }

    @Test
    void fromMvnw() throws IOException {
        final String expectedVersion = readWrapperVersion(Paths.get("..").toAbsolutePath().normalize());
        // @formatter:off
        // tag::fromMvnw[]
        // Find .mvn/wrapper/maven-wrapper.properties
        // under the nearest ancestor of the current directory,
        // extract the distribution URL from there
        // find Maven version from the distribution URL
        // and install that version if needed.
        InstalledMaven maven = Maven.fromMvnw().installIfNeeded();
        maven
            // Obtain a CommandSpec with bin/mvn[.cmd] set as executable
            .mvn()
            // Call `mvn[.cmd] --version`
            .args("--version")
            .then()
                .stdout()
                    .hasLinesMatching("Apache Maven\\s+" + Pattern.quote(maven.version()) + "(?:\\s|$)")
            .execute()
            .assertSuccess();
        // end::fromMvnw[]
        // @formatter:on
        Assertions.assertThat(maven.version()).isEqualTo(expectedVersion);
    }

    private static String readWrapperVersion(Path projectRoot) throws IOException {
        final Properties properties = new Properties();
        try (InputStream in = Files.newInputStream(projectRoot.resolve(".mvn/wrapper/maven-wrapper.properties"))) {
            properties.load(in);
        }
        final String distributionUrl = properties.getProperty("distributionUrl");
        Assertions.assertThat(distributionUrl).as("Wrapper distribution URL in %s", projectRoot).isNotNull();
        final Matcher matcher = Pattern.compile("/apache-maven-(.+)-bin\\.zip$").matcher(distributionUrl);
        Assertions.assertThat(matcher.find()).as("Maven version in %s", distributionUrl).isTrue();
        return matcher.group(1);
    }

}
