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
package org.jnosql.diana.document.query;

import org.jnosql.diana.QueryException;
import org.jnosql.diana.document.DocumentCollectionManager;
import org.jnosql.diana.document.DocumentEntity;
import org.jnosql.diana.document.DocumentObserverParser;
import org.jnosql.diana.document.DocumentPreparedStatement;
import org.jnosql.diana.document.DocumentQueryParser;

import java.util.List;
import java.util.Objects;

public final class DefaultDocumentQueryParser implements DocumentQueryParser {

    private final SelectQueryParser select = new SelectQueryParser();
    private final DeleteQueryParser delete = new DeleteQueryParser();
    private final InsertQueryParser insert = new InsertQueryParser();
    private final UpdateQueryParser update = new UpdateQueryParser();

    @Override
    public List<DocumentEntity> query(String query, DocumentCollectionManager collectionManager,
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

    @Override
    public DocumentPreparedStatement prepare(String query, DocumentCollectionManager collectionManager,
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


    private void validation(String query, DocumentCollectionManager collectionManager, DocumentObserverParser observer) {
        Objects.requireNonNull(query, "query is required");
        Objects.requireNonNull(collectionManager, "collectionManager is required");
        Objects.requireNonNull(observer, "observer is required");
        if (query.length() < 6) {
            throw new QueryException(String.format("The query %s is invalid", query));
        }
    }
}
