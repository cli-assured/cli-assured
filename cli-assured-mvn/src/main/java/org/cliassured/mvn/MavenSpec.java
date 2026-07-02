/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 CLI Assured contributors as indicated by the @author tags
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cliassured.mvn;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.slf4j.LoggerFactory;

/**
 * A specification of a Maven installation
 *
 * @author <a href="https://github.com/ppalaga">Peter Palaga</a>
 * @since  0.0.1
 */
public class MavenSpec {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(MavenSpec.class);
    private static final Pattern MAVEN_URL_PATH_PATTERN = Pattern.compile("/apache-maven-(.*)-bin\\.zip$");
    private static final Pattern MAVEN_CORE_PATTERN = Pattern.compile("^maven-core-(.*)\\.jar$");
    private static final int BUFFER_SIZE = 8192;

    private final String version;
    private final Path m2Directory;
    private final Path home;
    private final String distributionUrl;

    MavenSpec(String version) {
        this.version = Objects.requireNonNull(version, "version");
        this.m2Directory = findM2Directory();
        this.distributionUrl = defaultDistributionUrl(version);
        this.home = null;
    }

    MavenSpec(String version, Path m2Directory, Path home, String downloadUrl) {
        this.version = Objects.requireNonNull(version, "version");
        this.m2Directory = m2Directory;
        this.home = home;
        this.distributionUrl = downloadUrl;
    }

    /**
     * Set a different Maven user home directory instead of the default {@code ~/.m2}.
     *
     * @param  m2Directory the {@code .m2} directory path
     * @return             an adjusted copy of this {@link MavenSpec}
     * @since              0.0.1
     */
    public MavenSpec m2Directory(Path m2Directory) {
        return new MavenSpec(version, m2Directory, home, distributionUrl);
    }

    /**
     * Set Maven home instead of the default that is computed based on the {@link #version(String)} or
     * {@link #distributionUrl(String)}.
     * Maven home is the directory containing {@code bin/mvn[.cmd]}.
     *
     * @param  m2Directory the {@code .m2} directory path
     * @return             an adjusted copy of this {@link MavenSpec}
     * @since              0.0.1
     */
    public MavenSpec home(Path home) {
        return new MavenSpec(version, m2Directory, home, distributionUrl);
    }

    /**
     * @return the Maven home directory of this {@link MavenSpec} - the one containing {@code bin/mvn[.cmd]}
     * @since  0.0.1
     */
    public Path home() {
        return home != null ? home : findDefaultHome(version, m2Directory, distributionUrl);
    }

    /**
     * Set the distribution URL from which {@link MavenSpec} can be installed instead of the default that is computed based
     * on the {@link #version(String)} using the template
     * {@code https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/<version>/apache-maven-<version>-bin.zip}
     *
     * @param  distributionUrl the distribution URL
     * @return                 an adjusted copy of this {@link MavenSpec}
     * @since                  0.0.1
     */
    public MavenSpec distributionUrl(String distributionUrl) {
        return new MavenSpec(version, m2Directory, home, distributionUrl);
    }

