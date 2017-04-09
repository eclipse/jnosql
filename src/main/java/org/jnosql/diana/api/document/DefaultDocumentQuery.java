/*
 * Copyright 2017 Otavio Santana and others
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 */
package org.jnosql.diana.api.document;


import org.jnosql.diana.api.Sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

class DefaultDocumentQuery implements DocumentQuery {


    private final String collection;

    private DocumentCondition condition;

    private final List<Sort> sorts = new ArrayList<>();

    private final List<String> documents = new ArrayList<>();

    private long maxResults = -1;

    private long firstResult;

    private DefaultDocumentQuery(String collection) {
        this.collection = Objects.requireNonNull(collection, "column family is required");
    }

    static DefaultDocumentQuery of(String documentCollection) throws NullPointerException {
        Objects.requireNonNull(documentCollection, "documentCollection is required");
        return new DefaultDocumentQuery(documentCollection);
    }

    @Override
    public DocumentQuery and(DocumentCondition condition) throws NullPointerException {
        Objects.requireNonNull(condition, "condition is required");
        if (Objects.isNull(this.condition)) {
            this.condition = condition;
        } else {
            this.condition = this.condition.and(condition);
        }

        return this;
    }

    @Override
    public DocumentQuery or(DocumentCondition condition) throws NullPointerException {
        if (Objects.isNull(this.condition)) {
            this.condition = condition;
        } else {
            this.condition = this.condition.or(condition);
        }
        return this;
    }

    @Override
    public DocumentQuery withFirstResult(long firstResult) {
        this.firstResult = firstResult;
        return this;
    }

    @Override
    public DocumentQuery withMaxResults(long maxResults) {
        this.maxResults = maxResults;
        return this;
    }

    @Override
    public DocumentQuery with(DocumentCondition condition) throws NullPointerException {
        this.condition = Objects.requireNonNull(condition, "condition is required");
        return this;
    }

    @Override
    public long getMaxResults() {
        return maxResults;
    }

    @Override
    public long getFirstResult() {
        return firstResult;
    }

    @Override
    public DocumentQuery addSort(Sort sort) throws NullPointerException {
        this.sorts.add(Objects.requireNonNull(sort, "Sort is required"));
        return this;
    }

    @Override
    public DocumentQuery addColumn(String document) throws NullPointerException {
        this.documents.add(Objects.requireNonNull(document, "document is required"));
        return this;
    }


    @Override
    public String getCollection() {
        return collection;
    }

    @Override
    public Optional<DocumentCondition> getCondition() {
        return Optional.ofNullable(condition);
    }

    @Override
    public List<Sort> getSorts() {
        return Collections.unmodifiableList(sorts);
    }

    @Override
    public List<String> getDocuments() {
        return Collections.unmodifiableList(documents);
    }


    @Override
    public DocumentDeleteQuery toDeleteQuery() {
        DocumentDeleteQuery documentDeleteQuery = DocumentDeleteQuery.of(collection, condition);
        if (!documents.isEmpty()) {
            documentDeleteQuery.addAll(documents);
        }
        return documentDeleteQuery;
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
        return maxResults == that.getMaxResults() &&
                firstResult == that.getFirstResult() &&
                Objects.equals(collection, that.getCollection()) &&
                Objects.equals(condition, that.getCondition()) &&
                Objects.equals(sorts, that.getSorts()) &&
                Objects.equals(documents, that.getDocuments());
    }

    @Override
    public int hashCode() {
        return Objects.hash(collection, condition, sorts, documents, maxResults, firstResult);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DefaultDocumentQuery{");
        sb.append("collection='").append(collection).append('\'');
        sb.append(", condition=").append(condition);
        sb.append(", sorts=").append(sorts);
        sb.append(", documents=").append(documents);
        sb.append(", maxResults=").append(maxResults);
        sb.append(", firstResult=").append(firstResult);
        sb.append('}');
        return sb.toString();
    }
}
