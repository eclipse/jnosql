/*
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
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

import jakarta.data.exceptions.NonUniqueResultException;
import org.eclipse.jnosql.mapping.core.Converters;
import org.eclipse.jnosql.mapping.core.spi.EntityMetadataExtension;
import org.eclipse.jnosql.mapping.graph.entities.Person;
import org.eclipse.jnosql.mapping.graph.spi.GraphExtension;
import org.eclipse.jnosql.mapping.reflection.Reflections;
import org.eclipse.jnosql.mapping.semistructured.EntityConverter;
import org.jboss.weld.junit5.auto.AddExtensions;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@EnableAutoWeld
@AddPackages(value = {Converters.class, EntityConverter.class, GraphTemplate.class})
@AddPackages(GraphProducer.class)
@AddPackages(Reflections.class)
@AddExtensions({EntityMetadataExtension.class, GraphExtension.class})
class DefaultValueMapTraversalTest extends AbstractTraversalTest {


    @Test
    void shouldCount() {
        long count = graphTemplate.traversalVertex()
                .hasLabel(Person.class).valueMap("name").count();
        assertEquals(3L, count);
    }


    @Test
    void shouldReturnMapValues() {
        List<String> names = graphTemplate.traversalVertex()
                .hasLabel(Person.class).valueMap("name")
                .stream()
                .map(m -> m.getOrDefault("name", "").toString()).collect(Collectors.toList());


        assertThat(names).contains("[Poliana]", "[Otavio]", "[Paulo]");
    }

    @Test
    void shouldReturnStream() {
        Stream<Map<String, Object>> stream = graphTemplate.traversalVertex()
                .hasLabel(Person.class).valueMap("name")
                .stream();
        assertNotNull(stream);
        assertEquals(3L, stream.count());
    }


    @Test
    void shouldReturnResultAsList() {
        List<Map<String, Object>> maps = graphTemplate.traversalVertex()
                .hasLabel(Person.class).valueMap("name")
                .resultList();
        assertEquals(3, maps.size());
    }

    @Test
    void shouldReturnErrorWhenThereAreMoreThanOneInGetSingleResult() {
        assertThrows(NonUniqueResultException.class, () -> graphTemplate.traversalVertex()
                .hasLabel(Person.class).valueMap("name")
                .singleResult());
    }

    @Test
    void shouldReturnOptionalEmptyWhenThereIsNotResultInSingleResult() {
        Optional<Map<String, Object>> entity =   graphTemplate.traversalVertex()
                .hasLabel("not_found").valueMap("name").singleResult();
        assertFalse(entity.isPresent());
    }

    @Test
    void shouldReturnSingleResult() {
        String name = "Poliana";
        Optional<Map<String, Object>> poliana = graphTemplate.traversalVertex().hasLabel("Person").
                has("name", name).valueMap("name").singleResult();
        assertEquals(name, poliana.map(m -> ((List) m.get("name")).get(0)).orElse(""));
    }
}