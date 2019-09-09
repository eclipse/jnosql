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

package org.eclipse.jnosql.diana.document;

import jakarta.nosql.Value;
import jakarta.nosql.document.Document;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

/**
 * This class has utilitarian class to {@link Document}
 */
public final class Documents {


    private static final Predicate<Map.Entry<String, ?>> IS_VALUE_NULL = e -> Objects.nonNull(e.getValue());

    private Documents() {
    }

    /**
     * An alias to {@link Document#of(String, Object)}
     *
     * @param name  the name
     * @param value the value
     * @return the document instance
     */
    public static Document of(String name, Object value) {
        return Document.of(name, Value.of(value));
    }


    /**
     * Converts the map to {@link List} of {@link Document}
     *
     * @param values the map
     * @return the list instance
     * @throws NullPointerException when map is null
     */
    public static List<Document> of(Map<String, ?> values) {
        Objects.requireNonNull(values, "values is required");

        return values.entrySet().stream()
                .filter(IS_VALUE_NULL)
                .map(e -> Document.of(e.getKey(), getValue(e.getValue())))
                .collect(toList());
    }

    private static Object getValue(Object value) {

        if (value instanceof Map) {
            List list = Documents.of((Map.class.cast(value)));
            if(list.size() == 1) {
                return list.get(0);
            }
            return list;
        }
        if (value instanceof Iterable) {
            return stream(Iterable.class.cast(value).spliterator(), false)
                    .map(Documents::getValue).collect(toList());
        }
        return value;
    }

}
