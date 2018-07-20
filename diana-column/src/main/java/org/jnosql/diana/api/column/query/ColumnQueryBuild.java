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
package org.jnosql.diana.api.column.query;

import org.jnosql.diana.api.column.ColumnEntity;
import org.jnosql.diana.api.column.ColumnFamilyManager;
import org.jnosql.diana.api.column.ColumnFamilyManagerAsync;
import org.jnosql.diana.api.column.ColumnQuery;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * The last step to the build of {@link ColumnQuery}.
 * It either can return a new {@link ColumnQuery} instance or execute a query with
 * {@link ColumnFamilyManager} and {@link ColumnFamilyManagerAsync}
 */
public interface ColumnQueryBuild {

    /**
     * Creates a new instance of {@link ColumnQuery}
     *
     * @return a new {@link ColumnQuery} instance
     */
    ColumnQuery build();

    /**
     * Executes {@link ColumnFamilyManager#select(ColumnQuery)}
     *
     * @param manager the entity manager
     * @return the result of {@link ColumnFamilyManager#select(ColumnQuery)}
     * @throws NullPointerException when manager is null
     */
    List<ColumnEntity> select(ColumnFamilyManager manager);

    /**
     * Executes {@link ColumnFamilyManager#singleResult(ColumnQuery)}
     *
     * @param manager the entity manager
     * @return the result of {@link ColumnFamilyManager#singleResult(ColumnQuery)}
     * @throws NullPointerException when manager is null
     */
    Optional<ColumnEntity> singleResult(ColumnFamilyManager manager);

    /**
     * Executes {@link ColumnFamilyManagerAsync#select(ColumnQuery, Consumer)}
     *
     * @param manager  the entity manager
     * @param callback the callback
     * @throws NullPointerException when there is null parameter
     */
    void select(ColumnFamilyManagerAsync manager, Consumer<List<ColumnEntity>> callback);

    /**
     * Executes {@link ColumnFamilyManagerAsync#singleResult(ColumnQuery, Consumer)}
     *
     * @param manager  the entity manager
     * @param callback the callback
     * @throws NullPointerException when there is null parameter
     */
    void singleResult(ColumnFamilyManagerAsync manager, Consumer<Optional<ColumnEntity>> callback);
}
