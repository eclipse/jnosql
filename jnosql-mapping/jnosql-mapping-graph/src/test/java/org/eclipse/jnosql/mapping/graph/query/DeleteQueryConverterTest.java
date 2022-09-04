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
package org.eclipse.jnosql.mapping.graph.query;

import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.Repository;
import org.eclipse.jnosql.mapping.graph.model.Person;
import org.eclipse.jnosql.mapping.reflection.EntityMetadata;
import org.eclipse.jnosql.mapping.reflection.EntitiesMetadata;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import jakarta.nosql.tck.test.CDIExtension;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.inject.Inject;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@CDIExtension
class DeleteQueryConverterTest {
    private final DeleteQueryConverter converter = DeleteQueryConverter.INSTANCE;

    @Inject
    private EntitiesMetadata mappings;

    @Inject
    private Converters converters;

    @Inject
    private Graph graph;

    @BeforeEach
    public void setUp() {
        graph.traversal().E().toList().forEach(Edge::remove);
        graph.traversal().V().toList().forEach(Vertex::remove);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteByName"})
    public void shouldRunQuery(String methodName) {
        checkEquals(methodName);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteByNameEquals"})
    public void shouldRunQuery1(String methodName) {
        checkEquals(methodName);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteByNameNotEquals"})
    public void shouldRunQuery2(String methodName) {
        Method method = Stream.of(PersonRepository.class.getMethods())
                .filter(m -> m.getName().equals(methodName)).findFirst().get();

        graph.addVertex("Person").property("name", "Otavio");
        graph.addVertex("Person").property("name", "Ada");
        graph.addVertex("Person").property("name", "Poliana");
        EntityMetadata mapping = mappings.get(Person.class);
        GraphQueryMethod queryMethod = new GraphQueryMethod(mapping, graph.traversal().V(),
                converters, method, new Object[]{"Ada"});

        List<Vertex> vertices = converter.apply(queryMethod);
        assertEquals(2, vertices.size());
        assertNotEquals("Ada", vertices.get(0).value("name"));
        assertNotEquals("Ada", vertices.get(1).value("name"));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteByAgeLessThan"})
    public void shouldRunQuery3(String methodName) {
        Method method = Stream.of(PersonRepository.class.getMethods())
                .filter(m -> m.getName().equals(methodName)).findFirst().get();

        graph.addVertex(T.label, "Person", "name", "Otavio", "age", 30);
        graph.addVertex(T.label, "Person", "name", "Ada", "age", 40);
        graph.addVertex(T.label, "Person", "name", "Poliana", "age", 25);
        EntityMetadata mapping = mappings.get(Person.class);
        GraphQueryMethod queryMethod = new GraphQueryMethod(mapping, graph.traversal().V(),
                converters, method, new Object[]{30});

        List<Vertex> vertices = converter.apply(queryMethod);
        assertEquals(1, vertices.size());
        assertEquals("Poliana", vertices.get(0).value("name"));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteByAgeLessThanEqual"})
    public void shouldRunQuery4(String methodName) {
        Method method = Stream.of(PersonRepository.class.getMethods())
                .filter(m -> m.getName().equals(methodName)).findFirst().get();

        graph.addVertex(T.label, "Person", "name", "Otavio", "age", 30);
        graph.addVertex(T.label, "Person", "name", "Ada", "age", 40);
        graph.addVertex(T.label, "Person", "name", "Poliana", "age", 25);
        EntityMetadata mapping = mappings.get(Person.class);
        GraphQueryMethod queryMethod = new GraphQueryMethod(mapping, graph.traversal().V(),
                converters, method, new Object[]{30});

        List<Vertex> vertices = converter.apply(queryMethod);
        assertEquals(2, vertices.size());
        assertNotEquals("Ada", vertices.get(0).value("name"));
        assertNotEquals("Ada", vertices.get(1).value("name"));
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteByAgeGreaterThan"})
    public void shouldRunQuery5(String methodName) {
        Method method = Stream.of(PersonRepository.class.getMethods())
                .filter(m -> m.getName().equals(methodName)).findFirst().get();

        graph.addVertex(T.label, "Person", "name", "Otavio", "age", 30);
        graph.addVertex(T.label, "Person", "name", "Ada", "age", 40);
        graph.addVertex(T.label, "Person", "name", "Poliana", "age", 25);
        EntityMetadata mapping = mappings.get(Person.class);
        GraphQueryMethod queryMethod = new GraphQueryMethod(mapping, graph.traversal().V(),
                converters, method, new Object[]{30});

        List<Vertex> vertices = converter.apply(queryMethod);
        assertEquals(1, vertices.size());
        assertEquals("Ada", vertices.get(0).value("name"));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteByAgeGreaterThanEqual"})
    public void shouldRunQuery6(String methodName) {
        Method method = Stream.of(PersonRepository.class.getMethods())
                .filter(m -> m.getName().equals(methodName)).findFirst().get();

        graph.addVertex(T.label, "Person", "name", "Otavio", "age", 30);
        graph.addVertex(T.label, "Person", "name", "Ada", "age", 40);
        graph.addVertex(T.label, "Person", "name", "Poliana", "age", 25);
        EntityMetadata mapping = mappings.get(Person.class);
        GraphQueryMethod queryMethod = new GraphQueryMethod(mapping, graph.traversal().V(),
                converters, method, new Object[]{30});

        List<Vertex> vertices = converter.apply(queryMethod);
        assertEquals(2, vertices.size());
        assertNotEquals("Poliana", vertices.get(0).value("name"));
        assertNotEquals("Poliana", vertices.get(1).value("name"));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteByAgeBetween"})
    public void shouldRunQuery7(String methodName) {
        Method method = Stream.of(PersonRepository.class.getMethods())
                .filter(m -> m.getName().equals(methodName)).findFirst().get();

        graph.addVertex(T.label, "Person", "name", "Otavio", "age", 30);
        graph.addVertex(T.label, "Person", "name", "Ada", "age", 40);
        graph.addVertex(T.label, "Person", "name", "Poliana", "age", 25);
        EntityMetadata mapping = mappings.get(Person.class);
        GraphQueryMethod queryMethod = new GraphQueryMethod(mapping, graph.traversal().V(),
                converters, method, new Object[]{29, 41});

        List<Vertex> vertices = converter.apply(queryMethod);
        assertEquals(2, vertices.size());
        assertNotEquals("Poliana", vertices.get(0).value("name"));
        assertNotEquals("Poliana", vertices.get(1).value("name"));
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteByAgeIn"})
    public void shouldRunQuery11(String methodName) {
        Method method = Stream.of(PersonRepository.class.getMethods())
                .filter(m -> m.getName().equals(methodName)).findFirst().get();

        graph.addVertex(T.label, "Person", "name", "Otavio", "age", 30);
        graph.addVertex(T.label, "Person", "name", "Ada", "age", 40);
        graph.addVertex(T.label, "Person", "name", "Poliana", "age", 25);
        EntityMetadata mapping = mappings.get(Person.class);
        GraphQueryMethod queryMethod = new GraphQueryMethod(mapping, graph.traversal().V(),
                converters, method, new Object[]{Arrays.asList(25,40,30)});

        List<Vertex> vertices = converter.apply(queryMethod);
        List<Object> names = vertices.stream().map(v -> v.value("name"))
                .sorted()
                .collect(Collectors.toList());
        assertEquals(3, vertices.size());
        MatcherAssert.assertThat(names, Matchers.contains("Ada", "Otavio", "Poliana"));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteByNameIn"})
    public void shouldRunQuery12(String methodName) {
        Method method = Stream.of(PersonRepository.class.getMethods())
                .filter(m -> m.getName().equals(methodName)).findFirst().get();

        graph.addVertex(T.label, "Person", "name", "Otavio", "age", 30);
        graph.addVertex(T.label, "Person", "name", "Ada", "age", 40);
        graph.addVertex(T.label, "Person", "name", "Poliana", "age", 25);
        EntityMetadata mapping = mappings.get(Person.class);
        GraphQueryMethod queryMethod = new GraphQueryMethod(mapping, graph.traversal().V(),
                converters, method, new Object[]{Arrays.asList("Otavio", "Ada", "Poliana")});

        List<Vertex> vertices = converter.apply(queryMethod);
        List<Object> names = vertices.stream().map(v -> v.value("name"))
                .sorted()
                .collect(Collectors.toList());
        assertEquals(3, vertices.size());
        MatcherAssert.assertThat(names, Matchers.contains("Ada", "Otavio", "Poliana"));
    }


    private void checkEquals(String methodName) {
        Method method = Stream.of(PersonRepository.class.getMethods())
                .filter(m -> m.getName().equals(methodName)).findFirst().get();

        graph.addVertex("Person").property("name", "Otavio");
        graph.addVertex("Person").property("name", "Ada");
        graph.addVertex("Person").property("name", "Poliana");
        EntityMetadata mapping = mappings.get(Person.class);
        GraphQueryMethod queryMethod = new GraphQueryMethod(mapping, graph.traversal().V(),
                converters, method, new Object[]{"Ada"});

        List<Vertex> vertices = converter.apply(queryMethod);
        assertEquals(1, vertices.size());
        assertEquals("Ada", vertices.get(0).value("name"));
    }


    interface PersonRepository extends Repository<Person, String> {

        List<Person> deleteByName(String name);

        List<Person> deleteByNameEquals(String name);

        List<Person> deleteByNameNotEquals(String name);

        List<Person> deleteByAgeLessThan(Integer age);

        List<Person> deleteByAgeLessThanEqual(Integer age);

        List<Person> deleteByAgeGreaterThan(Integer age);

        List<Person> deleteByAgeGreaterThanEqual(Integer age);

        List<Person> deleteByAgeBetween(Integer age, Integer ageB);

        List<Person> deleteByAgeLessThanOrderByName(Integer age);

        List<Person> deleteByAgeLessThanOrderByNameDesc(Integer age);

        List<Person> deleteByAgeLessThanOrderByNameDescAgeAsc(Integer age);

        List<Person> deleteByAgeIn(List<Integer> ages);

        List<Person> deleteByNameIn(List<String> names);
    }

}