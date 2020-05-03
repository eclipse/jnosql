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
package org.eclipse.jnosql.artemis.keyvalue.reactive.query;

import org.eclipse.jnosql.artemis.keyvalue.reactive.ReactiveKeyValueTemplate;
import org.eclipse.jnosql.artemis.reactive.ReactiveRepository;
import org.eclipse.microprofile.reactive.streams.operators.CompletionSubscriber;
import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams;
import org.eclipse.microprofile.reactive.streams.operators.SubscriberBuilder;
import org.reactivestreams.Publisher;

import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractReactiveKeyValueRepository<T, K> implements ReactiveRepository<T, K> {

    private final Class<T> typeClass;

    public AbstractReactiveKeyValueRepository(Class<T> typeClass) {
        this.typeClass = typeClass;
    }

    protected abstract ReactiveKeyValueTemplate getTemplate();

    @Override
    public <S extends T> Publisher<S> save(S entity) {
        return getTemplate().put(entity);
    }

    @Override
    public <S extends T> Publisher<S> save(Iterable<S> entities) {
        return getTemplate().put(entities);
    }

    @Override
    public Publisher<Void> deleteById(K id) {
        return getTemplate().delete(id);
    }

    @Override
    public Publisher<Void> deleteById(Iterable<K> ids) {
        return getTemplate().delete(ids);
    }

    @Override
    public Publisher<T> findById(K id) {
        return getTemplate().get(id, typeClass);
    }

    @Override
    public Publisher<T> findById(Iterable<K> ids) {
        return getTemplate().get(ids, typeClass);
    }

    @Override
    public Publisher<Boolean> existsById(K id) {
        final Publisher<T> publisher = getTemplate().get(id, typeClass);
        final CompletionSubscriber<Object, Optional<Object>> first = ReactiveStreams.builder().<T>findFirst().build();
        publisher.subscribe(first);
        final CompletionStage<Optional<Object>> completion = first.getCompletion();
        Iterable<Boolean> iterable = () -> {
            AtomicBoolean atomicBoolean = new AtomicBoolean();
            completion.thenAccept(o -> atomicBoolean.set(o.isPresent()));
            return Collections.singleton(atomicBoolean.get()).iterator();
        };
        return ReactiveStreams.fromIterable(iterable).buildRs();
    }

    @Override
    public Publisher<Long> count() {
        throw new UnsupportedOperationException("The key-value type does not support count method");
    }
}

