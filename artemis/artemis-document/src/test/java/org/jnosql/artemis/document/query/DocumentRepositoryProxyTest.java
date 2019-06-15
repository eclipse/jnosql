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
package org.jnosql.artemis.document.query;

import org.hamcrest.Matchers;
import org.jnosql.artemis.CDIExtension;
import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.Param;
import jakarta.nosql.mapping.PreparedStatement;
import jakarta.nosql.mapping.Query;
import jakarta.nosql.mapping.Repository;
import jakarta.nosql.mapping.document.DocumentTemplate;
import org.jnosql.artemis.model.Person;
import org.jnosql.artemis.model.Vendor;
import jakarta.nosql.mapping.reflection.ClassMappings;
import jakarta.nosql.Condition;
import jakarta.nosql.TypeReference;
import jakarta.nosql.Value;
import jakarta.nosql.document.Document;
import jakarta.nosql.document.DocumentCondition;
import jakarta.nosql.document.DocumentDeleteQuery;
import jakarta.nosql.document.DocumentQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static  jakarta.nosql.Condition.AND;
import static  jakarta.nosql.Condition.BETWEEN;
import static  jakarta.nosql.Condition.EQUALS;
import static  jakarta.nosql.Condition.GREATER_THAN;
import static  jakarta.nosql.Condition.IN;
import static  jakarta.nosql.Condition.LESSER_EQUALS_THAN;
import static  jakarta.nosql.Condition.LESSER_THAN;
import static  jakarta.nosql.Condition.LIKE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(CDIExtension.class)
public class DocumentRepositoryProxyTest {

    private DocumentTemplate template;

    @Inject
    private ClassMappings classMappings;

    @Inject
    private Converters converters;

    private PersonRepository personRepository;

    private VendorRepository vendorRepository;


    @BeforeEach
    public void setUp() {
        this.template = Mockito.mock(DocumentTemplate.class);

        DocumentRepositoryProxy personHandler = new DocumentRepositoryProxy(template,
                classMappings, PersonRepository.class, converters);

        DocumentRepositoryProxy vendorHandler = new DocumentRepositoryProxy(template,
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
    public void shouldSaveUsingInsertWhenDataDoesNotExist() {
        when(template.find(Mockito.eq(Person.class), Mockito.eq(10L)))
                .thenReturn(Optional.empty());


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
        when(template.find(Mockito.eq(Person.class), Mockito.eq(10L)))
                .thenReturn(Optional.of(Person.builder().build()));

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

        when(template.singleResult(Mockito.any(DocumentQuery.class))).thenReturn(Optional.empty());

        ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);

        Person person = Person.builder().withName("Ada")
                .withId(10L)
                .withPhones(singletonList("123123"))
                .build();
        personRepository.save(singletonList(person));
        verify(template).insert(captor.capture());
        verify(template).insert(captor.capture());
        Person personCapture = captor.getValue();
        assertEquals(person, personCapture);
    }


    @Test
    public void shouldFindByNameInstance() {

        when(template.singleResult(Mockito.any(DocumentQuery.class))).thenReturn(Optional
                .of(Person.builder().build()));

        personRepository.findByName("name");

        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).singleResult(captor.capture());
        DocumentQuery query = captor.getValue();
        DocumentCondition condition = query.getCondition().get();
        assertEquals("Person", query.getDocumentCollection());
        assertEquals(Condition.EQUALS, condition.getCondition());
        assertEquals(Document.of("name", "name"), condition.getDocument());

        assertNotNull(personRepository.findByName("name"));
        when(template.singleResult(Mockito.any(DocumentQuery.class))).thenReturn(Optional
                .empty());

        assertNull(personRepository.findByName("name"));


    }

