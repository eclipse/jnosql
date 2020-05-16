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
package org.eclipse.jnosql.artemis.reactive;

import org.eclipse.microprofile.reactive.streams.operators.CompletionSubscriber;
import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ObservableTest {


    @Test
    public void shouldReturnInstance() {
        Publisher<Animal> publisher = ReactiveStreams
                .fromIterable(Arrays.asList(new Animal("Lion"))).buildRs();
        assertNotNull(Observable.of(publisher));
    }

    @Test
    public void shouldReturnPredicate() {
        Publisher<Animal> publisher = ReactiveStreams
                .fromIterable(Arrays.asList(new Animal("Lion"))).buildRs();
        final Observable<Animal> observable = Observable.of(publisher);
        assertNotNull(observable);
        assertNotNull(observable.getPublisher());
        assertEquals(publisher, observable.getPublisher());
    }

    @Test
    public void shouldReturnSingleResult() {
        Publisher<Animal> publisher = ReactiveStreams
                .fromIterable(Arrays.asList(new Animal("Lion"))).buildRs();
        final Observable<Animal> observable = Observable.of(publisher);
        final CompletionStage<Optional<Animal>> singleResult = observable.getSingleResult();
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        singleResult.thenApply(Optional::isPresent).thenAccept(atomicBoolean::set);
        Assertions.assertTrue(atomicBoolean.get());
    }

    @Test
    public void shouldReturnEmptySingleResult() {
        Publisher<Animal> publisher = ReactiveStreams
                .<Animal>fromIterable(Collections.emptyList()).buildRs();
        final Observable<Animal> observable = Observable.of(publisher);
        final CompletionStage<Optional<Animal>> singleResult = observable.getSingleResult();
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        singleResult.thenApply(Optional::isPresent).thenAccept(atomicBoolean::set);
        Assertions.assertFalse(atomicBoolean.get());
    }

    @Test
    public void shouldReturnErrorSingleResult() {
        Publisher<Animal> publisher = ReactiveStreams
                .fromIterable(Arrays.asList(new Animal("Lion"), new Animal("Tiger"))).buildRs();
        final Observable<Animal> observable = Observable.of(publisher);
        final CompletionStage<Optional<Animal>> singleResult = observable.getSingleResult();
        final CompletableFuture<Optional<Animal>> future = singleResult.toCompletableFuture();
        Assertions.assertThrows(ExecutionException.class, () -> future.get());
    }

    @Test
    public void shouldReturnFirst() {
        Publisher<Animal> publisher = ReactiveStreams
                .fromIterable(Arrays.asList(new Animal("Lion"), new Animal("Tiger"))).buildRs();
        final Observable<Animal> observable = Observable.of(publisher);
        final CompletionStage<Optional<Animal>> singleResult = observable.getFirst();
        AtomicReference<Animal> reference = new AtomicReference();
        singleResult.thenApply(Optional::get).thenAccept(reference::set);
        Assertions.assertEquals(new Animal("Lion"), reference.get());
    }

    @Test
    public void shouldReturnList() {
        final List<Animal> animals = Arrays.asList(new Animal("Lion"), new Animal("Tiger"));
        Publisher<Animal> publisher = ReactiveStreams
                .fromIterable(animals).buildRs();
        final Observable<Animal> observable = Observable.of(publisher);
        final CompletionStage<List<Animal>> result = observable.getList();
        AtomicReference<List<Animal>> reference = new AtomicReference();
        result.thenAccept(reference::set);
        Assertions.assertEquals(animals, reference.get());
    }


    @Test
    public void shouldReturnCollect() {
        final List<Animal> animals = Arrays.asList(new Animal("Lion"), new Animal("Tiger"));
        Publisher<Animal> publisher = ReactiveStreams
                .fromIterable(animals).buildRs();
        final Observable<Animal> observable = Observable.of(publisher);
        final CompletionStage<Set<Animal>> result = observable.collect(Collectors.toSet());
        AtomicReference<Set<Animal>> reference = new AtomicReference();
        result.thenAccept(reference::set);
        assertThat(reference.get(), containsInAnyOrder(new Animal("Lion"),
                new Animal("Tiger")));
    }

    @Test
    public void shouldCompletionSubscriber() {
        final List<Animal> animals = Arrays.asList(new Animal("Lion"), new Animal("Tiger"));
        Publisher<Animal> publisher = ReactiveStreams
                .fromIterable(animals).buildRs();
        final Observable<Animal> observable = Observable.of(publisher);
        final CompletionSubscriber<Animal, List<String>> subscriber = ReactiveStreams.<Animal>builder()
                .map(Animal::getName).toList().build();
        final CompletionStage<List<String>> result = observable.subscribe(subscriber);
        AtomicReference<List<String>> reference = new AtomicReference();
        result.thenAccept(reference::set);
        assertThat(reference.get(), containsInAnyOrder("Lion", "Tiger"));
    }

    @Test
    public void shouldSubscribe() {
        Publisher<Animal> publisher = Mockito.mock(Publisher.class);
        final Observable<Animal> observable = Observable.of(publisher);
        final Subscriber<Animal> subscriber = ReactiveStreams.<Animal>builder()
                .map(Animal::getName).toList().build();
        observable.subscribe(subscriber);

    }

    @Test
    public void shouldBlockSingleResult() {
        Publisher<Animal> publisher = ReactiveStreams
                .fromIterable(Arrays.asList(new Animal("Lion"))).buildRs();
        final Observable<Animal> observable = Observable.of(publisher);
        final Optional<Animal> singleResult = observable.blockSingleResult();
        Assertions.assertTrue(singleResult.isPresent());
    }

    @Test
    public void shouldBlockFirst() {
        Publisher<Animal> publisher = ReactiveStreams
                .fromIterable(Arrays.asList(new Animal("Lion"), new Animal("Tiger"))).buildRs();
        final Observable<Animal> observable = Observable.of(publisher);
        final Optional<Animal> singleResult = observable.blockFirst();
        Assertions.assertEquals(new Animal("Lion"), singleResult.get());
    }

    @Test
    public void shouldBlockList() {
        final List<Animal> animals = Arrays.asList(new Animal("Lion"), new Animal("Tiger"));
        Publisher<Animal> publisher = ReactiveStreams
                .fromIterable(animals).buildRs();
        final Observable<Animal> observable = Observable.of(publisher);
        final List<Animal> result = observable.blockList();
        Assertions.assertEquals(animals, result);
    }

    @Test
    public void shouldBlockCollect() {
        final List<Animal> animals = Arrays.asList(new Animal("Lion"), new Animal("Tiger"));
        Publisher<Animal> publisher = ReactiveStreams
                .fromIterable(animals).buildRs();
        final Observable<Animal> observable = Observable.of(publisher);
        final Set<Animal> result = observable.blockCollect(Collectors.toSet());
        assertThat(result, containsInAnyOrder(new Animal("Lion"),
                new Animal("Tiger")));
    }


    @Test
    public void shouldBlockCompletionSubscriber() {
        final List<Animal> animals = Arrays.asList(new Animal("Lion"), new Animal("Tiger"));
        Publisher<Animal> publisher = ReactiveStreams
                .fromIterable(animals).buildRs();
        final Observable<Animal> observable = Observable.of(publisher);
        final CompletionSubscriber<Animal, List<String>> subscriber = ReactiveStreams.<Animal>builder()
                .map(Animal::getName).toList().build();
        final List<String> result = observable.blockSubscribe(subscriber);
        assertThat(result, containsInAnyOrder("Lion", "Tiger"));
    }


    @Test
    public void shouldBlockSingleResultDuration() {
        Publisher<Animal> publisher = ReactiveStreams
                .fromIterable(Arrays.asList(new Animal("Lion"))).buildRs();
        final Observable<Animal> observable = Observable.of(publisher);
        final Optional<Animal> singleResult = observable.blockSingleResult(Duration.ofSeconds(10));
        Assertions.assertTrue(singleResult.isPresent());
    }

    @Test
    public void shouldBlockFirstDuration() {
        Publisher<Animal> publisher = ReactiveStreams
                .fromIterable(Arrays.asList(new Animal("Lion"), new Animal("Tiger"))).buildRs();
        final Observable<Animal> observable = Observable.of(publisher);
        final Optional<Animal> singleResult = observable.blockFirst((Duration.ofSeconds(10)));
        Assertions.assertEquals(new Animal("Lion"), singleResult.get());
    }

    @Test
    public void shouldBlockListDuration() {
        final List<Animal> animals = Arrays.asList(new Animal("Lion"), new Animal("Tiger"));
        Publisher<Animal> publisher = ReactiveStreams
                .fromIterable(animals).buildRs();
        final Observable<Animal> observable = Observable.of(publisher);
        final List<Animal> result = observable.blockList((Duration.ofSeconds(10)));
        Assertions.assertEquals(animals, result);
    }

    @Test
    public void shouldBlockCollectDuration() {
        final List<Animal> animals = Arrays.asList(new Animal("Lion"), new Animal("Tiger"));
        Publisher<Animal> publisher = ReactiveStreams
                .fromIterable(animals).buildRs();
        final Observable<Animal> observable = Observable.of(publisher);
        final Set<Animal> result = observable.blockCollect(Collectors.toSet(), Duration.ofSeconds(10));
        assertThat(result, containsInAnyOrder(new Animal("Lion"),
                new Animal("Tiger")));
    }

    @Test
    public void shouldBlockCompletionSubscriberDuration() {
        final List<Animal> animals = Arrays.asList(new Animal("Lion"), new Animal("Tiger"));
        Publisher<Animal> publisher = ReactiveStreams
                .fromIterable(animals).buildRs();
        final Observable<Animal> observable = Observable.of(publisher);
        final CompletionSubscriber<Animal, List<String>> subscriber = ReactiveStreams.<Animal>builder()
                .map(Animal::getName).toList().build();
        final List<String> result = observable.blockSubscribe(subscriber, Duration.ofMinutes(1L));
        assertThat(result, containsInAnyOrder("Lion", "Tiger"));
    }


}