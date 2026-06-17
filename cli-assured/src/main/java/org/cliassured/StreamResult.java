/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 CLI Assured contributors as indicated by the @author tags
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cliassured;

import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Information about {@code stdout} or {@code stderr} of the executed command.
 *
 * @author <a href="https://github.com/ppalaga">Peter Palaga</a>
 * @since  0.1.0
 */
public class StreamResult {
    private final long byteCount;
    private final int lineCount;
    private final Supplier<Stream<String>> lines;
    private final Supplier<byte[]> bytes;

    StreamResult(int lineCount, Supplier<Stream<String>> lines, long byteCount, Supplier<byte[]> bytes) {
        this.lineCount = lineCount;
        this.lines = lines;
        this.byteCount = byteCount;
        this.bytes = bytes;
    }

    /**
     * @return                       a {@link Stream} of lines captured from {@code stdout} or {@code stderr} of the
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
     * Returns the raw bytes captured from {@code stdout} or {@code stderr} of the executed command.
     * <p>
     * For the given command execution, this method always returns the same byte array instance.
     * Mutating the array will mutate it for all subsequent callers.
     *
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
