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
package org.jnosql.diana.api.key;

import org.jnosql.diana.api.Value;

import java.util.List;

/**
 * A query parser to key-value database type, this class will convert a String to an operation in {@link BucketManager}.
 */
public interface KeyValueQueryParser {

    /**
     * Executes a query and returns the result, when the operations are <b>put</b>, <b>get</b> and <b>del</b>
     * command it will return the result of the operation when the command is either <b>put</b> or <b>del</b> it will return an empty collection.
     *
     * @param query             the query as {@link String}
     * @param manager the manager
     * @return the result of the operation if delete it will always return an empty list
     * @throws NullPointerException            when there is parameter null
     * @throws IllegalArgumentException        when the query has value parameters
     * @throws org.jnosql.query.QueryException when there is error in the syntax
     */
    List<Value> query(String query, BucketManager manager);

    /**
     * Executes a query and returns a {@link KeyValuePreparedStatement}, when the operations are <b>insert</b>, <b>update</b> and <b>select</b>
     * command it will return the result of the operation when the command is <b>del</b> it will return an empty collection.
     *
     * @param query             the query as {@link String}
     * @param manager the manager
     * @return a {@link KeyValuePreparedStatement} instance
     * @throws NullPointerException            when there is parameter null
     * @throws IllegalArgumentException        when the query has value parameters
     * @throws org.jnosql.query.QueryException when there is error in the syntax
     */
    KeyValuePreparedStatement prepare(String query, BucketManager manager);

    /**
     * It returns a {@link KeyValueQueryParser} from {@link java.util.ServiceLoader}
     *
     * @return {@link KeyValueQueryParser} instance
     * @throws IllegalStateException when there isn't KeyValueQueryParser from service loader.
     */
    static KeyValueQueryParser getParser() {
        return KeyValueQueryParserServiceLoader.getInstance();
    }
}
