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
import jakarta.nosql.Sort;
import jakarta.nosql.column.Column;
import jakarta.nosql.column.ColumnCondition;
import jakarta.nosql.column.ColumnQuery;
import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.Pagination;
import jakarta.nosql.mapping.Repository;
import jakarta.nosql.mapping.Sorts;
import jakarta.nosql.mapping.column.ColumnTemplate;
import jakarta.nosql.mapping.reflection.ClassMappings;
import org.eclipse.jnosql.artemis.CDIExtension;
import org.eclipse.jnosql.artemis.model.Person;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import javax.inject.Inject;
import java.lang.reflect.Proxy;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static jakarta.nosql.Condition.AND;
import static jakarta.nosql.Condition.EQUALS;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(CDIExtension.class)
public class ColumnRepositoryProxySortTest {

    private ColumnTemplate template;

    @Inject
    private ClassMappings classMappings;

    @Inject
    private Converters converters;

    private PersonRepository personRepository;

    @BeforeEach
    public void setUp() {
        this.template = Mockito.mock(ColumnTemplate.class);

        ColumnRepositoryProxy personHandler = new ColumnRepositoryProxy(template,
                classMappings, PersonRepository.class, converters);

        when(template.insert(any(Person.class))).thenReturn(Person.builder().build());
        when(template.insert(any(Person.class), any(Duration.class))).thenReturn(Person.builder().build());
        when(template.update(any(Person.class))).thenReturn(Person.builder().build());

        personRepository = (PersonRepository) Proxy.newProxyInstance(PersonRepository.class.getClassLoader(),
                new Class[]{PersonRepository.class},
                personHandler);
    }

    @Test
    public void shouldFindAll() {

        when(template.select(any(ColumnQuery.class))).thenReturn(Stream.of(Person.builder().build()));

        Pagination pagination = getPagination();
        personRepository.findAll(pagination, Sorts.sorts().asc("name"));

        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        ColumnQuery query = captor.getValue();
        assertEquals("Person", query.getColumnFamily());
        assertEquals(pagination.getSkip(), query.getSkip());
        assertEquals(pagination.getLimit(), query.getLimit());
        assertThat(query.getSorts(), Matchers.contains(Sort.asc("name")));

    }

    @Test
    public void shouldFindByName() {

        when(template.singleResult(any(ColumnQuery.class))).thenReturn(Optional
                .of(Person.builder().build()));

        Pagination pagination = getPagination();
        personRepository.findByName("name", pagination, Sort.desc("name"));

        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).singleResult(captor.capture());
        ColumnQuery query = captor.getValue();
        ColumnCondition condition = query.getCondition().get();
        assertEquals("Person", query.getColumnFamily());
        assertEquals(Condition.EQUALS, condition.getCondition());
        assertEquals(pagination.getSkip(), query.getSkip());
        assertEquals(pagination.getLimit(), query.getLimit());
        assertThat(query.getSorts(), Matchers.contains(Sort.desc("name")));
        assertEquals(Column.of("name", "name"), condition.getColumn());

        assertNotNull(personRepository.findByName("name", pagination, Sort.asc("name")));
        when(template.singleResult(any(ColumnQuery.class))).thenReturn(Optional
                .empty());

        assertNull(personRepository.findByName("name", pagination, Sort.asc("name")));

    }

    @Test
    public void shouldFindByAge() {

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(Person.builder().build()));

        personRepository.findByAge(10, Sort.desc("name"));

        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        ColumnQuery query = captor.getValue();
        ColumnCondition condition = query.getCondition().get();
        assertEquals("Person", query.getColumnFamily());
        assertEquals(Condition.EQUALS, condition.getCondition());
        assertEquals(0, query.getSkip());
        assertEquals(0, query.getLimit());
        assertThat(query.getSorts(), Matchers.contains(Sort.desc("name")));
        assertEquals(Column.of("age", 10), condition.getColumn());

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(Person.builder().build()));
        assertNotNull(personRepository.findByAge(10, Sort.asc("name")));
        when(template.singleResult(any(ColumnQuery.class))).thenReturn(Optional
                .empty());

    }


    @Test
    public void shouldFindByNameAndAge() {

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(Person.builder().build()));

        personRepository.findByNameAndAge("name", 10, Sorts.sorts().desc("name"));

        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        ColumnQuery query = captor.getValue();
        ColumnCondition condition = query.getCondition().get();
        assertEquals("Person", query.getColumnFamily());
        assertEquals(AND, condition.getCondition());
        assertEquals(0, query.getSkip());
        assertEquals(0, query.getLimit());
        assertThat(query.getSorts(), Matchers.contains(Sort.desc("name")));

    }

    @Test
    public void shouldFindByNameOrderByName() {

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of((Person.builder().build())));

        Pagination pagination = getPagination();
        personRepository.findByNameOrderByName("name", pagination, Sort.desc("age"));

        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        ColumnQuery query = captor.getValue();
        ColumnCondition condition = query.getCondition().get();
        assertEquals("Person", query.getColumnFamily());
        assertEquals(EQUALS, condition.getCondition());
        assertEquals(Column.of("name", "name"), condition.getColumn());
        assertEquals(pagination.getSkip(), query.getSkip());
        assertEquals(pagination.getLimit(), query.getLimit());
        assertThat(query.getSorts(), Matchers.contains(Sort.asc("name"), Sort.desc("age")));

    }

    @Test
    public void shouldFindByNameOrderByName2() {

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of((Person.builder().build())));

        Pagination pagination = getPagination();
        personRepository.findByNameOrderByName("name", pagination, Sorts.sorts().desc("age").asc("phone"));

        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        ColumnQuery query = captor.getValue();
        ColumnCondition condition = query.getCondition().get();
        assertEquals("Person", query.getColumnFamily());
        assertEquals(EQUALS, condition.getCondition());
        assertEquals(Column.of("name", "name"), condition.getColumn());
        assertEquals(pagination.getSkip(), query.getSkip());
        assertEquals(pagination.getLimit(), query.getLimit());
        assertThat(query.getSorts(), Matchers.contains(Sort.asc("name"), Sort.desc("age"), Sort.asc("phone")));

    }


    private Pagination getPagination() {
        return Pagination.page(current().nextLong(1, 10)).size(current().nextLong(1, 10));
    }

    interface PersonRepository extends Repository<Person, Long> {

        List<Person> findAll(Pagination pagination, Sorts sorts);

        Person findByName(String name, Pagination pagination, Sort sort);

        List<Person> findByAge(Integer age, Sort sort);

        List<Person> findByNameAndAge(String name, Integer age, Sorts sorts);

        List<Person> findByNameOrderByName(String name, Pagination pagination, Sort sort);

        List<Person> findByNameOrderByName(String name, Pagination pagination, Sorts sorts);


    }
}