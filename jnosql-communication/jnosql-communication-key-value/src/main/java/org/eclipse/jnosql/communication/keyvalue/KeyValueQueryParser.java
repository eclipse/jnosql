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
package org.eclipse.jnosql.communication.keyvalue;


import org.eclipse.jnosql.communication.QueryException;
import org.eclipse.jnosql.communication.Value;

import java.util.Objects;
import java.util.stream.Stream;




/**
 * A query parser to key-value database type, this class will convert a String to an operation in {@link BucketManager}.
 */
final class KeyValueQueryParser {

    private final PutQueryParser putQueryParser = new PutQueryParser();
    private final GetQueryParser getQueryParser = new GetQueryParser();
    private final DelQueryParser delQueryParser = new DelQueryParser();

    /**
     * Executes a query and returns the result, when the operations are <b>put</b>, <b>get</b> and <b>del</b>
     * command it will return the result of the operation when the command is either <b>put</b> or <b>del</b> it will return an empty collection.
     *
     * @param query             the query as {@link String}
     * @param manager the manager
     * @return the result of the operation if delete it will always return an empty list
     * @throws NullPointerException            when there is parameter null
     * @throws IllegalArgumentException        when the query has value parameters
     * @throws QueryException when there is error in the syntax
     */
    public Stream<Value> query(String query, BucketManager manager) {
        validation(query, manager);
        String command = query.substring(0, 3);
        return switch (command) {
            case "get" -> getQueryParser.query(query, manager);
            case "del" -> delQueryParser.query(query, manager);
            case "put" -> putQueryParser.query(query, manager);
            default ->
                    throw new QueryException(String.format("The command was not recognized at the query %s ", query));
        };
    }


    /**
     * Executes a query and returns a {@link KeyValuePreparedStatement}, when the operations are <b>insert</b>, <b>update</b> and <b>select</b>
     * command it will return the result of the operation when the command is <b>del</b> it will return an empty collection.
     *
     * @param query             the query as {@link String}
     * @param manager the manager
     * @return a {@link KeyValuePreparedStatement} instance
     * @throws NullPointerException            when there is parameter null
     * @throws IllegalArgumentException        when the query has value parameters
     * @throws QueryException when there is error in the syntax
     */
    public KeyValuePreparedStatement prepare(String query, BucketManager manager) {
        validation(query, manager);
        String command = query.substring(0, 3);
        return switch (command) {
            case "get" -> getQueryParser.prepare(query, manager);
            case "del" -> delQueryParser.prepare(query, manager);
            case "put" -> putQueryParser.prepare(query, manager);
            default ->
                    throw new QueryException(String.format("The command was not recognized at the query %s ", query));
        };
    }

    private void validation(String query, BucketManager manager) {
        Objects.requireNonNull(query, "query is required");
        Objects.requireNonNull(manager, "manager is required");
        if (query.length() <= 4) {
            throw new QueryException(String.format("The query %s is invalid", query));
        }
    }
}
