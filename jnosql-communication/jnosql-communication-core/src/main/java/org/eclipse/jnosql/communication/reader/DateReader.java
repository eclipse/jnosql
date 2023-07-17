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


import org.eclipse.jnosql.communication.ValueReader;

import java.util.Date;

/**
 * Class to reads and converts Date type
 */
public final class DateReader implements ValueReader {

    @Override
    public boolean test(Class<?> type) {
        return Date.class.equals(type);
    }

    @Override
    public <T> T read(Class<T> type, Object value) {

        if (value instanceof Date) {
            return (T) value;
        }

        if (value instanceof Number number) {
            return (T) new Date(number.longValue());
        }

        return (T) new Date(value.toString());
    }
}
