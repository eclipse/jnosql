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
package org.eclipse.jnosql.keyvalue;

import jakarta.nosql.mapping.keyvalue.KeyValueTemplate;
import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams;
import org.reactivestreams.Publisher;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.time.Duration;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Collections.singleton;

@ApplicationScoped
class DefaultReactiveKeyValueManager implements ReactiveKeyValueManager {


    private Instance<KeyValueTemplate> template;

    @Inject
    DefaultReactiveKeyValueManager(Instance<KeyValueTemplate> template) {
        this.template = template;
    }

    DefaultReactiveKeyValueManager() {
    }

    @Override
    public <T> Publisher<T> put(T entity) {
        Iterable<T> iterable = () -> singleton(template.get().put(entity)).iterator();
        return ReactiveStreams.fromIterable(iterable).buildRs();
    }

    @Override
    public <T> Publisher<T> put(T entity, Duration ttl) {
        Iterable<T> iterable = () -> singleton(template.get().put(entity, ttl)).iterator();
        return ReactiveStreams.fromIterable(iterable).buildRs();
    }

    @Override
    public <T> Publisher<T> put(Iterable<T> entities) {
        final Iterable<T> iterable = () -> template.get().put(entities).iterator();
        return ReactiveStreams.fromIterable(iterable).buildRs();
    }

    @Override
    public <T> Publisher<T> put(Iterable<T> entities, Duration ttl) {
        return ReactiveStreams.fromIterable(template.get().put(entities, ttl)).buildRs();
    }

    @Override
    public <K, T> Publisher<T> get(K key, Class<T> entityClass) {
        final Optional<T> optional = template.get().get(key, entityClass);
        final Iterable<T> iterable = optional.map(Collections::singleton).orElse(Collections.emptySet());
        return ReactiveStreams.fromIterable(iterable).buildRs();
    }

    @Override
    public <T> Publisher<T> query(String query, Class<T> entityClass) {
        final Stream<T> stream = template.get().query(query, entityClass);
        return ReactiveStreams.fromIterable(() -> stream.iterator()).buildRs();
    }

    @Override
    public <T> Publisher<T> getSingleResult(String query, Class<T> entityClass) {
        final Optional<T> optional = template.get().getSingleResult(query, entityClass);
        final Iterable<T> iterable = optional.map(Collections::singleton).orElse(Collections.emptySet());
        return ReactiveStreams.fromIterable(iterable).buildRs();
    }

    @Override
    public Publisher<Void> query(String query) {
        template.get().query(query);
        return ReactiveStreams.fromIterable(Collections.<Void>emptyList()).buildRs();
    }

    @Override
    public <K, T> Publisher<T> get(Iterable<K> keys, Class<T> entityClass) {
        final Iterable<T> iterable = template.get().get(keys, entityClass);
        return ReactiveStreams.fromIterable(iterable).buildRs();
    }

    @Override
    public <K> Publisher<Void> delete(K key) {
        template.get().delete(key);
        return ReactiveStreams.fromIterable(Collections.<Void>emptyList()).buildRs();
    }

    @Override
    public <K> Publisher<Void> delete(Iterable<K> keys) {
        template.get().delete(keys);
        return ReactiveStreams.fromIterable(Collections.<Void>emptyList()).buildRs();
    }
}
