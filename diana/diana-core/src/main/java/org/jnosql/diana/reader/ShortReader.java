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

package org.jnosql.diana.reader;


import jakarta.nosql.ValueReader;

/**
 * Class to reads and converts to {@link Short}, first it verify if is Double if yes return itself then verifies if is
 * {@link Number} and use {@link Number#shortValue()} otherwise convert to {@link String} and then {@link Short}
 */
@SuppressWarnings("unchecked")
public final class ShortReader implements ValueReader {

    @Override
    public <T> boolean isCompatible(Class<T> clazz) {
        return Short.class.equals(clazz) || short.class.equals(clazz);
    }

    @Override
    public <T> T read(Class<T> clazz, Object value) {

        if (Short.class.isInstance(value)) {
            return (T) value;
        }
        if (Number.class.isInstance(value)) {
            return (T) Short.valueOf(Number.class.cast(value).shortValue());
        } else {
            return (T) Short.valueOf(value.toString());
        }
    }
}
