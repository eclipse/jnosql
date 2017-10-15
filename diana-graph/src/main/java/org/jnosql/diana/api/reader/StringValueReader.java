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

package org.jnosql.diana.api.reader;


import org.jnosql.diana.api.ValueReader;

/**
 * Class to reads and converts to both {@link String} and {@link CharSequence}.
 */
@SuppressWarnings("unchecked")
public final class StringValueReader implements ValueReader {

    @Override
    public <T> boolean isCompatible(Class<T> clazz) {
        return CharSequence.class.equals(clazz) || String.class.equals(clazz);
    }

    @Override
    public <T> T read(Class<T> clazz, Object value) {

        boolean isClazzString = String.class.equals(clazz);

        if (CharSequence.class.equals(clazz) && CharSequence.class.isInstance(value)) {
            return (T) value;
        }
        return (T) value.toString();
    }


}
