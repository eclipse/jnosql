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
package org.eclipse.jnosql.diana.document.query;


import jakarta.nosql.document.DocumentCondition;
import jakarta.nosql.document.DocumentDeleteQuery;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Collections.unmodifiableList;
import static java.util.Optional.ofNullable;

/**
 * The default implementation of {@link DocumentDeleteQuery}
 */
class DefaultDocumentDeleteQuery implements DocumentDeleteQuery {

    private final String documentCollection;

    private final DocumentCondition condition;

    private final List<String> documents;

    DefaultDocumentDeleteQuery(String documentCollection, DocumentCondition condition, List<String> documents) {
        this.documentCollection = documentCollection;
        this.condition = ofNullable(condition).map(ReadOnlyDocumentCondition::new).orElse(null);
        this.documents = documents;
    }

    @Override
    public String getDocumentCollection() {
        return documentCollection;
    }

    @Override
    public Optional<DocumentCondition> getCondition() {
        return ofNullable(condition);
    }

    @Override
    public List<String> getDocuments() {
        return unmodifiableList(documents);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentDeleteQuery)) {
            return false;
        }
        DocumentDeleteQuery that = (DocumentDeleteQuery) o;
        return Objects.equals(documentCollection, that.getDocumentCollection()) &&
                Objects.equals(condition, that.getCondition().orElse(null)) &&
                Objects.equals(documents, that.getDocuments());
    }

    @Override
    public int hashCode() {
        return Objects.hash(documentCollection, condition, documents);
    }

    @Override
    public String toString() {
        return  "DefaultDocumentDeleteQuery{" + "documentCollection='" + documentCollection + '\'' +
                ", condition=" + condition +
                ", documents=" + documents +
                '}';
    }
}
