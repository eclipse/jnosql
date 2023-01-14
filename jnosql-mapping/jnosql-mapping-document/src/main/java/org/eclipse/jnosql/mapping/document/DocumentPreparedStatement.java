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
package org.eclipse.jnosql.mapping.document;

import jakarta.nosql.PreparedStatement;
import org.eclipse.jnosql.communication.document.DocumentEntity;

import java.util.Optional;
import java.util.stream.Stream;

final class DocumentPreparedStatement implements PreparedStatement {

    private final org.eclipse.jnosql.communication.document.DocumentPreparedStatement preparedStatement;


    private final DocumentEntityConverter converter;

    DocumentPreparedStatement(org.eclipse.jnosql.communication.document.DocumentPreparedStatement preparedStatement,
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
    public <T> Stream<T> result() {
        return preparedStatement.result().map(c -> (T) converter.toEntity(c));
    }

    @Override
    public <T> Optional<T> singleResult() {
        Optional<DocumentEntity> singleResult = preparedStatement.singleResult();
        return singleResult.map(converter::toEntity);
    }
}