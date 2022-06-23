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
package org.eclipse.jnosql.mapping.keyvalue;

import jakarta.nosql.Value;
import jakarta.nosql.mapping.PreparedStatement;

import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

final class KeyValuePreparedStatement implements PreparedStatement {

    private final jakarta.nosql.keyvalue.KeyValuePreparedStatement preparedStatement;

    private final Class<?> entityClass;

    KeyValuePreparedStatement(jakarta.nosql.keyvalue.KeyValuePreparedStatement preparedStatement, Class<?> entityClass) {
        this.preparedStatement = preparedStatement;
        this.entityClass = entityClass;
    }

    @Override
    public PreparedStatement bind(String name, Object value) {
        preparedStatement.bind(name, value);
        return this;
    }

    @Override
    public <T> Stream<T> getResult() {
        Stream<Value> values = preparedStatement.getResult();
        requireNonNull(entityClass, "entityClass is required when the command returns value");
        return values.map(v -> v.get((Class<T>) entityClass));
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
