/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 CLI Assured contributors as indicated by the @author tags
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cliassured;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.cliassured.CliAssertUtils.ExcludeFromJacocoGeneratedReport;
import org.cliassured.StreamExpectationsSpec.ProcessOutput;

/**
 * A specification of what should be captured from {@code std[out|err]}
 *
 * @since  0.1.0
 * @author <a href="https://github.com/ppalaga">Peter Palaga</a>
 */
class OutputCaptureSpec {
    static final int DEFAULT_CAPTURE_SIZE = 16;

    private final int maxHead;
    private final int maxTail;
    private final Charset charset;
    private final StreamExpectationsSpec.ProcessOutput stream;

    static OutputCaptureSpec defaultCapture(Charset charset, ProcessOutput stream) {
        return new OutputCaptureSpec(DEFAULT_CAPTURE_SIZE, DEFAULT_CAPTURE_SIZE, charset, stream);
    }

    OutputCapture build() {
        if (capturesAll()) {
            return new ByteOutputCapture(charset, stream);
        }
        return new LineOutputCapture(maxHead, maxTail, stream);
    }

    static OutputCaptureSpec captureAll(Charset charset, ProcessOutput stream) {
        return new OutputCaptureSpec(-1, -1, charset, stream);
    }

    OutputCaptureSpec(int maxHead, int maxTail, Charset charset, ProcessOutput stream) {
        this.maxHead = maxHead;
        this.maxTail = maxTail;
        this.charset = charset;
        this.stream = stream;
    }

    boolean capturesAll() {
        return maxHead < 0;
    }

    OutputCaptureSpec charset(Charset charset) {
        return new OutputCaptureSpec(maxHead, maxTail, charset, stream);
    }

    /**
     * A facility for recording output lines for the sake of failure reporting
     *
     * @since 0.0.1
     */
    interface OutputCapture {
        static LineOutputCapture noCapture(ProcessOutput stream) {
            return new LineOutputCapture(0, 0, stream);
        }

        /**
         * Capture the given {@code line} if there is enough capacity in this {@link OutputCapture}.
         *
         * @param line the line to capture
         * @since      0.0.1
         */
        void capture(String line);

        /**
         * Optionally wrap the given {@link InputStream} to store the bytes for later reporting or other kind of usages
         *
         * @param  input the {@link InputStream} to wrap
         * @return       {@code input} or a new {@link InputStream} wrapping {@code input}
         * @since        0.1.0
         */
        InputStream wrap(InputStream input);

        /**
         * Seal this {@link OutputCapture} and return the {@link StreamResult}
         *
         * @return a possibly cached {@link StreamResult}
         * @since  0.1.0
         */
        StreamResult result();

        /**
         * Append the captured content to the given {@link StringBuilder}
         *
         * @param  stringBuilder a {@link StringBuilder} to which the captured content should be appended
         * @return               the {@code stringBuilder} instance
         * @since                0.1.0
         */
        StringBuilder toString(StringBuilder stringBuilder);

    }

    /**
     * An {@link OutputCapture} storing the content in a byte array. Handy when capturing the whole output.
     */
    static class ByteOutputCapture implements OutputCapture {
        static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

        private final Charset charset;
        private final StreamExpectationsSpec.ProcessOutput stream;

        private int lineCount = 0;
        private final Object lock = new Object();
        private final SparseByteArrayOutputStream writableBuffer = new SparseByteArrayOutputStream();
        private StreamResult sealed;

        private ByteOutputCapture(Charset charset, ProcessOutput stream) {
            this.charset = charset;
            this.stream = stream;
        }

        @Override
        public void capture(String line) {
            synchronized (lock) {
                lineCount++;
            }
        }

        @Override
        public InputStream wrap(InputStream input) {
            return new TeeInputStream(input, writableBuffer);
        }

        @Override
        public StringBuilder toString(StringBuilder sb) {
            StreamResult result = result();
            if (result.lineCount() == 0) {
                sb.append(stream.name()).append(": <no output>");
            } else {
                sb.append(stream.name()).append(":\n");
                result.lines().forEach(line -> sb.append("\n    ").append(line));
            }
            return sb;
        }

        @ExcludeFromJacocoGeneratedReport
        public String toString() {
            StringBuilder sb = new StringBuilder();
            toString(sb);
            return sb.toString();
        }

        @Override
        public StreamResult result() {
            synchronized (lock) {
                if (sealed == null) {
                    final byte[] bytes = writableBuffer.seal();
                    final Supplier<Stream<String>> lines;
                    if (bytes.length == 0) {
                        lines = () -> Stream.empty();
                    } else {
                        lines = () -> new BufferedReader(
                                new InputStreamReader(new ByteArrayInputStream(bytes), charset)).lines();
                    }
                    sealed = new StreamResult(lineCount, lines, bytes.length, () -> bytes);
                }
            }
            return sealed;
        }

        class SparseByteArrayOutputStream extends OutputStream {

            private byte[] buffer = EMPTY_BYTE_ARRAY;
            int byteCount = 0;

            public SparseByteArrayOutputStream() {
            }

            byte[] seal() {
                synchronized (lock) {
                    final byte[] buf = this.buffer;
                    this.buffer = null;
                    return buf;
                }
            }

            @ExcludeFromJacocoGeneratedReport
            private void ensureCapacity(int newCapacity) {
                if (newCapacity > buffer.length) {
                    buffer = Arrays.copyOf(buffer, newCapacity);
                }
            }

            @ExcludeFromJacocoGeneratedReport
            public void write(int b) {
                synchronized (lock) {
                    ensureCapacity(byteCount + 1);
                    buffer[byteCount] = (byte) b;
                    byteCount += 1;
                }
            }

