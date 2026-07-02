/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 CLI Assured contributors as indicated by the @author tags
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cliassured.mvn;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import org.cliassured.mvn.MavenSpec.ExcludeFromJacocoGeneratedReport;

/**
 * Entry methods for installing and invoking Maven.
 *
 * @author <a href="https://github.com/ppalaga">Peter Palaga</a>
 * @since  0.2.0
 */
public class Maven {
    @ExcludeFromJacocoGeneratedReport
    private Maven() {
    }

    /**
     * Create a new {@link Maven} of the given version.
     *
     * @param  version the Maven version to select, such as {@code 3.9.11}
     * @return         a new {@link Maven}
     *
     * @since          0.0.1
     */
    public static MavenSpec version(String version) {
        return new MavenSpec(version);
    }

    /**
     * Create a new {@link Maven} with the {@link #distributionUrl} looked up in
     * {@code .mvn/wrapper/maven-wrapper.properties}
     * relative to current directory or its nearest ancestor directory.
     * Equivalent to {@code Mvn.fromMvnw(Paths.get(".").toAbsolutePath().normalize())}.
     *
     * @return                       a new {@link Maven}
     * @throws IllegalStateException if {@code .mvn/wrapper/maven-wrapper.properties} cannot be found under any of the
     *                               ancestors
     *
     * @since                        0.0.1
     */
    @ExcludeFromJacocoGeneratedReport
    public static MavenSpec fromMvnw() {
        return fromMvnw(Paths.get(".").toAbsolutePath().normalize());
    }

    /**
     * Create a new {@link Maven} with the {@link #distributionUrl} looked up in
     * {@code .mvn/wrapper/maven-wrapper.properties}
     * relative to the given {@code directory} or its nearest ancestor directory.
     *
     * @param  directory             the directory where to start looking for {@code .mvn/wrapper/maven-wrapper.properties}
     * @return                       a new {@link Maven}
     * @throws IllegalStateException if {@code .mvn/wrapper/maven-wrapper.properties} cannot be found under any of the
     *                               ancestors
     *
     * @since                        0.0.1
     */
    @ExcludeFromJacocoGeneratedReport
    public static MavenSpec fromMvnw(Path directory) {
        return fromMvnw(directory, MavenSpec.findM2Directory());
    }

    /**
     * Create a new {@link Maven} with the {@link #distributionUrl} looked up in
     * {@code .mvn/wrapper/maven-wrapper.properties}
     * relative to current directory or its nearest ancestor directory.
     *
     * @param  directory             the directory where to start looking for {@code .mvn/wrapper/maven-wrapper.properties}
     * @param  m2Directory           a custom Maven user home directory instead of the default {@code ~/.m2}
     * @return                       a new {@link Maven}
     * @throws IllegalStateException if {@code .mvn/wrapper/maven-wrapper.properties} cannot be found under any of the
     *                               ancestors
     *
     * @since                        0.0.1
     */
    @ExcludeFromJacocoGeneratedReport
    public static MavenSpec fromMvnw(Path directory, Path m2Directory) {
        Path dir = directory;
        while (dir != null) {
            final Path wrapperProps = dir.resolve(".mvn/wrapper/maven-wrapper.properties");
            if (Files.isRegularFile(wrapperProps)) {
                Properties props = new Properties();
                try (InputStream in = Files.newInputStream(wrapperProps)) {
                    props.load(in);
                } catch (IOException e) {
                    throw new UncheckedIOException("Could not read " + wrapperProps, e);
                }
                return MavenSpec.fromDistributionUrl((String) props.get("distributionUrl"), m2Directory);
            }
            dir = dir.getParent();
        }
        throw new IllegalStateException(
                "Could not find .mvn/wrapper/maven-wrapper.properties in the parent hierarchy of " + directory);
    }
}
