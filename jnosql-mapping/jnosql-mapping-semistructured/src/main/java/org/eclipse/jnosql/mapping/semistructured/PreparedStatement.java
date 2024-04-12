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
import org.eclipse.jnosql.communication.semistructured.SelectQuery;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Represents a prepared statement specifically for JNoSQL operations, facilitating
 * the binding and execution of queries with parameters. This class wraps a
 * {@link org.eclipse.jnosql.communication.semistructured.CommunicationPreparedStatement} to integrate easily
 * with different JNoSQL components and provides a high-level abstraction to interact
 * with various databases in a semi-structured format.
 *
 * @see org.eclipse.jnosql.mapping.PreparedStatement
 */
public final class PreparedStatement implements org.eclipse.jnosql.mapping.PreparedStatement {

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

    /**
     * Optionally returns the underlying {@link SelectQuery} associated with this PreparedStatement,
     * if applicable.
     *
     * @return an optional {@link SelectQuery} representing the query details bound to this PreparedStatement
     */
    public Optional<SelectQuery> selectQuery(){
        return preparedStatement.select();
    }
}
