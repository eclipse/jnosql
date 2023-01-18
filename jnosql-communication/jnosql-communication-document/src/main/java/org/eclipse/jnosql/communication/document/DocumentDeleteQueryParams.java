/*
 *
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
 *
 */
package org.eclipse.jnosql.communication.document;

import org.eclipse.jnosql.communication.Params;

import java.util.Objects;

/**
 * The result of {@link DocumentDeleteQueryParams} that has {@link DocumentDeleteQuery} and {@link Params}.
 */
public final class DocumentDeleteQueryParams  {

    private final DocumentDeleteQuery query;

    private final Params params;

    DocumentDeleteQueryParams(DocumentDeleteQuery query, Params params) {
        this.query = query;
        this.params = params;
    }

    /**
     * The {@link DocumentDeleteQuery}
     *
     * @return a {@link DocumentDeleteQuery} instance
     */
    public DocumentDeleteQuery query() {
        return query;
    }

    /**
     * The {@link Params}
     *
     * @return a {@link Params} instance
     */
    public Params params() {
        return params;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DocumentDeleteQueryParams that = (DocumentDeleteQueryParams) o;
        return Objects.equals(query, that.query) &&
                Objects.equals(params, that.params);
    }

    @Override
    public int hashCode() {
        return Objects.hash(query, params);
    }
}
