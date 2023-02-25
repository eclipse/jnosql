/*
 *  Copyright (c) 2023 Contributors to the Eclipse Foundation
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
package org.eclipse.jnosql.mapping.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This singleton has the goal to interpolate params inside the Gremlin query.
 * Thus, given the query:
 * "g.V().hasLabel(@param)" where the params is {"param":"Otavio"}
 * It should return the query to: g.V().hasLabel("Otavio")
 * It should check the Gremlin query options:
 * <a href="https://github.com/apache/tinkerpop/blob/e1396223ea9e1d6240c1f051036cbb5507f47f8d/gremlin-language/src/main/antlr4/Gremlin.g4">Gremlin.g4</a>
 * <p>
 * Thus, given a current query with params it should convert to Gremlin compatible syntax.
 */
enum GremlinParamParser implements BiFunction<String, Map<String, Object>, String> {
    INSTANCE;

    private final Pattern pattern = Pattern.compile("@\\w+");

    @Override
    public String apply(String query, Map<String, Object> params) {
        Objects.requireNonNull(query, "query is required");
        Objects.requireNonNull(query, "params is required");
        Matcher matcher = pattern.matcher(query);
        List<String> leftParams = new ArrayList<>(params.keySet());
        StringBuilder gremlin = new StringBuilder();
        while (matcher.find()) {
            String param = matcher.group().substring(1);
            leftParams.remove(param);
            Object value = params.get(param);
            if (value == null) {
                throw new GremlinQueryException("The param is " + param + " is required on the query " + query);
            }
            matcher.appendReplacement(gremlin, toString(value));
        }
        matcher.appendTail(gremlin);
        if (leftParams.isEmpty()) {
            return gremlin.toString();
        }

        throw new GremlinQueryException("There are params missing on the parser: " + leftParams + " on the query" + query);
    }

    private String toString(Object value) {
        if (value instanceof Number) {
            return value.toString();
        }
        return '\'' + value.toString() + '\'';
    }
}
