/*
 *
 *  Copyright (c) 2017 Otávio Santana and others
 *   All rights reserved. This program and the accompanying materials
 *   are made available under the terms of the Eclipse Public License v1.0
 *   and Apache License v2.0 which accompanies this distribution.
 *   The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *   and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *
 *   You may elect to redistribute this code under either of these licenses.
 *
 *   Contributors:
 *
 *   Otavio Santana
 *
 */

package org.jnosql.diana.api;

/**
 * To put your own Java Structure in NoSQL database is necessary convert it to a supported one.
 * So, the ValueWriter has the goal to convert to any specific structure type that a database might support.
 * These implementation will loaded by ServiceLoad and a NoSQL implementation will may use it.
 *
 * @param <T> current type
 * @param <S> the converted type
 */
public interface ValueWriter<T, S> {

    /**
     * verifies if the writer has support of instance from this class.
     *
     * @param <T>   the type
     * @param clazz - {@link Class} to be verified
     * @return true if the implementation is can support this class, otherwise false
     */
    <T> boolean isCompatible(Class<T> clazz);

    /**
     * Converts a specific structure to a new one.
     *
     * @param object the instance to be converted
     * @return a new instance with the new class
     */

    S write(T object);
}
