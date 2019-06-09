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
package org.jnosql.diana.document.query;

import org.jnosql.diana.Params;
import org.jnosql.diana.document.DocumentDeleteQuery;

import java.util.Objects;

final class DefaultDocumentDeleteQueryParams implements DocumentDeleteQueryParams {

    private final DocumentDeleteQuery query;

    private final Params params;

    DefaultDocumentDeleteQueryParams(DocumentDeleteQuery query, Params params) {
        this.query = query;
        this.params = params;
    }

    @Override
    public DocumentDeleteQuery getQuery() {
        return query;
    }

    @Override
    public Params getParams() {
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
        DefaultDocumentDeleteQueryParams that = (DefaultDocumentDeleteQueryParams) o;
        return Objects.equals(query, that.query) &&
                Objects.equals(params, that.params);
    }

    @Override
    public int hashCode() {
        return Objects.hash(query, params);
    }
}
