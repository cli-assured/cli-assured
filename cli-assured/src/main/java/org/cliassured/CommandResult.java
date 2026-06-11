/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 CLI Assured contributors as indicated by the @author tags
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cliassured;

import java.time.Duration;
import org.cliassured.asserts.Assert;

/**
 * A result of a {@link CommandProcess}'s execution.
 *
 * @since  0.0.1
 * @author <a href="https://github.com/ppalaga">Peter Palaga</a>
 */
public class CommandResult {
    private final String command;
    private final int exitCode;
    private final Duration duration;
    private final StreamResult stdout;
    private final StreamResult stderr;
    private final Throwable exception;
    private final Assert outputAssert;

    CommandResult(
            String command,
            int exitCode,
            Duration runtimeMs,
            StreamResult stdout,
            StreamResult stderr,
            Throwable exception,
            Assert outputAssert) {
        super();
        this.command = command;
        this.exitCode = exitCode;
        this.duration = runtimeMs;
        this.stdout = stdout;
        this.stderr = stderr;
        this.exception = exception;
        this.outputAssert = outputAssert;
    }

    /**
     * Assert that the execution of the command was successful, namely that
     * <ul>
     * <li>No exception was thrown
     * <li>All assertions defined via {@link CommandSpec#then()} are satisfied
     * </ul>
     *
     * @return this {@link CommandResult}
     *
     * @since  0.0.1
     */
    public CommandResult assertSuccess() {
        Assert.FailureCollector collector = new Assert.FailureCollector(command);
        if (exception != null) {
            collector.exception(null, exception);
        }
        outputAssert.evaluate(collector);
        collector.assertSatisfied();
        return this;
    }

    /**
     * Assert that the execution of the command timed out in accordance with the timeout value passe via
     * {@link CommandProcess#awaitTermination(Duration)}, {@link CommandProcess#awaitTermination(long)},
     * {@link CommandSpec#execute(Duration)} or {@link CommandSpec#execute(long)}
     *
     * @return this {@link CommandResult}
     *
     * @since  0.0.1
     */
    public CommandResult assertTimeout() {
        if (!(exception instanceof TimeoutAssertionError)) {
            throw new AssertionError(
                    "Expected a timeout when running\n\n    " + command + "\n\nbut it terminated in " + duration
                            + " with exit code " + exitCode,
                    exception);
        }
        return this;
    }

    /**
     * @return the exit code if the underlying {@link Process}
     * @since  0.0.1
     */
    public int exitCode() {
        return exitCode;
    }

    /**
     * @return the duration of the command execution
     * @since  0.0.1
     */
    public Duration duration() {
        return duration;
    }

    /**
     * @return     the number of bytes produced on {@code stdout}
     * @since      0.0.1
     * @deprecated use {@code stdout().byteCount()}
     */
    @Deprecated(forRemoval = true)
    public long byteCountStdout() {
        return stdout.byteCount();
    }

    /**
     * @return     the number of bytes produced on {@code stderr}
     * @since      0.0.1
     * @deprecated use {@code stderr().byteCount()}
     */
    @Deprecated(forRemoval = true)
    public long byteCountStderr() {
        return stderr.byteCount();
    }

    /**
     * @return the {@link StreamResult} containing {@code stdout} result data of the executed command
     * @since  0.1.0
     */
    public StreamResult stdout() {
        return stdout;
    }

    /**
     * @return the {@link StreamResult} containing {@code stderr} result data of the executed command
     * @since  0.1.0
     */
    public StreamResult stderr() {
        return stderr;
    }

}
