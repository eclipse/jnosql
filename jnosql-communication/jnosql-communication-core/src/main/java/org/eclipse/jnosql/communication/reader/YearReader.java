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

import java.time.Year;

/**
 * Class to reads and converts to {@link Year}, first it verify if is Year if yes return itself
 * otherwise convert to {@link String} and then {@link Year}
 */
public final class YearReader implements ValueReader {

    @Override
    public boolean test(Class<?> type) {
        return Year.class.equals(type);
    }

    @Override
    public <T> T read(Class<T> clazz, Object value) {

        if (Year.class.isInstance(value)) {
            return (T) value;
        }
        return (T) Year.parse(value.toString());
    }
}
