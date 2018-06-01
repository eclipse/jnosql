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
package org.jnosql.diana.api.document.query;

import org.jnosql.query.ArrayValue;
import org.jnosql.query.Function;
import org.jnosql.query.FunctionValue;
import org.jnosql.query.JSONValue;
import org.jnosql.query.ParamValue;
import org.jnosql.query.QueryException;
import org.jnosql.query.Value;
import org.jnosql.query.ValueType;

import java.util.Arrays;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

final class Values {

    private Values() {
    }

    static Object get(Value<?> value, Params parameters) {

        ValueType type = value.getType();
        switch (type) {
            case NUMBER:
            case STRING:
                return value.get();
            case PARAMETER:
                return parameters.add(ParamValue.class.cast(value).get());
            case ARRAY:
                return Stream.of(ArrayValue.class.cast(value).get())
                        .map(v -> get(v, parameters))
                        .collect(toList());
            case FUNCTION:
                Function function = FunctionValue.class.cast(value).get();
                String name = function.getName();
                Object[] params = function.getParams();
                if ("convert".equals(name)) {
                    return org.jnosql.diana.api.Value.of(get(Value.class.cast(params[0]), parameters))
                            .get((Class<?>) params[1]);
                }
                String message = String.format("There is not support to the fuction: %s with parameters %s", name,
                        Arrays.toString(params));
                throw new QueryException(message);
            case JSON:
                return JsonObjects.getDocuments(JSONValue.class.cast(value).get());
            case CONDITION:
            default:
                throw new QueryException("There is not suppor to the value: " + type);

        }
    }
}
