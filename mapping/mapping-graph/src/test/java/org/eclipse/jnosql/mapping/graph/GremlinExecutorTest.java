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
package org.eclipse.jnosql.mapping.graph;

import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.eclipse.jnosql.mapping.graph.model.Person;
import jakarta.nosql.tck.test.CDIExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonMap;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@CDIExtension
class GremlinExecutorTest {


    @Inject
    private GraphConverter converter;

    @Inject
    private Graph graph;

    private GremlinExecutor executor;

    @BeforeEach
    public void before() {
        executor = new GremlinExecutor(converter);
        graph.traversal().V().toList().forEach(Vertex::remove);
        graph.traversal().E().toList().forEach(Edge::remove);
        Vertex bruce = graph.addVertex(T.label, "Person", "name", "Bruce Banner", "age", 30);
        Vertex nat = graph.addVertex(T.label, "Person", "name", "Natasha Romanoff", "age", 30);
        bruce.addEdge("loves", nat);

    }

    @Test
    public void shouldExecuteQueryEdges() {

        List<EdgeEntity> edges = executor.<EdgeEntity> executeGremlin(graph.traversal(), "g.E()")
                .collect(toList());
        assertFalse(edges.isEmpty());
        EdgeEntity edgeEntity = edges.get(0);
        Person person = edgeEntity.getIncoming();
        Person person2 = edgeEntity.getOutgoing();
        assertEquals("loves", edgeEntity.getLabel());
        assertEquals("Bruce Banner", person2.getName());
        assertEquals("Natasha Romanoff", person.getName());

    }

    @Test
    public void shouldExecuteQueryEdges2() {

        List<EdgeEntity> edges = executor.<EdgeEntity> executeGremlin(graph.traversal(), "g.E().toList()")
                .collect(toList());
        assertFalse(edges.isEmpty());
        EdgeEntity edgeEntity = edges.get(0);
        Person person = edgeEntity.getIncoming();
        Person person2 = edgeEntity.getOutgoing();
        assertEquals("loves", edgeEntity.getLabel());
        assertEquals("Bruce Banner", person2.getName());
        assertEquals("Natasha Romanoff", person.getName());

    }

    @Test
    public void shouldExecuteQueryVertex() {
        List<Person> people = executor.<Person>executeGremlin(graph.traversal(), "g.V()")
                .collect(toList());
        assertFalse(people.isEmpty());
        List<String> names = people.stream().map(Person::getName).collect(toList());
        assertThat(names, containsInAnyOrder("Bruce Banner", "Natasha Romanoff"));

    }

    @Test
    public void shouldExecuteQueryVertex2() {
        List<Person> people = executor.<Person>executeGremlin(graph.traversal(), "g.V().toList()")
                .collect(toList());
        assertFalse(people.isEmpty());
        List<String> names = people.stream().map(Person::getName).collect(toList());
        assertThat(names, containsInAnyOrder("Bruce Banner", "Natasha Romanoff"));

    }

    @Test
    public void shouldExecuteQueryVertex3() {
        List<Person> people = executor.<Person>executeGremlin(graph.traversal(), "g.V().in('loves').toList()")
                .collect(toList());
        List<String> names = people.stream().map(Person::getName).collect(toList());
        assertThat(names, containsInAnyOrder("Bruce Banner"));

    }

    @Test
    public void shouldExecuteQueryVertex4() {
        List<Person> people = executor.<Person>executeGremlin(graph.traversal(), "g.V().in('loves').toSet()")
                .collect(toList());
        List<String> names = people.stream().map(Person::getName).collect(toList());
        assertThat(names, containsInAnyOrder("Bruce Banner"));

    }

    @Test
    public void shouldExecuteQueryVertex5() {
        List<Person> people = executor.<Person>executeGremlin(graph.traversal(), "g.V().in('loves').toStream()")
                .collect(toList());
        List<String> names = people.stream().map(Person::getName).collect(toList());
        assertThat(names, containsInAnyOrder("Bruce Banner"));

    }

    @Test
    public void shouldExecuteQueryCount() {
        List<Long> count = executor.<Long>executeGremlin(graph.traversal(), "g.V().count().toList()")
                .collect(toList());
        assertFalse(count.isEmpty());
        assertThat(count, containsInAnyOrder(2L));
    }

    @Test
    public void shouldExecuteQueryCount2() {
        List<Long> count = executor.<Long>executeGremlin(graph.traversal(), "g.V().count()")
                .collect(toList());
        assertFalse(count.isEmpty());
        assertThat(count, containsInAnyOrder(2L));
    }

    @Test
    public void shouldExecuteQueryProperties() {
        List<Object> properties = executor.<Object>executeGremlin(graph.traversal(), "g.V().values()")
                .collect(toList());
        assertFalse(properties.isEmpty());
        assertEquals(4, properties.size());
        assertThat(properties, containsInAnyOrder("Bruce Banner", 30, 30, "Natasha Romanoff"));
    }

    @Test
    public void shouldExecuteQueryProperties2() {
        List<Object> properties = executor.executeGremlin(graph.traversal(), "g.V().values().toList()")
                .collect(toList());
        assertFalse(properties.isEmpty());
        assertEquals(4, properties.size());
        assertThat(properties, containsInAnyOrder("Bruce Banner", 30, 30, "Natasha Romanoff"));
    }

    @Test
    public void shouldExecuteQueryPropertiesMap() {
        List<Map<String, List<String>>> properties = executor.<Map<String, List<String>>>
                executeGremlin(graph.traversal(), "g.V().valueMap('name')")
                .collect(toList());
        assertFalse(properties.isEmpty());
        assertEquals(2, properties.size());
        List<String> names = properties.stream().map(p -> p.get("name")).flatMap(List::stream).collect(toList());
        assertThat(names, containsInAnyOrder("Bruce Banner", "Natasha Romanoff"));
    }

    @Test
    public void shouldExecuteQueryPropertiesMap2() {
        List<Map<String, List<String>>> properties = executor.<Map<String, List<String>>>
                executeGremlin(graph.traversal(), "g.V().valueMap('name').toList()")
                .collect(toList());
        assertEquals(2, properties.size());
        List<String> names = properties.stream().map(p -> p.get("name")).flatMap(List::stream).collect(toList());
        assertThat(names, containsInAnyOrder("Bruce Banner", "Natasha Romanoff"));
    }

    @Test
    public void shouldExecuteWithParams() {

        List<Person> people = executor.<Person>executeGremlin(graph.traversal(),
                "g.V().in(param).toList()",singletonMap("param", "loves"))
                .collect(toList());

        assertFalse(people.isEmpty());
        List<String> names = people.stream().map(Person::getName).collect(toList());
        assertThat(names, containsInAnyOrder("Bruce Banner"));
    }
}