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


import java.util.List;
import java.util.Optional;

/**
 * A unit that has the columnFamily and condition to delete from conditions
 *
 * <p>{@link DocumentDeleteQuery#of(String, DocumentCondition)}</p>
 * This instance will be used on:
 * <p>{@link DocumentCollectionManager#delete(DocumentDeleteQuery)}</p>
 * <p>{@link DocumentCollectionManagerAsync#delete(DocumentDeleteQuery)}</p>
 * <p>{@link DocumentCollectionManagerAsync#delete(DocumentDeleteQuery, java.util.function.Consumer)}</p>
 */
public interface DocumentDeleteQuery {

    /**
     * getter the collection name
     *
     * @return the collection name
     */
    String getCollection();

    /**
     * getter the condition
     *
     * @return the condition
     */
    Optional<DocumentCondition> getCondition();

    /**
     * Defines which columns will be removed, the database provider might use this information
     * to remove just these fields instead of all entity from {@link DocumentDeleteQuery}
     *
     * @return the columns
     */
    List<String> getDocuments();

    /**
     * Adds a document to be removed
     *
     * @param document the document
     * @throws NullPointerException when document is null
     * @see DocumentDeleteQuery#getDocuments()
     */
    void add(String document) throws NullPointerException;

    /**
     * Adds all documents
     *
     * @param documents the documents to be added
     * @throws NullPointerException when column is null
     * @see DocumentDeleteQuery#getDocuments()
     */
    void addAll(Iterable<String> documents) throws NullPointerException;

    /**
     * Appends a new condition in the select
     * using {{@link DocumentCondition#and(DocumentCondition)}}
     *
     * @param condition condition to be added
     * @return the same instance with a condition added
     * @throws NullPointerException when condition is null
     */
    DocumentDeleteQuery and(DocumentCondition condition) throws NullPointerException;

    /**
     * Appends a new condition in the select
     * using {{@link DocumentCondition#or(DocumentCondition)}}
     *
     * @param condition condition to be added
     * @return the same instance with a condition added
     * @throws NullPointerException when condition is null
     */
    DocumentDeleteQuery or(DocumentCondition condition) throws NullPointerException;


    /**
     * Sets the document condition
     *
     * @param condition
     * @return this instance
     * @throws NullPointerException when condition is null
     */
    DocumentDeleteQuery with(DocumentCondition condition) throws NullPointerException;

    /**
     * Removes a document from
     *
     * @param document the document to be removed
     * @throws NullPointerException when document is null
     * @see DocumentDeleteQuery#getDocuments()
     */
    void remove(String document) throws NullPointerException;

    /**
     * Removes documents
     *
     * @param documents the documents to be removed
     * @throws NullPointerException when documents is null
     * @see DocumentDeleteQuery#getDocuments()
     */
    void removeAll(Iterable<String> documents) throws NullPointerException;

    /**
     * Creates a instance of column family
     *
     * @param collection the column family name
     * @param condition  the condition
     * @return an {@link DocumentDeleteQuery}
     * @throws NullPointerException when either collection
     */
    static DocumentDeleteQuery of(String collection, DocumentCondition condition) throws NullPointerException {
        return DefaultDocumentDeleteQuery.of(collection, condition);
    }

    /**
     * Creates a instance of column family
     *
     * @param collection the column family name
     * @return an {@link DocumentDeleteQuery}
     * @throws NullPointerException when collection is null
     */
    static DocumentDeleteQuery of(String collection) throws NullPointerException {
        return DefaultDocumentDeleteQuery.of(collection);
    }
}
