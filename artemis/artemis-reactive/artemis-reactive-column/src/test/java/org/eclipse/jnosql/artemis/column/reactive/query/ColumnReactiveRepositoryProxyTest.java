/*
 *  Copyright (c) 2020 Otávio Santana and others
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */
package org.eclipse.jnosql.artemis.column.reactive.query;

import jakarta.nosql.Condition;
import jakarta.nosql.column.Column;
import jakarta.nosql.column.ColumnCondition;
import jakarta.nosql.column.ColumnDeleteQuery;
import jakarta.nosql.column.ColumnQuery;
import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.Param;
import jakarta.nosql.mapping.PreparedStatement;
import jakarta.nosql.mapping.Query;
import jakarta.nosql.mapping.column.ColumnTemplate;
import jakarta.nosql.mapping.reflection.ClassMappings;
import jakarta.nosql.tck.entities.Person;
import jakarta.nosql.tck.test.CDIExtension;
import org.eclipse.jnosql.artemis.column.reactive.ReactiveColumnTemplate;
import org.eclipse.jnosql.artemis.column.reactive.ReactiveColumnTemplateProducer;
import org.eclipse.jnosql.artemis.reactive.Observable;
import org.eclipse.jnosql.artemis.reactive.ReactiveRepository;
import org.eclipse.microprofile.reactive.streams.operators.CompletionSubscriber;
import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;

import javax.inject.Inject;
import java.lang.reflect.Proxy;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@CDIExtension
public class ColumnReactiveRepositoryProxyTest {

    private ColumnTemplate template;

    @Inject
    private ClassMappings classMappings;

    @Inject
    private Converters converters;

    @Inject
    private ReactiveColumnTemplateProducer reactiveProducer;

    private PersonRepository personRepository;


    @BeforeEach
    public void setUp() {
        this.template = Mockito.mock(ColumnTemplate.class);

        final ReactiveColumnTemplate reactiveTemplate = reactiveProducer.get(template);

        ReactiveColumnRepositoryProxy personHandler = new ReactiveColumnRepositoryProxy(reactiveTemplate,
                template, converters, classMappings, PersonRepository.class);

        when(template.insert(any(Person.class))).thenReturn(Person.builder().build());
        when(template.insert(any(Person.class), any(Duration.class))).thenReturn(Person.builder().build());
        when(template.update(any(Person.class))).thenReturn(Person.builder().build());
        personRepository = (PersonRepository) Proxy.newProxyInstance(PersonRepository.class.getClassLoader(),
                new Class[]{PersonRepository.class},
                personHandler);
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
        final Observable<Person> save = personRepository.save(person);
        assertNotNull(save);
        AtomicReference<Person> atomicReference = new AtomicReference<>();
        final CompletionStage<Optional<Person>> completion = save.getFirst();
        completion.thenApply(Optional::get).thenAccept(atomicReference::set);
        verify(template).insert(captor.capture());
        Person value = captor.getValue();
        assertEquals(person, value);
        assertNotNull(atomicReference.get());
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
        final Observable<Person> observable = personRepository.save(person);
        assertNotNull(observable);
        final CompletionStage<Optional<Person>> completion = observable.getFirst();
        AtomicBoolean atomicBoolean = new AtomicBoolean();
        completion.thenAccept(o -> atomicBoolean.set(true));
        verify(template).update(captor.capture());
        Person value = captor.getValue();
        assertEquals(person, value);
        assertNotNull(atomicBoolean.get());
    }


