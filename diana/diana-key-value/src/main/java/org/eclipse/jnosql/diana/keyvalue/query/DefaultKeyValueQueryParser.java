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
package org.eclipse.jnosql.diana.keyvalue.query;

import jakarta.nosql.QueryException;
import jakarta.nosql.Value;
import jakarta.nosql.keyvalue.BucketManager;
import jakarta.nosql.keyvalue.KeyValuePreparedStatement;
import jakarta.nosql.keyvalue.KeyValueQueryParser;

import java.util.Objects;
import java.util.stream.Stream;

public class DefaultKeyValueQueryParser implements KeyValueQueryParser {

    private final PutQueryParser putQueryParser = new PutQueryParser();
    private final GetQueryParser getQueryParser = new GetQueryParser();
    private final DelQueryParser delQueryParser = new DelQueryParser();

    @Override
    public Stream<Value> query(String query, BucketManager manager) {
        validation(query, manager);
        String command = query.substring(0, 3);
        switch (command) {
            case "get":
                return getQueryParser.query(query, manager);
            case "del":
                return delQueryParser.query(query, manager);
            case "put":
                return putQueryParser.query(query, manager);
            default:
                throw new QueryException(String.format("The command was not recognized at the query %s ", query));
        }
    }


    @Override
    public KeyValuePreparedStatement prepare(String query, BucketManager manager) {
        validation(query, manager);
        String command = query.substring(0, 3);
        switch (command) {
            case "get":
                return getQueryParser.prepare(query, manager);
            case "del":
                return delQueryParser.prepare(query, manager);
            case "put":
                return putQueryParser.prepare(query, manager);
            default:
                throw new QueryException(String.format("The command was not recognized at the query %s ", query));
        }
    }

    private void validation(String query, BucketManager manager) {
        Objects.requireNonNull(query, "query is required");
        Objects.requireNonNull(manager, "manager is required");
        if (query.length() <= 4) {
            throw new QueryException(String.format("The query %s is invalid", query));
        }
    }
}
