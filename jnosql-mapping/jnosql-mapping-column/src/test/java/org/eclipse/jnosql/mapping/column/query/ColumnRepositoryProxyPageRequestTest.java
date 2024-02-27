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

import jakarta.data.Limit;
import jakarta.data.page.Page;
import jakarta.data.page.PageRequest;
import jakarta.data.repository.BasicRepository;
import jakarta.data.page.Slice;
import jakarta.data.Sort;
import jakarta.inject.Inject;
import org.eclipse.jnosql.communication.Condition;
import org.eclipse.jnosql.communication.TypeReference;
import org.eclipse.jnosql.communication.Value;
import org.eclipse.jnosql.communication.column.Column;
import org.eclipse.jnosql.communication.column.ColumnCondition;
import org.eclipse.jnosql.communication.column.ColumnQuery;
import org.eclipse.jnosql.mapping.core.Converters;
import org.eclipse.jnosql.mapping.core.NoSQLPage;
import org.eclipse.jnosql.mapping.column.ColumnEntityConverter;
import org.eclipse.jnosql.mapping.column.JNoSQLColumnTemplate;
import org.eclipse.jnosql.mapping.column.MockProducer;
import org.eclipse.jnosql.mapping.column.entities.Person;
import org.eclipse.jnosql.mapping.column.entities.Vendor;
import org.eclipse.jnosql.mapping.column.spi.ColumnExtension;
import org.eclipse.jnosql.mapping.metadata.EntitiesMetadata;
import org.eclipse.jnosql.mapping.reflection.Reflections;
import org.eclipse.jnosql.mapping.core.spi.EntityMetadataExtension;
import org.jboss.weld.junit5.auto.AddExtensions;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Assertions;
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
import static org.eclipse.jnosql.communication.Condition.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@EnableAutoWeld
@AddPackages(value = {Converters.class, ColumnEntityConverter.class})
@AddPackages(MockProducer.class)
@AddPackages(Reflections.class)
@AddExtensions({EntityMetadataExtension.class, ColumnExtension.class})
public class ColumnRepositoryProxyPageRequestTest {

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

