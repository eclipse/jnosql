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
package org.jnosql.artemis.column;

import org.jnosql.artemis.PreparedStatement;
import org.jnosql.diana.column.ColumnEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

final class ColumnPreparedStatement implements PreparedStatement {

    private final org.jnosql.diana.column.ColumnPreparedStatement preparedStatement;


    private final ColumnEntityConverter converter;

    ColumnPreparedStatement(org.jnosql.diana.column.ColumnPreparedStatement preparedStatement,
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
    public <T> List<T> getResultList() {
        return preparedStatement.getResultList().stream().map(c -> (T) converter.toEntity(c))
                .collect(Collectors.toList());
    }

    @Override
    public <T> Optional<T> getSingleResult() {
        Optional<ColumnEntity> singleResult = preparedStatement.getSingleResult();
        return singleResult.map(converter::toEntity);
    }
}
