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
 * Class reader for {@link Number}, this converter first verify if the object is a Number instance,
 * if it will return itself, otherwise convert to String and then to {@link Double}
 */
@SuppressWarnings("unchecked")
public final class NumberValueReader implements ValueReader {

    @Override
    public <T> boolean isCompatible(Class<T> clazz) {
        return Number.class.equals(clazz);
    }

    @Override
    public <T> T read(Class<T> clazz, Object value) {
        if (Number.class.isInstance(value)) {
            return (T) value;
        } else {
            return (T) Double.valueOf(value.toString());
        }
    }


}
