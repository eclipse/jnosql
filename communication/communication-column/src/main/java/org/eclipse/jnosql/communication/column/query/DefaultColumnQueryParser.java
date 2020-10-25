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
package org.eclipse.jnosql.communication.column.query;

import jakarta.nosql.QueryException;
import jakarta.nosql.column.ColumnEntity;
import jakarta.nosql.column.ColumnFamilyManager;
import jakarta.nosql.column.ColumnObserverParser;
import jakarta.nosql.column.ColumnPreparedStatement;
import jakarta.nosql.column.ColumnQueryParser;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * The default implementation of {@link ColumnQueryParser}
 */
public final class DefaultColumnQueryParser implements ColumnQueryParser {

    private final DefaultSelectQueryConverter select = new DefaultSelectQueryConverter();
    private final DefaultDeleteQueryConverter delete = new DefaultDeleteQueryConverter();
    private final InsertQueryParser insert = new InsertQueryParser();
    private final UpdateQueryParser update = new UpdateQueryParser();

    @Override
    public Stream<ColumnEntity> query(String query, ColumnFamilyManager manager, ColumnObserverParser observer) {
        validation(query, manager, observer);
        String command = query.substring(0, 6);
        switch (command) {
            case "select":
                return select.query(query, manager, observer);
            case "delete":
                return delete.query(query, manager, observer);
            case "insert":
                return insert.query(query, manager, observer);
            case "update":
                return update.query(query, manager, observer);
            default:
                throw new QueryException(String.format("The command was not recognized at the query %s ", query));
        }
    }

    @Override
    public ColumnPreparedStatement prepare(String query, ColumnFamilyManager manager, ColumnObserverParser observer) {
        validation(query, manager, observer);
        String command = query.substring(0, 6);

        switch (command) {
            case "select":
                return select.prepare(query, manager, observer);
            case "delete":
                return delete.prepare(query, manager, observer);
            case "insert":
                return insert.prepare(query, manager, observer);
            case "update":
                return update.prepare(query, manager, observer);
            default:
                throw new QueryException(String.format("The command was not recognized at the query %s ", query));
        }
    }


    private void validation(String query, ColumnFamilyManager manager, ColumnObserverParser observer) {
        Objects.requireNonNull(query, "query is required");
        Objects.requireNonNull(manager, "manager is required");
        Objects.requireNonNull(observer, "manager is observer");
        if (query.length() < 6) {
            throw new QueryException(String.format("The query %s is invalid", query));
        }
    }
}