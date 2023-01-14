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
package org.eclipse.jnosql.mapping.column.query;

import jakarta.data.repository.Pageable;
import jakarta.data.repository.PageableRepository;
import jakarta.inject.Inject;
import org.eclipse.jnosql.communication.Condition;
import org.eclipse.jnosql.communication.TypeReference;
import org.eclipse.jnosql.communication.Value;
import org.eclipse.jnosql.communication.column.Column;
import org.eclipse.jnosql.communication.column.ColumnCondition;
import org.eclipse.jnosql.communication.column.ColumnQuery;
import org.eclipse.jnosql.mapping.Converters;
import org.eclipse.jnosql.mapping.NoSQLPage;
import org.eclipse.jnosql.mapping.column.JNoSQLColumnTemplate;
import org.eclipse.jnosql.mapping.reflection.EntitiesMetadata;
import org.eclipse.jnosql.mapping.test.entities.Person;
import org.eclipse.jnosql.mapping.test.entities.Vendor;
import org.eclipse.jnosql.mapping.test.jupiter.CDIExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

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

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.jnosql.communication.Condition.AND;
import static org.eclipse.jnosql.communication.Condition.BETWEEN;
import static org.eclipse.jnosql.communication.Condition.EQUALS;
import static org.eclipse.jnosql.communication.Condition.GREATER_THAN;
import static org.eclipse.jnosql.communication.Condition.IN;
import static org.eclipse.jnosql.communication.Condition.LESSER_EQUALS_THAN;
import static org.eclipse.jnosql.communication.Condition.LESSER_THAN;
import static org.eclipse.jnosql.communication.Condition.LIKE;
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

    private JNoSQLColumnTemplate template;

    @Inject
    private EntitiesMetadata entities;

    @Inject
    private Converters converters;

    private PersonRepository personRepository;

    private VendorRepository vendorRepository;


    @BeforeEach
    public void setUp() {
        this.template = Mockito.mock(JNoSQLColumnTemplate.class);

        ColumnRepositoryProxy personHandler = new ColumnRepositoryProxy(template,
                entities, PersonRepository.class, converters);

        ColumnRepositoryProxy vendorHandler = new ColumnRepositoryProxy(template,
                entities, VendorRepository.class, converters);

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

        Pageable pagination = getPagination();
        personRepository.findByName("name", pagination);

        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).singleResult(captor.capture());
        ColumnQuery query = captor.getValue();
        ColumnCondition condition = query.condition().get();
        assertEquals("Person", query.columnFamily());
        assertEquals(Condition.EQUALS, condition.condition());
        assertEquals(pagination.size(), query.skip());
        assertEquals(NoSQLPage.skip(pagination), query.limit());

        assertEquals(Column.of("name", "name"), condition.column());

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

        Pageable pagination = getPagination();
        List<Person> persons = personRepository.findByNameAndAge("name", 20, pagination);
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        assertThat(persons).contains(ada);

        ColumnQuery query = captor.getValue();
        assertEquals("Person", query.columnFamily());
        assertEquals(NoSQLPage.skip(pagination), query.limit());
        assertEquals(pagination.size(), query.limit());

    }

    @Test
    public void shouldFindByAgeANDName() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(ada));

        Pageable pagination = getPagination();
        Set<Person> persons = personRepository.findByAgeAndName(20, "name", pagination);
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        assertThat(persons).contains(ada);
        ColumnQuery query = captor.getValue();
        assertEquals("Person", query.columnFamily());
        assertEquals(NoSQLPage.skip(pagination), query.limit());
        assertEquals(pagination.size(), query.limit());

    }

    @Test
    public void shouldFindByNameANDAgeOrderByName() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(ada));

        Pageable pagination = getPagination();

        Stream<Person> persons = personRepository.findByNameAndAgeOrderByName("name", 20, pagination);
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        assertThat(persons.collect(Collectors.toList())).contains(ada);
        ColumnQuery query = captor.getValue();
        assertEquals("Person", query.columnFamily());
        assertEquals(NoSQLPage.skip(pagination), query.limit());
        assertEquals(pagination.size(), query.limit());

    }

    @Test
    public void shouldFindByNameANDAgeOrderByAge() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(ada));

        Pageable pagination = getPagination();
        Queue<Person> persons = personRepository.findByNameAndAgeOrderByAge("name", 20, pagination);
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        assertThat(persons).contains(ada);
        ColumnQuery query = captor.getValue();
        assertEquals("Person", query.columnFamily());
        assertEquals(NoSQLPage.skip(pagination), query.limit());
        assertEquals(pagination.size(), query.limit());


    }


    @Test
    public void shouldFindAll() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.findAll(Person.class))
                .thenReturn(Stream.of(ada));

        Pageable pagination = getPagination();

        List<Person> persons = personRepository.findAll(pagination).content();
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        ColumnQuery query = captor.getValue();
        assertFalse(query.condition().isPresent());
        assertEquals("Person", query.columnFamily());
        assertEquals(NoSQLPage.skip(pagination), query.limit());
        assertEquals(pagination.size(), query.limit());
    }


    @Test
    public void shouldFindByNameAndAgeGreaterEqualThan() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(ada));

        Pageable pagination = getPagination();
        personRepository.findByNameAndAgeGreaterThanEqual("Ada", 33, pagination);
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        ColumnQuery query = captor.getValue();
        ColumnCondition condition = query.condition().get();
        assertEquals("Person", query.columnFamily());
        assertEquals(AND, condition.condition());
        List<ColumnCondition> conditions = condition.column().get(new TypeReference<>() {
        });
        ColumnCondition columnCondition = conditions.get(0);
        ColumnCondition columnCondition2 = conditions.get(1);

        assertEquals(Condition.EQUALS, columnCondition.condition());
        assertEquals("Ada", columnCondition.column().get());
        assertEquals("name", columnCondition.column().name());

        assertEquals(Condition.GREATER_EQUALS_THAN, columnCondition2.condition());
        assertEquals(33, columnCondition2.column().get());
        assertEquals("age", columnCondition2.column().name());
        assertEquals(NoSQLPage.skip(pagination), query.limit());
        assertEquals(pagination.size(), query.limit());
    }

    @Test
    public void shouldFindByGreaterThan() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(ada));

        Pageable pagination = getPagination();
        personRepository.findByAgeGreaterThan(33, pagination);
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        ColumnQuery query = captor.getValue();
        ColumnCondition condition = query.condition().get();
        assertEquals("Person", query.columnFamily());
        assertEquals(GREATER_THAN, condition.condition());
        assertEquals(Column.of("age", 33), condition.column());
        assertEquals(NoSQLPage.skip(pagination), query.limit());
        assertEquals(pagination.size(), query.limit());

    }

    @Test
    public void shouldFindByAgeLessThanEqual() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(ada));

        Pageable pagination = getPagination();
        personRepository.findByAgeLessThanEqual(33, pagination);
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        ColumnQuery query = captor.getValue();
        ColumnCondition condition = query.condition().get();
        assertEquals("Person", query.columnFamily());
        assertEquals(LESSER_EQUALS_THAN, condition.condition());
        assertEquals(Column.of("age", 33), condition.column());
        assertEquals(NoSQLPage.skip(pagination), query.limit());
        assertEquals(pagination.size(), query.limit());

    }

    @Test
    public void shouldFindByAgeLessEqual() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(ada));

        Pageable pagination = getPagination();
        personRepository.findByAgeLessThan(33, pagination);
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        ColumnQuery query = captor.getValue();
        ColumnCondition condition = query.condition().get();
        assertEquals("Person", query.columnFamily());
        assertEquals(LESSER_THAN, condition.condition());
        assertEquals(Column.of("age", 33), condition.column());
        assertEquals(NoSQLPage.skip(pagination), query.limit());
        assertEquals(pagination.size(), query.limit());

    }

    @Test
    public void shouldFindByAgeBetween() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(ada));

        Pageable pagination = getPagination();
        personRepository.findByAgeBetween(10, 15, pagination);
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        ColumnQuery query = captor.getValue();
        ColumnCondition condition = query.condition().get();
        assertEquals("Person", query.columnFamily());
        assertEquals(BETWEEN, condition.condition());
        List<Value> values = condition.column().get(new TypeReference<>() {
        });
        assertEquals(Arrays.asList(10, 15), values.stream().map(Value::get).collect(Collectors.toList()));
        assertTrue(condition.column().name().contains("age"));
        assertEquals(NoSQLPage.skip(pagination), query.limit());
        assertEquals(pagination.size(), query.limit());
    }


    @Test
    public void shouldFindByNameLike() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(ada));

        Pageable pagination = getPagination();
        personRepository.findByNameLike("Ada", pagination);
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        ColumnQuery query = captor.getValue();
        ColumnCondition condition = query.condition().get();
        assertEquals("Person", query.columnFamily());
        assertEquals(LIKE, condition.condition());
        assertEquals(Column.of("name", "Ada"), condition.column());
        assertEquals(NoSQLPage.skip(pagination), query.limit());
        assertEquals(pagination.size(), query.limit());

    }


    @Test
    public void shouldFindByStringWhenFieldIsSet() {
        Vendor vendor = new Vendor("vendor");
        vendor.setPrefixes(Collections.singleton("prefix"));

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(vendor));

        Pageable pagination = getPagination();
        vendorRepository.findByPrefixes("prefix", pagination);

        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).singleResult(captor.capture());
        ColumnQuery query = captor.getValue();
        ColumnCondition condition = query.condition().get();
        assertEquals("vendors", query.columnFamily());
        assertEquals(EQUALS, condition.condition());
        assertEquals(Column.of("prefixes", "prefix"), condition.column());
        assertEquals(NoSQLPage.skip(pagination), query.limit());
        assertEquals(pagination.size(), query.limit());

    }

    @Test
    public void shouldFindByIn() {
        Vendor vendor = new Vendor("vendor");
        vendor.setPrefixes(Collections.singleton("prefix"));

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(vendor));

        Pageable pagination = getPagination();
        vendorRepository.findByPrefixesIn(singletonList("prefix"), pagination);

        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).singleResult(captor.capture());
        ColumnQuery query = captor.getValue();
        ColumnCondition condition = query.condition().get();
        assertEquals("vendors", query.columnFamily());
        assertEquals(IN, condition.condition());
        assertEquals(NoSQLPage.skip(pagination), query.limit());
        assertEquals(pagination.size(), query.limit());

    }

    @Test
    public void shouldConvertFieldToTheType() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(ada));

        Pageable pagination = getPagination();
        personRepository.findByAge("120", pagination);
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        ColumnQuery query = captor.getValue();
        ColumnCondition condition = query.condition().get();
        assertEquals("Person", query.columnFamily());
        assertEquals(EQUALS, condition.condition());
        assertEquals(Column.of("age", 120), condition.column());
        assertEquals(NoSQLPage.skip(pagination), query.limit());
        assertEquals(pagination.size(), query.limit());
    }

    private Pageable getPagination() {
        return Pageable.ofPage(2).size(6);
    }

    interface PersonRepository extends PageableRepository<Person, Long> {

        Person findByName(String name, Pageable pagination);

        List<Person> findByAge(String age, Pageable pagination);

        List<Person> findByNameAndAge(String name, Integer age, Pageable pagination);

        Set<Person> findByAgeAndName(Integer age, String name, Pageable pagination);

        Stream<Person> findByNameAndAgeOrderByName(String name, Integer age, Pageable pagination);

        Queue<Person> findByNameAndAgeOrderByAge(String name, Integer age, Pageable pagination);

        Set<Person> findByNameAndAgeGreaterThanEqual(String name, Integer age, Pageable pagination);

        Set<Person> findByAgeGreaterThan(Integer age, Pageable pagination);

        Set<Person> findByAgeLessThanEqual(Integer age, Pageable pagination);

        Set<Person> findByAgeLessThan(Integer age, Pageable pagination);

        Set<Person> findByAgeBetween(Integer ageA, Integer ageB, Pageable pagination);

        Set<Person> findByNameLike(String name, Pageable pagination);

    }

    public interface VendorRepository extends PageableRepository<Vendor, String> {

        Vendor findByPrefixes(String prefix, Pageable pagination);

        Vendor findByPrefixesIn(List<String> prefix, Pageable pagination);

    }
}