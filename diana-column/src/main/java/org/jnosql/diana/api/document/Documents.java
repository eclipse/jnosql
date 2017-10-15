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

package org.jnosql.diana.api.document;

import org.jnosql.diana.api.Value;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * This class has utilitarian class to {@link Document}
 */
public final class Documents {

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
    public static List<Document> of(Map<String, ?> values) throws NullPointerException {
        Objects.requireNonNull(values, "values is required");
        Predicate<String> isNotNull = s -> values.get(s) != null;
        Function<String, Document> documentMap = key -> {
            Object value = values.get(key);
            return Document.of(key, Value.of(value));
        };
        return values.keySet().stream().filter(isNotNull).map(documentMap).collect(Collectors.toList());
    }


}
