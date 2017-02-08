/*
 * Copyright 2017 Eclipse Foundation
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
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
    public static Column of(String name, Object value) throws NullPointerException {
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
    public static List<Column> of(Map<String, ?> values) throws NullPointerException {
        Objects.requireNonNull(values, "values is required");
        Predicate<String> isNotNull = s -> values.get(s) != null;
        Function<String, Column> columnMap = key -> {
            Object value = values.get(key);
            return Column.of(key, Value.of(value));
        };
        return values.keySet().stream().filter(isNotNull).map(columnMap).collect(Collectors.toList());
    }
}
