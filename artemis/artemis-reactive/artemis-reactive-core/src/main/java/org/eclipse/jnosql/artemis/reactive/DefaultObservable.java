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
import org.reactivestreams.Publisher;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collector;

final class DefaultObservable<T> implements Observable<T> {

    private final SingleResultFunction<T> singleResultFunction = new SingleResultFunction<>();
    private final Publisher<T> publisher;

    DefaultObservable(Publisher<T> publisher) {
        this.publisher = publisher;
    }

    @Override
    public Publisher<T> getPublisher() {
        return publisher;
    }

    @Override
    public CompletionStage<Optional<T>> getSingleResult() {
        final CompletionSubscriber<T, List<T>> subscriber = ReactiveStreams.<T>builder().toList().build();
        publisher.subscribe(subscriber);
        final CompletionStage<List<T>> completion = subscriber.getCompletion();
        return completion.thenApply(singleResultFunction);
    }

    @Override
    public CompletionStage<Optional<T>> getFirst() {
        final CompletionSubscriber<T, Optional<T>> subscriber = ReactiveStreams.<T>builder().findFirst().build();
        publisher.subscribe(subscriber);
        return subscriber.getCompletion();
    }

    @Override
    public CompletionStage<List<T>> getList() {
        final CompletionSubscriber<T, List<T>> subscriber = ReactiveStreams.<T>builder().toList().build();
        publisher.subscribe(subscriber);
        return subscriber.getCompletion();
    }

    @Override
    public <R, A> CompletionStage<R> collect(Collector<? super T, A, R> collector) {
        Objects.requireNonNull(collector, "collector is required");
        final CompletionSubscriber<T, R> subscriber = ReactiveStreams.<T>builder().collect(collector).build();
        return subscriber.getCompletion();
    }

    @Override
    public Optional<T> blockSingleResult() {
        final CompletionStage<Optional<T>> singleResult = getSingleResult();
        return block(singleResult, null);
    }

    @Override
    public Optional<T> blockFirst() {
        final CompletionStage<Optional<T>> first = getFirst();
        return block(first, null);
    }

    @Override
    public List<T> blockList() {
        return getList(null);
    }

    @Override
    public <R, A> R blockCollect(Collector<? super T, A, R> collector) {
        return getCollector(collector, null);
    }

    @Override
    public Optional<T> blockSingleResult(Duration duration) {
        Objects.requireNonNull(duration, "duration is required");
        final CompletionStage<Optional<T>> singleResult = getSingleResult();
        return block(singleResult, duration);
    }

    @Override
    public Optional<T> blockFirst(Duration duration) {
        Objects.requireNonNull(duration, "duration is required");
        final CompletionStage<Optional<T>> singleResult = getFirst();
        return block(singleResult, duration);
    }

    @Override
    public List<T> blockList(Duration duration) {
        Objects.requireNonNull(duration, "duration is required");
        return getList(duration);
    }


    @Override
    public <R, A> R blockCollect(Collector<? super T, A, R> collector, Duration duration) {
        Objects.requireNonNull(duration, "duration is required");
        return getCollector(collector, duration);
    }

    private <R, A> R getCollector(Collector<? super T, A, R> collector, Duration duration) {
        Objects.requireNonNull(collector, "collector is required");
        final CompletionStage<R> stage = collect(collector);
        final CompletableFuture<R> future = stage.toCompletableFuture();
        return execute(future, duration);
    }

    private List<T> getList(Duration duration) {
        final CompletionStage<List<T>> completionStage = getList();
        final CompletableFuture<List<T>> future = completionStage.toCompletableFuture();
        return execute(future, duration);
    }

    private Optional<T> block(CompletionStage<Optional<T>> result, Duration duration) {
        final CompletableFuture<Optional<T>> future = result.toCompletableFuture();
        return execute(future, duration);
    }

    private <E> E execute(CompletableFuture<E> future, Duration duration) {
        try {
            if (duration == null) {
                return future.get();
            } else {
                return future.get(duration.getNano(), TimeUnit.NANOSECONDS);
            }
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new ReactiveException("An error to block an Observable", e);
        }
    }

}
