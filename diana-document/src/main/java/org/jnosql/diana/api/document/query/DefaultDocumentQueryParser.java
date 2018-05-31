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
package org.jnosql.diana.api.document.query;

import org.jnosql.diana.api.document.DocumentCollectionManager;
import org.jnosql.diana.api.document.DocumentEntity;
import org.jnosql.diana.api.document.DocumentQueryParser;
import org.jnosql.query.QueryException;

import java.util.List;
import java.util.Objects;

public class DefaultDocumentQueryParser implements DocumentQueryParser {

    private final SelectQueryParser select = new SelectQueryParser();
    private final DeleteQueryParser delete = new DeleteQueryParser();
    private final InsertQueryParser insert = new InsertQueryParser();
    private final UpdateQueryParser update = new UpdateQueryParser();

    @Override
    public List<DocumentEntity> query(String query, DocumentCollectionManager collectionManager) {
        Objects.requireNonNull(query, "query is required");
        Objects.requireNonNull(collectionManager, "collectionManager is required");

        if (query.length() < 6) {
            throw new QueryException(String.format("The query %s is invalid", query));
        }
        String command = query.substring(0, 6);
        switch (command) {
            case "select":
                return select.query(query, collectionManager);
            case "delete":
                return delete.query(query, collectionManager);
            case "insert":
                return insert.query(query, collectionManager);
            case "update":
                return update.query(query, collectionManager);
            default:
                throw new QueryException(String.format("The command was not recognized at the query %s ", query));
        }
//        return null;
    }
}
