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
package org.eclipse.jnosql.artemis.keyvalue.reactive;

import jakarta.nosql.mapping.keyvalue.KeyValueTemplate;
import org.eclipse.jnosql.artemis.reactive.Observable;
import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams;

import java.time.Duration;
import java.util.Collections;
import java.util.Optional;

import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;

/**
 * The template method of {@link ReactiveKeyValueTemplate}
 */
public abstract class AbstractReactiveKeyValueTemplate implements ReactiveKeyValueTemplate {

    protected abstract KeyValueTemplate getTemplate();
    
    @Override
    public <T> Observable<T> put(T entity) {
        Iterable<T> iterable = () -> singleton(getTemplate().put(entity)).iterator();
        return Observable.of(ReactiveStreams.fromIterable(iterable).buildRs());
    }

    @Override
    public <T> Observable<T> put(T entity, Duration ttl) {
        Iterable<T> iterable = () -> singleton(getTemplate().put(entity, ttl)).iterator();
        return Observable.of(ReactiveStreams.fromIterable(iterable).buildRs());
    }

    @Override
    public <T> Observable<T> put(Iterable<T> entities) {
        final Iterable<T> iterable = () -> getTemplate().put(entities).iterator();
        return Observable.of(ReactiveStreams.fromIterable(iterable).buildRs());
    }

    @Override
    public <T> Observable<T> put(Iterable<T> entities, Duration ttl) {
        final Iterable<T> iterable = () -> getTemplate().put(entities, ttl).iterator();
        return Observable.of(ReactiveStreams.fromIterable(iterable).buildRs());
    }

    @Override
    public <K, T> Observable<T> get(K key, Class<T> entityClass) {
        final Iterable<T> iterable = () -> {
            final Optional<T> optional = getTemplate().get(key, entityClass);
            return optional.map(Collections::singleton).orElse(emptySet()).iterator();
        };
        return Observable.of(ReactiveStreams.fromIterable(iterable).buildRs());
    }

    @Override
    public <K, T> Observable<T> get(Iterable<K> keys, Class<T> entityClass) {
        final Iterable<T> iterable = () -> getTemplate().get(keys, entityClass).iterator();
        return Observable.of(ReactiveStreams.fromIterable(iterable).buildRs());
    }

    @Override
    public <T> Observable<T> query(String query, Class<T> entityClass) {
        final Iterable<T> iterable = () ->getTemplate().query(query, entityClass).iterator();
        return Observable.of(ReactiveStreams.fromIterable(iterable).buildRs());
    }

    @Override
    public <T> Observable<T> getSingleResult(String query, Class<T> entityClass) {
        final Iterable<T> iterable = () -> {
            final Optional<T> optional = getTemplate().getSingleResult(query, entityClass);
            return optional.map(Collections::singleton).orElse(emptySet()).iterator();
        };
        return Observable.of(ReactiveStreams.fromIterable(iterable).buildRs());
    }

    @Override
    public Observable<Void> query(String query) {
        final Iterable<Void> iterable = () -> {
            getTemplate().query(query);
            return Collections.<Void>emptyList().iterator();
        };
        return Observable.of(ReactiveStreams.fromIterable(iterable).buildRs());
    }


    @Override
    public <K> Observable<Void> delete(K key) {
        final Iterable<Void> iterable = () -> {
            getTemplate().delete(key);
            return Collections.<Void>emptyList().iterator();
        };
        return Observable.of(ReactiveStreams.fromIterable(iterable).buildRs());
    }

    @Override
    public <K> Observable<Void> delete(Iterable<K> keys) {
        final Iterable<Void> iterable = () -> {
            getTemplate().delete(keys);
            return Collections.<Void>emptyList().iterator();
        };

        return Observable.of(ReactiveStreams.fromIterable(iterable).buildRs());
    }
    
}