    @Test
    public void shouldSaveIterable() {

        when(template.singleResult(Mockito.any(ColumnQuery.class))).thenReturn(Optional.empty());

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
    public void shouldDeleteByName() {
        ArgumentCaptor<ColumnDeleteQuery> captor = ArgumentCaptor.forClass(ColumnDeleteQuery.class);
        final Publisher<Void> publisher = personRepository.deleteByName("Ada");
        final CompletionSubscriber<Void, Optional<Void>> subscriber = ReactiveStreams.<Void>builder().findFirst().build();
        publisher.subscribe(subscriber);
        final CompletionStage<Optional<Void>> completion = subscriber.getCompletion();
        AtomicBoolean atomicBoolean = new AtomicBoolean();
        completion.thenAccept(v -> atomicBoolean.set(true));

        verify(template).delete(captor.capture());
        ColumnDeleteQuery deleteQuery = captor.getValue();
        ColumnCondition condition = deleteQuery.getCondition().get();
        assertEquals("Person", deleteQuery.getColumnFamily());
        assertEquals(Condition.EQUALS, condition.getCondition());
        assertEquals(Column.of("name", "Ada"), condition.getColumn());

    }

    @Test
    public void shouldFindById() {
        final Observable<Person> observable = personRepository.findById(10L);
        final CompletionStage<Optional<Person>> completion = observable.getFirst();
        AtomicReference<Person> reference = new AtomicReference<>();
        completion.thenApply(Optional::get).thenAccept(reference::set);
        Assertions.assertNotNull(reference);
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
        final Observable<Void> observable = personRepository.deleteById(10L);
        final CompletionStage<Optional<Void>> completion = observable.getFirst();
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        completion.thenAccept(o -> atomicBoolean.set(true));

        verify(template).delete(Person.class, 10L);
        Assertions.assertTrue(atomicBoolean.get());

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

        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        final Observable<Boolean> observable = personRepository.existsById(10L);
        final CompletionStage<Optional<Boolean>> completion = observable.getFirst();
        completion.thenApply(o -> o.orElse(false)).thenAccept(atomicBoolean::set);
        assertTrue(atomicBoolean.get());
        Mockito.verify(template).find(Mockito.eq(Person.class), Mockito.eq(10L));
    }

    @Test
    public void shouldNotContainsById() {
        when(template.find(Mockito.eq(Person.class), Mockito.any(Long.class)))
                .thenReturn(Optional.empty());

        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        final Observable<Boolean> observable = personRepository.existsById(10L);
        final CompletionStage<Optional<Boolean>> completion = observable.getFirst();
        completion.thenApply(o -> o.orElse(true)).thenAccept(atomicBoolean::set);
        assertFalse(atomicBoolean.get());
        Mockito.verify(template).find(Mockito.eq(Person.class), Mockito.eq(10L));

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
    public void shouldFindAll() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(ada));

        List<Person> persons = personRepository.findAll();
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        ColumnQuery query = captor.getValue();
        assertFalse(query.getCondition().isPresent());
        assertEquals("Person", query.getColumnFamily());

    }

    @Test
    public void shouldFindByNameANDAge() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(Mockito.any(ColumnQuery.class)))
                .thenReturn(Stream.of(ada));

        Publisher<Person> publisher = personRepository.findByNameAndAge("name", 20);
        final CompletionSubscriber<Person, List<Person>> subscriber = ReactiveStreams.<Person>builder().toList().build();
        publisher.subscribe(subscriber);
        AtomicReference<List<Person>> reference = new AtomicReference<>();
        final CompletionStage<List<Person>> completion = subscriber.getCompletion();
        completion.thenAccept(reference::set);
        final List<Person> people = reference.get();
        Assertions.assertNotNull(people);
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        assertThat(people, Matchers.contains(ada));

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
        final Publisher<Person> publisher = personRepository.findByQuery("Ada");
        final CompletionSubscriber<Person, List<Person>> subscriber = ReactiveStreams.<Person>builder().toList().build();
        publisher.subscribe(subscriber);
        final CompletionStage<List<Person>> completion = subscriber.getCompletion();
        AtomicReference<List<Person>> reference = new AtomicReference<>();
        completion.thenAccept(reference::set);
        final List<Person> people = reference.get();
        assertNotNull(people);
        verify(statement).bind("id", "Ada");
    }

    interface PersonRepository extends ReactiveRepository<Person, Long> {

        List<Person> findAll();

        Publisher<Void> deleteByName(String name);

        Publisher<Person> findByNameAndAge(String name, Integer age);

        @Query("select * from Person")
        Optional<Person> findByQuery();

        @Query("select * from Person where id = @id")
        Publisher<Person> findByQuery(@Param("id") String id);
    }
}
