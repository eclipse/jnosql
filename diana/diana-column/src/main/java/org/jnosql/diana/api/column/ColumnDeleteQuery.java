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
package org.jnosql.diana.api.column;

import java.util.List;
import java.util.Optional;

/**
 * A unit that has the columnFamily and condition to delete from conditions.
 * This instance will be used on:
 * <p>{@link ColumnFamilyManager#delete(ColumnDeleteQuery)}</p>
 * <p>{@link ColumnFamilyManagerAsync#delete(ColumnDeleteQuery)}</p>
 * <p>{@link ColumnFamilyManagerAsync#delete(ColumnDeleteQuery, java.util.function.Consumer)}</p>
 */
public interface ColumnDeleteQuery {


    /**
     * getter the columnFamily name
     *
     * @return the columnFamily name
     */
    String getColumnFamily();

    /**
     * getter the condition
     * If empty, {@link Optional#empty()} is true, the implementation might either return an unsupported exception or delete same elements in the database.
     *
     * @return the condition
     */
    Optional<ColumnCondition> getCondition();

    /**
     * Defines which columns will be removed, the database provider might use this information
     * to remove just these fields instead of all entity from {@link ColumnDeleteQuery}
     *
     * @return the columns
     */
    List<String> getColumns();
}
