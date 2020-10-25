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
package org.eclipse.jnosql.mapping.keyvalue.reactive;

import jakarta.nosql.mapping.keyvalue.KeyValueTemplate;
import jakarta.nosql.tck.entities.Person;
import org.eclipse.jnosql.mapping.reactive.Observable;
import org.eclipse.microprofile.reactive.streams.operators.CompletionSubscriber;
import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import javax.enterprise.inject.Instance;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.Collections.singleton;
import static org.junit.jupiter.api.Assertions.assertEquals;

@MockitoSettings(strictness = Strictness.WARN)
class DefaultReactiveKeyValueTemplateTest {

    @Mock
    private KeyValueTemplate template;

    @Mock
    private Instance<KeyValueTemplate> instance;

    private ReactiveKeyValueTemplate manager;

    @BeforeEach
    public void setUp() {
        Mockito.when(instance.get()).thenReturn(template);
        this.manager = new DefaultReactiveKeyValueTemplate(instance);
    }

    @Test
    public void shouldPut() {

        AtomicReference<Person> reference = new AtomicReference<>();
        Person ada = Person.builder()
                .withId(1L)
                .withAge(30)
                .withName("Ada").build();
        Mockito.when(template.put(ada)).thenReturn(ada);
        final Observable<Person> observable = manager.put(ada);
        Mockito.verify(template, Mockito.never()).put(ada);

        CompletionStage<Optional<Person>> completion = observable.getFirst();
        completion.thenAccept(o -> reference.set(o.get()));
        Mockito.verify(template).put(ada);
        assertEquals(ada, reference.get());

    }

    @Test
    public void shouldPutIterable() {
        List<Person> people = new ArrayList<>();
        Person ada = Person.builder()
                .withId(1L)
                .withAge(30)
                .withName("Ada").build();
        List<Person> adas = Arrays.asList(ada, ada);
        Mockito.when(template.put(adas)).thenReturn(adas);
        final Observable<Person> observable = manager.put(adas);
        CompletionSubscriber<Person, Void> subscriber = ReactiveStreams.<Person>builder()
                .forEach(people::add).build();
        Mockito.verify(template, Mockito.never()).put(adas);

        observable.subscribe(subscriber);
        MatcherAssert.assertThat(people, Matchers.containsInAnyOrder(ada, ada));
        Mockito.verify(template).put(adas);
    }

    @Test
    public void shouldPutDuration() {

        AtomicReference<Person> reference = new AtomicReference<>();
        Person ada = Person.builder()
                .withId(1L)
                .withAge(30)
                .withName("Ada").build();
        Duration duration = Duration.ofSeconds(1L);
        Mockito.when(template.put(ada, duration)).thenReturn(ada);
        final Observable<Person> publisher = manager.put(ada, duration);
        CompletionSubscriber<Person, Optional<Person>> subscriber = ReactiveStreams.<Person>builder().findFirst().build();
        Mockito.verify(template, Mockito.never()).put(ada, duration);

        publisher.subscribe(subscriber);
        CompletionStage<Optional<Person>> completion = subscriber.getCompletion();
        completion.thenAccept(o -> reference.set(o.get()));
        Mockito.verify(template).put(ada, duration);
        assertEquals(ada, reference.get());

    }

    @Test
    public void shouldPutIterableDuration() {
        Duration duration = Duration.ofSeconds(1L);
        List<Person> people = new ArrayList<>();
        Person ada = Person.builder()
                .withId(1L)
                .withAge(30)
                .withName("Ada").build();
        List<Person> adas = Arrays.asList(ada, ada);
        Mockito.when(template.put(adas, duration)).thenReturn(adas);
        final Observable<Person> publisher = manager.put(adas, duration);
        CompletionSubscriber<Person, Void> subscriber = ReactiveStreams.<Person>builder()
                .forEach(people::add).build();
        Mockito.verify(template, Mockito.never()).put(adas, duration);

        publisher.subscribe(subscriber);
        MatcherAssert.assertThat(people, Matchers.containsInAnyOrder(ada, ada));
        Mockito.verify(template).put(adas, duration);
    }

    @Test
    public void shouldGet() {
        AtomicReference<Person> reference = new AtomicReference<>();
        Person ada = Person.builder()
                .withId(1L)
                .withAge(30)
                .withName("Ada").build();
        Mockito.when(template.get(1L, Person.class)).thenReturn(Optional.of(ada));
        final Observable<Person> publisher = manager.get(1L, Person.class);
        CompletionSubscriber<Person, Optional<Person>> subscriber = ReactiveStreams.<Person>builder().findFirst().build();
        Mockito.verify(template, Mockito.never()).get(ada, Person.class);

        publisher.subscribe(subscriber);
        CompletionStage<Optional<Person>> completion = subscriber.getCompletion();
        completion.thenAccept(o -> reference.set(o.get()));
        Mockito.verify(template).get(1L, Person.class);
        assertEquals(ada, reference.get());
    }