            public void write(byte b[], int off, int len) {
                synchronized (lock) {
                    ensureCapacity(byteCount + len);
                    System.arraycopy(b, off, buffer, byteCount, len);
                    byteCount += len;
                }
            }

            @ExcludeFromJacocoGeneratedReport
            public void writeBytes(byte b[]) {
                synchronized (lock) {
                    write(b, 0, b.length);
                }
            }

            public void close() throws IOException {
            }

        }
    }

    /**
     * An {@link OutputCapture} storing some limited number of head and tail lines.
     *
     * @since 0.1.0
     */
    static class LineOutputCapture implements OutputCapture {
        private final int maxHead;
        private final int maxTail;
        private final StreamExpectationsSpec.ProcessOutput stream;
        private final Object lock = new Object();
        private final ByteCountOutputStream byteCounter = new ByteCountOutputStream();

        private int lineCount = 0;
        private StreamResult sealed;
        private List<String> headLines = new ArrayList<>();
        private List<String> tailLines;
        private int tailLinesCount = 0;

        @ExcludeFromJacocoGeneratedReport
        LineOutputCapture(int maxHead, int maxTail, ProcessOutput stream) {
            if (maxHead < 0) {
                throw new IllegalStateException("ByteOutputCapture should be used for maxHead < 0");
            }
            if (maxTail < 0) {
                throw new IllegalStateException("ByteOutputCapture should be used for maxTail < 0");
            }
            this.maxHead = maxHead;
            this.maxTail = maxTail;
            this.stream = stream;
        }

        @Override
        public void capture(String line) {
            synchronized (lock) {
                if (headLines.size() < maxHead) {
                    headLines.add(line);
                }
                if (lineCount >= maxHead && maxTail > 0) {
                    if (tailLines == null) {
                        tailLines = new ArrayList<>(maxTail);
                    }
                    final int index = tailLinesCount++ % maxTail;
                    if (index >= tailLines.size()) {
                        tailLines.add(line);
                    } else {
                        tailLines.set(index, line);
                    }
                }
                lineCount++;
            }
        }

        @Override
        public InputStream wrap(InputStream input) {
            return new TeeInputStream(input, byteCounter);
        }

        @Override
        @ExcludeFromJacocoGeneratedReport
        public StreamResult result() {
            synchronized (lock) {
                if (sealed == null) {
                    final String streamName = stream.name();
                    sealed = new StreamResult(lineCount, () -> {
                        throw new IllegalStateException(
                                "Call CliAssured.command(...).then()."
                                        + streamName + "().captureAll() to be able to retrieve all lines via CommandResult."
                                        + streamName + "().lines()");
                    },
                            byteCounter.byteCount,
                            () -> {
                                throw new IllegalStateException(
                                        "Call CliAssured.command(...).then()."
                                                + streamName
                                                + "().captureAll() to be able to retrieve all bytes via CommandResult."
                                                + streamName + "().bytes()");
                            });
                }
                return sealed;
            }
        }

        @ExcludeFromJacocoGeneratedReport
        public StringBuilder toString(StringBuilder sb) {
            int storedTailLines = Math.min(tailLinesCount, maxTail);
            if (lineCount == 0) {
                sb.append(stream.name()).append(": <no output>");
            } else if (headLines.isEmpty() && storedTailLines == 0) {
                sb.append(stream.name()).append(": <no lines captured>");
            } else {
                sb.append(stream.name()).append(":\n");
                for (String line : headLines) {
                    sb.append("\n    ").append(line);
                }
                int omitted = lineCount - headLines.size();
                if (storedTailLines > 0) {
                    omitted -= storedTailLines;
                }
                if (omitted > 0) {
                    if (headLines.size() > 0) {
                        sb.append("\n    ...");
                    }
                    sb
                            .append("\n    [")
                            .append(omitted).append(" lines omitted; set ")
                            .append(stream.name()).append("().capture(maxHeadLines, maxTailLines) or ")
                            .append(stream.name()).append("().captureAll() to capure more lines]");

                    if (storedTailLines > 0) {
                        sb.append("\n    ...");
                    }
                }
                if (storedTailLines > 0) {
                    for (int i = 0; i < storedTailLines; i++) {
                        sb.append("\n    ").append(tailLines.get((i + tailLinesCount) % storedTailLines));
                    }
                }
            }
            return sb;
        }

        @ExcludeFromJacocoGeneratedReport
        public String toString() {
            StringBuilder sb = new StringBuilder();
            toString(sb);
            return sb.toString();
        }

        class ByteCountOutputStream extends OutputStream {
            int byteCount;

            @ExcludeFromJacocoGeneratedReport
            public void write(int b) {
                synchronized (lock) {
                    byteCount += 1;
                }
            }

            public void write(byte b[], int off, int len) {
                synchronized (lock) {
                    byteCount += len;
                }
            }

        }
    }

    static class TeeInputStream extends InputStream {
        private final InputStream delegate;
        private final OutputStream buffer;

        public TeeInputStream(InputStream delegate, OutputStream buffer) {
            this.delegate = delegate;
            this.buffer = buffer;
        }

        @Override
        @ExcludeFromJacocoGeneratedReport
        public int read() throws IOException {
            int b = delegate.read();
            if (b != -1) {
                buffer.write(b);
            }
            return b;
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            int n = delegate.read(b, off, len);
            if (n > 0) {
                buffer.write(b, off, n);
            }
            return n;
        }

        @Override
        public void close() throws IOException {
            buffer.close();
            delegate.close();
        }
    }
}
