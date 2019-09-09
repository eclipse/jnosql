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
package org.eclipse.jnosql.artemis.column.query;


import jakarta.nosql.mapping.RepositoryAsync;
import jakarta.nosql.mapping.column.ColumnTemplateAsync;
import jakarta.nosql.mapping.reflection.ClassMapping;
import jakarta.nosql.mapping.reflection.FieldMapping;

import java.util.Optional;
import java.util.function.Consumer;

import static jakarta.nosql.mapping.IdNotFoundException.KEY_NOT_FOUND_EXCEPTION_SUPPLIER;
import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

/**
 * The template method to {@link RepositoryAsync}
 */
public abstract class AbstractColumnRepositoryAsync<T, K> implements RepositoryAsync<T, K> {

    protected abstract ColumnTemplateAsync getTemplate();

    protected abstract ClassMapping getClassMapping();

    @Override
    public <S extends T> void save(S entity) {
        requireNonNull(entity, "Entity is required");
        Object id = getIdField().read(entity);

        if (isNull(id)) {
            getTemplate().insert(entity);
            return;
        }

        Consumer<Boolean> callBack = exist -> {
            if (exist) {
                getTemplate().update(entity);
            } else {
                getTemplate().insert(entity);
            }
        };
        existsById((K) id, callBack);
    }


    @Override
    public <S extends T> void save(Iterable<S> entities) {
        requireNonNull(entities, "entities is required");
        entities.forEach(this::save);
    }


    @Override
    public void deleteById(K id) {
        requireNonNull(id, "is is required");
        getTemplate().delete(getEntityClass(), id);
    }


    @Override
    public void existsById(K id, Consumer<Boolean> callBack) {
        Consumer<Optional<T>> as = o -> callBack.accept(o.isPresent());
        findById(id, as);
    }


    @Override
    public void findById(K id, Consumer<Optional<T>> callBack) {
        requireNonNull(id, "id is required");
        requireNonNull(callBack, "callBack is required");

        getTemplate().find(getEntityClass(), id, callBack);
    }

    @Override
    public void count(Consumer<Long> callback) {
        getTemplate().count(getEntityClass(), callback);
    }

    private Class<T> getEntityClass() {
        return (Class<T>) getClassMapping().getClassInstance();
    }

    private FieldMapping getIdField() {
        return getClassMapping().getId().orElseThrow(KEY_NOT_FOUND_EXCEPTION_SUPPLIER);
    }
}