        PageRequest pageRequest = getPageRequest();
        personRepository.findByName("name", pageRequest);

        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).singleResult(captor.capture());
        ColumnQuery query = captor.getValue();
        ColumnCondition condition = query.condition().get();
        assertEquals("Person", query.name());
        assertEquals(Condition.EQUALS, condition.condition());
        assertEquals(pageRequest.size(), query.skip());
        assertEquals(NoSQLPage.skip(pageRequest), query.limit());

        assertEquals(Column.of("name", "name"), condition.column());

        assertNotNull(personRepository.findByName("name", pageRequest));
        when(template.singleResult(any(ColumnQuery.class))).thenReturn(Optional
                .empty());

        assertNull(personRepository.findByName("name", pageRequest));


    }

    @Test
    public void shouldFindByNameANDAge() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(ada));

        PageRequest pageRequest = getPageRequest();
        List<Person> persons = personRepository.findByNameAndAge("name", 20, pageRequest);
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        assertThat(persons).contains(ada);

        ColumnQuery query = captor.getValue();
        assertEquals("Person", query.name());
        assertEquals(NoSQLPage.skip(pageRequest), query.limit());
        assertEquals(pageRequest.size(), query.limit());

    }

    @Test
    public void shouldFindByAgeANDName() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(ada));

        PageRequest pageRequest = getPageRequest();
        Set<Person> persons = personRepository.findByAgeAndName(20, "name", pageRequest);
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        assertThat(persons).contains(ada);
        ColumnQuery query = captor.getValue();
        assertEquals("Person", query.name());
        assertEquals(NoSQLPage.skip(pageRequest), query.limit());
        assertEquals(pageRequest.size(), query.limit());

    }

    @Test
    public void shouldFindByNameANDAgeOrderByName() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(ada));

        PageRequest pageRequest = getPageRequest();

        Stream<Person> persons = personRepository.findByNameAndAgeOrderByName("name", 20, pageRequest);
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        assertThat(persons.collect(Collectors.toList())).contains(ada);
        ColumnQuery query = captor.getValue();
        assertEquals("Person", query.name());
        assertEquals(NoSQLPage.skip(pageRequest), query.limit());
        assertEquals(pageRequest.size(), query.limit());

    }

    @Test
    public void shouldFindByNameANDAgeOrderByAge() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(ada));

        PageRequest pageRequest = getPageRequest();
        Queue<Person> persons = personRepository.findByNameAndAgeOrderByAge("name", 20, pageRequest);
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        assertThat(persons).contains(ada);
        ColumnQuery query = captor.getValue();
        assertEquals("Person", query.name());
        assertEquals(NoSQLPage.skip(pageRequest), query.limit());
        assertEquals(pageRequest.size(), query.limit());


    }


    @Test
    public void shouldFindAll() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.findAll(Person.class))
                .thenReturn(Stream.of(ada));

        PageRequest pageRequest = getPageRequest();

        List<Person> persons = personRepository.findAll(pageRequest).content();
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        ColumnQuery query = captor.getValue();
        assertFalse(query.condition().isPresent());
        assertEquals("Person", query.name());
        assertEquals(NoSQLPage.skip(pageRequest), query.limit());
        assertEquals(pageRequest.size(), query.limit());
    }


    @Test
    public void shouldFindByNameAndAgeGreaterEqualThan() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(ada));

        PageRequest pageRequest = getPageRequest();
        personRepository.findByNameAndAgeGreaterThanEqual("Ada", 33, pageRequest);
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        ColumnQuery query = captor.getValue();
        ColumnCondition condition = query.condition().get();
        assertEquals("Person", query.name());
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
        assertEquals(NoSQLPage.skip(pageRequest), query.limit());
        assertEquals(pageRequest.size(), query.limit());
    }

    @Test
    public void shouldFindByGreaterThan() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(ada));

        PageRequest pageRequest = getPageRequest();
        personRepository.findByAgeGreaterThan(33, pageRequest);
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        ColumnQuery query = captor.getValue();
        ColumnCondition condition = query.condition().get();
        assertEquals("Person", query.name());
        assertEquals(GREATER_THAN, condition.condition());
        assertEquals(Column.of("age", 33), condition.column());
        assertEquals(NoSQLPage.skip(pageRequest), query.limit());
        assertEquals(pageRequest.size(), query.limit());

    }

    @Test
    public void shouldFindByAgeLessThanEqual() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(ada));

        PageRequest pageRequest = getPageRequest();
        personRepository.findByAgeLessThanEqual(33, pageRequest);
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        ColumnQuery query = captor.getValue();
        ColumnCondition condition = query.condition().get();
        assertEquals("Person", query.name());
        assertEquals(LESSER_EQUALS_THAN, condition.condition());
        assertEquals(Column.of("age", 33), condition.column());
        assertEquals(NoSQLPage.skip(pageRequest), query.limit());
        assertEquals(pageRequest.size(), query.limit());

    }

    @Test
    public void shouldFindByAgeLessEqual() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(ada));

        PageRequest pageRequest = getPageRequest();
        personRepository.findByAgeLessThan(33, pageRequest);
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        ColumnQuery query = captor.getValue();
        ColumnCondition condition = query.condition().get();
        assertEquals("Person", query.name());
        assertEquals(LESSER_THAN, condition.condition());
        assertEquals(Column.of("age", 33), condition.column());
        assertEquals(NoSQLPage.skip(pageRequest), query.limit());
        assertEquals(pageRequest.size(), query.limit());

    }

    @Test
    public void shouldFindByAgeBetween() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(ada));

        PageRequest pageRequest = getPageRequest();
        personRepository.findByAgeBetween(10, 15, pageRequest);
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        ColumnQuery query = captor.getValue();
        ColumnCondition condition = query.condition().get();
        assertEquals("Person", query.name());
        assertEquals(BETWEEN, condition.condition());
        List<Value> values = condition.column().get(new TypeReference<>() {
        });
        assertEquals(Arrays.asList(10, 15), values.stream().map(Value::get).collect(Collectors.toList()));
        assertTrue(condition.column().name().contains("age"));
        assertEquals(NoSQLPage.skip(pageRequest), query.limit());
        assertEquals(pageRequest.size(), query.limit());
    }


    @Test
    public void shouldFindByNameLike() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(ada));

        PageRequest pageRequest = getPageRequest();
        personRepository.findByNameLike("Ada", pageRequest);
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        ColumnQuery query = captor.getValue();
        ColumnCondition condition = query.condition().get();
        assertEquals("Person", query.name());
        assertEquals(LIKE, condition.condition());
        assertEquals(Column.of("name", "Ada"), condition.column());
        assertEquals(NoSQLPage.skip(pageRequest), query.limit());
        assertEquals(pageRequest.size(), query.limit());

    }


    @Test
    public void shouldFindByStringWhenFieldIsSet() {
        Vendor vendor = new Vendor("vendor");
        vendor.setPrefixes(Collections.singleton("prefix"));

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(vendor));

        PageRequest pageRequest = getPageRequest();
        vendorRepository.findByPrefixes("prefix", pageRequest);

        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).singleResult(captor.capture());
        ColumnQuery query = captor.getValue();
        ColumnCondition condition = query.condition().get();
        assertEquals("vendors", query.name());
        assertEquals(EQUALS, condition.condition());
        assertEquals(Column.of("prefixes", "prefix"), condition.column());
        assertEquals(NoSQLPage.skip(pageRequest), query.limit());
        assertEquals(pageRequest.size(), query.limit());

    }

    @Test
    public void shouldFindByIn() {
        Vendor vendor = new Vendor("vendor");
        vendor.setPrefixes(Collections.singleton("prefix"));

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(vendor));

        PageRequest pageRequest = getPageRequest();
        vendorRepository.findByPrefixesIn(singletonList("prefix"), pageRequest);

        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).singleResult(captor.capture());
        ColumnQuery query = captor.getValue();
        ColumnCondition condition = query.condition().get();
        assertEquals("vendors", query.name());
        assertEquals(IN, condition.condition());
        assertEquals(NoSQLPage.skip(pageRequest), query.limit());
        assertEquals(pageRequest.size(), query.limit());

    }

    @Test
    public void shouldConvertFieldToTheType() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(ada));

        PageRequest pageRequest = getPageRequest();
        Slice<Person> slice = personRepository.findByAge("120", pageRequest);
        Assertions.assertNotNull(slice);
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        ColumnQuery query = captor.getValue();
        ColumnCondition condition = query.condition().get();
        assertEquals("Person", query.name());
        assertEquals(EQUALS, condition.condition());
        assertEquals(Column.of("age", 120), condition.column());
        assertEquals(NoSQLPage.skip(pageRequest), query.limit());
        assertEquals(pageRequest.size(), query.limit());
    }

    @Test
    public void shouldFindByNameOrderName() {

        when(template.singleResult(any(ColumnQuery.class))).thenReturn(Optional
                .of(Person.builder().build()));

        PageRequest pageRequest = getPageRequest().sortBy(Sort.asc("name"));
        personRepository.findByName("name", pageRequest);

        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).singleResult(captor.capture());
        ColumnQuery query = captor.getValue();
        ColumnCondition condition = query.condition().get();
        assertEquals("Person", query.name());
        assertEquals(EQUALS, condition.condition());
        assertEquals(NoSQLPage.skip(pageRequest), query.limit());
        assertEquals(pageRequest.size(), query.limit());
        assertThat(query.sorts()).hasSize(1)
                .contains(Sort.asc("name"));

        assertEquals(Column.of("name", "name"), condition.column());

        assertNotNull(personRepository.findByName("name", pageRequest));
        when(template.singleResult(any(ColumnQuery.class))).thenReturn(Optional
                .empty());
        assertNull(personRepository.findByName("name", pageRequest));
    }

    @Test
    public void shouldFindByNameOrderName2() {

        when(template.singleResult(any(ColumnQuery.class))).thenReturn(Optional
                .of(Person.builder().build()));

        PageRequest pageRequest = getPageRequest().sortBy(Sort.asc("name"));
        Page<Person> page = personRepository.findByNameOrderByAge("name", pageRequest);

        Assertions.assertNotNull(page);

        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        ColumnQuery query = captor.getValue();
        ColumnCondition condition = query.condition().get();
        assertEquals("Person", query.name());
        assertEquals(EQUALS, condition.condition());
        assertEquals(NoSQLPage.skip(pageRequest), query.limit());
        assertEquals(pageRequest.size(), query.limit());
        assertThat(query.sorts()).hasSize(2)
                .containsExactly(Sort.asc("age"), Sort.asc("name"));

        assertEquals(Column.of("name", "name"), condition.column());

        assertNotNull(personRepository.findByName("name", pageRequest));
        when(template.singleResult(any(ColumnQuery.class))).thenReturn(Optional
                .empty());
        assertNull(personRepository.findByName("name", pageRequest));
    }

    @Test
    public void shouldFindByNameSort() {
        when(template.singleResult(any(ColumnQuery.class))).thenReturn(Optional
                .of(Person.builder().build()));

        PageRequest pageRequest = getPageRequest().sortBy(Sort.desc("age"));
        personRepository.findByName("name", Sort.asc("name"), pageRequest);

        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        ColumnQuery query = captor.getValue();
        ColumnCondition condition = query.condition().get();
        assertEquals("Person", query.name());
        assertEquals(EQUALS, condition.condition());
        assertThat(query.sorts()).hasSize(2)
                .containsExactly(Sort.asc("name"), Sort.desc("age"));
        assertEquals(Column.of("name", "name"), condition.column());
    }

    @Test
    public void shouldFindByNameSortPagination() {
        when(template.singleResult(any(ColumnQuery.class))).thenReturn(Optional
                .of(Person.builder().build()));

        personRepository.findByName("name", Sort.asc("name"));

        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        ColumnQuery query = captor.getValue();
        ColumnCondition condition = query.condition().get();
        assertEquals("Person", query.name());
        assertEquals(EQUALS, condition.condition());
        assertThat(query.sorts()).hasSize(1)
                .containsExactly(Sort.asc("name"));
        assertEquals(Column.of("name", "name"), condition.column());
    }

    @Test
    public void shouldFindByNameLimit() {
        when(template.singleResult(any(ColumnQuery.class))).thenReturn(Optional
                .of(Person.builder().build()));

        personRepository.findByName("name", Limit.of(3), Sort.asc("name"));
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        ColumnQuery query = captor.getValue();
        ColumnCondition condition = query.condition().get();
        assertEquals("Person", query.name());
        assertEquals(0, query.skip());
        assertEquals(3, query.limit());
        assertEquals(EQUALS, condition.condition());
        assertThat(query.sorts()).hasSize(1)
                .containsExactly(Sort.asc("name"));
        assertEquals(Column.of("name", "name"), condition.column());
    }

    @Test
    public void shouldFindByNameLimit2() {
        when(template.singleResult(any(ColumnQuery.class))).thenReturn(Optional
                .of(Person.builder().build()));

        personRepository.findByName("name", Limit.range(1, 3), Sort.asc("name"));
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        ColumnQuery query = captor.getValue();
        ColumnCondition condition = query.condition().get();
        assertEquals("Person", query.name());
        assertEquals(0, query.skip());
        assertEquals(3, query.limit());
        assertEquals(EQUALS, condition.condition());
        assertThat(query.sorts()).hasSize(1)
                .containsExactly(Sort.asc("name"));
        assertEquals(Column.of("name", "name"), condition.column());
    }

    @Test
    public void shouldFindByNameLimit3() {
        when(template.singleResult(any(ColumnQuery.class))).thenReturn(Optional
                .of(Person.builder().build()));

        personRepository.findByName("name", Limit.range(2, 3), Sort.asc("name"));
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        ColumnQuery query = captor.getValue();
        ColumnCondition condition = query.condition().get();
        assertEquals("Person", query.name());
        assertEquals(1, query.skip());
        assertEquals(2, query.limit());
        assertEquals(EQUALS, condition.condition());
        assertThat(query.sorts()).hasSize(1)
                .containsExactly(Sort.asc("name"));
        assertEquals(Column.of("name", "name"), condition.column());
    }


    private PageRequest getPageRequest() {
        return PageRequest.ofPage(2).size(6);
    }

    interface PersonRepository extends BasicRepository<Person, Long> {

        Person findByName(String name, PageRequest pageRequest);

        List<Person> findByName(String name, Sort sort);

        List<Person> findByName(String name, Limit limit, Sort sort);

        List<Person> findByName(String name, Sort sort, PageRequest pageRequest);

        Page<Person> findByNameOrderByAge(String name, PageRequest pageRequest);

        Slice<Person> findByAge(String age, PageRequest pageRequest);

        List<Person> findByNameAndAge(String name, Integer age, PageRequest pageRequest);

        Set<Person> findByAgeAndName(Integer age, String name, PageRequest pageRequest);

        Stream<Person> findByNameAndAgeOrderByName(String name, Integer age, PageRequest pageRequest);

        Queue<Person> findByNameAndAgeOrderByAge(String name, Integer age, PageRequest pageRequest);

        Set<Person> findByNameAndAgeGreaterThanEqual(String name, Integer age, PageRequest pageRequest);

        Set<Person> findByAgeGreaterThan(Integer age, PageRequest pageRequest);

        Set<Person> findByAgeLessThanEqual(Integer age, PageRequest pageRequest);

        Set<Person> findByAgeLessThan(Integer age, PageRequest pageRequest);

        Set<Person> findByAgeBetween(Integer ageA, Integer ageB, PageRequest pageRequest);

        Set<Person> findByNameLike(String name, PageRequest pageRequest);

    }

    public interface VendorRepository extends BasicRepository<Vendor, String> {

        Vendor findByPrefixes(String prefix, PageRequest pageRequest);

        Vendor findByPrefixesIn(List<String> prefix, PageRequest pageRequest);

    }
}
