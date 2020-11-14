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
package org.eclipse.jnosql.communication.document.query;


import jakarta.nosql.Sort;
import jakarta.nosql.document.DocumentCondition;
import jakarta.nosql.document.DocumentQuery;
import org.eclipse.jnosql.communication.document.DefaultDocumentCondition;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Collections.unmodifiableList;
import static java.util.Optional.ofNullable;

class DefaultDocumentQuery implements DocumentQuery {

    private final long maxResult;

    private final long firstResult;

    private final String documentCollection;

    private final DocumentCondition condition;

    private final List<Sort> sorts;

    private final List<String> documents;

    DefaultDocumentQuery(long maxResult, long firstResult, String documentCollection,
                         List<String> documents, List<Sort> sorts, DocumentCondition condition) {

        this.maxResult = maxResult;
        this.firstResult = firstResult;
        this.documentCollection = documentCollection;
        this.condition = ofNullable(condition).map(DefaultDocumentCondition::readOnly).orElse(null);
        this.sorts = sorts;
        this.documents = documents;
    }

    @Override
    public long getLimit() {
        return maxResult;
    }

    @Override
    public long getSkip() {
        return firstResult;
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
    public List<Sort> getSorts() {
        return unmodifiableList(sorts);
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
        if (!(o instanceof DocumentQuery)) {
            return false;
        }
        DocumentQuery that = (DocumentQuery) o;
        return maxResult == that.getLimit() &&
                firstResult == that.getSkip() &&
                Objects.equals(documentCollection, that.getDocumentCollection()) &&
                Objects.equals(condition, that.getCondition().orElse(null)) &&
                Objects.equals(sorts, that.getSorts()) &&
                Objects.equals(documents, that.getDocuments());
    }

    @Override
    public int hashCode() {
        return Objects.hash(maxResult, firstResult, documentCollection, condition, sorts, documents);
    }

    @Override
    public String toString() {
        return  "DefaultDocumentQuery{" + "maxResult=" + maxResult +
                ", firstResult=" + firstResult +
                ", documentCollection='" + documentCollection + '\'' +
                ", condition=" + condition +
                ", sorts=" + sorts +
                ", documents=" + documents +
                '}';
    }
}
