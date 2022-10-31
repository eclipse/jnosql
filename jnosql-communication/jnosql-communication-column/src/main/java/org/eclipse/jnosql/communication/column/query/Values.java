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
package org.eclipse.jnosql.communication.column.query;

import jakarta.nosql.Params;
import jakarta.nosql.QueryException;
import jakarta.nosql.query.ArrayQueryValue;
import jakarta.nosql.query.Function;
import jakarta.nosql.query.FunctionQueryValue;
import jakarta.nosql.query.JSONQueryValue;
import jakarta.nosql.query.ParamQueryValue;
import jakarta.nosql.query.QueryValue;
import jakarta.nosql.query.ValueType;

import java.util.Arrays;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

final class Values {

    private Values() {
    }

    static Object get(QueryValue<?> value, Params parameters) {

        ValueType type = value.getType();
        switch (type) {
            case NUMBER:
            case STRING:
                return value.get();
            case PARAMETER:
                return parameters.add(ParamQueryValue.class.cast(value).get());
            case ARRAY:
                return Stream.of(ArrayQueryValue.class.cast(value).get())
                        .map(v -> get(v, parameters))
                        .collect(toList());
            case FUNCTION:
                Function function = FunctionQueryValue.class.cast(value).get();
                String name = function.getName();
                Object[] params = function.getParams();
                if ("convert".equals(name)) {
                    return jakarta.nosql.Value.of(get(QueryValue.class.cast(params[0]), parameters))
                            .get((Class<?>) params[1]);
                }
                String message = String.format("There is not support to the function: %s with parameters %s", name,
                        Arrays.toString(params));
                throw new QueryException(message);
            case JSON:
                return JsonObjects.getColumns(JSONQueryValue.class.cast(value).get());
            case CONDITION:
            default:
                throw new QueryException("There is not support to the value: " + type);

        }
    }
}