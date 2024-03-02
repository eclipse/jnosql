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
package org.eclipse.jnosql.communication.column;

import org.eclipse.jnosql.communication.QueryException;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * A query parser to column database type, this class will convert a String to an operation in {@link ColumnManager}.
 */
public final class ColumnQueryParser {

    private final SelectQueryParser select = new SelectQueryParser();
    private final DeleteQueryParser delete = new DeleteQueryParser();
    private final InsertQueryParser insert = new InsertQueryParser();
    private final UpdateQueryParser update = new UpdateQueryParser();

    /**
     * Executes a query and returns the result, when the operations are <b>insert</b>, <b>update</b> and <b>select</b>
     * command it will return the result of the operation when the command is <b>delete</b> it will return an empty collection.
     *
     * @param query    the query as {@link String}
     * @param manager  the manager
     * @param observer the observer
     * @return the result of the operation if delete it will always return an empty list
     * @throws NullPointerException     when there is parameter null
     * @throws IllegalArgumentException when the query has value parameters
     * @throws QueryException           when there is error in the syntax
     */
    public Stream<ColumnEntity> query(String query, ColumnManager manager, ColumnObserverParser observer) {
        validation(query, manager, observer);
        String command = query.substring(0, 6);
        return switch (command) {
            case "select" -> select.query(query, manager, observer);
            case "delete" -> delete.query(query, manager, observer);
            case "insert" -> insert.query(query, manager, observer);
            case "update" -> update.query(query, manager, observer);
            default ->
                    throw new QueryException(String.format("The command was not recognized at the query %s ", query));
        };
    }

    /**
     * Executes a query and returns a {@link ColumnPreparedStatement}, when the operations are <b>insert</b>, <b>update</b> and <b>select</b>
     * command it will return the result of the operation when the command is <b>delete</b> it will return an empty collection.
     *
     * @param query    the query as {@link String}
     * @param manager  the manager
     * @param observer the observer
     * @return a {@link ColumnPreparedStatement} instance
     * @throws NullPointerException     when there is parameter null
     * @throws IllegalArgumentException when the query has value parameters
     * @throws QueryException           when there is error in the syntax
     */
    public ColumnPreparedStatement prepare(String query, ColumnManager manager, ColumnObserverParser observer) {
        validation(query, manager, observer);
        String command = query.substring(0, 6);

        return switch (command) {
            case "select" -> select.prepare(query, manager, observer);
            case "delete" -> delete.prepare(query, manager, observer);
            case "insert" -> insert.prepare(query, manager, observer);
            case "update" -> update.prepare(query, manager, observer);
            default ->
                    throw new QueryException(String.format("The command was not recognized at the query %s ", query));
        };
    }


    private void validation(String query, ColumnManager manager, ColumnObserverParser observer) {
        Objects.requireNonNull(query, "query is required");
        Objects.requireNonNull(manager, "manager is required");
        Objects.requireNonNull(observer, "manager is observer");
        if (query.length() < 6) {
            throw new QueryException(String.format("The query %s is invalid", query));
        }
    }
}