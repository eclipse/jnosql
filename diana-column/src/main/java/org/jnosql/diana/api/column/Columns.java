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

package org.jnosql.diana.api.column;

import org.jnosql.diana.api.Value;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Utilitarian class to {@link Column}
 */
public final class Columns {

    private Columns() {
    }

    /**
     * Creates a column instance
     *
     * @param name  column's name
     * @param value column's value
     * @return a column's instance
     * @throws NullPointerException when either name or value are null
     */
    public static Column of(String name, Object value) {
        return Column.of(name, Value.of(value));
    }

    /**
     * Converts a Map to columns where: the key gonna be a column's name the value a column's value and null values
     * elements will be ignored.
     *
     * @param values map to be converted
     * @return a list of columns
     * @throws NullPointerException when values is null
     */
    public static List<Column> of(Map<String, ?> values) {
        Objects.requireNonNull(values, "values is required");
        Predicate<String> isNotNull = s -> values.get(s) != null;
        Function<String, Column> columnMap = key -> {
            Object value = values.get(key);
            return Column.of(key, Value.of(value));
        };
        return values.keySet().stream().filter(isNotNull).map(columnMap).collect(Collectors.toList());
    }
}
