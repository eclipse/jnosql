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
package org.jnosql.diana.document.query;

import org.jnosql.diana.document.DocumentCollectionManager;
import org.jnosql.diana.document.DocumentCollectionManagerAsync;
import org.jnosql.diana.document.DocumentDeleteQuery;

import java.util.function.Consumer;

/**
 * The last step to the build of {@link DocumentDeleteQuery}.
 * It either can return a new {@link DocumentDeleteQuery} instance or execute a query with
 * {@link DocumentCollectionManager} and {@link DocumentCollectionManagerAsync}
 */
public interface DocumentDeleteQueryBuild {

    /**
     * Creates a new instance of {@link DocumentDeleteQuery}
     *
     * @return a new {@link DocumentDeleteQuery} instance
     */
    DocumentDeleteQuery build();

    /**
     * executes the {@link DocumentCollectionManager#delete(DocumentDeleteQuery)}
     *
     * @param manager the entity manager
     * @throws NullPointerException when manager is null
     */
    void execute(DocumentCollectionManager manager);

    /**
     * executes the {@link DocumentCollectionManagerAsync#delete(DocumentDeleteQuery)}
     *
     * @param manager the entity manager
     * @throws NullPointerException when manager is null
     */
    void execute(DocumentCollectionManagerAsync manager);

    /**
     * executes the {@link DocumentCollectionManagerAsync#delete(DocumentDeleteQuery, Consumer)}
     *
     * @param manager  the entity manager
     * @param callback the callback
     * @throws NullPointerException when there is null parameter
     */
    void execute(DocumentCollectionManagerAsync manager, Consumer<Void> callback);
}
