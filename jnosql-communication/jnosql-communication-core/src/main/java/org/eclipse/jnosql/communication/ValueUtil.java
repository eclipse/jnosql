/*
 *  Copyright (c) 2024 Contributors to the Eclipse Foundation
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
 */
package org.eclipse.jnosql.communication;



import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

/**
 * Utilitarian class to {@link Value}
 */
public final class ValueUtil {
    private static final String PARAM_CLASS_NAME = "org.eclipse.jnosql.communication.ParamValue";
    @SuppressWarnings("rawtypes")
    private static final ValueWriter VALUE_WRITER = ValueWriterDecorator.getInstance();
    @SuppressWarnings("rawtypes")
    private static final Function CONVERT = o -> {
        if (o instanceof Value) {
            return convert(Value.class.cast(o));
        }
        return getObject(o);
    };

    private ValueUtil() {
    }

    /**
     * converter a {@link Value} to Object
     *
     * @param value the value
     * @return a object converted
     */
    public static Object convert(Value value) {
        Objects.requireNonNull(value, "value is required");
        Object val = value.get();
        if (val instanceof Iterable) {
            return getObjects(val);
        }
        return getObject(val);
    }


    /**
     * Converts the {@link Value} to {@link List}
     *
     * @param value the value
     * @return a list object
     */
    @SuppressWarnings("unchecked")
    public static List<Object> convertToList(Value value) {
        Objects.requireNonNull(value, "value is required");
        Object val = value.get();
        if (val instanceof Iterable) {
            List<Object> items = new ArrayList<>();
            Iterable.class.cast(val).forEach(items::add);
            if (items.size() == 1) {
                Object item = items.get(0);
                //check if it is dynamic params
                if (PARAM_CLASS_NAME.equals(item.getClass().getName())) {
                    Object params = Value.class.cast(item).get();
                    if (params instanceof Iterable) {
                        return getObjects(Iterable.class.cast(params));
                    } else {
                        return Collections.singletonList(getObject(params));
                    }
                }
            }
            return (List<Object>) items.stream().map(CONVERT).collect(toList());

        }
        return Collections.singletonList(getObject(val));
    }

    @SuppressWarnings("unchecked")
    private static List<Object> getObjects(Object val) {
        return (List<Object>) StreamSupport.stream(Iterable.class.cast(val).spliterator(), false)
                .map(CONVERT).collect(toList());
    }

    @SuppressWarnings("unchecked")
    private static Object getObject(Object val) {
        if (val == null) {
            return null;
        } else if (VALUE_WRITER.test(val.getClass())) {
            return VALUE_WRITER.write(val);
        }
        return val;
    }
}