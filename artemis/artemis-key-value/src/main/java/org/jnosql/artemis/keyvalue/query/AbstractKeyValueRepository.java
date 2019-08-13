/*
 *  Copyright (c) 2017 Otávio Santana and others
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
package org.jnosql.artemis.keyvalue.query;

import jakarta.nosql.mapping.Repository;
import jakarta.nosql.mapping.keyvalue.KeyValueTemplate;

import java.util.Optional;

/**
 * The template method to key-value repository
 */
public abstract class AbstractKeyValueRepository<T> implements Repository {


    private final Class<T> typeClass;


    protected abstract KeyValueTemplate getTemplate();

    public AbstractKeyValueRepository(Class<T> typeClass) {
        this.typeClass = typeClass;
    }


    @Override
    public Object save(Object entity) {
        return getTemplate().put(entity);
    }

    @Override
    public Iterable save(Iterable entities) {
        return getTemplate().put(entities);
    }

    @Override
    public void deleteById(Object key) {
        getTemplate().remove(key);
    }

    @Override
    public void deleteById(Iterable ids) {
        getTemplate().remove(ids);
    }

    @Override
    public Optional findById(Object key) {
        return getTemplate().get(key, typeClass);
    }

    @Override
    public Iterable findById(Iterable keys) {
        return getTemplate().get(keys, typeClass);
    }

    @Override
    public boolean existsById(Object key) {
        return getTemplate().get(key, typeClass).isPresent();
    }

    public long count() {
        throw new UnsupportedOperationException("The key-value type does not support count method");
    }
}
