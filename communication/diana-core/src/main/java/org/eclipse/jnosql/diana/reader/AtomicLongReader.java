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

import java.util.concurrent.atomic.AtomicLong;

/**
 * Class to reads and converts to {@link AtomicLong}, first it verify if is Double if yes return itself then verifies
 * if is {@link Number} and use {@link Number#longValue()} otherwise convert to {@link String}
 * and then {@link AtomicLong}
 */
public final class AtomicLongReader implements ValueReader {

    @Override
    public boolean test(Class<?> type) {
        return AtomicLong.class.equals(type);
    }

    @Override
    public <T> T read(Class<T> clazz, Object value) {

        if (AtomicLong.class.isInstance(value)) {
            return (T) value;
        }
        if (Number.class.isInstance(value)) {
            return (T) new AtomicLong(Number.class.cast(value).longValue());
        } else {
            return (T) new AtomicLong(Long.valueOf(value.toString()));
        }
    }

}
