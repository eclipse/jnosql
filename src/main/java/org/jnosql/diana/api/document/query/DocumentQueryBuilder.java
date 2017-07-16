/*
 *
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
 *
 */
package org.jnosql.diana.api.document.query;


import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

/**
 * A utilitarian class to query.
 */
public final class DocumentQueryBuilder {


    private DocumentQueryBuilder() {
    }


    /**
     * Creates a query to Document
     *
     * @param documents - The document fields to query, optional.
     * @return a new {@link DocumentSelect} instance
     * @throws NullPointerException when there is a null element
     */
    public static DocumentSelect select(String... documents) throws NullPointerException {
        return new DefaultSelectQueryBuilder(Stream.of(documents)
                .peek(c -> requireNonNull(c, "element is cannot be null"))
                .collect(toList()));
    }

    /**
     * Creates a delete query to Document
     *
     * @param documents - The document fields to query, optional.
     * @return a new {@link DocumentDelete} instance
     * @throws NullPointerException when there is a null element
     */
    public static DocumentDelete delete(String... documents) throws NullPointerException {
        return new DefaultDeleteQueryBuilder(Stream.of(documents)
                .peek(c -> requireNonNull(c, "element is cannot be null"))
                .collect(toList()));
    }
}
