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
package org.jnosql.diana.column;

import org.jnosql.diana.QueryException;

import java.util.List;
import java.util.function.Consumer;

/**
 * A query parser to column database type, this class will convert a String to an operation in {@link ColumnFamilyManagerAsync}.
 */
public interface ColumnQueryParserAsync {

    /**
     * Executes a query and returns the result, when the operations are <b>insert</b>, <b>update</b> and <b>select</b>
     * command it will return the result of the operation when the command is <b>delete</b> it will return an empty collection.
     *
     * @param query    the query as {@link String}
     * @param manager  the manager
     * @param observer the observer
     * @param callBack the callback
     * @throws NullPointerException            when there is parameter null
     * @throws IllegalArgumentException        when the query has value parameters
     * @throws QueryException when there is error in the syntax
     */
    void query(String query, ColumnFamilyManagerAsync manager, Consumer<List<ColumnEntity>> callBack, ColumnObserverParser observer);

    /**
     * Executes a query and returns a {@link ColumnPreparedStatementAsync}, when the operations are <b>insert</b>, <b>update</b> and <b>select</b>
     * command it will return the result of the operation when the command is <b>delete</b> it will return an empty collection.
     *
     * @param query    the query as {@link String}
     * @param observer the observer
     * @param manager  the manager
     * @return a {@link ColumnPreparedStatementAsync} instance
     * @throws NullPointerException            when there is parameter null
     * @throws IllegalArgumentException        when the query has value parameters
     * @throws QueryException when there is error in the syntax
     */
    ColumnPreparedStatementAsync prepare(String query, ColumnFamilyManagerAsync manager, ColumnObserverParser observer);

    /**
     * It returns a {@link ColumnQueryParserAsync} from {@link java.util.ServiceLoader}
     *
     * @return {@link ColumnQueryParserAsync} instance
     * @throws IllegalStateException when there isn't ColumnQueryParser from service loader.
     */
    static ColumnQueryParserAsync getParser() {
        return ColumnQueryParserServiceAsyncLoader.getInstance();
    }


}
