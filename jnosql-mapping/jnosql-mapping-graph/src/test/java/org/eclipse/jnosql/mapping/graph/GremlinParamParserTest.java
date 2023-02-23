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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GremlinParamParserTest {

    private GremlinParamParser parser = GremlinParamParser.INSTANCE;

    @Test
    public void shouldGotErrorWhenNull() {
        assertNotNull(NullPointerException.class, () ->
                parser.apply(null, null));
        assertNotNull(NullPointerException.class, () ->
                parser.apply("null", null));

        assertNotNull(NullPointerException.class, () ->
                parser.apply(null, Collections.emptyMap()));
    }

    @Test
    public void shouldParserParamString() {
        String query = "g.V().hasLabel(@param)";
        String expected = "g.V().hasLabel(\"Otavio\")";
        Map<String, Object> params = Map.of("param", "Otavio");

        String gremlin = parser.apply(query, params);
        Assertions.assertEquals(expected, gremlin);
    }
    //number
    //negative number
    //boolean
    //string
}