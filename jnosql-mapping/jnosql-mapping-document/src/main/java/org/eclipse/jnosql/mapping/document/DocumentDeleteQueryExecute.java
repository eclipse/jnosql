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



import org.eclipse.jnosql.communication.document.DocumentDeleteQuery;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * When a document delete query is executed this event if fired
 */
public final class DocumentDeleteQueryExecute implements Supplier<DocumentDeleteQuery> {

    private final DocumentDeleteQuery query;

    DocumentDeleteQueryExecute(DocumentDeleteQuery query) {
        this.query = Objects.requireNonNull(query, "query is required");
    }

    @Override
    public DocumentDeleteQuery get() {
        return query;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentDeleteQueryExecute)) {
            return false;
        }
        DocumentDeleteQueryExecute that = (DocumentDeleteQueryExecute) o;
        return Objects.equals(query, that.query);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(query);
    }

    @Override
    public String toString() {
        return  "DocumentDeleteQueryExecute{" + "query=" + query +
                '}';
    }
}
