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
package org.jnosql.diana.api.document.query;

import org.jnosql.diana.api.document.DocumentCollectionManager;
import org.jnosql.diana.api.document.DocumentCollectionManagerAsync;
import org.jnosql.diana.api.document.DocumentEntity;
import org.jnosql.diana.api.document.DocumentQuery;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * The last step to the build of {@link org.jnosql.diana.api.document.DocumentQuery}.
 * It either can return a new {@link org.jnosql.diana.api.document.DocumentQuery} instance or execute a query with
 * {@link org.jnosql.diana.api.document.DocumentCollectionManager}
 * and {@link org.jnosql.diana.api.document.DocumentCollectionManagerAsync}
 */
public interface DocumentQueryBuild {

    /**
     * Creates a new instance of {@link DocumentQuery}
     *
     * @return a new {@link DocumentQuery} instance
     */
    DocumentQuery build();

    /**
     * Executes {@link DocumentCollectionManager#select(DocumentQuery)}
     *
     * @param manager the entity manager
     * @return the result of {@link DocumentCollectionManager#select(DocumentQuery)}
     * @throws NullPointerException when manager is null
     */
    List<DocumentEntity> execute(DocumentCollectionManager manager);

    /**
     * Executes {@link DocumentCollectionManager#singleResult(DocumentQuery)}
     *
     * @param manager the entity manager
     * @return the result of {@link DocumentCollectionManager#singleResult(DocumentQuery)}
     * @throws NullPointerException when manager is null
     */
    Optional<DocumentEntity> executeSingle(DocumentCollectionManager manager);

    /**
     * Executes {@link DocumentCollectionManagerAsync#select(DocumentQuery, Consumer)}
     *
     * @param manager  the entity manager
     * @param callback the callback
     * @throws NullPointerException when there is null parameter
     */
    void execute(DocumentCollectionManager manager, Consumer<List<DocumentEntity>> callback);

    /**
     * Executes {@link DocumentCollectionManagerAsync#singleResult(DocumentQuery, Consumer)}
     *
     * @param manager  the entity manager
     * @param callback the callback
     * @throws NullPointerException when there is null parameter
     */
    void executeSingle(DocumentCollectionManagerAsync manager, Consumer<Optional<DocumentEntity>> callback);
}
