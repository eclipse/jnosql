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
package org.jnosql.diana.document;


/**
 * {@link DocumentCollectionManager} factory.
 * When the application has finished using the document collection manager factory, and/or at application shutdown,
 * the application should close the column family manager factory.
 *
 * @param <A> {@link DocumentCollectionManagerAsync} type
 */
public interface DocumentCollectionManagerAsyncFactory<A extends DocumentCollectionManagerAsync> extends AutoCloseable {

    /**
     * Creates a {@link DocumentCollectionManagerAsync} from database's name
     *
     * @param database a database name
     * @return a new {@link DocumentCollectionManagerAsync} instance
     * @throws UnsupportedOperationException when this operation is not supported
     * @throws NullPointerException          when the database is null
     */
    A getAsync(String database);

    /**
     * closes a resource
     */
    void close();
}
