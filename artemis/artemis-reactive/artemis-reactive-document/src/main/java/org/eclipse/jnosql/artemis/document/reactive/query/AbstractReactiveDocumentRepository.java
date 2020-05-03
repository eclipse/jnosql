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
package org.eclipse.jnosql.artemis.document.reactive.query;

import jakarta.nosql.mapping.MappingException;
import jakarta.nosql.mapping.reflection.ClassMapping;
import jakarta.nosql.mapping.reflection.FieldMapping;
import org.eclipse.jnosql.artemis.document.reactive.ReactiveDocumentTemplate;
import org.eclipse.jnosql.artemis.reactive.ReactiveRepository;
import org.eclipse.microprofile.reactive.streams.operators.CompletionSubscriber;
import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams;
import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static jakarta.nosql.mapping.IdNotFoundException.KEY_NOT_FOUND_EXCEPTION_SUPPLIER;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;


public abstract class AbstractReactiveDocumentRepository<T, K> implements ReactiveRepository<T, K> {

    protected abstract ReactiveDocumentTemplate getTemplate();

    protected abstract ClassMapping getClassMapping();

    @Override
    public <S extends T> Publisher<S> save(S entity) {
        Objects.requireNonNull(entity, "entity is required");
        Object id = getIdField().read(entity);

        if (nonNull(id)) {
            final Publisher<Boolean> publisher = existsById((K) id);
            final CompletionSubscriber<Boolean, Optional<Boolean>> subscriber = ReactiveStreams.<Boolean>builder().findFirst().build();
            publisher.subscribe(subscriber);
            final CompletionStage<Boolean> stage = subscriber.getCompletion().thenApply(o -> o.orElse(false));

            final CompletionStage<S> operation = stage.thenApplyAsync(exist -> {
                if (exist) {
                    return getTemplate().update(entity);
                } else {
                    return getTemplate().insert(entity);
                }
            }).thenCompose(this::loadPublisher);
            return ReactiveStreams.fromCompletionStage(operation).buildRs();
        } else {
            return getTemplate().insert(entity);
        }
    }

    @Override
    public <S extends T> Publisher<S> save(Iterable<S> entities) {
        Objects.requireNonNull(entities, "entities is required");

        List<CompletionStage<S>> stages = new ArrayList<>();
        for (S entity : entities) {
            final Publisher<S> publisher = save(entity);
            final CompletionSubscriber<S, Optional<S>> subscriber = ReactiveStreams.<S>builder()
                    .findFirst().build();
            publisher.subscribe(subscriber);
            final CompletionStage<S> stage = subscriber.getCompletion().thenApply(v -> v.orElse(null));
            stages.add(stage);
        }

        final Publisher<S> publisher = stages.stream()
                .map(ReactiveStreams::fromCompletionStage)
                .reduce(ReactiveStreams::concat)
                .orElse(ReactiveStreams.empty()).buildRs();
        return publisher;
    }

    @Override
    public Publisher<Void> deleteById(K id) {
        return getTemplate().delete(getEntityClass(), id);
    }

    @Override
    public Publisher<Void> deleteById(Iterable<K> ids) {
        requireNonNull(ids, "ids is required");
        List<CompletionStage<Void>> stages = new ArrayList<>();
        for (K id : ids) {
            final Publisher<Void> publisher = deleteById(id);
            final CompletionSubscriber<Void, Optional<Void>> subscriber = ReactiveStreams.<Void>builder()
                    .findFirst().build();
            publisher.subscribe(subscriber);
            final CompletionStage<Void> stage = subscriber.getCompletion().thenApply(v -> v.orElse(null));
            stages.add(stage);
        }

        final Publisher<Void> publisher = stages.stream()
                .map(ReactiveStreams::fromCompletionStage)
                .reduce(ReactiveStreams::concat)
                .orElse(ReactiveStreams.empty()).buildRs();
        return publisher;
    }

    @Override
    public Publisher<T> findById(K id) {
        requireNonNull(id, "id is required");
        return getTemplate().find(getEntityClass(), id);
    }

    @Override
    public Publisher<T> findById(Iterable<K> ids) {
        requireNonNull(ids, "id is required");

        List<CompletionStage<T>> stages = new ArrayList<>();
        for (K id : ids) {
            final Publisher<T> publisher = findById(id);
            final CompletionSubscriber<T, Optional<T>> subscriber = ReactiveStreams.<T>builder().findFirst().build();
            publisher.subscribe(subscriber);
            final CompletionStage<T> completion = subscriber.getCompletion().thenApply(Optional::get);
            stages.add(completion);
        }

        final Publisher<T> publisher = stages.stream().map(ReactiveStreams::fromCompletionStage)
                .reduce(ReactiveStreams::concat)
                .orElse(ReactiveStreams.empty()).buildRs();
        return publisher;
    }

    @Override
    public Publisher<Boolean> existsById(K id) {
        requireNonNull(id, "is is required");
        final Publisher<T> publisher = findById(id);
        final CompletionSubscriber<T, Optional<T>> subscriber = ReactiveStreams.<T>builder().findFirst().build();
        publisher.subscribe(subscriber);
        final CompletionStage<Optional<T>> completion = subscriber.getCompletion();
        final CompletionStage<Boolean> exist = completion.thenApply(o -> o.isPresent());
        return ReactiveStreams.fromCompletionStage(exist).buildRs();
    }

    @Override
    public Publisher<Long> count() {
        return getTemplate().count(getEntityClass());
    }

    private FieldMapping getIdField() {
        return getClassMapping().getId().orElseThrow(KEY_NOT_FOUND_EXCEPTION_SUPPLIER);
    }

    private Class<T> getEntityClass() {
        return (Class<T>) getClassMapping().getClassInstance();
    }

    private <S extends T> CompletionStage<S> loadPublisher(Publisher<S> publisher) {
        final CompletionSubscriber<S, Optional<S>> first = ReactiveStreams.<S>builder().findFirst().build();
        publisher.subscribe(first);
        final CompletionStage<Optional<S>> completion = first.getCompletion();
        return completion.thenApply(o -> o.orElseThrow(()-> new MappingException("An Error to load the Reactive Save")));
    }

}
