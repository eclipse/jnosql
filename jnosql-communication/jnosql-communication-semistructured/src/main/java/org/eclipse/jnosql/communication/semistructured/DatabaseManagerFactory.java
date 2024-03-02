/*
 *  Copyright (c) 2024 Contributors to the Eclipse Foundation
 *   All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 * You may elect to redistribute this code under either of these licenses.
 *
 */
package org.eclipse.jnosql.communication.semistructured;

import java.util.function.Function;

/**
 * This interface represents a factory for creating database manager instances based on a database name.
 * It should throw a {@link NullPointerException} when the {@link String} parameter is null.
 *
 * <p>The {@link DatabaseManagerFactory} extends {@link java.util.function.Function}, indicating that it can be
 * used as a function to create database manager instances from database names.</p>
 *
 * <p>Implementations of this interface are expected to provide the necessary logic to instantiate appropriate
 * {@link DatabaseManager} instances based on the provided database name.</p>
 *
 * <p>Additionally, this interface extends {@link AutoCloseable}, indicating that implementations may manage
 * resources that require cleanup. The {@link #close()} method should be called to release any resources
 * held by the factory.</p>
 *
 * @see DatabaseManager
 */
public interface DatabaseManagerFactory extends Function<String, DatabaseManager>, AutoCloseable {


    /**
     * Closes any resources held by the factory.
     *
     * <p>Note: Some databases may not perform any specific actions upon closing.</p>
     */
    void close();
}
