/*
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
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

import jakarta.nosql.PreparedStatement;
import org.eclipse.jnosql.communication.Value;

import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

final class KeyValuePreparedStatement implements PreparedStatement {

    private final org.eclipse.jnosql.communication.keyvalue.KeyValuePreparedStatement preparedStatement;

    private final Class<?> type;

    KeyValuePreparedStatement(org.eclipse.jnosql.communication.keyvalue.KeyValuePreparedStatement preparedStatement, Class<?> type) {
        this.preparedStatement = preparedStatement;
        this.type = type;
    }

    @Override
    public PreparedStatement bind(String name, Object value) {
        preparedStatement.bind(name, value);
        return this;
    }

    @Override
    public <T> Stream<T> result() {
        Stream<Value> values = preparedStatement.result();
        requireNonNull(type, "type is required when the command returns value");
        return values.map(v -> v.get((Class<T>) type));
    }

    @Override
    public <T> Optional<T> singleResult() {
        Optional<Value> singleResult = preparedStatement.singleResult();
        if (singleResult.isPresent()) {
            requireNonNull(type, "type is required when the command returns value");
            return singleResult.map(v -> v.get((Class<T>) type));
        }
        return Optional.empty();
    }
}
