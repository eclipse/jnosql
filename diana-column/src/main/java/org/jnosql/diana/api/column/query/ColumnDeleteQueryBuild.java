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

import org.jnosql.diana.api.column.ColumnDeleteQuery;
import org.jnosql.diana.api.column.ColumnFamilyManager;
import org.jnosql.diana.api.column.ColumnFamilyManagerAsync;

import java.util.function.Consumer;

/**
 * The last step to the build of {@link ColumnDeleteQuery}.
 * It either can return a new {@link ColumnDeleteQuery} instance or execute a query with
 * {@link ColumnFamilyManager} and {@link ColumnFamilyManagerAsync}
 */
public interface ColumnDeleteQueryBuild {

    /**
     * Creates a new instance of {@link ColumnDeleteQuery}
     *
     * @return a new {@link ColumnDeleteQuery} instance
     */
    ColumnDeleteQuery build();

    /**
     * executes the {@link ColumnFamilyManager#delete(ColumnDeleteQuery)}
     *
     * @param manager the entity manager
     * @throws NullPointerException when manager is null
     */
    void execute(ColumnFamilyManager manager);

    /**
     * executes the {@link ColumnFamilyManagerAsync#delete(ColumnDeleteQuery)}
     *
     * @param manager the entity manager
     * @throws NullPointerException when manager is null
     */
    void execute(ColumnFamilyManagerAsync manager);

    /**
     * executes the {@link ColumnFamilyManagerAsync#delete(ColumnDeleteQuery, Consumer)}
     *
     * @param manager  the entity manager
     * @param callback the callback
     * @throws NullPointerException when there is null parameter
     */
    void execute(ColumnFamilyManagerAsync manager, Consumer<Void> callback);
}
