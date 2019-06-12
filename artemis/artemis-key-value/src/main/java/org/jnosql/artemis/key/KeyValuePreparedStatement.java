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
package org.jnosql.artemis.key;

import jakarta.nosql.mapping.PreparedStatement;
import jakarta.nosql.Value;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

final class KeyValuePreparedStatement implements PreparedStatement {

    private final org.jnosql.diana.key.KeyValuePreparedStatement preparedStatement;

    private final Class<?> entityClass;

    KeyValuePreparedStatement(org.jnosql.diana.key.KeyValuePreparedStatement preparedStatement, Class<?> entityClass) {
        this.preparedStatement = preparedStatement;
        this.entityClass = entityClass;
    }

    @Override
    public PreparedStatement bind(String name, Object value) {
        preparedStatement.bind(name, value);
        return this;
    }

    @Override
    public <T> List<T> getResultList() {
        List<Value> values = preparedStatement.getResultList();
        if (!values.isEmpty()) {
            requireNonNull(entityClass, "entityClass is required when the command returns value");
            return values.stream().map(v -> v.get((Class<T>) entityClass)).collect(toList());
        }
        return Collections.emptyList();
    }

    @Override
    public <T> Optional<T> getSingleResult() {
        Optional<Value> singleResult = preparedStatement.getSingleResult();
        if (singleResult.isPresent()) {
            requireNonNull(entityClass, "entityClass is required when the command returns value");
            return singleResult.map(v -> v.get((Class<T>) entityClass));
        }
        return Optional.empty();
    }
}
