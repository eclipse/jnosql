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
package org.eclipse.jnosql.mapping.document.query;

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
import org.eclipse.jnosql.communication.document.Document;
import org.eclipse.jnosql.communication.document.DocumentCondition;
import org.eclipse.jnosql.communication.document.DocumentQuery;
import org.eclipse.jnosql.mapping.core.Converters;
import org.eclipse.jnosql.mapping.core.NoSQLPage;
import org.eclipse.jnosql.mapping.document.DocumentEntityConverter;
import org.eclipse.jnosql.mapping.document.JNoSQLDocumentTemplate;
import org.eclipse.jnosql.mapping.document.MockProducer;
import org.eclipse.jnosql.mapping.document.entities.Person;
import org.eclipse.jnosql.mapping.document.entities.Vendor;
import org.eclipse.jnosql.mapping.document.spi.DocumentExtension;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.jnosql.communication.Condition.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@EnableAutoWeld
@AddPackages(value = {Converters.class, DocumentEntityConverter.class})
@AddPackages(MockProducer.class)
@AddPackages(Reflections.class)
@AddExtensions({EntityMetadataExtension.class, DocumentExtension.class})
class DocumentRepositoryProxyPageRequestTest {


    private JNoSQLDocumentTemplate template;

    @Inject
    private EntitiesMetadata entities;

    @Inject
    private Converters converters;

    private PersonRepository personRepository;

    private VendorRepository vendorRepository;

