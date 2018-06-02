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
package org.jnosql.diana.api.document;

import java.util.List;
import java.util.function.Consumer;

/**
 * A query parser to document database type, this class will convert a String to an operation
 * in {@link DocumentCollectionManagerAsync}.
 */
public interface DocumentQueryParserAsync {

    /**
     * Executes a query and returns the result, when the operations are <b>insert</b>, <b>update</b> and <b>select</b>
     * command it will return the result of the operation when the command is <b>delete</b> it will return an empty collection.
     *
     * @param query             the query as {@link String}
     * @param collectionManager the collection manager
     * @param callBack          the callback
     * @throws NullPointerException            when there is parameter null
     * @throws IllegalArgumentException        when the query has value parameters
     * @throws org.jnosql.query.QueryException when there is error in the syntax
     */
    void query(String query, DocumentCollectionManagerAsync collectionManager, Consumer<List<DocumentEntity>> callBack, DocumentObserverParser observer);

    /**
     * Executes a query and returns a {@link DocumentPreparedStatementAsync}, when the operations are <b>insert</b>, <b>update</b> and <b>select</b>
     * command it will return the result of the operation when the command is <b>delete</b> it will return an empty collection.
     *
     * @param query             the query as {@link String}
     * @param collectionManager the collection manager
     * @return a {@link DocumentPreparedStatement} instance
     * @throws NullPointerException            when there is parameter null
     * @throws IllegalArgumentException        when the query has value parameters
     * @throws org.jnosql.query.QueryException when there is error in the syntax
     */
    DocumentPreparedStatementAsync prepare(String query, DocumentCollectionManagerAsync collectionManager, DocumentObserverParser observer);

    /**
     * It returns a {@link DocumentQueryParserAsync} from {@link java.util.ServiceLoader}
     *
     * @return {@link DocumentQueryParserAsync} instance
     * @throws IllegalStateException when there isn't DocumentQueryParser from service loader.
     */
    static DocumentQueryParserAsync getParser() {
        return DocumentQueryParserServiceAsyncLoader.getInstance();
    }


}
