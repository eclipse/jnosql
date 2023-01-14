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


import org.eclipse.jnosql.communication.document.DocumentQuery;

import java.util.Objects;
import java.util.function.Supplier;


/**
 * When a document query is executed this event if fired
 */
public final class DocumentQueryExecute implements Supplier<DocumentQuery> {

    private final DocumentQuery query;

    DocumentQueryExecute(DocumentQuery query) {
        this.query = Objects.requireNonNull(query, "query is required");
    }

    @Override
    public DocumentQuery get() {
        return query;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentQueryExecute)) {
            return false;
        }
        DocumentQueryExecute that = (DocumentQueryExecute) o;
        return Objects.equals(query, that.query);
    }

    @Override
    public int hashCode() {
        return Objects.hash(query);
    }

    @Override
    public String toString() {
        return  "DocumentQueryExecute{" + "query=" + query +
                '}';
    }
}
