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
package org.eclipse.jnosql.artemis.column.reactive;

import jakarta.nosql.column.ColumnDeleteQuery;
import jakarta.nosql.column.ColumnQuery;
import jakarta.nosql.mapping.column.ColumnTemplate;
import jakarta.nosql.tck.entities.Person;
import org.eclipse.jnosql.artemis.reactive.Observable;
import org.eclipse.microprofile.reactive.streams.operators.CompletionSubscriber;
import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.reactivestreams.Publisher;

import javax.enterprise.inject.Instance;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MockitoSettings(strictness = Strictness.WARN)
class DefaultReactiveColumnTemplateTest {
    @Mock
    private ColumnTemplate template;

    @Mock
    private Instance<ColumnTemplate> instance;

    private ReactiveColumnTemplate manager;

    @BeforeEach
    public void setUp() {
        Mockito.when(instance.get()).thenReturn(template);
        this.manager = new DefaultReactiveColumnTemplate(instance);
    }

    @Test
    public void shouldInsert() {
        AtomicReference<Person> reference = new AtomicReference<>();
        Person ada = Person.builder()
                .withId(1L)
                .withAge(30)
                .withName("Ada").build();
        Mockito.when(template.insert(ada)).thenReturn(ada);
        final Publisher<Person> publisher = manager.insert(ada).getPublisher();
        CompletionSubscriber<Person, Optional<Person>> subscriber = ReactiveStreams.<Person>builder().findFirst().build();
        Mockito.verify(template, Mockito.never()).insert(ada);

        publisher.subscribe(subscriber);
        CompletionStage<Optional<Person>> completion = subscriber.getCompletion();
        completion.thenAccept(o -> reference.set(o.get()));
        Mockito.verify(template).insert(ada);
        assertEquals(ada, reference.get());

    }

    @Test
    public void shouldInsertDuration() {
        Duration duration = Duration.ofSeconds(1L);
        AtomicReference<Person> reference = new AtomicReference<>();
        Person ada = Person.builder()
                .withId(1L)
                .withAge(30)
                .withName("Ada").build();
        Mockito.when(template.insert(ada, duration)).thenReturn(ada);
        final Publisher<Person> publisher = manager.insert(ada, duration).getPublisher();
        CompletionSubscriber<Person, Optional<Person>> subscriber = ReactiveStreams.<Person>builder().findFirst().build();
        Mockito.verify(template, Mockito.never()).insert(ada, duration);

        publisher.subscribe(subscriber);
        CompletionStage<Optional<Person>> completion = subscriber.getCompletion();
        completion.thenAccept(o -> reference.set(o.get()));
        Mockito.verify(template).insert(ada, duration);
        assertEquals(ada, reference.get());
    }

    @Test
    public void shouldInsertIterable() {
        AtomicReference<List<Person>> reference = new AtomicReference<>();
        Person ada = Person.builder()
                .withId(1L)
                .withAge(30)
                .withName("Ada").build();
        Collection<Person> adas = Arrays.asList(ada, ada);
        Mockito.when(template.insert(adas)).thenReturn(adas);
        final Publisher<Person> publisher = manager.insert(adas).getPublisher();
        CompletionSubscriber<Person, List<Person>> subscriber = ReactiveStreams.<Person>builder().toList().build();
        Mockito.verify(template, Mockito.never()).insert(adas);

        publisher.subscribe(subscriber);
        CompletionStage<List<Person>> completion = subscriber.getCompletion();
        completion.thenAccept(reference::set);
        Mockito.verify(template).insert(adas);
        MatcherAssert.assertThat(reference.get(), Matchers.containsInAnyOrder(ada, ada));

    }