    @BeforeEach
    void setUp() {
        this.template = Mockito.mock(JNoSQLDocumentTemplate.class);

        DocumentRepositoryProxy personHandler = new DocumentRepositoryProxy(template,
                entities, PersonRepository.class, converters);

        DocumentRepositoryProxy vendorHandler = new DocumentRepositoryProxy(template,
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
    void shouldFindByNameInstance() {

        when(template.singleResult(any(DocumentQuery.class))).thenReturn(Optional
                .of(Person.builder().build()));

        PageRequest pageRequest = getPageRequest();
        personRepository.findByName("name", pagination);

        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).singleResult(captor.capture());
        DocumentQuery query = captor.getValue();
        DocumentCondition condition = query.condition().get();
        assertEquals("Person", query.name());
        assertEquals(EQUALS, condition.condition());
         assertEquals(NoSQLPage.skip(pagination), query.limit());
        assertEquals(pagination.size(), query.limit());

        assertEquals(Document.of("name", "name"), condition.document());

        assertNotNull(personRepository.findByName("name", pagination));
        when(template.singleResult(any(DocumentQuery.class))).thenReturn(Optional
                .empty());

        assertNull(personRepository.findByName("name", pagination));


    }

    @Test
    void shouldFindByNameANDAge() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(DocumentQuery.class)))
                .thenReturn(Stream.of(ada));

        PageRequest pageRequest = getPageRequest();
        List<Person> persons = personRepository.findByNameAndAge("name", 20, pagination);
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).select(captor.capture());
        assertThat(persons).contains(ada);

        DocumentQuery query = captor.getValue();
        assertEquals("Person", query.name());
         assertEquals(NoSQLPage.skip(pagination), query.limit());
        assertEquals(pagination.size(), query.limit());

    }

    @Test
    void shouldFindByAgeANDName() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(DocumentQuery.class)))
                .thenReturn(Stream.of(ada));

        PageRequest pageRequest = getPageRequest();
        Set<Person> persons = personRepository.findByAgeAndName(20, "name", pagination);
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).select(captor.capture());
        assertThat(persons).contains(ada);
        DocumentQuery query = captor.getValue();
        assertEquals("Person", query.name());
         assertEquals(NoSQLPage.skip(pagination), query.limit());
        assertEquals(pagination.size(), query.limit());

    }

    @Test
    void shouldFindByNameANDAgeOrderByName() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(DocumentQuery.class)))
                .thenReturn(Stream.of(ada));

        PageRequest pageRequest = getPageRequest();

        Stream<Person> persons = personRepository.findByNameAndAgeOrderByName("name", 20, pagination);
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).select(captor.capture());
        assertThat(persons.collect(Collectors.toList())).contains(ada);
        DocumentQuery query = captor.getValue();
        assertEquals("Person", query.name());
         assertEquals(NoSQLPage.skip(pagination), query.limit());
        assertEquals(pagination.size(), query.limit());

    }

    @Test
    void shouldFindByNameANDAgeOrderByAge() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(DocumentQuery.class)))
                .thenReturn(Stream.of(ada));

        PageRequest pageRequest = getPageRequest();
        Queue<Person> persons = personRepository.findByNameAndAgeOrderByAge("name", 20, pagination);
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).select(captor.capture());
        assertThat(persons).contains(ada);
        DocumentQuery query = captor.getValue();
        assertEquals("Person", query.name());
         assertEquals(NoSQLPage.skip(pagination), query.limit());
        assertEquals(pagination.size(), query.limit());


    }


    @Test
    void shouldFindAll() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(DocumentQuery.class)))
                .thenReturn(Stream.of(ada));

        PageRequest pageRequest = getPageRequest();

        List<Person> persons = personRepository.findAll(pagination).content();
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).select(captor.capture());
        DocumentQuery query = captor.getValue();
        assertFalse(query.condition().isPresent());
        assertEquals("Person", query.name());
        assertEquals(NoSQLPage.skip(pagination), query.limit());
        assertEquals(pagination.size(), query.limit());
    }


    @Test
    void shouldFindByNameAndAgeGreaterEqualThan() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(DocumentQuery.class)))
                .thenReturn(Stream.of(ada));

        PageRequest pageRequest = getPageRequest();
        personRepository.findByNameAndAgeGreaterThanEqual("Ada", 33, pagination);
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).select(captor.capture());
        DocumentQuery query = captor.getValue();
        DocumentCondition condition = query.condition().get();
        assertEquals("Person", query.name());
        assertEquals(AND, condition.condition());
        List<DocumentCondition> conditions = condition.document().get(new TypeReference<>() {
        });
        DocumentCondition columnCondition = conditions.get(0);
        DocumentCondition columnCondition2 = conditions.get(1);

        assertEquals(EQUALS, columnCondition.condition());
        assertEquals("Ada", columnCondition.document().get());
        assertEquals("name", columnCondition.document().name());

        assertEquals(Condition.GREATER_EQUALS_THAN, columnCondition2.condition());
        assertEquals(33, columnCondition2.document().get());
        assertEquals("age", columnCondition2.document().name());
         assertEquals(NoSQLPage.skip(pagination), query.limit());
        assertEquals(pagination.size(), query.limit());
    }

    @Test
    void shouldFindByGreaterThan() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(DocumentQuery.class)))
                .thenReturn(Stream.of(ada));

        PageRequest pageRequest = getPageRequest();
        personRepository.findByAgeGreaterThan(33, pagination);
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).select(captor.capture());
        DocumentQuery query = captor.getValue();
        DocumentCondition condition = query.condition().get();
        assertEquals("Person", query.name());
        assertEquals(GREATER_THAN, condition.condition());
        assertEquals(Document.of("age", 33), condition.document());
         assertEquals(NoSQLPage.skip(pagination), query.limit());
        assertEquals(pagination.size(), query.limit());

    }

    @Test
    void shouldFindByAgeLessThanEqual() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(DocumentQuery.class)))
                .thenReturn(Stream.of(ada));

        PageRequest pageRequest = getPageRequest();
        personRepository.findByAgeLessThanEqual(33, pagination);
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).select(captor.capture());
        DocumentQuery query = captor.getValue();
        DocumentCondition condition = query.condition().get();
        assertEquals("Person", query.name());
        assertEquals(LESSER_EQUALS_THAN, condition.condition());
        assertEquals(Document.of("age", 33), condition.document());
         assertEquals(NoSQLPage.skip(pagination), query.limit());
        assertEquals(pagination.size(), query.limit());

    }

    @Test
    void shouldFindByAgeLessEqual() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(DocumentQuery.class)))
                .thenReturn(Stream.of(ada));

        PageRequest pageRequest = getPageRequest();
        personRepository.findByAgeLessThan(33, pagination);
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).select(captor.capture());
        DocumentQuery query = captor.getValue();
        DocumentCondition condition = query.condition().get();
        assertEquals("Person", query.name());
        assertEquals(LESSER_THAN, condition.condition());
        assertEquals(Document.of("age", 33), condition.document());
         assertEquals(NoSQLPage.skip(pagination), query.limit());
        assertEquals(pagination.size(), query.limit());

    }

    @Test
    void shouldFindByAgeBetween() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(DocumentQuery.class)))
                .thenReturn(Stream.of(ada));

        PageRequest pageRequest = getPageRequest();
        personRepository.findByAgeBetween(10,15, pagination);
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).select(captor.capture());
        DocumentQuery query = captor.getValue();
        DocumentCondition condition = query.condition().get();
        assertEquals("Person", query.name());
        assertEquals(BETWEEN, condition.condition());
        List<Value> values = condition.document().get(new TypeReference<>() {
        });
        assertEquals(Arrays.asList(10, 15), values.stream().map(Value::get).collect(Collectors.toList()));
        assertTrue(condition.document().name().contains("age"));
         assertEquals(NoSQLPage.skip(pagination), query.limit());
        assertEquals(pagination.size(), query.limit());
    }


    @Test
    void shouldFindByNameLike() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(DocumentQuery.class)))
                .thenReturn(Stream.of(ada));

        PageRequest pageRequest = getPageRequest();
        personRepository.findByNameLike("Ada", pagination);
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).select(captor.capture());
        DocumentQuery query = captor.getValue();
        DocumentCondition condition = query.condition().get();
        assertEquals("Person", query.name());
        assertEquals(LIKE, condition.condition());
        assertEquals(Document.of("name", "Ada"), condition.document());
         assertEquals(NoSQLPage.skip(pagination), query.limit());
        assertEquals(pagination.size(), query.limit());

    }


    @Test
    void shouldFindByStringWhenFieldIsSet() {
        Vendor vendor = new Vendor("vendor");
        vendor.setPrefixes(Collections.singleton("prefix"));

        when(template.select(any(DocumentQuery.class)))
                .thenReturn(Stream.of(vendor));

        PageRequest pageRequest = getPageRequest();
        vendorRepository.findByPrefixes("prefix", pagination);

        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).singleResult(captor.capture());
        DocumentQuery query = captor.getValue();
        DocumentCondition condition = query.condition().get();
        assertEquals("vendors", query.name());
        assertEquals(EQUALS, condition.condition());
        assertEquals(Document.of("prefixes", "prefix"), condition.document());
         assertEquals(NoSQLPage.skip(pagination), query.limit());
        assertEquals(pagination.size(), query.limit());

    }

    @Test
    void shouldFindByIn() {
        Vendor vendor = new Vendor("vendor");
        vendor.setPrefixes(Collections.singleton("prefix"));

        when(template.select(any(DocumentQuery.class)))
                .thenReturn(Stream.of(vendor));

        PageRequest pageRequest = getPageRequest();
        vendorRepository.findByPrefixesIn(Collections.singletonList("prefix"), pagination);

        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).singleResult(captor.capture());
        DocumentQuery query = captor.getValue();
        DocumentCondition condition = query.condition().get();
        assertEquals("vendors", query.name());
        assertEquals(IN, condition.condition());
         assertEquals(NoSQLPage.skip(pagination), query.limit());
        assertEquals(pagination.size(), query.limit());

    }


    @Test
    void shouldConvertFieldToTheType() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(DocumentQuery.class)))
                .thenReturn(Stream.of(ada));

        PageRequest pageRequest = getPageRequest();
        Slice<Person> slice = personRepository.findByAge("120", pagination);
        Assertions.assertNotNull(slice);
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).select(captor.capture());
        DocumentQuery query = captor.getValue();

        DocumentCondition condition = query.condition().get();
        assertEquals("Person", query.name());
        assertEquals(EQUALS, condition.condition());
        assertEquals(Document.of("age", 120), condition.document());
         assertEquals(NoSQLPage.skip(pagination), query.limit());
        assertEquals(pagination.size(), query.limit());
    }

    @Test
    void shouldFindByNameOrderName() {

        when(template.singleResult(any(DocumentQuery.class))).thenReturn(Optional
                .of(Person.builder().build()));

        PageRequest pageRequest = getPageRequest().sortBy(Sort.asc("name"));
        personRepository.findByName("name", pagination);

        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).singleResult(captor.capture());
        DocumentQuery query = captor.getValue();
        DocumentCondition condition = query.condition().get();
        assertEquals("Person", query.name());
        assertEquals(EQUALS, condition.condition());
        assertEquals(NoSQLPage.skip(pagination), query.limit());
        assertEquals(pagination.size(), query.limit());
        assertThat(query.sorts()).hasSize(1)
                .contains(Sort.asc("name"));

        assertEquals(Document.of("name", "name"), condition.document());

        assertNotNull(personRepository.findByName("name", pagination));
        when(template.singleResult(any(DocumentQuery.class))).thenReturn(Optional
                .empty());
        assertNull(personRepository.findByName("name", pagination));
    }

    @Test
    void shouldFindByNameOrderName2() {

        when(template.singleResult(any(DocumentQuery.class))).thenReturn(Optional
                .of(Person.builder().build()));

        PageRequest pageRequest = getPageRequest().sortBy(Sort.asc("name"));
        Page<Person> page = personRepository.findByNameOrderByAge("name", pagination);

        Assertions.assertNotNull(page);

        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).select(captor.capture());
        DocumentQuery query = captor.getValue();
        DocumentCondition condition = query.condition().get();
        assertEquals("Person", query.name());
        assertEquals(EQUALS, condition.condition());
        assertEquals(NoSQLPage.skip(pagination), query.limit());
        assertEquals(pagination.size(), query.limit());
        assertThat(query.sorts()).hasSize(2)
                .containsExactly(Sort.asc("age"), Sort.asc("name"));

        assertEquals(Document.of("name", "name"), condition.document());

        assertNotNull(personRepository.findByName("name", pagination));
        when(template.singleResult(any(DocumentQuery.class))).thenReturn(Optional
                .empty());
        assertNull(personRepository.findByName("name", pagination));
    }

    @Test
    void shouldFindByNameSort() {
        when(template.singleResult(any(DocumentQuery.class))).thenReturn(Optional
                .of(Person.builder().build()));

        PageRequest pageRequest = getPageRequest().sortBy(Sort.desc("age"));
        personRepository.findByName("name", Sort.asc("name"), pagination);

        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).select(captor.capture());
        DocumentQuery query = captor.getValue();
        DocumentCondition condition = query.condition().get();
        assertEquals("Person", query.name());
        assertEquals(EQUALS, condition.condition());
        assertThat(query.sorts()).hasSize(2)
                .containsExactly(Sort.asc("name"), Sort.desc("age"));
        assertEquals(Document.of("name", "name"), condition.document());
    }

    @Test
    void shouldFindByNameSortPagination() {
        when(template.singleResult(any(DocumentQuery.class))).thenReturn(Optional
                .of(Person.builder().build()));

        personRepository.findByName("name", Sort.asc("name"));

        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).select(captor.capture());
        DocumentQuery query = captor.getValue();
        DocumentCondition condition = query.condition().get();
        assertEquals("Person", query.name());
        assertEquals(EQUALS, condition.condition());
        assertThat(query.sorts()).hasSize(1)
                .containsExactly(Sort.asc("name"));
        assertEquals(Document.of("name", "name"), condition.document());
    }


    @Test
    void shouldFindByNameLimit() {
        when(template.singleResult(any(DocumentQuery.class))).thenReturn(Optional
                .of(Person.builder().build()));

        personRepository.findByName("name", Limit.of(3), Sort.asc("name"));
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).select(captor.capture());
        DocumentQuery query = captor.getValue();
        DocumentCondition condition = query.condition().get();
        assertEquals("Person", query.name());
        assertEquals(0, query.skip());
        assertEquals(3, query.limit());
        assertEquals(EQUALS, condition.condition());
        assertThat(query.sorts()).hasSize(1)
                .containsExactly(Sort.asc("name"));
        assertEquals(Document.of("name", "name"), condition.document());
    }

    @Test
    void shouldFindByNameLimit2() {
        when(template.singleResult(any(DocumentQuery.class))).thenReturn(Optional
                .of(Person.builder().build()));

        personRepository.findByName("name", Limit.range(1, 3), Sort.asc("name"));
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).select(captor.capture());
        DocumentQuery query = captor.getValue();
        DocumentCondition condition = query.condition().get();
        assertEquals("Person", query.name());
        assertEquals(0, query.skip());
        assertEquals(3, query.limit());
        assertEquals(EQUALS, condition.condition());
        assertThat(query.sorts()).hasSize(1)
                .containsExactly(Sort.asc("name"));
        assertEquals(Document.of("name", "name"), condition.document());
    }

    @Test
    void shouldFindByNameLimit3() {
        when(template.singleResult(any(DocumentQuery.class))).thenReturn(Optional
                .of(Person.builder().build()));

        personRepository.findByName("name", Limit.range(2, 3), Sort.asc("name"));
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).select(captor.capture());
        DocumentQuery query = captor.getValue();
        DocumentCondition condition = query.condition().get();
        assertEquals("Person", query.name());
        assertEquals(1, query.skip());
        assertEquals(2, query.limit());
        assertEquals(EQUALS, condition.condition());
        assertThat(query.sorts()).hasSize(1)
                .containsExactly(Sort.asc("name"));
        assertEquals(Document.of("name", "name"), condition.document());
    }

    private PageRequest getPageRequest() {
        return PageRequest.ofPage(2).size(6);
    }

    interface PersonRepository extends BasicRepository<Person, Long> {


        Person findByName(String name, PageRequest pageRequest);

        List<Person> findByName(String name, Sort sort);

        List<Person> findByName(String name, Sort sort, PageRequest pageRequest);

        List<Person> findByName(String name, Limit limit, Sort sort);

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
