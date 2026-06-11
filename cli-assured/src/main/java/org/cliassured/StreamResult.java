/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 CLI Assured contributors as indicated by the @author tags
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cliassured;

import java.util.List;
import org.cliassured.StreamExpectationsSpec.OutputCapture.OutputCaptureResult;

/**
 * Information about {@code stdout} or {@code stderr} of the executed command.
 *
 * @author <a href="https://github.com/ppalaga">Peter Palaga</a>
 * @since  0.1.0
 */
public class StreamResult {
    private final long byteCount;
    private final OutputCaptureResult capture;

    StreamResult(long byteCount, OutputCaptureResult capture) {
        this.byteCount = byteCount;
        this.capture = capture;
    }

    /**
     * @return                       a {@link List} of lines captured from {@code stdout} or {@code stderr} of the
     *                               executed command
     * @throws IllegalStateException if {@link StreamExpectationsSpec#captureAll()} was not called on the associated stream
     * @since                        0.1.0
     */
    public List<String> lines() {
        return capture.lines.get();
    }

    /**
     * @return the number of lines captured from {@code stdout} or {@code stderr} of the executed command
     * @since  0.1.0
     */
    public int lineCount() {
        return capture.lineCount;
    }

    /**
     * @return the number of bytes captured from {@code stdout} or {@code stderr} of the executed command
     * @since  0.1.0
     */
    public long byteCount() {
        return byteCount;
    }
}
