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
import org.eclipse.jnosql.artemis.reactive.Observable;
import org.eclipse.jnosql.artemis.reactive.ReactiveRepository;
import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams;

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
    public <S extends T> Observable<S> save(S entity) {
        return getTemplate().put(entity);
    }

    @Override
    public <S extends T> Observable<S> save(Iterable<S> entities) {
        return getTemplate().put(entities);
    }

    @Override
    public Observable<Void> deleteById(K id) {
        return getTemplate().delete(id);
    }

    @Override
    public Observable<Void> deleteById(Iterable<K> ids) {
        return getTemplate().delete(ids);
    }

    @Override
    public Observable<T> findById(K id) {
        return getTemplate().get(id, typeClass);
    }

    @Override
    public Observable<T> findById(Iterable<K> ids) {
        return getTemplate().get(ids, typeClass);
    }

    @Override
    public Observable<Boolean> existsById(K id) {
        final Observable<T> observable = getTemplate().get(id, typeClass);
        final CompletionStage<Optional<T>> completion = observable.getFirst();
        Iterable<Boolean> iterable = () -> {
            AtomicBoolean atomicBoolean = new AtomicBoolean();
            completion.thenAccept(o -> atomicBoolean.set(o.isPresent()));
            return Collections.singleton(atomicBoolean.get()).iterator();
        };
        return Observable.of(ReactiveStreams.fromIterable(iterable).buildRs());
    }

    @Override
    public Observable<Long> count() {
        throw new UnsupportedOperationException("The key-value type does not support count method");
    }
}