    /**
     * Install this Maven version or fail if the version is installed already.
     *
     * @return                       a possibly new {@link MavenSpec} instance pointing at the freshly installed Maven
     *                               version
     * @throws IllegalStateException if this Maven version is installed already
     * @since                        0.0.1
     */
    @ExcludeFromJacocoGeneratedReport
    public InstalledMaven install() {
        final Path home = home();
        if (Files.exists(home)) {
            throw new IllegalStateException(
                    "Cannot download " + distributionUrl + " to " + home + " because it exists already");
        }
        log.info("Downloading " + distributionUrl + " to " + home);

        try {
            Files.createDirectories(home);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not create " + home, e);
        }
        final Path localFile = home.resolve(UUID.randomUUID() + ".zip");
        try (InputStream in = new URL(distributionUrl).openStream()) {
            Files.copy(in, localFile);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not download " + distributionUrl + " to " + home, e);
        }
        final String actualSha512 = sha512(localFile);
        final String expectedSha512 = dowloadText(distributionUrl + ".sha512", 512);

        if (!actualSha512.equals(expectedSha512)) {
            throw new AssertionError(
                    "Could not verify " + localFile + " downloaded from " + distributionUrl + ": expected SHA-512 "
                            + expectedSha512 + " but found " + actualSha512);
        }
        try (ZipFile zipFile = ZipFile.builder().setPath(localFile).setBufferSize(BUFFER_SIZE).get()) {
            Enumeration<ZipArchiveEntry> entries = zipFile.getEntries();

            while (entries.hasMoreElements()) {
                ZipArchiveEntry entry = entries.nextElement();

                Path entryPath = Paths.get(entry.getName());
                final int cnt = entryPath.getNameCount();
                if (cnt > 1) {
                    entryPath = entryPath.subpath(1, cnt);
                    Path newFile = home.resolve(entryPath).normalize();
                    if (!newFile.startsWith(home)) {
                        throw new AssertionError("Zip entry " + newFile + " attempted to write outside of " + home);
                    }
                    log.debug("Unpacking " + newFile);
                    if (entry.isDirectory()) {
                        Files.createDirectories(newFile);
                    } else if (entry.isUnixSymlink()) {
                        final Path linkTarget = newFile.getParent()
                                .resolve(zipFile.getUnixSymlink(entry))
                                .normalize();
                        if (!linkTarget.startsWith(home)) {
                            throw new AssertionError(
                                    "Zip symlink entry " + newFile + " -> " + linkTarget + " points outside of " + home);
                        }
                        Files.createSymbolicLink(newFile, linkTarget);
                    } else {
                        Files.createDirectories(newFile.getParent());
                        try (InputStream is = zipFile.getInputStream(entry)) {
                            Files.copy(is, newFile, StandardCopyOption.REPLACE_EXISTING);
                        }
                    }

                    final Date lastModified = entry.getLastModifiedDate();
                    if (lastModified != null) {
                        Files.setLastModifiedTime(newFile, FileTime.fromMillis(lastModified.getTime()));
                    }

                    /* Restore POSIX permissions */
                    restorePermissions(entry, newFile);
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Could not unzip " + distributionUrl + " to " + home, e);
        } finally {
            if (Files.exists(localFile)) {
                try {
                    Files.delete(localFile);
                } catch (IOException e) {
                    throw new UncheckedIOException("Could not delete " + localFile, e);
                }
            }
        }
        return new InstalledMaven(version, home);
    }

    /**
     * @return                this {@link MavenSpec}
     * @throws AssertionError if this Maven version is not installed yet
     */
    @ExcludeFromJacocoGeneratedReport
    public InstalledMaven assertInstalled() {
        final Path home = home();
        if (!Files.isDirectory(home)) {
            throw new AssertionError("Maven " + version + " is not installed in " + home
                    + " (directory does not exist). You may want to set Mvn.home(Path) or call Mvn.installIfNeeded()");
        }

        final Path executable = new InstalledMaven(version, home).mvnPath();
        if (!Files.isRegularFile(executable)) {
            throw new AssertionError("Maven " + version + " is not installed in " + home
                    + " (bin/mvn[.cmd] does not exist). You may want to set Mvn.home(Path) or call Mvn.installIfNeeded()");
        }
        return new InstalledMaven(version, home);
    }

    /**
     * @return {@code true} if this Maven version can be found under {@code ~/.m2/wrapper/dists}; {@code false} otherwise
     * @since  0.0.1
     */
    @ExcludeFromJacocoGeneratedReport
    public boolean isInstalled() {
        final Path home = home();
        return Files.isDirectory(home) && Files.isRegularFile(new InstalledMaven(version, home).mvnPath());
    }

    /**
     * Install this Maven version unless it is installed already.
     *
     * @return a possibly new {@link MavenSpec} instance pointing at the freshly installed Maven version
     * @since  0.0.1
     */
    public InstalledMaven installIfNeeded() {
        if (!isInstalled()) {
            return install();
        }
        return new InstalledMaven(version, home());
    }

    @ExcludeFromJacocoGeneratedReport
    static MavenSpec fromDistributionUrl(String distributionUrl, Path m2Directory) {
        final Path distsDir = m2Directory.resolve("wrapper/dists");
        final String hash = hashString(distributionUrl);
        final Optional<Path> mavenHome;
        if (Files.isDirectory(distsDir)) {
            try (Stream<Path> versionDirs = Files.list(distsDir)) {
                mavenHome = versionDirs
                        .map(vd -> vd.resolve(hash))
                        .filter(Files::isDirectory)
                        .findFirst();
            } catch (IOException e) {
                throw new UncheckedIOException("Could not list " + distsDir, e);
            }
        } else {
            mavenHome = Optional.empty();
        }
        final String version;
        if (!mavenHome.isPresent()) {
            final Matcher m = MAVEN_URL_PATH_PATTERN.matcher(distributionUrl);
            if (m.find()) {
                version = m.group(1);
            } else {
                throw new IllegalStateException("Cannot extract Maven version from distribution URL " + distributionUrl
                        + " because it does not match " + MAVEN_CORE_PATTERN.pattern());
            }
        } else {
            version = findVersion(mavenHome.get());
        }

        return new MavenSpec(version, m2Directory, mavenHome.orElse(null), distributionUrl);
    }

    static String defaultDistributionUrl(String version) {
        return "https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/" + version + "/apache-maven-" + version
                + "-bin.zip";
    }

    @ExcludeFromJacocoGeneratedReport
    static String findVersion(Path mavenHome) {
        final Path libDir = mavenHome.resolve("lib");
        try (Stream<Path> libs = Files.list(libDir)) {
            Optional<String> version = libs
                    .map(p -> MAVEN_CORE_PATTERN.matcher(p.getFileName().toString()))
                    .filter(Matcher::matches)
                    .map(matcher -> matcher.group(1))
                    .findFirst();
            if (version.isPresent()) {
                return version.get();
            } else {
                throw new IllegalStateException("Could not find maven-core-*.jar in " + libDir);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Could not list " + libDir, e);
        }
    }

    static Path findDefaultHome(String version, Path m2Directory, String downloadUrl) {
        return Stream.<Supplier<Path>> of(
                () -> m2Directory.resolve("wrapper/dists/apache-maven-" + version),
                () -> m2Directory.resolve("wrapper/dists/apache-maven-" + version + "-bin"))
                .map(Supplier::get)
                .filter(Files::isDirectory)
                .map(versionDir -> versionDir.resolve(hashString(downloadUrl)))
                .filter(Files::isDirectory)
                .findFirst()
                .orElseGet(() -> m2Directory.resolve("wrapper/dists/apache-maven-" + version + "/" + hashString(downloadUrl)));
    }

    @ExcludeFromJacocoGeneratedReport
    static Path findM2Directory() {
        final String muh = System.getenv("MAVEN_USER_HOME");
        if (muh != null) {
            return Paths.get(muh);
        }
        return Paths.get(System.getProperty("user.home") + "/.m2");
    }

    @ExcludeFromJacocoGeneratedReport
    static void restorePermissions(ZipArchiveEntry entry, Path path) throws IOException {
        int unixMode = entry.getUnixMode();
        if (unixMode == 0) {
            return;
        }

        if (Files.getFileStore(path).supportsFileAttributeView(PosixFileAttributeView.class)) {
            Set<PosixFilePermission> perms = PosixFilePermissions.fromString(
                    permissionString(unixMode));
            Files.setPosixFilePermissions(path, perms);
        }
    }

    static String permissionString(int mode) {
        StringBuilder sb = new StringBuilder(9);
        int[] masks = { 0400, 0200, 0100, 0040, 0020, 0010, 0004, 0002, 0001 };
        for (int m : masks) {
            sb.append((mode & m) != 0 ? permissionChar(m) : '-');
        }
        return sb.toString();
    }

    @ExcludeFromJacocoGeneratedReport
    static char permissionChar(int mask) {
        switch (mask) {
        case 0400:
        case 0040:
        case 0004:
            return 'r';
        case 0200:
        case 0020:
        case 0002:
            return 'w';
        case 0100:
        case 0010:
        case 0001:
            return 'x';
        default:
            return '-';
        }
    }

    @ExcludeFromJacocoGeneratedReport
    static String dowloadText(String url, int expectedByteSize) {
        ByteArrayOutputStream out = new ByteArrayOutputStream(expectedByteSize);
        try (InputStream in = new URL(url).openStream()) {
            final byte[] buff = new byte[Math.min(expectedByteSize, BUFFER_SIZE)];
            int bytesRead;
            while ((bytesRead = in.read(buff)) >= 0) {
                out.write(buff, 0, bytesRead);
            }
            return new String(out.toByteArray(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not download " + url, e);
        }
    }

    @ExcludeFromJacocoGeneratedReport
    static String sha512(Path file) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            try (InputStream in = Files.newInputStream(file)) {
                byte[] buffer = new byte[1024 * 1024];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    md.update(buffer, 0, read);
                }
            }

            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder(digest.length * 2);
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Could not compute SHA-512 has for " + file, e);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not compute SHA-512 has for " + file, e);
        }
    }

    @ExcludeFromJacocoGeneratedReport
    static String hashString(String s) {
        if (s == null) {
            s = "";
        }
        long h = 0L;
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        for (byte b : bytes) {
            int code = b & 0xFF;
            h = (h * 31 + code) & 0xFFFF_FFFFL;
        }
        return Long.toHexString(h);
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.CONSTRUCTOR, ElementType.METHOD })
    public @interface ExcludeFromJacocoGeneratedReport {
    }
}
