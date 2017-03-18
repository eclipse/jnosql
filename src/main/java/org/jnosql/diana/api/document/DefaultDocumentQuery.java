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

    private long limit = -1;

    private long start;

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
    public long getLimit() {
        return limit;
    }

    @Override
    public void setLimit(long limit) {
        this.limit = limit;
    }

    @Override
    public long getStart() {
        return start;
    }

    @Override
    public void setStart(long start) {
        this.start = start;
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
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DocumentQuery that = (DocumentQuery) o;
        return Objects.equals(collection, that.getCollection()) &&
                Objects.equals(condition, that.getCondition()) &&
                Objects.equals(sorts, that.getSorts());
    }

    @Override
    public int hashCode() {
        return Objects.hash(collection, condition, sorts);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DocumentQuery{");
        sb.append("collection='").append(collection).append('\'');
        sb.append(", condition=").append(condition);
        sb.append(", sorts=").append(sorts);
        sb.append('}');
        return sb.toString();
    }
}
