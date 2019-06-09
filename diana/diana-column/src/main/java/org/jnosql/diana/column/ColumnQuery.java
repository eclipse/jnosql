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


import org.jnosql.diana.Sort;

import java.util.List;
import java.util.Optional;

/**
 * Class that contains information to do a select to {@link ColumnEntity}
 *
 * @see ColumnFamilyManager#select(ColumnQuery)
 * @see ColumnCondition
 * @see Sort
 */
public interface ColumnQuery {


    /**
     * @return The maximum number of results the select object was set to retrieve.
     * The implementation might ignore this option.
     */
    long getLimit();

    /**
     * @return The position of the first result the select object was set to retrieve.
     * The implementation might ignore this option.
     */
    long getSkip();

    /**
     * The column family name
     *
     * @return the column family name
     */
    String getColumnFamily();

    /**
     * The conditions that contains in this {@link ColumnQuery}
     * If empty, {@link Optional#empty()} is true, the implementation might either return an unsupported exception or returns same elements in the database.
     *
     * @return the conditions
     */
    Optional<ColumnCondition> getCondition();

    /**
     * Returns the columns to returns in that query if empty will return all elements in the query.
     * The implementation might ignore this option.
     *
     * @return the columns
     */
    List<String> getColumns();

    /**
     * The sorts that contains in this {@link ColumnQuery}
     * The implementation might ignore this option.
     *
     * @return the sorts
     */
    List<Sort> getSorts();

}
