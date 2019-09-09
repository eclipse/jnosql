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

package org.eclipse.jnosql.diana.reader;


import jakarta.nosql.ValueReader;

/**
 * Class to reads and converts to {@link Byte}, first it verify if is Double if yes return itself then verifies if is
 * {@link Number} and use {@link Number#byteValue()} otherwise convert to {@link String} and then {@link Byte}
 */
@SuppressWarnings("unchecked")
public final class ByteReader implements ValueReader {

    @Override
    public <T> boolean isCompatible(Class<T> clazz) {
        return Byte.class.equals(clazz) || byte.class.equals(clazz);
    }

    @Override
    public <T> T read(Class<T> clazz, Object value) {

        if (Byte.class.isInstance(value)) {
            return (T) value;
        }
        if (Number.class.isInstance(value)) {
            return (T) Byte.valueOf(Number.class.cast(value).byteValue());
        } else {
            return (T) Byte.valueOf(value.toString());
        }
    }
}
