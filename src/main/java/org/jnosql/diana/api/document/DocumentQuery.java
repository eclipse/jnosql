/*
 * Copyright 2017 Otavio Santana and others
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

import java.util.List;
import java.util.Optional;

/**
 * Class that contains information to do a query to {@link DocumentCollectionManager}
 *
 * @see DocumentCollectionManager#find(DocumentQuery)
 * @see DocumentCondition
 * @see Sort
 */
public interface DocumentQuery {


    /**
     * Creates a {@link DocumentQuery}
     *
     * @param collection - the name of document collection to do a query
     * @return a {@link DocumentQuery} instance
     * @throws NullPointerException when documentCollecion is null
     */
    static DocumentQuery of(String collection) throws NullPointerException {
        return DefaultDocumentQuery.of(collection);
    }

    /**
     * Appends a new condition in the query
     * using {{@link DocumentCondition#and(DocumentCondition)}}
     *
     * @param condition condition to be added
     * @return the same instance with a condition added
     * @throws NullPointerException when condition is null
     */
    DocumentQuery and(DocumentCondition condition) throws NullPointerException;

    /**
     * Appends a new condition in the query
     * using {{@link DocumentCondition#or(DocumentCondition)}}
     *
     * @param condition condition to be added
     * @return the same instance with a condition added
     * @throws NullPointerException when condition is null
     */
    DocumentQuery or(DocumentCondition condition) throws NullPointerException;

    /**
     * Add the order how the result will returned
     *
     * @param sort the order way
     * @return the same way with a sort added
     * @throws NullPointerException when sort is null
     */
    DocumentQuery addSort(Sort sort) throws NullPointerException;

    /**
     * Add column to be either retrieve or deleted, if empty will either returns
     * all elements in a find query or delete all elements in a column family entity.
     *
     * @param document the document name
     * @return the same instance with a column added
     */
    DocumentQuery addColumn(String document) throws NullPointerException;


    /**
     * The document collection name
     *
     * @return the document collection name
     */
    String getCollection();

    /**
     * The conditions that contains in this {@link DocumentQuery}
     *
     * @return the conditions
     */
    Optional<DocumentCondition> getCondition();

    /**
     * The sorts that contains in this {@link DocumentQuery}
     *
     * @return the sorts
     */
    List<Sort> getSorts();

    /**
     * Get documents
     *
     * @return the documents
     */
    List<String> getDocuments();

    /**
     * Returns the max number of row in a query
     *
     * @return the limit to be used in a query
     */
    long getLimit();

    /**
     * the limit setter
     *
     * @param limit the limit
     */
    void setLimit(long limit);

    /**
     * Gets when the result starts
     *
     * @return the start
     */
    long getStart();

    /**
     * Setter to start a query
     *
     * @param start the starts
     */
    void setStart(long start);

    /**
     * Converts to {@link DocumentDeleteQuery}
     *
     * @return the {@link DocumentDeleteQuery} instance
     */
    DocumentDeleteQuery toDeleteQuery();

}
