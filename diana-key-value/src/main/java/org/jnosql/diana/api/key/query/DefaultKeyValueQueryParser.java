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
package org.jnosql.diana.api.key.query;

import org.jnosql.diana.api.Value;
import org.jnosql.diana.api.key.BucketManager;
import org.jnosql.diana.api.key.KeyValuePreparedStatement;
import org.jnosql.diana.api.key.KeyValueQueryParser;
import org.jnosql.query.QueryException;

import java.util.List;
import java.util.Objects;

public class DefaultKeyValueQueryParser implements KeyValueQueryParser {

    private final PutQueryParser putQueryParser = new PutQueryParser();
    private final GetQueryParser getQueryParser = new GetQueryParser();
    private final DelQueryParser delQueryParser = new DelQueryParser();

    @Override
    public List<Value> query(String query, BucketManager manager) {
        Objects.requireNonNull(query, "query is required");
        Objects.requireNonNull(manager, "manager is required");
        if(query.length() <= 4) {
            throw new QueryException(String.format("The query %s is invalid", query));
        }
        String command = query.substring(0, 6);
        switch (command) {
            case "get":
                return getQueryParser.query(query, manager);
            case "del":
                return delQueryParser.query(query, manager);
            case "put":
                return putQueryParser.query(query, manager);
        }
        return null;
    }

    @Override
    public KeyValuePreparedStatement prepare(String query, BucketManager manager) {
        return null;
    }
}
