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
package org.jnosql.artemis.document;

import jakarta.nosql.mapping.PreparedStatement;
import jakarta.nosql.document.DocumentEntity;
import jakarta.nosql.mapping.document.DocumentEntityConverter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

final class DocumentPreparedStatement implements PreparedStatement {

    private final jakarta.nosql.document.DocumentPreparedStatement preparedStatement;


    private final DocumentEntityConverter converter;

    DocumentPreparedStatement(jakarta.nosql.document.DocumentPreparedStatement preparedStatement,
                              DocumentEntityConverter converter) {
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
        Optional<DocumentEntity> singleResult = preparedStatement.getSingleResult();
        return singleResult.map(converter::toEntity);
    }
}