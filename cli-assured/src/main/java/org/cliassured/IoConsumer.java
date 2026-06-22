/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 CLI Assured contributors as indicated by the @author tags
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cliassured;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * Much like a {@link Consumer}, but throwing an {@link IOException} from {@link #accept(Object)}.
 *
 * @author     <a href="https://github.com/ppalaga">Peter Palaga</a>
 * @since      0.2.0
 * @param  <T> the type of the operation input
 */
@FunctionalInterface
public interface IoConsumer<T> {

    /**
     * Perform this operation on the given {@code t}.
     *
     * @param  t           the input argument
     * @throws IOException
     * @since              0.2.0
     */
    void accept(T t) throws IOException;
}
