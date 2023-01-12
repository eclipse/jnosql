/*
 *
 *  Copyright (c) 2023 Contributors to the Eclipse Foundation
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
package org.eclipse.jnosql.communication;


import java.util.function.Predicate;

/**
 * This interface represents the reader on the {@link Value} instance.
 * When the Value needs a conversion, it will load the implementations from SPI, Java Service Provider.
 * The {@link Predicate} verifies if the reader has the support the class type.
 * @see Value
 * @see Value#get(Class)
 * @see ValueWriter
 */
public interface ValueReader extends Predicate<Class<?>> {

    /**
     * Converts the value to the class type target.
     *
     * @param type  - the new instance class
     * @param value - instance to be converted
     * @param <T>   - the type class
     * @return an instance converted in the proper type
     */
    <T> T read(Class<T> type, Object value);

}
