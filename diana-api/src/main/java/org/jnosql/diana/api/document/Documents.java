/*
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
