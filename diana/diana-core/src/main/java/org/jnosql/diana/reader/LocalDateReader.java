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

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

/**
 * Class to reads and converts to {@link LocalDate} type
 *
 */
@SuppressWarnings("unchecked")
public final class LocalDateReader implements ValueReader {

    @Override
    public boolean isCompatible(Class clazz) {
        return LocalDate.class.equals(clazz);
    }

    @Override
    public <T> T read(Class<T> clazz, Object value) {

        if (LocalDate.class.isInstance(value)) {
            return (T) value;
        }

        if (Calendar.class.isInstance(value)) {
            return (T) ((Calendar) value).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }

        if (Date.class.isInstance(value)) {
            return (T) ((Date) value).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }

        if (Number.class.isInstance(value)) {
            return (T) new Date(((Number) value).longValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }

        return (T) LocalDate.parse(value.toString());
    }
}
