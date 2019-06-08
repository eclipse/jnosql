/*
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
 */
package org.jnosql.artemis.graph;

import org.hamcrest.Matchers;
import org.jnosql.artemis.graph.cdi.CDIExtension;
import org.jnosql.artemis.graph.model.Person;
import org.jnosql.diana.api.NonUniqueResultException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(CDIExtension.class)
class DefaultValueMapTraversalTest extends AbstractTraversalTest {


    @Test
    public void shouldCount() {
        long count = graphTemplate.getTraversalVertex()
                .hasLabel(Person.class).valueMap("name").count();
        assertEquals(3L, count);
    }


    @Test
    public void shouldReturnMapValues() {
        List<String> names = graphTemplate.getTraversalVertex()
                .hasLabel(Person.class).valueMap("name")
                .stream()
                .map(m -> m.getOrDefault("name", "").toString()).collect(Collectors.toList());


        assertThat(names, Matchers.containsInAnyOrder("[Poliana]", "[Otavio]", "[Paulo]"));
    }

    @Test
    public void shouldReturnStream() {
        Stream<Map<String, Object>> stream = graphTemplate.getTraversalVertex()
                .hasLabel(Person.class).valueMap("name")
                .stream();
        assertNotNull(stream);
        assertEquals(3L, stream.count());
    }


    @Test
    public void shouldReturnResultAsList() {
        List<Map<String, Object>> maps = graphTemplate.getTraversalVertex()
                .hasLabel(Person.class).valueMap("name")
                .getResultList();
        assertEquals(3, maps.size());
    }

    @Test
    public void shouldReturnErrorWhenThereAreMoreThanOneInGetSingleResult() {
        assertThrows(NonUniqueResultException.class, () -> graphTemplate.getTraversalVertex()
                .hasLabel(Person.class).valueMap("name")
                .getSingleResult());
    }

    @Test
    public void shouldReturnOptionalEmptyWhenThereIsNotResultInSingleResult() {
        Optional<Map<String, Object>> entity =   graphTemplate.getTraversalVertex()
                .hasLabel("not_found").valueMap("name").getSingleResult();
        assertFalse(entity.isPresent());
    }

    @Test
    public void shouldReturnSingleResult() {
        String name = "Poliana";
        Optional<Map<String, Object>> poliana = graphTemplate.getTraversalVertex().hasLabel("Person").
                has("name", name).valueMap("name").getSingleResult();
        assertEquals(name, poliana.map(m -> ((List) m.get("name")).get(0)).orElse(""));
    }
}