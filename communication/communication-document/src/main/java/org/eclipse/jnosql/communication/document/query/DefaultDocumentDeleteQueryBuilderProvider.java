/*
 *
 *  Copyright (c) 2022 Otávio Santana and others
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
 *
 */
package org.eclipse.jnosql.communication.document.query;

import jakarta.nosql.document.DocumentDeleteQuery;
import jakarta.nosql.document.DocumentDeleteQuery.DocumentDeleteQueryBuilderProvider;
import jakarta.nosql.document.DocumentDeleteQuery.DocumentDeleteQueryBuilder;

import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * the default implementation of {@link DocumentDeleteQueryBuilderProvider}
 */
public final class DefaultDocumentDeleteQueryBuilderProvider implements DocumentDeleteQueryBuilderProvider {
    @Override
    public DocumentDeleteQuery.DocumentDeleteQueryBuilder apply(String[] documents) {
        Stream.of(documents).forEach(d -> requireNonNull(d, "there is null document in the query"));
        DocumentDeleteQueryBuilder builder = new DefaultDeleteQueryBuilder();
        Stream.of(documents).forEach(builder::delete);
        return builder;
    }

    @Override
    public DocumentDeleteQuery.DocumentDeleteQueryBuilder get() {
        return new DefaultDeleteQueryBuilder();
    }
}
