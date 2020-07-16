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
package org.eclipse.jnosql.artemis.column.query;

import jakarta.nosql.Condition;
import jakarta.nosql.TypeReference;
import jakarta.nosql.Value;
import jakarta.nosql.column.Column;
import jakarta.nosql.column.ColumnCondition;
import jakarta.nosql.column.ColumnQuery;
import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.Pagination;
import jakarta.nosql.mapping.Repository;
import jakarta.nosql.mapping.column.ColumnTemplate;
import org.eclipse.jnosql.artemis.reflection.ClassMappings;
import jakarta.nosql.tck.entities.Person;
import jakarta.nosql.tck.entities.Vendor;
import jakarta.nosql.tck.test.CDIExtension;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import javax.inject.Inject;
import java.lang.reflect.Proxy;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static jakarta.nosql.Condition.AND;
import static jakarta.nosql.Condition.BETWEEN;
import static jakarta.nosql.Condition.EQUALS;
import static jakarta.nosql.Condition.GREATER_THAN;
import static jakarta.nosql.Condition.IN;
import static jakarta.nosql.Condition.LESSER_EQUALS_THAN;
import static jakarta.nosql.Condition.LESSER_THAN;
import static jakarta.nosql.Condition.LIKE;
import static java.util.Collections.singletonList;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@CDIExtension
public class ColumnRepositoryProxyPaginationTest {

    private ColumnTemplate template;

    @Inject
    private ClassMappings classMappings;

    @Inject
    private Converters converters;

    private PersonRepository personRepository;

    private VendorRepository vendorRepository;


    @BeforeEach
    public void setUp() {
        this.template = Mockito.mock(ColumnTemplate.class);

        ColumnRepositoryProxy personHandler = new ColumnRepositoryProxy(template,
                classMappings, PersonRepository.class, converters);

        ColumnRepositoryProxy vendorHandler = new ColumnRepositoryProxy(template,
                classMappings, VendorRepository.class, converters);

        when(template.insert(any(Person.class))).thenReturn(Person.builder().build());
        when(template.insert(any(Person.class), any(Duration.class))).thenReturn(Person.builder().build());
        when(template.update(any(Person.class))).thenReturn(Person.builder().build());

        personRepository = (PersonRepository) Proxy.newProxyInstance(PersonRepository.class.getClassLoader(),
                new Class[]{PersonRepository.class},
                personHandler);
        vendorRepository = (VendorRepository) Proxy.newProxyInstance(VendorRepository.class.getClassLoader(),
                new Class[]{VendorRepository.class}, vendorHandler);
    }




    @Test
    public void shouldFindByNameInstance() {

        when(template.singleResult(any(ColumnQuery.class))).thenReturn(Optional
                .of(Person.builder().build()));

        Pagination pagination = getPagination();
        personRepository.findByName("name", pagination);

        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).singleResult(captor.capture());
        ColumnQuery query = captor.getValue();
        ColumnCondition condition = query.getCondition().get();
        assertEquals("Person", query.getColumnFamily());
        assertEquals(Condition.EQUALS, condition.getCondition());
        assertEquals(pagination.getSkip(), query.getSkip());
        assertEquals(pagination.getLimit(), query.getLimit());

        assertEquals(Column.of("name", "name"), condition.getColumn());

        assertNotNull(personRepository.findByName("name", pagination));
        when(template.singleResult(any(ColumnQuery.class))).thenReturn(Optional
                .empty());