    @Test
    public void shouldInsertIterableDuration() {
        Duration duration = Duration.ofSeconds(10);
        AtomicReference<List<Person>> reference = new AtomicReference<>();
        Person ada = Person.builder()
                .withId(1L)
                .withAge(30)
                .withName("Ada").build();
        Collection<Person> adas = Arrays.asList(ada, ada);
        Mockito.when(template.insert(adas, duration)).thenReturn(adas);
        final Publisher<Person> publisher = manager.insert(adas, duration).getPublisher();
        CompletionSubscriber<Person, List<Person>> subscriber = ReactiveStreams.<Person>builder().toList().build();
        Mockito.verify(template, Mockito.never()).insert(adas, duration);

        publisher.subscribe(subscriber);
        CompletionStage<List<Person>> completion = subscriber.getCompletion();
        completion.thenAccept(reference::set);
        Mockito.verify(template).insert(adas, duration);
        MatcherAssert.assertThat(reference.get(), Matchers.containsInAnyOrder(ada, ada));
    }


    @Test
    public void shouldUpdate() {
        AtomicReference<Person> reference = new AtomicReference<>();
        Person ada = Person.builder()
                .withId(1L)
                .withAge(30)
                .withName("Ada").build();
        Mockito.when(template.update(ada)).thenReturn(ada);
        final Observable<Person> observable = manager.update(ada);
        Mockito.verify(template, Mockito.never()).update(ada);
        CompletionStage<Optional<Person>> completion = observable.getFirst();
        completion.thenAccept(o -> reference.set(o.get()));
        Mockito.verify(template).update(ada);
        assertEquals(ada, reference.get());

    }

    @Test
    public void shouldUpdateIterable() {
        AtomicReference<List<Person>> reference = new AtomicReference<>();
        Person ada = Person.builder()
                .withId(1L)
                .withAge(30)
                .withName("Ada").build();
        Collection<Person> adas = Arrays.asList(ada, ada);
        Mockito.when(template.update(adas)).thenReturn(adas);
        final Observable<Person> observable = manager.update(adas);
        Mockito.verify(template, Mockito.never()).update(adas);

        CompletionStage<List<Person>> completion = observable.getList();
        completion.thenAccept(reference::set);
        Mockito.verify(template).update(adas);
        MatcherAssert.assertThat(reference.get(), Matchers.containsInAnyOrder(ada, ada));

    }

    @Test
    public void shouldQuery() {
        String query = "select * from Person";
        AtomicReference<List<Person>> reference = new AtomicReference<>();
        Person ada = Person.builder()
                .withId(1L)
                .withAge(30)
                .withName("Ada").build();
        Mockito.when(template.query(query)).thenReturn(Stream.of(ada));
        final Observable<Person> observable = manager.query(query);
        Mockito.verify(template, Mockito.never()).query(query);

        CompletionStage<List<Person>> completion = observable.getList();
        completion.thenAccept(reference::set);
        Mockito.verify(template).query(query);
        MatcherAssert.assertThat(reference.get(), Matchers.containsInAnyOrder(ada));
    }


    @Test
    public void shouldSingleResult() {
        String query = "select * from Person";
        AtomicReference<List<Person>> reference = new AtomicReference<>();
        Person ada = Person.builder()
                .withId(1L)
                .withAge(30)
                .withName("Ada").build();
        Mockito.when(template.singleResult(query)).thenReturn(Optional.of(ada));
        final Observable<Person> publisher = manager.singleResult(query);
        Mockito.verify(template, Mockito.never()).singleResult(query);

        CompletionStage<List<Person>> completion = publisher.getList();
        completion.thenAccept(reference::set);
        Mockito.verify(template).singleResult(query);
        MatcherAssert.assertThat(reference.get(), Matchers.containsInAnyOrder(ada));
    }

    @Test
    public void shouldFind() {
        AtomicReference<List<Person>> reference = new AtomicReference<>();
        Person ada = Person.builder()
                .withId(1L)
                .withAge(30)
                .withName("Ada").build();
        Mockito.when(template.find(Person.class, 1L)).thenReturn(Optional.of(ada));
        final Observable<Person> observable = manager.find(Person.class, 1L);
        Mockito.verify(template, Mockito.never()).find(Person.class, 1L);

        CompletionStage<List<Person>> completion = observable.getList();
        completion.thenAccept(reference::set);
        Mockito.verify(template).find(Person.class, 1L);
        MatcherAssert.assertThat(reference.get(), Matchers.containsInAnyOrder(ada));
    }

