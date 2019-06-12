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
package org.jnosql.artemis.graph.query;

import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.jnosql.artemis.Converters;
import org.jnosql.artemis.Pagination;
import jakarta.nosql.mapping.Repository;
import org.jnosql.artemis.Sorts;
import org.jnosql.artemis.graph.GraphConverter;
import org.jnosql.artemis.graph.GraphTemplate;
import org.jnosql.artemis.graph.cdi.CDIExtension;
import org.jnosql.artemis.graph.model.Person;
import org.jnosql.artemis.reflection.ClassMappings;
import org.jnosql.diana.Sort;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import javax.inject.Inject;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(CDIExtension.class)
public class GraphRepositoryProxySortTest {


    private GraphTemplate template;

    @Inject
    private ClassMappings classMappings;


    @Inject
    private Graph graph;

    @Inject
    private GraphConverter converter;

    @Inject
    private Converters converters;

    private PersonRepository personRepository;

    @BeforeEach
    public void setUp() {

        graph.traversal().V().toList().forEach(Vertex::remove);
        graph.traversal().E().toList().forEach(Edge::remove);


        this.template = Mockito.mock(GraphTemplate.class);

        GraphRepositoryProxy personHandler = new GraphRepositoryProxy(template,
                classMappings, PersonRepository.class, graph, converter, converters);


        when(template.insert(any(Person.class))).thenReturn(Person.builder().build());
        when(template.update(any(Person.class))).thenReturn(Person.builder().build());

        personRepository = (PersonRepository) Proxy.newProxyInstance(PersonRepository.class.getClassLoader(),
                new Class[]{PersonRepository.class},
                personHandler);

        graph.addVertex(T.label, "Person", "name", "name1", "age", 21);
        graph.addVertex(T.label, "Person", "name", "name2", "age", 22);
        graph.addVertex(T.label, "Person", "name", "name3", "age", 23);
        graph.addVertex(T.label, "Person", "name", "name4", "age", 24);
        graph.addVertex(T.label, "Person", "name", "name5", "age", 25);
        graph.addVertex(T.label, "Person", "name", "name6", "age", 26);
    }

    @AfterEach
    public void after() {
        graph.traversal().V().toList().forEach(Vertex::remove);
        graph.traversal().E().toList().forEach(Edge::remove);

    }

    @Test
    public void shouldFindAll() {

        List<Person> people = personRepository.findAll(Pagination.page(1).size(2), Sorts.sorts().desc("name"));
        assertEquals(2, people.size());

        List<String> names = people.stream().map(Person::getName).collect(Collectors.toList());
        MatcherAssert.assertThat(names, Matchers.contains("name6", "name5"));
    }

    @Test
    public void shouldFindByName() {

        List<Person> people = personRepository.findByName("name1", Pagination.page(1).size(2), Sort.desc("name"));
        assertEquals(1, people.size());

        List<String> names = people.stream().map(Person::getName).collect(Collectors.toList());
        MatcherAssert.assertThat(names, Matchers.contains("name1"));
    }


    @Test
    public void shouldFindByAgeGreaterThan() {
        List<Person> people = personRepository.findByAgeGreaterThan(22, Sort.desc("name"));
        Assertions.assertEquals(4, people.size());
        List<String> names = people.stream().map(Person::getName).collect(Collectors.toList());
        MatcherAssert.assertThat(names, Matchers.contains("name6", "name5", "name4", "name3"));

    }

    @Test
    public void shouldFindByNameAndAgeGreaterThan() {
        List<Person> people = personRepository.findByNameAndAgeGreaterThan("name4", 22, Sorts.sorts().desc("name"));
        Assertions.assertEquals(1, people.size());
        List<String> names = people.stream().map(Person::getName).collect(Collectors.toList());
        MatcherAssert.assertThat(names, Matchers.contains("name4"));
    }

    @Test
    public void shouldFindByGreaterThanAgeOrderByName() {
        List<Person> people = personRepository.findByAgeGreaterThanOrderByName(22, Pagination.page(2).size(3), Sort.desc("age"));
        Assertions.assertEquals(1, people.size());
        List<String> names = people.stream().map(Person::getName).collect(Collectors.toList());
        MatcherAssert.assertThat(names, Matchers.contains("name3"));
    }

    @Test
    public void shouldFindByGreaterThanAgeOrderByName2() {
        List<Person> people = personRepository.findByAgeGreaterThanOrderByName(22, Pagination.page(2).size(3), Sorts.sorts().desc("age"));
        Assertions.assertEquals(1, people.size());
        List<String> names = people.stream().map(Person::getName).collect(Collectors.toList());
        MatcherAssert.assertThat(names, Matchers.contains("name3"));
    }



    interface PersonRepository extends Repository<Person, Long> {

        List<Person> findAll(Pagination pagination, Sorts sorts);

        List<Person> findByName(String name, Pagination pagination, Sort sort);

        List<Person> findByAgeGreaterThan(Integer age, Sort sort);

        List<Person> findByNameAndAgeGreaterThan(String name, Integer age, Sorts sorts);

        List<Person> findByAgeGreaterThanOrderByName(int age, Pagination pagination, Sort sort);

        List<Person> findByAgeGreaterThanOrderByName(int age, Pagination pagination, Sorts sorts);


    }

}
