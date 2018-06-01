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
package org.jnosql.diana.api.column.query;

import org.jnosql.diana.api.column.ColumnEntity;
import org.jnosql.diana.api.column.ColumnFamilyManagerAsync;
import org.jnosql.diana.api.column.ColumnPreparedStatementAsync;
import org.jnosql.diana.api.column.ColumnQueryParserAsync;
import org.jnosql.query.QueryException;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public final class DefaultColumnQueryParserAsync implements ColumnQueryParserAsync {

    private final SelectQueryParser select = new SelectQueryParser();
    private final DeleteQueryParser delete = new DeleteQueryParser();
    private final InsertQueryParser insert = new InsertQueryParser();
    private final UpdateQueryParser update = new UpdateQueryParser();

    @Override
    public void query(String query, ColumnFamilyManagerAsync collectionManager,
                      Consumer<List<ColumnEntity>> callBack) {

        validation(query, collectionManager, callBack);
        String command = query.substring(0, 6);
        switch (command) {
            case "select":
                select.queryAsync(query, collectionManager, callBack, this);
                return;
            case "delete":
                delete.queryAsync(query, collectionManager, callBack, this);
                return;
            case "insert":
                insert.queryAsync(query, collectionManager, callBack, this);
                return;
            case "update":
                update.queryAsync(query, collectionManager, callBack, this);
                return;
            default:
                throw new QueryException(String.format("The command was not recognized at the query %s ", query));
        }
    }

    @Override
    public ColumnPreparedStatementAsync prepare(String query, ColumnFamilyManagerAsync manager) {
        validation(query, manager);
        String command = query.substring(0, 6);

        switch (command) {
            case "select":
                return select.prepareAsync(query, manager, this);
            case "delete":
                return delete.prepareAsync(query, manager, this);
            case "insert":
                return insert.prepareAsync(query, manager, this);
            case "update":
                return update.prepareAsync(query, manager, this);
            default:
                throw new QueryException(String.format("The command was not recognized at the query %s ", query));
        }
    }

    private void validation(String query, ColumnFamilyManagerAsync manager) {

        Objects.requireNonNull(query, "query is required");
        Objects.requireNonNull(manager, "manager is required");
        if (query.length() < 6) {
            throw new QueryException(String.format("The query %s is invalid", query));
        }
    }
    private void validation(String query, ColumnFamilyManagerAsync manager,
                            Consumer<List<ColumnEntity>> callBack) {

        Objects.requireNonNull(query, "query is required");
        Objects.requireNonNull(manager, "manager is required");
        Objects.requireNonNull(callBack, "callBack is required");
        if (query.length() < 6) {
            throw new QueryException(String.format("The query %s is invalid", query));
        }
    }
}