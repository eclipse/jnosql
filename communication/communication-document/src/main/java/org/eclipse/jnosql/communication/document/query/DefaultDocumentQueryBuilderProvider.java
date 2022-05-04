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

import jakarta.nosql.document.DocumentQuery.DocumentQueryBuilder;
import jakarta.nosql.document.DocumentQuery.DocumentQueryBuilderProvider;

import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * The default implementation of {@link DocumentQueryBuilderProvider}
 */
public final class DefaultDocumentQueryBuilderProvider implements DocumentQueryBuilderProvider {

    @Override
    public DocumentQueryBuilder apply(String[] documents) {
        Stream.of(documents).forEach(d -> requireNonNull(d, "there is null document in the query"));
        DefaultDeleteQueryBuilder builder = new DefaultDeleteQueryBuilder();
        Stream.of(documents).forEach(builder::select);
        return builder;
    }

    @Override
    public DocumentQueryBuilder get() {
        return new DefaultDeleteQueryBuilder();
    }
}
