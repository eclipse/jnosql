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

import jakarta.inject.Inject;
import jakarta.nosql.tck.test.CDIExtension;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.eclipse.jnosql.mapping.graph.entities.Person;
import org.eclipse.jnosql.mapping.graph.entities.Worker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@CDIExtension
public class MapperSelectTest {


    @Inject
    private GraphTemplate template;

    @Inject
    private Graph graph;

    @AfterEach
    public void after() {
        graph.traversal().V().toList().forEach(Vertex::remove);
        graph.traversal().E().toList().forEach(Edge::remove);
    }

    @Test
    public void shouldExecuteSelectFrom() {
        Person otavio = template.insert(Person.builder().withName("Otavio")
                .withAge(35).build());
        Person ada = template.insert(Person.builder().withName("Ada")
                .withAge(12).build());
        Person poliana = template.insert(Person.builder().withName("Poliana")
                .withAge(30).build());

        List<Person> people = template.select(Person.class).result();

        Assertions.assertNotNull(people);

        assertThat(people).hasSize(3).map(Person::getName)
                .contains(otavio.getName(), ada.getName(), poliana.getName());
    }


    @Test
    public void shouldSelectOrderAsc() {
        Person otavio = template.insert(Person.builder().withName("Otavio")
                .withAge(35).build());
        Person ada = template.insert(Person.builder().withName("Ada")
                .withAge(12).build());
        Person poliana = template.insert(Person.builder().withName("Poliana")
                .withAge(30).build());

        List<Person> people = template.select(Person.class).orderBy("name").asc().result();
        assertThat(people).hasSize(3).map(Person::getName)
                .containsExactly(ada.getName(), otavio.getName(), poliana.getName());
    }

    @Test
    public void shouldSelectOrderDesc() {
        Person otavio = template.insert(Person.builder().withName("Otavio")
                .withAge(35).build());
        Person ada = template.insert(Person.builder().withName("Ada")
                .withAge(12).build());
        Person poliana = template.insert(Person.builder().withName("Poliana")
                .withAge(30).build());

        List<Person> people = template.select(Person.class).orderBy("name").desc().result();
        assertThat(people).hasSize(3).map(Person::getName)
                .containsExactly(poliana.getName(), otavio.getName(), ada.getName());
    }

    @Test
    public void shouldSelectLimit() {
        Person otavio = template.insert(Person.builder().withName("Otavio")
                .withAge(35).build());
        Person ada = template.insert(Person.builder().withName("Ada")
                .withAge(12).build());
        Person poliana = template.insert(Person.builder().withName("Poliana")
                .withAge(30).build());

        List<Person> people = template.select(Person.class).orderBy("name").desc().limit(2).result();

        assertThat(people).hasSize(2).map(Person::getName)
                .containsExactly(poliana.getName(), otavio.getName());
    }

    @Test
    public void shouldSelectStart() {
        Person otavio = template.insert(Person.builder().withName("Otavio")
                .withAge(35).build());
        Person ada = template.insert(Person.builder().withName("Ada")
                .withAge(12).build());
        Person poliana = template.insert(Person.builder().withName("Poliana")
                .withAge(30).build());

        List<Person> people = template.select(Person.class).orderBy("name").desc().skip(1).result();

        assertThat(people).hasSize(2).map(Person::getName)
                .containsExactly(otavio.getName(), ada.getName());
    }

    @Test
    public void shouldSelectWhereNameEq() {
        Person otavio = template.insert(Person.builder().withName("Otavio")
                .withAge(35).build());
        Person ada = template.insert(Person.builder().withName("Ada")
                .withAge(12).build());
        Person poliana = template.insert(Person.builder().withName("Poliana")
                .withAge(30).build());

        Optional<Person> person = template.select(Person.class)
                .where("name").eq("Otavio").singleResult();

        Assertions.assertNotNull(person);
        Assertions.assertTrue(person.isPresent());
        assertEquals(person.map(Person::getName).orElse(""), otavio.getName());
    }

    @Test
    public void shouldSelectWhereNameGt() {}

    @Test
    public void shouldSelectWhereNameGte() {}

    @Test
    public void shouldSelectWhereNameLt() {}

    @Test
    public void shouldSelectWhereNameLte() {}

    @Test
    public void shouldSelectWhereNameBetween() {}

    @Test
    public void shouldSelectWhereNameNot() {}

    @Test
    public void shouldSelectWhereNameAnd() {}

    @Test
    public void shouldSelectWhereNameOr() {}

    @Test
    public void shouldConvertField() {}

    @Test
    public void shouldUseAttributeConverter() {}

    @Test
    public void shouldResult() {}

    @Test
    public void shouldStream() {}

    @Test
    public void shouldSingleResult() {}
}
