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
package org.eclipse.jnosql.mapping.graph.query;

import jakarta.data.exceptions.MappingException;
import jakarta.data.repository.OrderBy;
import jakarta.data.repository.PageableRepository;
import jakarta.data.repository.Param;
import jakarta.data.repository.Query;
import jakarta.inject.Inject;
import jakarta.nosql.PreparedStatement;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.eclipse.jnosql.mapping.Convert;
import org.eclipse.jnosql.mapping.Converters;
import org.eclipse.jnosql.mapping.graph.BookRepository;
import org.eclipse.jnosql.mapping.graph.GraphConverter;
import org.eclipse.jnosql.mapping.graph.GraphTemplate;
import org.eclipse.jnosql.mapping.graph.Transactional;
import org.eclipse.jnosql.mapping.graph.entities.Person;
import org.eclipse.jnosql.mapping.graph.entities.Vendor;
import org.eclipse.jnosql.mapping.graph.spi.GraphExtension;
import org.eclipse.jnosql.mapping.reflection.EntitiesMetadata;
import org.eclipse.jnosql.mapping.reflection.EntityMetadataExtension;
import org.jboss.weld.junit5.auto.AddExtensions;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@EnableAutoWeld
@AddPackages(value = {Convert.class, Transactional.class})
@AddPackages(BookRepository.class)
@AddExtensions({EntityMetadataExtension.class, GraphExtension.class})
public class GraphRepositoryProxyTest {


    private GraphTemplate template;

    @Inject
    private EntitiesMetadata entities;


    @Inject
    private Graph graph;

    @Inject
    private GraphConverter converter;

    @Inject
    private Converters converters;

    private PersonRepository personRepository;

    private VendorRepository vendorRepository;

    @BeforeEach
    public void setUp() {

        graph.traversal().V().toList().forEach(Vertex::remove);
        graph.traversal().E().toList().forEach(Edge::remove);


        this.template = Mockito.mock(GraphTemplate.class);

        GraphRepositoryProxy personHandler = new GraphRepositoryProxy(template,
                entities, PersonRepository.class, graph, converter, converters);

        GraphRepositoryProxy vendorHandler = new GraphRepositoryProxy(template,
                entities, VendorRepository.class, graph, converter, converters);


        when(template.insert(any(Person.class))).thenReturn(Person.builder().build());
        when(template.update(any(Person.class))).thenReturn(Person.builder().build());

        personRepository = (PersonRepository) Proxy.newProxyInstance(PersonRepository.class.getClassLoader(),
                new Class[]{PersonRepository.class},
                personHandler);

        vendorRepository = (VendorRepository) Proxy.newProxyInstance(VendorRepository.class.getClassLoader(),
                new Class[]{VendorRepository.class},
                vendorHandler);
    }

    @AfterEach
    public void after() {
        graph.traversal().V().toList().forEach(Vertex::remove);
        graph.traversal().E().toList().forEach(Edge::remove);

    }


