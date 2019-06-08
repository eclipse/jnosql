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


import org.jnosql.diana.api.document.DocumentQuery;

import java.util.Objects;

class DefaultDocumentQueryExecute implements DocumentQueryExecute {

    private final DocumentQuery query;

    DefaultDocumentQueryExecute(DocumentQuery query) {
        this.query = Objects.requireNonNull(query, "query is required");
    }

    @Override
    public DocumentQuery getQuery() {
        return query;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DefaultDocumentQueryExecute)) {
            return false;
        }
        DefaultDocumentQueryExecute that = (DefaultDocumentQueryExecute) o;
        return Objects.equals(query, that.query);
    }

    @Override
    public int hashCode() {
        return Objects.hash(query);
    }

    @Override
    public String toString() {
        return  "DefaultDocumentQueryExecute{" + "query=" + query +
                '}';
    }
}