    @Test
    public void shouldGetIterable() {
        List<Person> people = new ArrayList<>();
        Person ada = Person.builder()
                .withId(1L)
                .withAge(30)
                .withName("Ada").build();
        Mockito.when(template.get(singleton(1L), Person.class)).thenReturn(singleton(ada));
        final Set<Long> ids = singleton(1L);
        final Observable<Person> publisher = manager.get(ids, Person.class);
        CompletionSubscriber<Person, Void> subscriber = ReactiveStreams.<Person>builder()
                .forEach(people::add).build();
        Mockito.verify(template, Mockito.never()).get(ids, Person.class);

        publisher.subscribe(subscriber);
        Mockito.verify(template).get(ids, Person.class);
        MatcherAssert.assertThat(people, Matchers.containsInAnyOrder(ada));
    }

    @Test
    public void shouldQuery() {
        List<Person> people = new ArrayList<>();
        Person ada = Person.builder()
                .withId(1L)
                .withAge(30)
                .withName("Ada").build();
        Mockito.when(template.query("get 1", Person.class))
                .thenReturn(singleton(ada).stream());
        final Observable<Person> publisher = manager.query("get 1", Person.class);
        CompletionSubscriber<Person, Void> subscriber = ReactiveStreams.<Person>builder()
                .forEach(people::add).build();
        Mockito.verify(template, Mockito.never()).query("get 1", Person.class);

        publisher.subscribe(subscriber);
        Mockito.verify(template).query("get 1", Person.class);
        MatcherAssert.assertThat(people, Matchers.containsInAnyOrder(ada));
    }


    @Test
    public void shouldGetSingleResult() {
        List<Person> people = new ArrayList<>();
        Person ada = Person.builder()
                .withId(1L)
                .withAge(30)
                .withName("Ada").build();
        Mockito.when(template.getSingleResult("get 1", Person.class))
                .thenReturn(Optional.of(ada));
        final Observable<Person> publisher = manager.getSingleResult("get 1", Person.class);
        CompletionSubscriber<Person, Void> subscriber = ReactiveStreams.<Person>builder()
                .forEach(people::add).build();
        Mockito.verify(template, Mockito.never()).getSingleResult("get 1", Person.class);

        publisher.subscribe(subscriber);
        Mockito.verify(template).getSingleResult("get 1", Person.class);
        MatcherAssert.assertThat(people, Matchers.containsInAnyOrder(ada));
    }

    @Test
    public void shouldQueryVoid() {
        AtomicBoolean atomic = new AtomicBoolean(false);
        final Observable<Void> publisher = manager.query("get 1");
        Mockito.verify(template, Mockito.never()).query("get 1");
        CompletionSubscriber<Void, Optional<Void>> subscriber = ReactiveStreams.<Void>builder()
                .findFirst().build();
        publisher.subscribe(subscriber);
        final CompletionStage<Optional<Void>> completion = subscriber.getCompletion();
        completion.thenAccept(v -> atomic.set(true));
        Mockito.verify(template).query("get 1");
        Assertions.assertTrue(atomic.get());
    }

    @Test
    public void shouldDelete() {
        AtomicBoolean atomic = new AtomicBoolean(false);
        final Observable<Void> publisher = manager.delete(1L);
        Mockito.verify(template, Mockito.never()).delete(1L);
        CompletionSubscriber<Void, Optional<Void>> subscriber = ReactiveStreams.<Void>builder()
                .findFirst().build();
        publisher.subscribe(subscriber);
        final CompletionStage<Optional<Void>> completion = subscriber.getCompletion();
        completion.thenAccept(v -> atomic.set(true));
        Mockito.verify(template).delete(1L);
        Assertions.assertTrue(atomic.get());
    }

    @Test
    public void shouldDeleteIterable() {
        AtomicBoolean atomic = new AtomicBoolean(false);
        List<Long> ids = Collections.singletonList(1L);
        final Observable<Void> publisher = manager.delete(ids);
        Mockito.verify(template, Mockito.never()).delete(ids);
        CompletionSubscriber<Void, Optional<Void>> subscriber = ReactiveStreams.<Void>builder()
                .findFirst().build();
        publisher.subscribe(subscriber);
        final CompletionStage<Optional<Void>> completion = subscriber.getCompletion();
        completion.thenAccept(v -> atomic.set(true));
        Mockito.verify(template).delete(ids);
        Assertions.assertTrue(atomic.get());
    }
}