    @Test
    public void shouldSaveUsingInsertWhenDataDoesNotExist() {
        when(template.find(Mockito.any(Long.class))).thenReturn(Optional.empty());

        ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);
        Person person = Person.builder().withName("Ada")
                .withId(10L)
                .withPhones(singletonList("123123"))
                .build();
        assertNotNull(personRepository.save(person));
        verify(template).insert(captor.capture());
        Person value = captor.getValue();
        assertEquals(person, value);
    }

    @Test
    public void shouldSaveUsingUpdateWhenDataExists() {
        when(template.find(Mockito.any(Long.class))).thenReturn(Optional.of(Person.builder().build()));

        ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);
        Person person = Person.builder().withName("Ada")
                .withId(10L)
                .withPhones(singletonList("123123"))
                .build();
        assertNotNull(personRepository.save(person));
        verify(template).update(captor.capture());
        Person value = captor.getValue();
        assertEquals(person, value);
    }


    @Test
    public void shouldSaveIterable() {
        when(personRepository.findById(10L)).thenReturn(Optional.empty());

        ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);
        Person person = Person.builder().withName("Ada")
                .withId(10L)
                .withPhones(singletonList("123123"))
                .build();

        personRepository.saveAll(singletonList(person));
        verify(template).insert(captor.capture());
        Person personCapture = captor.getValue();
        assertEquals(person, personCapture);
    }


    @Test
    public void shouldFindByNameInstance() {

        graph.addVertex(T.label, "Person", "name", "name", "age", 20);

        Person person = personRepository.findByName("name");
        assertNotNull(person);
        assertNull(personRepository.findByName("name2"));

    }

    @Test
    public void shouldFindByNameAndAge() {

        graph.addVertex(T.label, "Person", "name", "name", "age", 20);
        graph.addVertex(T.label, "Person", "name", "name", "age", 20);

        List<Person> people = personRepository.findByNameAndAge("name", 20);
        assertEquals(2, people.size());

    }

    @Test
    public void shouldFindByAgeAndName() {

        graph.addVertex(T.label, "Person", "name", "name", "age", 20);
        graph.addVertex(T.label, "Person", "name", "name", "age", 20);

        Set<Person> people = personRepository.findByAgeAndName(20, "name");
        assertEquals(2, people.size());

    }

    @Test
    public void shouldFindByAge() {

        graph.addVertex(T.label, "Person", "name", "name", "age", 20);

        Optional<Person> person = personRepository.findByAge(20);
        assertTrue(person.isPresent());

    }

    @Test
    public void shouldDeleteByName() {
        Vertex vertex = graph.addVertex(T.label, "Person", "name", "Ada", "age", 20);

        personRepository.deleteByName("Ada");
        assertFalse(graph.traversal().V(vertex.id()).tryNext().isPresent());

    }

    @Test
    public void shouldFindByNameAndAgeGreaterThanEqual() {

        graph.addVertex(T.label, "Person", "name", "name", "age", 20);
        graph.addVertex(T.label, "Person", "name", "name", "age", 20);

        Set<Person> people = personRepository.findByNameAndAgeGreaterThanEqual("name", 20);
        assertEquals(2, people.size());

    }

    @Test
    public void shouldFindById() {
        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        personRepository.findById(10L);
        verify(template).find(captor.capture());

        Object id = captor.getValue();

        assertEquals(10L, id);
    }

    @Test
    public void shouldFindByIds() {

        when(template.find(any(Object.class))).thenReturn(Optional.empty());
        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        personRepository.findAllById(singletonList(10L)).collect(Collectors.toUnmodifiableList());
        verify(template).find(captor.capture());

        personRepository.findAllById(asList(1L, 2L, 3L)).collect(Collectors.toUnmodifiableList());
        verify(template, times(4)).find(any(Long.class));
    }

    @Test
    public void shouldDeleteById() {
        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        personRepository.deleteById(10L);
        verify(template).delete(captor.capture());

        assertEquals(captor.getValue(), 10L);
    }

    @Test
    public void shouldDeleteByIds() {
        personRepository.deleteAllById(singletonList(10L));
        verify(template).delete(10L);

        personRepository.deleteAllById(asList(1L, 2L, 3L));
        verify(template, times(4)).delete(any(Long.class));
    }


    @Test
    public void shouldContainsById() {
        when(template.find(any(Long.class))).thenReturn(Optional.of(Person.builder().build()));

        assertTrue(personRepository.existsById(10L));
        verify(template).find(any(Long.class));

        when(template.find(any(Long.class))).thenReturn(Optional.empty());
        assertFalse(personRepository.existsById(10L));

    }

    @Test
    public void shouldFindAll() {
        List<Person> people = personRepository.findAll().collect(Collectors.toUnmodifiableList());
        verify(template).findAll(Person.class);
    }


    @Test
    public void shouldDeleteAll() {
        personRepository.deleteAll();
        verify(template).deleteAll(Person.class);
    }

    @Test
    public void shouldReturnEmptyAtFindAll() {
        List<Person> people = personRepository.findAll().collect(Collectors.toUnmodifiableList());
        assertTrue(people.isEmpty());
    }


    @Test
    public void shouldReturnToString() {
        assertNotNull(personRepository.toString());
    }

    @Test
    public void shouldReturnHasCode() {
        assertNotNull(personRepository.hashCode());
        assertEquals(personRepository.hashCode(), personRepository.hashCode());
    }

    @Test
    public void shouldExecuteQuery() {
        graph.addVertex(T.label, "Person", "name", "name", "age", 20);
        graph.addVertex(T.label, "Person", "name", "name", "age", 20);
        personRepository.findByQuery();
        when(template.query("g.V().hasLabel('Person').toList()"))
                .thenReturn(Stream.of(Person.builder().build()));

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        verify(template).query(captor.capture());
        assertEquals("g.V().hasLabel('Person').toList()", captor.getValue());
    }

    @Test
    public void shouldFindByActiveTrue() {
        graph.addVertex(T.label, "Person", "name", "Otavio", "age", 30, "active", false);
        graph.addVertex(T.label, "Person", "name", "Poliana", "age", 20, "active", true);
        graph.addVertex(T.label, "Person", "name", "Ada", "age", 30, "active", true);
        graph.addVertex(T.label, "Person", "name", "Otavio", "age", 15, "active", false);

        List<Person> people = personRepository.findByActiveTrue();
        assertThat(people).hasSize(2).map(Person::getName)
                .contains("Ada", "Poliana");
    }

    @Test
    public void shouldFindByActiveFalse() {
        graph.addVertex(T.label, "Person", "name", "Otavio", "age", 30, "active", false);
        graph.addVertex(T.label, "Person", "name", "Poliana", "age", 20, "active", true);
        graph.addVertex(T.label, "Person", "name", "Ada", "age", 30, "active", true);
        graph.addVertex(T.label, "Person", "name", "Otavio", "age", 15, "active", false);

        List<Person> people = personRepository.findByActiveFalse();
        assertThat(people).hasSize(2).map(Person::getName)
                .contains("Otavio", "Otavio");
    }

    @Test
    public void shouldFindByNameEquals() {
        graph.addVertex(T.label, "Person", "name", "Otavio", "age", 30, "active", false);
        graph.addVertex(T.label, "Person", "name", "Poliana", "age", 20, "active", false);
        graph.addVertex(T.label, "Person", "name", "Ada", "age", 30, "active", false);
        graph.addVertex(T.label, "Person", "name", "Otavio", "age", 15, "active", false);
        List<Person> people = personRepository.findByNameNotEquals("Otavio");

        assertThat(people).hasSize(2).map(Person::getName)
                .contains("Poliana", "Ada");
    }

    @Test
    public void shouldFindByAgeNotGreaterThan() {
        graph.addVertex(T.label, "Person", "name", "Otavio", "age", 30, "active", false);
        graph.addVertex(T.label, "Person", "name", "Poliana", "age", 20, "active", false);
        graph.addVertex(T.label, "Person", "name", "Ada", "age", 30, "active", false);
        graph.addVertex(T.label, "Person", "name", "Rafa", "age", 15, "active", false);
        List<Person> people = personRepository.findByAgeNotGreaterThan(20);

        assertThat(people).hasSize(2).map(Person::getName)
                .contains("Rafa", "Poliana");
    }

    @Test
    public void shouldCountByActiveFalse() {
        graph.addVertex(T.label, "Person", "name", "Otavio", "age", 30, "active", false);
        graph.addVertex(T.label, "Person", "name", "Poliana", "age", 20, "active", true);
        graph.addVertex(T.label, "Person", "name", "Ada", "age", 30, "active", true);
        graph.addVertex(T.label, "Person", "name", "Otavio", "age", 15, "active", false);

        Long count = personRepository.countByActiveTrue();
        assertEquals(2, count);
    }

    @Test
    public void shouldExistsByActiveFalse() {
        graph.addVertex(T.label, "Person", "name", "Otavio", "age", 30, "active", false);
        graph.addVertex(T.label, "Person", "name", "Poliana", "age", 20, "active", true);
        graph.addVertex(T.label, "Person", "name", "Ada", "age", 30, "active", true);
        graph.addVertex(T.label, "Person", "name", "Otavio", "age", 15, "active", false);

        boolean count = personRepository.existsByActiveTrue();
        assertTrue(count);
    }

    @Test
    public void shouldExistsByActiveFalse2() {
        graph.addVertex(T.label, "Person", "name", "Otavio", "age", 30, "active", false);
        graph.addVertex(T.label, "Person", "name", "Poliana", "age", 20, "active", false);
        graph.addVertex(T.label, "Person", "name", "Ada", "age", 30, "active", false);
        graph.addVertex(T.label, "Person", "name", "Otavio", "age", 15, "active", false);

        boolean count = personRepository.existsByActiveTrue();
        assertFalse(count);
    }

    @Test
    public void shouldGotOrderException() {
        Assertions.assertThrows(MappingException.class, () ->
                personRepository.findBy());
    }

    @Test
    public void shouldGotOrderException2() {
        Assertions.assertThrows(MappingException.class, () ->
                personRepository.findByException());
    }
    @Test
    public void shouldExecuteQuery2() {

        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        when(template.prepare(Mockito.anyString()))
                .thenReturn(preparedStatement);

        personRepository.findByQuery("Ada");
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        verify(template).prepare(captor.capture());
        assertEquals("g.V().hasLabel('Person').has('name', name).toList()", captor.getValue());
        verify(preparedStatement).bind("name", "Ada");
    }

    @Test
    public void shouldFindByStringWhenFieldIsSet() {
        Vendor vendor = new Vendor("vendor");
        vendor.setPrefixes(Collections.singleton("prefix"));

        graph.addVertex(T.label, "vendors", "name", "name", "prefixes", "prefix");


        Vendor prefix = vendorRepository.findByPrefixes("prefix");
        Assertions.assertNotNull(prefix);
    }

    @Test
    public void shouldFindByIn() {
        Vendor vendor = new Vendor("vendor");
        vendor.setPrefixes(Collections.singleton("prefix"));

        graph.addVertex(T.label, "vendors", "name", "name", "prefixes", "prefix");
        graph.addVertex(T.label, "vendors", "name", "name", "prefixes", "prefix1");
        graph.addVertex(T.label, "vendors", "name", "name", "prefixes", "prefix2");

        Vendor prefix = vendorRepository.findByPrefixesIn(Collections.singletonList("prefix"));
        Assertions.assertNotNull(prefix);

    }


    interface PersonRepository extends PageableRepository<Person, Long> {

        List<Person> findByActiveTrue();

        List<Person> findByActiveFalse();

        Long countByActiveTrue();

        boolean existsByActiveTrue();

        Person findByName(String name);

        List<Person> findByNameNotEquals(String name);

        List<Person> findByAgeNotGreaterThan(Integer age);

        void deleteByName(String name);

        Optional<Person> findByAge(Integer age);

        List<Person> findByNameAndAge(String name, Integer age);

        Set<Person> findByAgeAndName(Integer age, String name);

        Set<Person> findByNameAndAgeGreaterThanEqual(String name, Integer age);

        @Query("g.V().hasLabel('Person').toList()")
        List<Person> findByQuery();

        @Query("g.V().hasLabel('Person').has('name', name).toList()")
        List<Person> findByQuery(@Param("name") String name);

        @OrderBy("name")
        List<Person> findBy();

        @OrderBy("name")
        @OrderBy("age")
        List<Person> findByException();
    }

    public interface VendorRepository extends PageableRepository<Vendor, String> {

        Vendor findByPrefixes(String prefix);

        Vendor findByPrefixesIn(List<String> prefix);

    }
}
