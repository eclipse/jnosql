/*
 *  Copyright (c) 2018 Otávio Santana and others
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */
package org.jnosql.aphrodite.antlr.method;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public class MethodQuery implements Supplier<String> {

    private final String value;
    private static final Pattern PATTERN = Pattern.compile("findBy|deleteBy|OrderBy|And|Or(?!der)|Not|Equals|GreaterThanEqual|" +
            "LessThanEqual|GreaterThan|GreaterThan|LessThan|Between|In|Like|Asc|Desc");
    private static final Map<String, String> CACHE = Collections.synchronizedMap(new WeakHashMap<>());

    private MethodQuery(String value) {
        this.value = value;
    }

    @Override
    public String get() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MethodQuery that = (MethodQuery) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    public static MethodQuery of(String query) {
        Objects.requireNonNull(query, "query is required");
        String value = CACHE.get(query);
        if (Objects.isNull(value)) {
            value = PATTERN.matcher(query).replaceAll(" $0 ").trim();
            CACHE.put(query, value);
        }
        return new MethodQuery(value);
    }
}
