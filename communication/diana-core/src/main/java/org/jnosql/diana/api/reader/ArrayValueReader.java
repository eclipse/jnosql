/*
 *
 *  Copyright (c) 2019 Otávio Santana and others
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
import org.jnosql.diana.api.ValueReaderDecorator;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


/**
 * Class to reads and converts to array
 */
public class ArrayValueReader implements ValueReader {

    private static final transient ValueReader SERVICE_PROVIDER = ValueReaderDecorator.getInstance();

    @Override
    public <T> boolean isCompatible(Class<T> clazz) {
        return clazz.isArray();
    }

    @Override
    public <T> T read(Class<T> clazz, Object value) {
        Class<?> elementType = clazz.getComponentType();
        List<Object> values = convertValue(value);
        Object[] newArray = (Object[]) Array.newInstance(elementType, values.size());

        for (int index = 0; index < values.size(); index++) {
            newArray[index] = SERVICE_PROVIDER.read(elementType, values.get(index));
        }

        return (T) newArray;
    }

    private List<Object> convertValue(Object value) {
        if (value instanceof Iterable) {
            return (List<Object>) StreamSupport.stream(Iterable.class.cast(value).spliterator(),
                    false).collect(Collectors.toList());
        } else if (value.getClass().isArray()) {
            return Arrays.asList(value);
        } else {
            return Collections.singletonList(value);
        }
    }
}
