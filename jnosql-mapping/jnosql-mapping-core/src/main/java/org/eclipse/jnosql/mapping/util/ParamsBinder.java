/*
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
 */
package org.eclipse.jnosql.mapping.util;

import jakarta.nosql.Params;
import jakarta.nosql.Converters;
import jakarta.nosql.DynamicQueryException;
import org.eclipse.jnosql.mapping.reflection.EntityMetadata;
import org.eclipse.jnosql.mapping.reflection.FieldMapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A Binder class, that will apply values in the {@link Params} instance to a dynamic query.
 */
public class ParamsBinder {

    private final EntityMetadata mapping;

    private final Converters converters;

    /**
     * Creates a ParamsBinder instance
     *
     * @param mapping    the mapping of the used class
     * @param converters the converts
     * @throws NullPointerException when there is null parameter
     */
    public ParamsBinder(EntityMetadata mapping, Converters converters) {
        this.mapping = Objects.requireNonNull(mapping, "mapping is required");
        this.converters = Objects.requireNonNull(converters, "converters is required");
    }

    /**
     * Fill up the Params with the args
     *
     * @param params the params
     * @param args   the args
     * @param method the method
     * @throws NullPointerException when there is null parameter
     */
    public void bind(Params params, Object[] args, Method method) {

        Objects.requireNonNull(params, "params is required");
        Objects.requireNonNull(args, "args is required");
        Objects.requireNonNull(method, "method is required");

        List<String> names = params.getParametersNames();
        if (names.size() > args.length) {
            throw new DynamicQueryException("The number of parameters in a query is bigger than the number of " +
                    "parameters in the method: " + method);
        }
        for (int index = 0; index < names.size(); index++) {
            String name = names.get(index);
            int lastIndex = name.lastIndexOf('_') == -1 ? name.length() : name.lastIndexOf('_');
            String fieldName = name.substring(0, lastIndex);
            Optional<FieldMapping> field = this.mapping.getFields().stream()
                    .filter(f -> f.getName().equals(fieldName)).findFirst();

            Object value = getValue(args, index, field);
            params.bind(name, value);
        }
    }

    private Object getValue(Object[] args, int index, Optional<FieldMapping> field) {
        Object value = args[index];
        if (field.isPresent()) {
            if (value instanceof Iterable) {
                List<Object> values = new ArrayList<>();
                for (Object item : Iterable.class.cast(value)) {
                    values.add(ConverterUtil.getValue(item, converters, field.get()));
                }
                return values;
            }
            return ConverterUtil.getValue(value, converters, field.get());
        } else {
            return value;
        }
    }
}