    @Test
    public void shouldFindByNameANDAge() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(Mockito.any(DocumentQuery.class)))
                .thenReturn(singletonList(ada));

        List<Person> persons = personRepository.findByNameAndAge("name", 20);
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).select(captor.capture());
        assertThat(persons, Matchers.contains(ada));

    }

    @Test
    public void shouldFindByAgeANDName() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(Mockito.any(DocumentQuery.class)))
                .thenReturn(singletonList(ada));

        Set<Person> persons = personRepository.findByAgeAndName(20, "name");
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).select(captor.capture());
        assertThat(persons, Matchers.contains(ada));

    }

    @Test
    public void shouldFindByNameANDAgeOrderByName() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(Mockito.any(DocumentQuery.class)))
                .thenReturn(singletonList(ada));

        Stream<Person> persons = personRepository.findByNameAndAgeOrderByName("name", 20);
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).select(captor.capture());
        assertThat(persons.collect(Collectors.toList()), Matchers.contains(ada));

    }

    @Test
    public void shouldFindByNameANDAgeOrderByAge() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(Mockito.any(DocumentQuery.class)))
                .thenReturn(singletonList(ada));

        Queue<Person> persons = personRepository.findByNameAndAgeOrderByAge("name", 20);
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).select(captor.capture());
        assertThat(persons, Matchers.contains(ada));

    }

    @Test
    public void shouldDeleteByName() {
        ArgumentCaptor<DocumentDeleteQuery> captor = ArgumentCaptor.forClass(DocumentDeleteQuery.class);
        personRepository.deleteByName("Ada");
        verify(template).delete(captor.capture());
        DocumentDeleteQuery deleteQuery = captor.getValue();
        DocumentCondition condition = deleteQuery.getCondition().get();
        assertEquals("Person", deleteQuery.getDocumentCollection());
        assertEquals(Condition.EQUALS, condition.getCondition());
        assertEquals(Document.of("name", "Ada"), condition.getDocument());

    }

    @Test
    public void shouldFindById() {
        personRepository.findById(10L);
        verify(template).find(Mockito.eq(Person.class), Mockito.eq(10L));
    }

    @Test
    public void shouldFindByIds() {
        when(template.find(Mockito.eq(Person.class), Mockito.any(Long.class)))
                .thenReturn(Optional.of(Person.builder().build()));

        personRepository.findById(singletonList(10L));
        verify(template).find(Mockito.eq(Person.class), Mockito.eq(10L));
        personRepository.findById(Arrays.asList(10L, 11L, 12L));
        verify(template, times(4)).find(Mockito.eq(Person.class), any(Long.class));
    }

    @Test
    public void shouldDeleteById() {
        personRepository.deleteById(10L);
        verify(template).delete(Person.class, 10L);

    }

    @Test
    public void shouldDeleteByIds() {
        personRepository.deleteById(singletonList(10L));
        verify(template).delete(Person.class, 10L);

        personRepository.deleteById(asList(1L, 2L, 3L));
        verify(template, times(4)).delete(Mockito.eq(Person.class), any(Long.class));
    }


    @Test
    public void shouldContainsById() {
        when(template.find(Mockito.eq(Person.class), Mockito.any(Long.class)))
                .thenReturn(Optional.of(Person.builder().build()));

        assertTrue(personRepository.existsById(10L));
        Mockito.verify(template).find(Mockito.eq(Person.class), Mockito.eq(10L));

        when(template.find(Mockito.eq(Person.class), Mockito.any(Long.class)))
                .thenReturn(Optional.empty());
        assertFalse(personRepository.existsById(10L));

    }

    @Test
    public void shouldFindAll() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(DocumentQuery.class)))
                .thenReturn(singletonList(ada));

        List<Person> persons = personRepository.findAll();
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).select(captor.capture());
        DocumentQuery query = captor.getValue();
        assertFalse(query.getCondition().isPresent());
        assertEquals("Person", query.getDocumentCollection());

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
    public void shouldReturnEquals() {
        assertNotNull(personRepository.equals(personRepository));
    }

    @Test
    public void shouldFindByNameAndAgeGreaterEqualThan() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(DocumentQuery.class)))
                .thenReturn(singletonList(ada));

        personRepository.findByNameAndAgeGreaterThanEqual("Ada", 33);
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).select(captor.capture());
        DocumentQuery query = captor.getValue();
        DocumentCondition condition = query.getCondition().get();
        assertEquals("Person", query.getDocumentCollection());
        assertEquals(AND, condition.getCondition());
        List<DocumentCondition> conditions = condition.getDocument().get(new TypeReference<List<DocumentCondition>>() {
        });
        DocumentCondition columnCondition = conditions.get(0);
        DocumentCondition columnCondition2 = conditions.get(1);

        assertEquals(Condition.EQUALS, columnCondition.getCondition());
        assertEquals("Ada", columnCondition.getDocument().get());
        assertTrue(columnCondition.getDocument().getName().contains("name"));

        assertEquals(Condition.GREATER_EQUALS_THAN, columnCondition2.getCondition());
        assertEquals(33, columnCondition2.getDocument().get());
        assertTrue(columnCondition2.getDocument().getName().contains("age"));

    }

    @Test
    public void shouldFindByGreaterThan() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(DocumentQuery.class)))
                .thenReturn(singletonList(ada));

        personRepository.findByAgeGreaterThan(33);
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).select(captor.capture());
        DocumentQuery query = captor.getValue();
        DocumentCondition condition = query.getCondition().get();
        assertEquals("Person", query.getDocumentCollection());
        assertEquals(GREATER_THAN, condition.getCondition());
        assertEquals(Document.of("age", 33), condition.getDocument());

    }

    @Test
    public void shouldFindByAgeLessThanEqual() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(DocumentQuery.class)))
                .thenReturn(singletonList(ada));

        personRepository.findByAgeLessThanEqual(33);
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).select(captor.capture());
        DocumentQuery query = captor.getValue();
        DocumentCondition condition = query.getCondition().get();
        assertEquals("Person", query.getDocumentCollection());
        assertEquals(LESSER_EQUALS_THAN, condition.getCondition());
        assertEquals(Document.of("age", 33), condition.getDocument());

    }

    @Test
    public void shouldFindByAgeLessEqual() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(DocumentQuery.class)))
                .thenReturn(singletonList(ada));

        personRepository.findByAgeLessThan(33);
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).select(captor.capture());
        DocumentQuery query = captor.getValue();
        DocumentCondition condition = query.getCondition().get();
        assertEquals("Person", query.getDocumentCollection());
        assertEquals(LESSER_THAN, condition.getCondition());
        assertEquals(Document.of("age", 33), condition.getDocument());

    }


    @Test
    public void shouldFindByAgeBetween() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(DocumentQuery.class)))
                .thenReturn(singletonList(ada));

        personRepository.findByAgeBetween(10, 15);
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).select(captor.capture());
        DocumentQuery query = captor.getValue();
        DocumentCondition condition = query.getCondition().get();
        assertEquals("Person", query.getDocumentCollection());
        assertEquals(BETWEEN, condition.getCondition());
        List<Value> values = condition.getDocument().get(new TypeReference<List<Value>>() {
        });
        assertEquals(Arrays.asList(10, 15), values.stream().map(Value::get).collect(Collectors.toList()));
        assertTrue(condition.getDocument().getName().contains("age"));

    }


    @Test
    public void shouldFindByNameLike() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(DocumentQuery.class)))
                .thenReturn(singletonList(ada));

        personRepository.findByNameLike("Ada");
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).select(captor.capture());
        DocumentQuery query = captor.getValue();
        DocumentCondition condition = query.getCondition().get();
        assertEquals("Person", query.getDocumentCollection());
        assertEquals(LIKE, condition.getCondition());
        assertEquals(Document.of("name", "Ada"), condition.getDocument());
    }

    @Test
    public void shouldFindByStringWhenFieldIsSet() {
        Vendor vendor = new Vendor("vendor");
        vendor.setPrefixes(Collections.singleton("prefix"));

        when(template.select(any(DocumentQuery.class)))
                .thenReturn(singletonList(vendor));

        vendorRepository.findByPrefixes("prefix");

        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).singleResult(captor.capture());
        DocumentQuery query = captor.getValue();
        DocumentCondition condition = query.getCondition().get();
        assertEquals("vendors", query.getDocumentCollection());
        assertEquals(EQUALS, condition.getCondition());
        assertEquals(Document.of("prefixes", "prefix"), condition.getDocument());

    }

    @Test
    public void shouldFindByIn() {
        Vendor vendor = new Vendor("vendor");
        vendor.setPrefixes(Collections.singleton("prefix"));

        when(template.select(any(DocumentQuery.class)))
                .thenReturn(singletonList(vendor));

        vendorRepository.findByPrefixesIn(singletonList("prefix"));

        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).singleResult(captor.capture());
        DocumentQuery query = captor.getValue();
        DocumentCondition condition = query.getCondition().get();
        assertEquals("vendors", query.getDocumentCollection());
        assertEquals(IN, condition.getCondition());

    }


    @Test
    public void shouldConvertFieldToTheType() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(DocumentQuery.class)))
                .thenReturn(singletonList(ada));

        personRepository.findByAge("120");
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).select(captor.capture());
        DocumentQuery query = captor.getValue();
        DocumentCondition condition = query.getCondition().get();
        assertEquals("Person", query.getDocumentCollection());
        assertEquals(EQUALS, condition.getCondition());
        assertEquals(Document.of("age", 120), condition.getDocument());
    }

    @Test
    public void shouldExecuteJNoSQLQuery() {
        personRepository.findByQuery();
        verify(template).query("select * from Person");
    }

    @Test
    public void shouldExecuteJNoSQLPrepare() {
        PreparedStatement statement = Mockito.mock(PreparedStatement.class);
        when(template.prepare(Mockito.anyString())).thenReturn(statement);
        personRepository.findByQuery("Ada");
        verify(statement).bind("id", "Ada");
    }

    interface PersonRepository extends Repository<Person, Long> {

        List<Person> findAll();

        Person findByName(String name);


        List<Person> findByAge(String age);

        void deleteByName(String name);

        List<Person> findByNameAndAge(String name, Integer age);

        Set<Person> findByAgeAndName(Integer age, String name);

        Stream<Person> findByNameAndAgeOrderByName(String name, Integer age);

        Queue<Person> findByNameAndAgeOrderByAge(String name, Integer age);

        Set<Person> findByNameAndAgeGreaterThanEqual(String name, Integer age);

        Set<Person> findByAgeGreaterThan(Integer age);

        Set<Person> findByAgeLessThanEqual(Integer age);

        Set<Person> findByAgeLessThan(Integer age);

        Set<Person> findByAgeBetween(Integer ageA, Integer ageB);

        Set<Person> findByNameLike(String name);

        @Query("select * from Person")
        Optional<Person> findByQuery();

        @Query("select * from Person where id = @id")
        Optional<Person> findByQuery(@Param("id") String id);
    }

    public interface VendorRepository extends Repository<Vendor, String> {

        Vendor findByPrefixes(String prefix);

        Vendor findByPrefixesIn(List<String> prefix);

    }
}