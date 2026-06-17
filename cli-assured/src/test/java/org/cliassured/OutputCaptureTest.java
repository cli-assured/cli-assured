/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 CLI Assured contributors as indicated by the @author tags
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cliassured;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.assertj.core.api.Assertions;
import org.cliassured.OutputCaptureSpec.LineOutputCapture;
import org.cliassured.OutputCaptureSpec.OutputCapture;
import org.cliassured.StreamExpectationsSpec.ProcessOutput;
import org.junit.jupiter.api.Test;

public class OutputCaptureTest {
    @Test
    void captureHead3Tail4() {
        final OutputCapture capture = new LineOutputCapture(3, 4, ProcessOutput.stdout);
        for (int i = 0; i < 10; i++) {
            capture.capture("Line " + i);
        }
        StringBuilder expected = new StringBuilder("stdout:\n");
        for (int i = 0; i < 3; i++) {
            expected.append("\n    Line ").append(i);
        }
        expected.append(
                "\n    ...\n    [3 lines omitted; set stdout().capture(maxHeadLines, maxTailLines) or stdout().captureAll() to capure more lines]\n    ...");
        for (int i = 6; i < 10; i++) {
            expected.append("\n    Line ").append(i);
        }
        Assertions.assertThat(capture.toString()).isEqualTo(expected.toString());
    }

    @Test
    void capture10LinesHead0Tail4() {
        final OutputCapture capture = new LineOutputCapture(0, 4, ProcessOutput.stdout);
        for (int i = 0; i < 10; i++) {
            capture.capture("Line " + i);
        }
        StringBuilder expected = new StringBuilder("stdout:\n");
        expected.append(
                "\n    [6 lines omitted; set stdout().capture(maxHeadLines, maxTailLines) or stdout().captureAll() to capure more lines]\n    ...");
        for (int i = 6; i < 10; i++) {
            expected.append("\n    Line ").append(i);
        }
        Assertions.assertThat(capture.toString()).isEqualTo(expected.toString());
    }

    @Test
    void capture2LinesHead0Tail4() {
        final OutputCapture capture = new LineOutputCapture(0, 4, ProcessOutput.stdout);
        for (int i = 0; i < 2; i++) {
            capture.capture("Line " + i);
        }
        StringBuilder expected = new StringBuilder("stdout:\n");
        for (int i = 0; i < 2; i++) {
            expected.append("\n    Line ").append(i);
        }
        Assertions.assertThat(capture.toString()).isEqualTo(expected.toString());
    }

    @Test
    void capture10LinesHead0Tail0() {
        final OutputCapture capture = new LineOutputCapture(0, 0, ProcessOutput.stdout);
        for (int i = 0; i < 10; i++) {
            capture.capture("Line " + i);
        }
        Assertions.assertThat(capture.toString()).isEqualTo("stdout: <no lines captured>");
    }

    @Test
    void capture6LinesHead4Tail4() {
        final OutputCapture capture = new LineOutputCapture(4, 4, ProcessOutput.stdout);
        StringBuilder expected = new StringBuilder("stdout:\n");
        for (int i = 0; i < 6; i++) {
            capture.capture("Line " + i);
            expected.append("\n    Line ").append(i);
        }
        Assertions.assertThat(capture.toString()).isEqualTo(expected.toString());
    }

    @Test
    void capture2LinesHead4Tail0() {
        final OutputCapture capture = new LineOutputCapture(4, 0, ProcessOutput.stdout);
        for (int i = 0; i < 2; i++) {
            capture.capture("Line " + i);
        }
        StringBuilder expected = new StringBuilder("stdout:\n");
        for (int i = 0; i < 2; i++) {
            expected.append("\n    Line ").append(i);
        }
        Assertions.assertThat(capture.toString()).isEqualTo(expected.toString());
    }

    @Test
    void capture6LinesHead4Tail0() {
        final OutputCapture capture = new LineOutputCapture(4, 0, ProcessOutput.stdout);
        for (int i = 0; i < 6; i++) {
            capture.capture("Line " + i);
        }
        StringBuilder expected = new StringBuilder("stdout:\n");
        for (int i = 0; i < 4; i++) {
            expected.append("\n    Line ").append(i);
        }
        expected.append(
                "\n    ...\n    [2 lines omitted; set stdout().capture(maxHeadLines, maxTailLines) or stdout().captureAll() to capure more lines]");
        Assertions.assertThat(capture.toString()).isEqualTo(expected.toString());
    }

    @Test
    void captureAll() throws IOException {
        final Charset charset = StandardCharsets.UTF_8;
        final OutputCapture capture = OutputCaptureSpec.captureAll(charset, ProcessOutput.stdout).build();

        StringBuilder expected = new StringBuilder("stdout:\n");
        String source = IntStream.range(0, 3)
                .peek(i -> expected.append("\n    Line ").append(i))
                .mapToObj(i -> "Line " + i)
                .collect(Collectors.joining("\n"));
        final byte[] sourceBytes = source.getBytes(charset);
        try (BufferedReader r = new BufferedReader(
                new InputStreamReader(capture.wrap(new ByteArrayInputStream(sourceBytes))))) {
            String line;
            while ((line = r.readLine()) != null) {
                capture.capture(line);
            }
        }

        Assertions.assertThat(capture.toString()).isEqualTo(expected.toString().trim());
        final StreamResult result = capture.result();
        Assertions.assertThat(result.lineCount).isEqualTo(4);
        Assertions.assertThat(result.lines.get().count()).isEqualTo(4);
        Assertions.assertThat(result.lines.get().collect(Collectors.toList())).containsExactly("Line 0", "Line 1", "Line 2",
                "Line 3");
        Assertions.assertThat(result.bytes.get()).isEqualTo(sourceBytes);
    }
}
