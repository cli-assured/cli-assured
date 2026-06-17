/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 CLI Assured contributors as indicated by the @author tags
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cliassured;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.cliassured.CliAssertUtils.ExcludeFromJacocoGeneratedReport;

/**
 * Information about {@code stdout} or {@code stderr} of the executed command.
 *
 * @author <a href="https://github.com/ppalaga">Peter Palaga</a>
 * @since  0.1.0
 */
public class StreamResult {
    static final StreamResult EMPTY = emptyResult();
    private final long byteCount;
    final int lineCount;
    final Supplier<Stream<String>> lines;
    final Supplier<byte[]> bytes;

    StreamResult(int lineCount, Supplier<Stream<String>> lines, long byteCount, Supplier<byte[]> bytes) {
        this.lineCount = lineCount;
        this.lines = lines;
        this.byteCount = byteCount;
        this.bytes = bytes;
    }

    @ExcludeFromJacocoGeneratedReport
    private static StreamResult emptyResult() {
        return new StreamResult(0, Collections.<String> emptyList()::stream, 0, () -> new byte[0]);
    }

    /**
     * @return                       a {@link List} of lines captured from {@code stdout} or {@code stderr} of the
     *                               executed command
     * @throws IllegalStateException if {@link StreamExpectationsSpec#captureAll()} was not called on the associated stream
     * @since                        0.1.0
     */
    public Stream<String> lines() {
        return lines.get();
    }

    /**
     * @return the number of lines captured from {@code stdout} or {@code stderr} of the executed command
     * @since  0.1.0
     */
    public int lineCount() {
        return lineCount;
    }

    /**
     * @return                       the raw bytes captured from {@code stdout} or {@code stderr} of the executed command
     * @throws IllegalStateException if {@link StreamExpectationsSpec#captureAll()} was not called on the associated stream
     * @since                        0.1.0
     */
    public byte[] bytes() {
        return bytes.get();
    }

    /**
     * @return the number of bytes captured from {@code stdout} or {@code stderr} of the executed command
     * @since  0.1.0
     */
    public long byteCount() {
        return byteCount;
    }
}
