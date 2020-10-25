/*
 *  Copyright (c) 2020 Otávio Santana and others
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
package org.eclipse.jnosql.mapping.document.reactive.query;

import jakarta.nosql.mapping.MappingException;
import org.eclipse.jnosql.mapping.reflection.ClassMapping;
import org.eclipse.jnosql.mapping.reflection.FieldMapping;
import org.eclipse.jnosql.mapping.document.reactive.ReactiveDocumentTemplate;
import org.eclipse.jnosql.mapping.reactive.Observable;
import org.eclipse.jnosql.mapping.reactive.ReactiveException;
import org.eclipse.jnosql.mapping.reactive.ReactiveRepository;
import org.eclipse.microprofile.reactive.streams.operators.CompletionSubscriber;
import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams;
import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import static jakarta.nosql.mapping.IdNotFoundException.KEY_NOT_FOUND_EXCEPTION_SUPPLIER;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;


public abstract class AbstractReactiveDocumentRepository<T, K> implements ReactiveRepository<T, K> {

    protected abstract ReactiveDocumentTemplate getTemplate();

    protected abstract ClassMapping getClassMapping();
    @Override
    public <S extends T> Observable<S> save(S entity) {
        Objects.requireNonNull(entity, "entity is required");
        Object id = getIdField().read(entity);

        if (nonNull(id)) {
            final Observable<Boolean> publisher = existsById((K) id);
            final CompletionStage<Boolean> stage = publisher.getFirst().thenApply(o -> o.orElse(false));

            final CompletionStage<S> operation = stage.thenApplyAsync(exist -> {
                if (exist) {
                    return getTemplate().update(entity);
                } else {
                    return getTemplate().insert(entity);
                }
            }).thenCompose(this::loadPublisher);
            return Observable.of(ReactiveStreams.fromCompletionStage(operation).buildRs());
        } else {
            return getTemplate().insert(entity);
        }
    }

    @Override
    public <S extends T> Observable<S> save(Iterable<S> entities) {
        Objects.requireNonNull(entities, "entities is required");

        List<CompletionStage<S>> stages = new ArrayList<>();
        for (S entity : entities) {
            final Observable<S> publisher = save(entity);
            final CompletionStage<S> stage = publisher.getFirst().thenApply(v -> v.orElse(null));
            stages.add(stage);
        }

        final Publisher<S> publisher = stages.stream()
                .map(ReactiveStreams::fromCompletionStage)
                .reduce(ReactiveStreams::concat)
                .orElse(ReactiveStreams.empty()).buildRs();
        return Observable.of(publisher);
    }

    @Override
    public Observable<Void> deleteById(K id) {
        return getTemplate().delete(getEntityClass(), id);
    }

    @Override
    public Observable<Void> deleteById(Iterable<K> ids) {
        requireNonNull(ids, "ids is required");
        List<CompletionStage<Void>> stages = new ArrayList<>();
        for (K id : ids) {
            final Observable<Void> publisher = deleteById(id);
            final CompletionStage<Void> stage = publisher.getFirst().thenApply(v -> v.orElse(null));
            stages.add(stage);
        }

        final Publisher<Void> publisher = stages.stream()
                .map(ReactiveStreams::fromCompletionStage)
                .reduce(ReactiveStreams::concat)
                .orElse(ReactiveStreams.empty()).buildRs();
        return Observable.of(publisher);
    }

    @Override
    public Observable<T> findById(K id) {
        requireNonNull(id, "id is required");
        return getTemplate().find(getEntityClass(), id);
    }

    @Override
    public Observable<T> findById(Iterable<K> ids) {
        requireNonNull(ids, "id is required");

        List<CompletionStage<Optional<T>>> stages = new ArrayList<>();
        for (K id : ids) {
            final Observable<T> publisher = findById(id);
            final CompletionStage<Optional<T>> subscriber = publisher.getFirst();
            stages.add(subscriber);
        }

        final Publisher<Optional<T>> publisher = stages.stream()
                .map(ReactiveStreams::fromCompletionStage)
                .reduce(ReactiveStreams::concat)
                .orElse(ReactiveStreams.empty()).buildRs();

        final CompletionSubscriber<Optional<T>, List<T>> subscriber = ReactiveStreams.<Optional<T>>builder()
                .filter(Optional::isPresent)
                .map(Optional::get).toList().build();

        publisher.subscribe(subscriber);

        Iterable<T> iterable = () -> {
            final CompletionStage<List<T>> completion = subscriber.getCompletion();
            final CompletableFuture<List<T>> future = completion.toCompletableFuture();
            try {
                return future.get().iterator();
            } catch (InterruptedException | ExecutionException e) {
                throw new ReactiveException("There is a error to load the findById", e);
            }
        };
        return Observable.of(ReactiveStreams.fromIterable(iterable).buildRs());
    }

    @Override
    public Observable<Boolean> existsById(K id) {
        requireNonNull(id, "is is required");
        final Observable<T> publisher = findById(id);
        final CompletionStage<Optional<T>> completion = publisher.getFirst();
        final CompletionStage<Boolean> exist = completion.thenApply(Optional::isPresent);
        return Observable.of(ReactiveStreams.fromCompletionStage(exist).buildRs());
    }

    @Override
    public Observable<Long> count() {
        return getTemplate().count(getEntityClass());
    }

    private FieldMapping getIdField() {
        return getClassMapping().getId().orElseThrow(KEY_NOT_FOUND_EXCEPTION_SUPPLIER);
    }

    private Class<T> getEntityClass() {
        return (Class<T>) getClassMapping().getClassInstance();
    }

    private <S extends T> CompletionStage<S> loadPublisher(Observable<S> publisher) {
        final CompletionStage<Optional<S>> completion = publisher.getFirst();
        return completion.thenApply(o -> o.orElseThrow(() -> new MappingException("An Error to load the Reactive Save")));
    }
}
