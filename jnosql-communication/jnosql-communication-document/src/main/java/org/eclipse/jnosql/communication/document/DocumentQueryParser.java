/*
 *
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
 *
 */
package org.eclipse.jnosql.communication.document;


import org.eclipse.jnosql.communication.QueryException;

import java.util.Objects;
import java.util.stream.Stream;


/**
 * A query parser to document database type, this class will convert a String to an operation in {@link DocumentManager}.
 */
public final class DocumentQueryParser {

    private final SelectQueryParser select = new SelectQueryParser();
    private final DeleteQueryParser delete = new DeleteQueryParser();
    private final InsertQueryParser insert = new InsertQueryParser();
    private final UpdateQueryParser update = new UpdateQueryParser();

    /**
     * Executes a query and returns the result, when the operations are <b>insert</b>, <b>update</b> and <b>select</b>
     * command it will return the result of the operation when the command is <b>delete</b> it will return an empty collection.
     *
     * @param query             the query as {@link String}
     * @param collectionManager the collection manager
     * @param observer          the observer
     * @return the result of the operation if delete it will always return an empty list
     * @throws NullPointerException            when there is parameter null
     * @throws IllegalArgumentException        when the query has value parameters
     * @throws QueryException when there is error in the syntax
     */
    public Stream<DocumentEntity> query(String query, DocumentManager collectionManager,
                                        DocumentObserverParser observer) {
        validation(query, collectionManager, observer);
        String command = query.substring(0, 6);
        switch (command) {
            case "select":
                return select.query(query, collectionManager, observer);
            case "delete":
                return delete.query(query, collectionManager, observer);
            case "insert":
                return insert.query(query, collectionManager, observer);
            case "update":
                return update.query(query, collectionManager, observer);
            default:
                throw new QueryException(String.format("The command was not recognized at the query %s ", query));
        }
    }

    /**
     * Executes a query and returns a {@link DocumentPreparedStatement}, when the operations are <b>insert</b>, <b>update</b> and <b>select</b>
     * command it will return the result of the operation when the command is <b>delete</b> it will return an empty collection.
     *
     * @param query             the query as {@link String}
     * @param collectionManager the collection manager
     * @param observer          the observer
     * @return a {@link DocumentPreparedStatement} instance
     * @throws NullPointerException            when there is parameter null
     * @throws IllegalArgumentException        when the query has value parameters
     * @throws QueryException when there is error in the syntax
     */
    public DocumentPreparedStatement prepare(String query, DocumentManager collectionManager,
                                             DocumentObserverParser observer) {

        validation(query, collectionManager, observer);
        String command = query.substring(0, 6);

        switch (command) {
            case "select":
                return select.prepare(query, collectionManager, observer);
            case "delete":
                return delete.prepare(query, collectionManager, observer);
            case "insert":
                return insert.prepare(query, collectionManager, observer);
            case "update":
                return update.prepare(query, collectionManager, observer);
            default:
                throw new QueryException(String.format("The command was not recognized at the query %s ", query));
        }
    }


    private void validation(String query, DocumentManager collectionManager, DocumentObserverParser observer) {
        Objects.requireNonNull(query, "query is required");
        Objects.requireNonNull(collectionManager, "collectionManager is required");
        Objects.requireNonNull(observer, "observer is required");
        if (query.length() < 6) {
            throw new QueryException(String.format("The query %s is invalid", query));
        }
    }
}
