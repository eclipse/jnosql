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

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Class to reads and converts to {@link Enum}
 *
 */
public final class EnumReader implements ValueReader {

    @Override
    public boolean test(Class<?> type) {
        return Enum.class.isAssignableFrom(type);
    }

    @Override
    public <T> T read(Class<T> type, Object value) {


        if (clazz.isInstance(value)) {
            return (T) value;
        }
        if (!Enum.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException("The informed class isn't an enum type: " + clazz);
        }
        List<Enum> elements = getEnumList((Class<Enum>) clazz);

        if (Number.class.isInstance(value)) {
            int index = Number.class.cast(value).intValue();
            return (T) elements.stream().filter(element -> element.ordinal() == index).findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("There is not index in enum to value: " + index));
        }
        String name = value.toString();
        return (T) elements.stream().filter(element -> element.name().equals(name)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("There isn't name in enum to value: " + name));
    }

    private <T> List<Enum> getEnumList(Class<Enum> clazz) {
        EnumSet enumSet = EnumSet.allOf(clazz);
        return new ArrayList<>(enumSet);
    }


}
