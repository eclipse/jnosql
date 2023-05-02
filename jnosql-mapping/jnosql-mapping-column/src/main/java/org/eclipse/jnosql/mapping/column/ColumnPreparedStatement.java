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
package org.eclipse.jnosql.mapping.column;

import jakarta.nosql.PreparedStatement;
import org.eclipse.jnosql.communication.column.ColumnEntity;

import java.util.Optional;
import java.util.stream.Stream;

final class ColumnPreparedStatement implements PreparedStatement {

    private final org.eclipse.jnosql.communication.column.ColumnPreparedStatement preparedStatement;

    private final ColumnEntityConverter converter;

    ColumnPreparedStatement(org.eclipse.jnosql.communication.column.ColumnPreparedStatement preparedStatement,
                            ColumnEntityConverter converter) {
        this.preparedStatement = preparedStatement;
        this.converter = converter;
    }

    @Override
    public PreparedStatement bind(String name, Object value) {
        preparedStatement.bind(name, value);
        return this;
    }

    @Override
    public <T> Stream<T> result() {
        return preparedStatement.result().map(c -> converter.toEntity(c));
    }

    @Override
    public <T> Optional<T> singleResult() {
        Optional<ColumnEntity> singleResult = preparedStatement.singleResult();
        return singleResult.map(converter::toEntity);
    }
}
