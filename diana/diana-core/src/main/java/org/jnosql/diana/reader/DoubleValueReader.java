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


import org.jnosql.diana.ValueReader;

/**
 * Class to reads and converts to {@link Double}, first it verify if is Double if yes return itself then verifies if is
 * {@link Number} and use {@link Number#doubleValue()} otherwise convert to {@link String} and then {@link Double}
 */
@SuppressWarnings("unchecked")
public final class DoubleValueReader implements ValueReader {

    @Override
    public <T> boolean isCompatible(Class<T> clazz) {
        return Double.class.equals(clazz) || double.class.equals(clazz);
    }

    @Override
    public <T> T read(Class<T> clazz, Object value) {

        if (Double.class.isInstance(value)) {
            return (T) value;
        }
        if (Number.class.isInstance(value)) {
            return (T) Double.valueOf(Number.class.cast(value).doubleValue());
        } else {
            return (T) Double.valueOf(value.toString());
        }
    }
}
