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
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collector;

import static java.util.Objects.requireNonNull;

/**
 * it is a wrapper of Publisher that observers and give several options to this instance.
 *
 * @param <T> the Entity
 */
public interface Observable<T> {


    /**
     * Get the {@link Publisher}
     *
     * @return the {@link Publisher}
     */
    Publisher<T> getPublisher();

    /**
     * Creates a {@link Subscriber} and return a single result
     *
     * @return the {@link CompletionStage} with the single result
     * @throws jakarta.nosql.NonUniqueResultException when there is more than one result
     */
    CompletionStage<Optional<T>> getSingleResult();

    /**
     * Creates a {@link Subscriber} and return the first result
     *
     * @return the {@link CompletionStage} with the single result
     */
    CompletionStage<Optional<T>> getFirst();

    /**
     * Creates a {@link Subscriber} and return the result as list
     *
     * @return the {@link CompletionStage} with the list
     */
    CompletionStage<List<T>> getList();

    /**
     * Creates a {@link Subscriber} and return the result as {@link Collector}
     *
     * @param collector the collect
     * @param <R>       the type of the result
     * @param <A>       the intermediate accumulation type of the Collector
     * @return the result of the reduction as @{@link CompletionStage}
     * @throws NullPointerException when collector is null
     */
    <R, A> CompletionStage<R> collect(Collector<? super T, A, R> collector);

    /**
     * Request {@link Publisher} to start streaming data.
     *
     * @param subscriber the subscriber
     */
    void subscribe(Subscriber<? super T> subscriber);

    /**
     * Request {@link Publisher} to start streaming data and returns its {@link CompletionStage}
     *
     * @param subscriber the subscriber
     * @param <E>        the return type
     * @return the {@link CompletionStage}
     */
    <E> CompletionStage<E> subscribe(CompletionSubscriber<T, E> subscriber);

    /**
     * Subscribe to this {@link Publisher} and block indefinitely until the upstream signals its completes.
     *
     * @return the {@link Optional} of a single result
     * @throws jakarta.nosql.NonUniqueResultException
     */
    Optional<T> blockSingleResult();

    /**
     * Subscribe to this {@link Publisher} and block indefinitely until the upstream signals its first value or completes.
     *
     * @return the {@link Optional} of the first result
     */
    Optional<T> blockFirst();

    /**
     * Subscribe to this {@link Publisher} and block indefinitely until the upstream signals its list of values
     *
     * @return the {@link List} of a single result
     */
    List<T> blockList();

    /**
     * Subscribe to this {@link Publisher} and block indefinitely until the upstream signals its values as {@link Collector}
     *
     * @return the {@link Collector} of a single result
     * @throws NullPointerException
     */
    <R, A> R blockCollect(Collector<? super T, A, R> collector);

    /**
     * Subscribe to this {@link Publisher} and block indefinitely until the upstream signals its values
     *
     * @param subscriber the subscriber
     * @param <E>        the return type
     * @return the result
     */
    <E> E blockSubscribe(CompletionSubscriber<T, E> subscriber);

    /**
     * Subscribe to this {@link Publisher} and block indefinitely until the upstream signals its completes.
     *
     * @param duration the maximum time period to wait for before raising an Exception
     * @return the {@link Optional} of a single result
     * @throws jakarta.nosql.NonUniqueResultException
     */
    Optional<T> blockSingleResult(Duration duration);

    /**
     * Subscribe to this {@link Publisher} and block indefinitely until the upstream signals its first value or completes.
     *
     * @param duration the maximum time period to wait for before raising an Exception
     * @return the {@link Optional} of the first result
     * @throws jakarta.nosql.NonUniqueResultException
     */
    Optional<T> blockFirst(Duration duration);

    /**
     * Subscribe to this {@link Publisher} and block indefinitely until the upstream signals its list of values
     *
     * @param duration the maximum time period to wait for before raising an Exception
     * @return the {@link List} of a single result
     * @throws jakarta.nosql.NonUniqueResultException
     */
    List<T> blockList(Duration duration);

    /**
     * Subscribe to this {@link Publisher} and block indefinitely until the upstream signals its values as {@link Collector}
     *
     * @param duration the maximum time period to wait for before raising an Exception
     * @return the {@link Collector} of a single result
     * @throws jakarta.nosql.NonUniqueResultException
     */
    <R, A> R blockCollect(Collector<? super T, A, R> collector, Duration duration);


    /**
     * Subscribe to this {@link Publisher} and block indefinitely until the upstream signals its values
     *
     * @param duration   the maximum time period to wait for before raising an Exception
     * @param <E>        the return type
     * @param subscriber the subscriber
     * @return the result
     */
    <E> E blockSubscribe(CompletionSubscriber<T, E> subscriber, Duration duration);

    /**
     * Creates a {@link Observable} instance
     *
     * @param publisher the publisher
     * @param <T>       the entity type
     * @return a instance of {@link Observable}
     * @throws NullPointerException when publisher is null
     */
    static <T> Observable<T> of(Publisher<T> publisher) {
        return new DefaultObservable<>(requireNonNull(publisher, "publisher"));
    }

}
