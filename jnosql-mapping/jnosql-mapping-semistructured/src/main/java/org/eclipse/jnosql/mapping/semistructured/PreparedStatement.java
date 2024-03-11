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
package org.eclipse.jnosql.mapping.semistructured;

import org.eclipse.jnosql.communication.semistructured.CommunicationEntity;

import java.util.Optional;
import java.util.stream.Stream;

final class PreparedStatement implements org.eclipse.jnosql.mapping.PreparedStatement {

    private final org.eclipse.jnosql.communication.semistructured.CommunicationPreparedStatement preparedStatement;

    private final EntityConverter converter;

    PreparedStatement(org.eclipse.jnosql.communication.semistructured.CommunicationPreparedStatement preparedStatement,
                      EntityConverter converter) {
        this.preparedStatement = preparedStatement;
        this.converter = converter;
    }

    @Override
    public org.eclipse.jnosql.mapping.PreparedStatement bind(String name, Object value) {
        preparedStatement.bind(name, value);
        return this;
    }

    @Override
    public <T> Stream<T> result() {
        return preparedStatement.result().map(converter::toEntity);
    }

    @Override
    public <T> Optional<T> singleResult() {
        Optional<CommunicationEntity> singleResult = preparedStatement.singleResult();
        return singleResult.map(converter::toEntity);
    }
}
