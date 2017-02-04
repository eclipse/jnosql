/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.jnosql.diana.api.document;


import org.jnosql.diana.api.Sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Class that contains information to do a query to {@link DocumentCollectionManager}
 *
 * @see DocumentCollectionManager#find(DocumentQuery)
 * @see DocumentCondition
 * @see Sort
 */
public class DocumentQuery {

    private final String collection;

    private DocumentCondition condition;

    private final List<Sort> sorts = new ArrayList<>();

    private final List<String> documents = new ArrayList<>();

    private long limit = -1;

    private long start;

    private DocumentQuery(String collection) {
        this.collection = Objects.requireNonNull(collection, "column family is required");
    }

    /**
     * Creates a {@link DocumentQuery}
     *
     * @param documentCollection - the name of document collection to do a query
     * @return a {@link DocumentQuery} instance
     * @throws NullPointerException when documentCollecion is null
     */
    public static DocumentQuery of(String documentCollection) throws NullPointerException {
        Objects.requireNonNull(documentCollection, "documentCollection is required");
        return new DocumentQuery(documentCollection);
    }

    /**
     * Appends a new condition in the query
     * using {{@link DocumentCondition#and(DocumentCondition)}}
     *
     * @param condition condition to be added
     * @return the same instance with a condition added
     * @throws NullPointerException when condition is null
     */
    public DocumentQuery and(DocumentCondition condition) throws NullPointerException {
        Objects.requireNonNull(condition, "condition is required");
        if (Objects.isNull(this.condition)) {
            this.condition = condition;
        } else {
            this.condition = this.condition.and(condition);
        }

        return this;
    }

    /**
     * Appends a new condition in the query
     * using {{@link DocumentCondition#or(DocumentCondition)}}
     *
     * @param condition condition to be added
     * @return the same instance with a condition added
     * @throws NullPointerException when condition is null
     */
    public DocumentQuery or(DocumentCondition condition) throws NullPointerException {
        this.condition = Objects.requireNonNull(condition, "condition is required");
        return this;
    }

    /**
     * Add the order how the result will returned
     *
     * @param sort the order way
     * @return the same way with a sort added
     * @throws NullPointerException when sort is null
     */
    public DocumentQuery addSort(Sort sort) throws NullPointerException {
        this.sorts.add(Objects.requireNonNull(sort, "Sort is required"));
        return this;
    }

    /**
     * Add column to be either retrieve or deleted, if empty will either returns
     * all elements in a find query or delete all elements in a column family entity.
     *
     * @param document the document name
     * @return the same instance with a column added
     */
    public DocumentQuery addColumn(String document) throws NullPointerException {
        this.documents.add(Objects.requireNonNull(document, "document is required"));
        return this;
    }


    /**
     * The document collection name
     *
     * @return the document collection name
     */
    public String getCollection() {
        return collection;
    }

    /**
     * The conditions that contains in this {@link DocumentQuery}
     *
     * @return the conditions
     */
    public DocumentCondition getCondition() {
        return condition;
    }

    /**
     * The sorts that contains in this {@link DocumentQuery}
     *
     * @return the sorts
     */
    public List<Sort> getSorts() {
        return Collections.unmodifiableList(sorts);
    }

    public List<String> getDocuments() {
        return Collections.unmodifiableList(documents);
    }

    /**
     * Returns the max number of row in a query
     *
     * @return the limit to be used in a query
     */
    public long getLimit() {
        return limit;
    }

    /**
     * Sets the max number of row in a query, if negative the value will ignored
     *
     * @param limit the new limit to query
     */
    public void setLimit(long limit) {
        this.limit = limit;
    }

    /**
     * Gets when the result starts
     * @return
     */
    public long getStart() {
        return start;
    }

    /**
     * Setter to start a query
     * @param start the starts
     */
    public void setStart(long start) {
        this.start = start;
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
        return Objects.equals(collection, that.collection) &&
                Objects.equals(condition, that.condition) &&
                Objects.equals(sorts, that.sorts);
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
