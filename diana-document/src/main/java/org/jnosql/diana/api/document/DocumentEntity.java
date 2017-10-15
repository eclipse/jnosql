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

package org.jnosql.diana.api.document;


import org.jnosql.diana.api.Value;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

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
     * Remove a Document whose name is informed in parameter.
     *
     * @param documentName a document name
     * @return if a column was removed or not
     * @throws NullPointerException when documentName is null
     */
    boolean remove(String documentName) throws NullPointerException;

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
     * add a document within {@link DocumentEntity}
     *
     * @param documentName  a name of the document
     * @param value the information of the document
     * @throws UnsupportedOperationException when this method is not supported
     * @throws NullPointerException          when either name or value are null
     */
    void add(String documentName, Object value) throws UnsupportedOperationException, NullPointerException;

    /**
     * add a document within {@link DocumentEntity}
     *
     * @param documentName  a name of the document
     * @param value the information of the document
     * @throws UnsupportedOperationException when this method is not supported
     * @throws NullPointerException          when either name or value are null
     */
    void add(String documentName, Value value) throws UnsupportedOperationException, NullPointerException;

    /**
     * add all documents within {@link DocumentEntity}
     *
     * @param documents documents to be included
     * @throws UnsupportedOperationException when this method is not supported
     * @throws NullPointerException          when document is null
     */
    void addAll(Iterable<Document> documents) throws UnsupportedOperationException, NullPointerException;

    /**
     * Find document a document from document name
     *
     * @param documentName a name of a document
     * @return an {@link Optional} instance with the result
     * @throws NullPointerException when documentName is null
     */
    Optional<Document> find(String documentName) throws NullPointerException;

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
     * Removes all Documents
     */
    void clear();

    /**
     * Returns a Set view of the names of document contained in Document Entity
     *
     * @return the keys
     */
    Set<String> getDocumentNames();

    /**
     * Returns a Collection view of the values contained in this DocumentEntity.
     *
     * @return the collection of values
     */
    Collection<Value> getValues();

    /**
     * Returns true if this DocumentEntity contains a document whose the name is informed
     *
     * @param documentName
     * @return true if find a document and otherwise false
     */
    boolean contains(String documentName);

    /**
     * Converts the columns to a Map where:
     * the key is the name the column
     * The value is the {@link org.jnosql.diana.api.Value#get()} of the map
     *
     * @return a map instance
     */
    Map<String, Object> toMap();

}
