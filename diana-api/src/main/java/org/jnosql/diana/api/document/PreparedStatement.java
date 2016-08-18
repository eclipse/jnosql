/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.jnosql.diana.api.document;


import org.jnosql.diana.api.CloseResource;
import org.jnosql.diana.api.ExecuteAsyncQueryException;

import java.util.List;
import java.util.function.Consumer;

/**
 * Represents a prepared statement, a query with bound variables that has been
 * prepared (pre-parsed) by the database.
 */
public interface PreparedStatement extends CloseResource {


    /**
     * Executes the query
     *
     * @return the result
     */
    List<DocumentCollectionEntity> executeQuery();

    /**
     * Executes the query asynchronously
     *
     * @param callBack the callback, when the process is finished will call this instance returning
     *                 the result of query within parameters
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not have support to run async.
     */
    void executeQueryAsync(Consumer<List<DocumentCollectionEntity>> callBack) throws ExecuteAsyncQueryException,
            UnsupportedOperationException;
    /**
     * Bind the properties within query
     *
     * @param values the values to be put in the query
     * @return a  {@link PreparedStatement} binded with the parameters to execute a query
     */
    PreparedStatement bind(Object... values);
}
