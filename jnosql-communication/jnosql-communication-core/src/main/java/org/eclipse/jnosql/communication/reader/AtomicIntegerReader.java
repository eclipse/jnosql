/*
 *
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.jnosql.communication.reader;


import jakarta.nosql.ValueReader;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class to reads and converts to {@link AtomicInteger}, first it verify if is AtomicInteger if yes return itself then
 * verifies if is {@link Number} and use {@link Number#intValue()} ()} otherwise convert to {@link String}
 * and then {@link AtomicInteger}
 */
public final class AtomicIntegerReader implements ValueReader {

    @Override
    public boolean test(Class<?> type) {
        return AtomicInteger.class.equals(type);
    }

    @Override
    public <T> T read(Class<T> clazz, Object value) {

        if (AtomicInteger.class.isInstance(value)) {
            return (T) value;
        }
        if (Number.class.isInstance(value)) {
            return (T) new AtomicInteger(Number.class.cast(value).intValue());
        } else {
            return (T) new AtomicInteger(Integer.parseInt(value.toString()));
        }
    }
}