    @Test
    public void shouldSelect() {
        ColumnQuery query = Mockito.mock(ColumnQuery.class);
        AtomicReference<List<Person>> reference = new AtomicReference<>();
        Person ada = Person.builder()
                .withId(1L)
                .withAge(30)
                .withName("Ada").build();
        Mockito.when(template.select(query)).thenReturn(Stream.of(ada));
        final Observable<Person> observable = manager.select(query);
        Mockito.verify(template, Mockito.never()).select(query);

        CompletionStage<List<Person>> completion = observable.getList();
        completion.thenAccept(reference::set);
        Mockito.verify(template).select(query);
        MatcherAssert.assertThat(reference.get(), Matchers.containsInAnyOrder(ada));
    }

    @Test
    public void shouldQuerySingleResult() {
        ColumnQuery query = Mockito.mock(ColumnQuery.class);
        AtomicReference<List<Person>> reference = new AtomicReference<>();
        Person ada = Person.builder()
                .withId(1L)
                .withAge(30)
                .withName("Ada").build();
        Mockito.when(template.singleResult(query)).thenReturn(Optional.of(ada));
        final Observable<Person> observable = manager.singleResult(query);
        Mockito.verify(template, Mockito.never()).singleResult(query);

        CompletionStage<List<Person>> completion = observable.getList();
        completion.thenAccept(reference::set);
        Mockito.verify(template).singleResult(query);
        MatcherAssert.assertThat(reference.get(), Matchers.containsInAnyOrder(ada));
    }

    @Test
    public void shouldCountString() {
        String entity = "entity";
        AtomicLong atomicLong = new AtomicLong();
        Mockito.when(template.count(entity)).thenReturn(1L);
        final Observable<Long> observable = manager.count(entity);
        Mockito.verify(template, Mockito.never()).count(entity);

        CompletionStage<Optional<Long>> completion = observable.getFirst();
        completion.thenAccept(o -> atomicLong.set(o.get()));
        Mockito.verify(template).count(entity);
        assertEquals(1L, atomicLong.get());
    }

    @Test
    public void shouldCountClass() {
        Class<?> entity = Person.class;
        AtomicLong atomicLong = new AtomicLong();
        Mockito.when(template.count(entity)).thenReturn(1L);
        final Observable<Long> observable = manager.count(entity);
        Mockito.verify(template, Mockito.never()).count(entity);

        CompletionStage<Optional<Long>> completion = observable.getFirst();
        completion.thenAccept(o -> atomicLong.set(o.get()));
        Mockito.verify(template).count(entity);
        assertEquals(1L, atomicLong.get());
    }


    @Test
    public void shouldDelete() {
        AtomicLong atomicLong = new AtomicLong();
        ColumnDeleteQuery query = Mockito.mock(ColumnDeleteQuery.class);
        final Observable<Void> observable = manager.delete(query);
        Mockito.verify(template, Mockito.never()).delete(query);

        CompletionStage<Optional<Void>> completion = observable.getFirst();
        completion.thenAccept(o -> atomicLong.incrementAndGet());
        Mockito.verify(template).delete(query);
        assertEquals(1L, atomicLong.get());
    }

    @Test
    public void shouldDeleteClass() {
        Class<?> entity = Person.class;
        AtomicLong atomicLong = new AtomicLong();
        Mockito.when(template.count(entity)).thenReturn(1L);
        ColumnDeleteQuery query = Mockito.mock(ColumnDeleteQuery.class);
        final Observable<Void> observable = manager.delete(Person.class, 1L);
        Mockito.verify(template, Mockito.never()).delete(Person.class, 1L);

        CompletionStage<Optional<Void>> completion = observable.getFirst();
        completion.thenAccept(o -> atomicLong.incrementAndGet());
        Mockito.verify(template).delete(Person.class, 1L);
        assertEquals(1L, atomicLong.get());
    }
}
