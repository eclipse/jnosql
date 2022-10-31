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

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

/**
 * Class to reads and converts to {@link OffsetDateTime} type
 *
 */
public class OffsetDateTimeReader implements ValueReader {

    @Override
    public boolean test(Class<?> type) {
        return OffsetDateTime.class.equals(type);
    }

    @Override
    public <T> T read(Class<T> typeClass, Object value) {
        return (T) getOffSetDateTime(value);
    }

    private OffsetDateTime getOffSetDateTime(Object value) {
        if (OffsetDateTime.class.isInstance(value)) {
            return OffsetDateTime.class.cast(value);
        }

        if (Calendar.class.isInstance(value)) {
            return ((Calendar) value).toInstant().atZone(ZoneId.systemDefault()).toOffsetDateTime();
        }

        if (Date.class.isInstance(value)) {
            return ((Date) value).toInstant().atZone(ZoneId.systemDefault()).toOffsetDateTime();
        }

        if (Number.class.isInstance(value)) {
            return new Date(((Number) value).longValue()).toInstant().atZone(ZoneId.systemDefault()).toOffsetDateTime();
        }

        return OffsetDateTime.parse(value.toString());
    }
}
