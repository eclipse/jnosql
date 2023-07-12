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

import java.time.OffsetTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

/**
 * Class to reads and converts to {@link OffsetTime} type
 *
 */
public class OffsetTimeReader implements ValueReader {

    @Override
    public boolean test(Class<?> type) {
        return OffsetTime.class.equals(type);
    }

    @Override
    public <T> T read(Class<T> typeClass, Object value) {

        return (T) getOffSetDateTime(value);
    }

    private OffsetTime getOffSetDateTime(Object value) {
        if (OffsetTime.class.isInstance(value)) {
            return OffsetTime.class.cast(value);
        }

        if (value instanceof Calendar calendar) {
            return calendar.toInstant().atZone(ZoneId.systemDefault())
                    .toOffsetDateTime().toOffsetTime();
        }

        if (Date.class.isInstance(value)) {
            return ((Date) value).toInstant().atZone(ZoneId.systemDefault())
                    .toOffsetDateTime()
                    .toOffsetTime();
        }

        if (value instanceof Number number) {
            return new Date(number.longValue()).toInstant().atZone(ZoneId.systemDefault())
                    .toOffsetDateTime()
                    .toOffsetTime();
        }

        return OffsetTime.parse(value.toString());
    }
}
