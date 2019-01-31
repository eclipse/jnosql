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


/**
 * The Column Where whose define the condition in the delete query.
 */
public interface ColumnDeleteWhere extends ColumnDeleteQueryBuild {


    /**
     * Starts a new condition in the select using {@link org.jnosql.diana.api.column.ColumnCondition#and(org.jnosql.diana.api.column.ColumnCondition)}
     *
     * @param name a condition to be added
     * @return the same {@link ColumnDeleteNameCondition} with the condition appended
     * @throws NullPointerException when condition is null
     */
    ColumnDeleteNameCondition and(String name);

    /**
     * Starts a new condition in the select using {@link org.jnosql.diana.api.column.ColumnCondition#or(org.jnosql.diana.api.column.ColumnCondition)}
     *
     * @param name a condition to be added
     * @return the same {@link ColumnDeleteNameCondition} with the condition appended
     * @throws NullPointerException when condition is null
     */
    ColumnDeleteNameCondition or(String name);

}
