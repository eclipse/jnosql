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


import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * A document-oriented database, or document store, is a computer program designed for storing, retrieving,
 * and managing document-oriented information, also known as semi-structured data. D
 * ocument-oriented databases are one of the main categories of NoSQL databases, and the
 * popularity of the term "document-oriented database" has grown with the use of the term NoSQL itself.
 * XML databases are a subclass of document-oriented databases that are optimized to work with XML documents.
 * Graph databases are similar, but add another layer, the relationship,
 * which allows them to link documents for rapid traversal.
 */
public interface DocumentEntity extends Serializable {

    /**
     * Creates a {@link DocumentEntity} instance
     *
     * @param name the name of the collection
     * @return a {@link DocumentEntity} instance
     * @throws NullPointerException when name is null
     */
    static DocumentEntity of(String name) throws NullPointerException {
        Objects.requireNonNull(name, "name is required");
        return new DefaultDocumentEntity(name);
    }

    /**
     * Creates a {@link DocumentEntity} instance
     *
     * @param name      the collection name
     * @param documents the intial document inside {@link DocumentEntity}
     * @return a {@link DocumentEntity} instance
     * @throws NullPointerException when either name or documents are null
     */
    static DocumentEntity of(String name, List<Document> documents) throws NullPointerException {
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(documents, "documents is required");
        DefaultDocumentEntity entity = new DefaultDocumentEntity(name);
        entity.addAll(documents);
        return entity;
    }

    /**
     * The collection name to {@link DocumentEntity}
     *
     * @return collection name
     */
    String getName();

    /**
     * Remove a name whose name is informed in parameter.
     *
     * @param name a document name
     * @return if a column was removed or not
     * @throws NullPointerException when name is null
     */
    boolean remove(String name) throws NullPointerException;

    /**
     * Removes a Document
     *
     * @param document the document to be removed
     * @return if a column was removed or not
     * @throws NullPointerException when document is null
     */
    boolean remove(Document document) throws NullPointerException;

    /**
     * List of all documents
     *
     * @return all documents
     */
    List<Document> getDocuments();

    /**
     * add a document within {@link DocumentEntity}
     *
     * @param document a document to be included
     * @throws UnsupportedOperationException when this method is not supported
     * @throws NullPointerException          when document is null
     */
    void add(Document document) throws UnsupportedOperationException, NullPointerException;

    /**
     * add all documents within {@link DocumentEntity}
     *
     * @param documents documents to be included
     * @throws UnsupportedOperationException when this method is not supported
     * @throws NullPointerException          when document is null
     */
    void addAll(Iterable<Document> documents) throws UnsupportedOperationException, NullPointerException;

    /**
     * Find document a document from name
     *
     * @param name a document name
     * @return an {@link Optional} instance with the result
     * @throws NullPointerException when name is null
     */
    Optional<Document> find(String name) throws NullPointerException;

    /**
     * Returns the number of elements in this list.
     *
     * @return the number of elements in this list
     */
    int size();

    /**
     * Returns <tt>true</tt> if this list contains no elements.
     *
     * @return <tt>true</tt> if this list contains no elements
     */
    boolean isEmpty();

    /**
     * make copy of itself
     *
     * @return an instance copy
     */
    DocumentEntity copy();

    /**
     * Converts the columns to a Map where:
     * the key is the name the column
     * The value is the {@link org.jnosql.diana.api.Value#get()} of the map
     *
     * @return a map instance
     */
    Map<String, Object> toMap();

}
