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
import org.eclipse.jnosql.artemis.reactive.Observable;
import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams;
import org.reactivestreams.Publisher;

import java.time.Duration;
import java.util.Collections;
import java.util.Optional;

public abstract class AbstractReactiveColumnTemplate implements ReactiveColumnTemplate {

    protected abstract ColumnTemplate getTemplate();

    @Override
    public <T> Observable<T> insert(T entity) {
        Iterable<T> iterable = () -> Collections.singleton(getTemplate().insert(entity)).iterator();
        return Observable.of(ReactiveStreams.fromIterable(iterable).buildRs());
    }

    @Override
    public <T> Observable<T> insert(T entity, Duration ttl) {
        Iterable<T> iterable = () -> Collections.singleton(getTemplate().insert(entity, ttl)).iterator();
        return Observable.of(ReactiveStreams.fromIterable(iterable).buildRs());
    }

    @Override
    public <T> Observable<T> insert(Iterable<T> entities) {
        Iterable<T> iterable = () -> getTemplate().insert(entities).iterator();
        return Observable.of(ReactiveStreams.fromIterable(iterable).buildRs());
    }

    @Override
    public <T> Observable<T> insert(Iterable<T> entities, Duration ttl) {
        Iterable<T> iterable = () -> getTemplate().insert(entities, ttl).iterator();
        return Observable.of(ReactiveStreams.fromIterable(iterable).buildRs());
    }

    @Override
    public <T> Observable<T> update(T entity) {
        Iterable<T> iterable = () -> Collections.singleton(getTemplate().update(entity)).iterator();
        return Observable.of(ReactiveStreams.fromIterable(iterable).buildRs());
    }

    @Override
    public <T> Observable<T> update(Iterable<T> entities) {
        Iterable<T> iterable = () -> getTemplate().update(entities).iterator();
        return Observable.of(ReactiveStreams.fromIterable(iterable).buildRs());
    }

    @Override
    public Observable<Void> delete(ColumnDeleteQuery query) {
        Iterable<Void> iterable = () -> {
            getTemplate().delete(query);
            return Collections.<Void>emptyList().iterator();
        };
        return Observable.of(ReactiveStreams.fromIterable(iterable).buildRs());
    }

    @Override
    public <T> Observable<T> select(ColumnQuery query) {
        Iterable<T> iterable = () -> getTemplate().<T>select(query).iterator();
        return Observable.of(ReactiveStreams.fromIterable(iterable).buildRs());
    }

    @Override
    public <T> Observable<T> query(String query) {
        Iterable<T> iterable = () -> getTemplate().<T>query(query).iterator();
        return Observable.of(ReactiveStreams.fromIterable(iterable).buildRs());
    }

    @Override
    public <T> Observable<T> singleResult(String query) {
        Iterable<T> iterable = () -> {
            final Optional<T> result = getTemplate().<T>singleResult(query);
            return result.map(Collections::singleton).orElse(Collections.emptySet()).iterator();
        };
        return Observable.of(ReactiveStreams.fromIterable(iterable).buildRs());
    }

    @Override
    public <T, K> Observable<T> find(Class<T> entityClass, K id) {

        Iterable<T> iterable = () -> {
            final Optional<T> result = getTemplate().find(entityClass, id);
            return result.map(Collections::singleton).orElse(Collections.emptySet()).iterator();
        };
        return Observable.of(ReactiveStreams.fromIterable(iterable).buildRs());
    }

    @Override
    public <T, K> Observable<Void> delete(Class<T> entityClass, K id) {
        Iterable<Void> iterable = () -> {
            getTemplate().delete(entityClass, id);
            return Collections.<Void>emptyList().iterator();
        };
        return Observable.of(ReactiveStreams.fromIterable(iterable).buildRs());
    }

    @Override
    public Observable<Long> count(String columnFamily) {
        Iterable<Long> iterable = () -> {
            final long count = getTemplate().count(columnFamily);
            return Collections.singleton(count).iterator();
        };
        return Observable.of(ReactiveStreams.fromIterable(iterable).buildRs());
    }

    @Override
    public <T> Observable<Long> count(Class<T> entityType) {
        Iterable<Long> iterable = () -> {
            final long count = getTemplate().count(entityType);
            return Collections.singleton(count).iterator();
        };
        return Observable.of(ReactiveStreams.fromIterable(iterable).buildRs());
    }

    @Override
    public <T> Observable<T> singleResult(ColumnQuery query) {
        Iterable<T> iterable = () -> {
            final Optional<T> result = getTemplate().singleResult(query);
            return result.map(Collections::singleton).orElse(Collections.emptySet()).iterator();
        };
        return Observable.of(ReactiveStreams.fromIterable(iterable).buildRs());
    }
}
