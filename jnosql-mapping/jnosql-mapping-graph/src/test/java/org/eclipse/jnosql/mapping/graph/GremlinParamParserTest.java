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
        String expected = "g.V().hasLabel(\'Otavio\')";
        Map<String, Object> params = Map.of("param", "Otavio");

        String gremlin = parser.apply(query, params);
        Assertions.assertEquals(expected, gremlin);
    }

    @Test
    public void shouldParserBoolean() {
        String query = "g.V().hasLabel(@label).has('active', @active)";
        String expected = "g.V().hasLabel(\'Otavio\').has('active', 'true')";
        Map<String, Object> params = Map.of("label", "Otavio", "active", Boolean.TRUE);

        String gremlin = parser.apply(query, params);
        Assertions.assertEquals(expected, gremlin);
    }

    @Test
    public void shouldParserPrimitiveBoolean() {
        String query = "g.V().hasLabel(@label).has('active', @active)";
        String expected = "g.V().hasLabel(\'Otavio\').has('active', 'true')";
        Map<String, Object> params = Map.of("label", "Otavio", "active", true);

        String gremlin = parser.apply(query, params);
        Assertions.assertEquals(expected, gremlin);
    }
    @Test
    public void shouldParserBooleanFalse() {
        String query = "g.V().hasLabel(@label).has('active', @active)";
        String expected = "g.V().hasLabel(\'Otavio\').has('active', 'false')";
        Map<String, Object> params = Map.of("label", "Otavio", "active", Boolean.FALSE);

        String gremlin = parser.apply(query, params);
        Assertions.assertEquals(expected, gremlin);
    }

    @Test
    public void shouldParserPrimitiveBooleanFalse() {
        String query = "g.V().hasLabel(@label).has('active', @active)";
        String expected = "g.V().hasLabel(\'Otavio\').has('active', 'false')";
        Map<String, Object> params = Map.of("label", "Otavio", "active", false);

        String gremlin = parser.apply(query, params);
        Assertions.assertEquals(expected, gremlin);
    }

    @Test
    public void shouldParserNumber() {
        String query = "g.V().hasLabel(@label).has('age', @age)";
        String expected = "g.V().hasLabel(\'Otavio\').has('age', 15)";
        Map<String, Object> params = Map.of("label", "Otavio", "age", 15);

        String gremlin = parser.apply(query, params);
        Assertions.assertEquals(expected, gremlin);

    }

    @Test
    public void shouldParserNegativeNumber() {
        String query = "g.V().hasLabel(@label).has('age', @age)";
        String expected = "g.V().hasLabel(\'Otavio\').has('age', -15)";
        Map<String, Object> params = Map.of("label", "Otavio", "age", -15);

        String gremlin = parser.apply(query, params);
        Assertions.assertEquals(expected, gremlin);
    }

    @Test
    public void shouldReplaceAll() {
        String query = "g.V().hasLabel(@label).has('name', @label)";
        String expected = "g.V().hasLabel(\'Otavio\').has('name', 'Otavio')";
        Map<String, Object> params = Map.of("label", "Otavio");

        String gremlin = parser.apply(query, params);
        Assertions.assertEquals(expected, gremlin);
    }

    @Test
    public void shouldGetAnExceptionWhenThereIsMoreParamsLeft() {
        String query = "g.V().hasLabel(@label).has('age', @age)";
        Map<String, Object> params = Map.of("label", "Otavio");

        Assertions.assertThrows(GremlinQueryException.class, () ->
                parser.apply(query, params));
    }

    @Test
    public void shouldReturnErrorWhenMissingParameter(){
        String query = "g.V().hasLabel(@label)";
        Map<String, Object> params = Map.of("label", "Otavio", "age", 15);

        Assertions.assertThrows(GremlinQueryException.class, () ->
                parser.apply(query, params));
    }
}