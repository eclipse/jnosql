/*
 *  Copyright (c) 2019 Otávio Santana and others
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

import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.jnosql.artemis.Page;
import org.jnosql.artemis.Pagination;
import org.jnosql.artemis.graph.cdi.CDIExtension;
import org.jnosql.artemis.graph.model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static org.jnosql.artemis.graph.model.Person.builder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(CDIExtension.class)
class GraphPageTest {

    @Inject
    protected GraphTemplate template;

    @Inject
    private Graph graph;

    private Person otavio;
    private Person poliana;
    private Person paulo;


    @BeforeEach
    public void setUp() {

        graph.traversal().V().toList().forEach(Vertex::remove);
        graph.traversal().E().toList().forEach(Edge::remove);

        otavio = template.insert(builder().withAge(27)
                .withName("Otavio").build());
        poliana = template.insert(builder().withAge(26)
                .withName("Poliana").build());
        paulo = template.insert(builder().withAge(50)
                .withName("Paulo").build());
    }

    @AfterEach
    public void after() {
        template.delete(otavio.getId());
        template.delete(poliana.getId());
        template.delete(paulo.getId());

        graph.traversal().V().toList().forEach(Vertex::remove);
        graph.traversal().E().toList().forEach(Edge::remove);
    }

    @Test
    public void shouldReturnErrorWhenPaginationIsNull() {
        assertThrows(NullPointerException.class, () -> template.getTraversalVertex().page(null));
    }

    @Test
    public void shouldPaginate() {
        Pagination pagination = Pagination.page(1).size(1);
        Page<Person> page = template.getTraversalVertex()
                .orderBy("name")
                .desc()
                .page(pagination);

        assertNotNull(page);
        List<Person> people = page.getContent();

        assertEquals(pagination, page.getPagination());
        assertEquals(1, people.size());
        assertEquals(otavio.getName(), people.stream().map(Person::getName).collect(joining()));
    }

    @Test
    public void shouldStream() {
        Pagination pagination = Pagination.page(1).size(1);
        Page<Person> page = template.getTraversalVertex()
                .orderBy("name")
                .desc()
                .page(pagination);

        assertNotNull(page);
        Stream<Person> people = page.get();

        assertEquals(pagination, page.getPagination());
        assertEquals(otavio.getName(), people.map(Person::getName).collect(joining()));
    }

    @Test
    public void shouldReturnErrorWhenCollectionFactoryIsNull() {
        Pagination pagination = Pagination.page(1).size(1);
        Page<Person> page = template.getTraversalVertex()
                .orderBy("name")
                .desc()
                .page(pagination);
        assertNotNull(page);
        assertThrows(NullPointerException.class, () -> page.getContent(null));
    }

    @Test
    public void shouldReturnCollectionFromCollectionFactory() {
        Pagination pagination = Pagination.page(1).size(1);
        Page<Person> page = template.getTraversalVertex()
                .orderBy("name")
                .desc()
                .page(pagination);
        assertNotNull(page);

        Set<Person> people = page.getContent(HashSet::new);

        assertEquals(pagination, page.getPagination());
        assertEquals(1, people.size());
        assertEquals(otavio.getName(), people.stream().map(Person::getName).collect(joining()));

    }

    @Test
    public void shouldNext() {
        Pagination pagination = Pagination.page(1).size(1);
        Page<Person> page = template.getTraversalVertex()
                .orderBy("name")
                .desc()
                .page(pagination);

        assertNotNull(page);
        Stream<Person> people = page.get();

        assertEquals(pagination, page.getPagination());
        assertEquals(otavio.getName(), people.map(Person::getName).collect(joining()));

        pagination = pagination.next();

        page = page.next();
        people = page.get();
        assertEquals(pagination, page.getPagination());
        assertEquals(poliana.getName(), people.map(Person::getName).collect(joining()));

        pagination = pagination.next();
        page = page.next();
        people = page.get();

        assertEquals(pagination, page.getPagination());
        assertEquals(paulo.getName(), people.map(Person::getName).collect(joining()));

    }


}