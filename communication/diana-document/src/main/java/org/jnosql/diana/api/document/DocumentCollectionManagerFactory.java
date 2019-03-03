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


/**
 * {@link DocumentCollectionManager} factory.
 * When the application has finished using the document collection manager factory, and/or at application shutdown,
 * the application should close the column family manager factory.
 *
 * @param <S> the {@link DocumentCollectionManager} type
 */
public interface DocumentCollectionManagerFactory<S extends DocumentCollectionManager> extends AutoCloseable {

    /**
     * Creates a {@link DocumentCollectionManager} from database's name
     *
     * @param database a database name
     * @return a new {@link DocumentCollectionManager} instance
     * @throws UnsupportedOperationException when this operation is not supported
     * @throws NullPointerException          when the database is null
     */
    S get(String database);


    /**
     * closes a resource
     */
    void close();
}
