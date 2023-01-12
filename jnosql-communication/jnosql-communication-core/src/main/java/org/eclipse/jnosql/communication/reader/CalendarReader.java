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

import java.util.Calendar;
import java.util.Date;

/**
 * Class to reads and converts to {@link Calendar}, first it verify if is Calendar if yes return itself then verifies
 * if is {@link Long} and use {@link Calendar#setTimeInMillis(long)}} otherwise convert to {@link String}
 */
public final class CalendarReader implements ValueReader {

    @Override
    public boolean test(Class<?> type) {
        return Calendar.class.equals(type);
    }

    @Override
    public <T> T read(Class<T> type, Object value) {

        if (Calendar.class.isInstance(value)) {
            return (T) value;
        }

        if (Number.class.isInstance(value)) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis( ((Number) value).longValue());
            return (T) calendar;
        }

        if (Date.class.isInstance(value)) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((Date)value);
            return (T) calendar;
        }

        Date date = new Date(value.toString());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return (T) calendar;
    }
}