        assertNull(personRepository.findByName("name", pagination));


    }

    @Test
    public void shouldFindByNameANDAge() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(ada));

        Pagination pagination = getPagination();
        List<Person> persons = personRepository.findByNameAndAge("name", 20, pagination);
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        assertThat(persons, Matchers.contains(ada));

        ColumnQuery query = captor.getValue();
        assertEquals("Person", query.getColumnFamily());
        assertEquals(pagination.getSkip(), query.getSkip());
        assertEquals(pagination.getLimit(), query.getLimit());

    }

    @Test
    public void shouldFindByAgeANDName() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(ada));

        Pagination pagination = getPagination();
        Set<Person> persons = personRepository.findByAgeAndName(20, "name", pagination);
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        assertThat(persons, Matchers.contains(ada));
        ColumnQuery query = captor.getValue();
        assertEquals("Person", query.getColumnFamily());
        assertEquals(pagination.getSkip(), query.getSkip());
        assertEquals(pagination.getLimit(), query.getLimit());

    }

    @Test
    public void shouldFindByNameANDAgeOrderByName() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(ada));

        Pagination pagination = getPagination();

        Stream<Person> persons = personRepository.findByNameAndAgeOrderByName("name", 20, pagination);
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        assertThat(persons.collect(Collectors.toList()), Matchers.contains(ada));
        ColumnQuery query = captor.getValue();
        assertEquals("Person", query.getColumnFamily());
        assertEquals(pagination.getSkip(), query.getSkip());
        assertEquals(pagination.getLimit(), query.getLimit());

    }

    @Test
    public void shouldFindByNameANDAgeOrderByAge() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(ada));

        Pagination pagination = getPagination();
        Queue<Person> persons = personRepository.findByNameAndAgeOrderByAge("name", 20, pagination);
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        assertThat(persons, Matchers.contains(ada));
        ColumnQuery query = captor.getValue();
        assertEquals("Person", query.getColumnFamily());
        assertEquals(pagination.getSkip(), query.getSkip());
        assertEquals(pagination.getLimit(), query.getLimit());


    }


    @Test
    public void shouldFindAll() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(ada));

        Pagination pagination = getPagination();

        List<Person> persons = personRepository.findAll(pagination);
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        ColumnQuery query = captor.getValue();
        assertFalse(query.getCondition().isPresent());
        assertEquals("Person", query.getColumnFamily());
        assertEquals(pagination.getSkip(), query.getSkip());
        assertEquals(pagination.getLimit(), query.getLimit());
    }


    @Test
    public void shouldFindByNameAndAgeGreaterEqualThan() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(ada));

        Pagination pagination = getPagination();
        personRepository.findByNameAndAgeGreaterThanEqual("Ada", 33, pagination);
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        ColumnQuery query = captor.getValue();
        ColumnCondition condition = query.getCondition().get();
        assertEquals("Person", query.getColumnFamily());
        assertEquals(AND, condition.getCondition());
        List<ColumnCondition> conditions = condition.getColumn().get(new TypeReference<List<ColumnCondition>>() {
        });
        ColumnCondition columnCondition = conditions.get(0);
        ColumnCondition columnCondition2 = conditions.get(1);

        assertEquals(Condition.EQUALS, columnCondition.getCondition());
        assertEquals("Ada", columnCondition.getColumn().get());
        assertEquals("name", columnCondition.getColumn().getName());

        assertEquals(Condition.GREATER_EQUALS_THAN, columnCondition2.getCondition());
        assertEquals(33, columnCondition2.getColumn().get());
        assertEquals("age", columnCondition2.getColumn().getName());
        assertEquals(pagination.getSkip(), query.getSkip());
        assertEquals(pagination.getLimit(), query.getLimit());
    }

    @Test
    public void shouldFindByGreaterThan() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(ada));

        Pagination pagination = getPagination();
        personRepository.findByAgeGreaterThan(33, pagination);
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        ColumnQuery query = captor.getValue();
        ColumnCondition condition = query.getCondition().get();
        assertEquals("Person", query.getColumnFamily());
        assertEquals(GREATER_THAN, condition.getCondition());
        assertEquals(Column.of("age", 33), condition.getColumn());
        assertEquals(pagination.getSkip(), query.getSkip());
        assertEquals(pagination.getLimit(), query.getLimit());

    }

    @Test
    public void shouldFindByAgeLessThanEqual() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(ada));

        Pagination pagination = getPagination();
        personRepository.findByAgeLessThanEqual(33, pagination);
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        ColumnQuery query = captor.getValue();
        ColumnCondition condition = query.getCondition().get();
        assertEquals("Person", query.getColumnFamily());
        assertEquals(LESSER_EQUALS_THAN, condition.getCondition());
        assertEquals(Column.of("age", 33), condition.getColumn());
        assertEquals(pagination.getSkip(), query.getSkip());
        assertEquals(pagination.getLimit(), query.getLimit());

    }

    @Test
    public void shouldFindByAgeLessEqual() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(ada));

        Pagination pagination = getPagination();
        personRepository.findByAgeLessThan(33, pagination);
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        ColumnQuery query = captor.getValue();
        ColumnCondition condition = query.getCondition().get();
        assertEquals("Person", query.getColumnFamily());
        assertEquals(LESSER_THAN, condition.getCondition());
        assertEquals(Column.of("age", 33), condition.getColumn());
        assertEquals(pagination.getSkip(), query.getSkip());
        assertEquals(pagination.getLimit(), query.getLimit());

    }

    @Test
    public void shouldFindByAgeBetween() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(ada));

        Pagination pagination = getPagination();
        personRepository.findByAgeBetween(10,15, pagination);
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        ColumnQuery query = captor.getValue();
        ColumnCondition condition = query.getCondition().get();
        assertEquals("Person", query.getColumnFamily());
        assertEquals(BETWEEN, condition.getCondition());
        List<Value> values = condition.getColumn().get(new TypeReference<List<Value>>() {
        });
        assertEquals(Arrays.asList(10, 15), values.stream().map(Value::get).collect(Collectors.toList()));
        assertTrue(condition.getColumn().getName().contains("age"));
        assertEquals(pagination.getSkip(), query.getSkip());
        assertEquals(pagination.getLimit(), query.getLimit());
    }


    @Test
    public void shouldFindByNameLike() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(ada));

        Pagination pagination = getPagination();
        personRepository.findByNameLike("Ada", pagination);
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        ColumnQuery query = captor.getValue();
        ColumnCondition condition = query.getCondition().get();
        assertEquals("Person", query.getColumnFamily());
        assertEquals(LIKE, condition.getCondition());
        assertEquals(Column.of("name", "Ada"), condition.getColumn());
        assertEquals(pagination.getSkip(), query.getSkip());
        assertEquals(pagination.getLimit(), query.getLimit());

    }


    @Test
    public void shouldFindByStringWhenFieldIsSet() {
        Vendor vendor = new Vendor("vendor");
        vendor.setPrefixes(Collections.singleton("prefix"));

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(vendor));

        Pagination pagination = getPagination();
        vendorRepository.findByPrefixes("prefix", pagination);

        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).singleResult(captor.capture());
        ColumnQuery query = captor.getValue();
        ColumnCondition condition = query.getCondition().get();
        assertEquals("vendors", query.getColumnFamily());
        assertEquals(EQUALS, condition.getCondition());
        assertEquals(Column.of("prefixes", "prefix"), condition.getColumn());
        assertEquals(pagination.getSkip(), query.getSkip());
        assertEquals(pagination.getLimit(), query.getLimit());

    }

    @Test
    public void shouldFindByIn() {
        Vendor vendor = new Vendor("vendor");
        vendor.setPrefixes(Collections.singleton("prefix"));

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(vendor));

        Pagination pagination = getPagination();
        vendorRepository.findByPrefixesIn(singletonList("prefix"), pagination);

        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).singleResult(captor.capture());
        ColumnQuery query = captor.getValue();
        ColumnCondition condition = query.getCondition().get();
        assertEquals("vendors", query.getColumnFamily());
        assertEquals(IN, condition.getCondition());
        assertEquals(pagination.getSkip(), query.getSkip());
        assertEquals(pagination.getLimit(), query.getLimit());

    }

    @Test
    public void shouldConvertFieldToTheType() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(ada));

        Pagination pagination = getPagination();
        personRepository.findByAge("120", pagination);
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        ColumnQuery query = captor.getValue();
        ColumnCondition condition = query.getCondition().get();
        assertEquals("Person", query.getColumnFamily());
        assertEquals(EQUALS, condition.getCondition());
        assertEquals(Column.of("age", 120), condition.getColumn());
        assertEquals(pagination.getSkip(), query.getSkip());
        assertEquals(pagination.getLimit(), query.getLimit());
    }

    private Pagination getPagination() {
        return Pagination.page(current().nextLong(1, 10)).size(current().nextLong(1, 10));
    }
    interface PersonRepository extends Repository<Person, Long> {

        List<Person> findAll(Pagination pagination);

        Person findByName(String name, Pagination pagination);

        List<Person> findByAge(String age, Pagination pagination);

        List<Person> findByNameAndAge(String name, Integer age, Pagination pagination);

        Set<Person> findByAgeAndName(Integer age, String name, Pagination pagination);

        Stream<Person> findByNameAndAgeOrderByName(String name, Integer age, Pagination pagination);

        Queue<Person> findByNameAndAgeOrderByAge(String name, Integer age, Pagination pagination);

        Set<Person> findByNameAndAgeGreaterThanEqual(String name, Integer age, Pagination pagination);

        Set<Person> findByAgeGreaterThan(Integer age, Pagination pagination);

        Set<Person> findByAgeLessThanEqual(Integer age, Pagination pagination);

        Set<Person> findByAgeLessThan(Integer age, Pagination pagination);

        Set<Person> findByAgeBetween(Integer ageA, Integer ageB, Pagination pagination);

        Set<Person> findByNameLike(String name, Pagination pagination);

    }

    public interface VendorRepository extends Repository<Vendor, String> {

        Vendor findByPrefixes(String prefix, Pagination pagination);

        Vendor findByPrefixesIn(List<String> prefix, Pagination pagination);

    }
}