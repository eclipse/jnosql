/*
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */

package org.eclipse.jnosql.communication.query;

import org.eclipse.jnosql.communication.Condition;

import java.util.Objects;


/**
 * The WHERE clause specifies a filter to the result set of a database query. It allows you to narrow down the
 * selection based on specified conditions. These conditions are boolean operations composed of one or more
 * criteria, joined together using the logical AND ({@link Condition#AND}) and OR ({@link Condition#OR}) operators.
 * For instance, you can use the WHERE clause to retrieve rows from a table where certain conditions are met.
 */
public final class Where {

    private final QueryCondition condition;

    Where(QueryCondition condition) {
        this.condition = condition;
    }

    /**
     * Retrieves the condition associated with this WHERE clause.
     *
     * @return the condition associated with this WHERE clause.
     */
    public QueryCondition condition() {
        return condition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Where that)) {
            return false;
        }
        return Objects.equals(condition, that.condition);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(condition);
    }

    @Override
    public String toString() {
        return "where " + condition;
    }

    /**
     * Creates a new WHERE clause with the specified condition.
     *
     * @param condition the condition to be applied in the WHERE clause.
     * @return a new WHERE clause with the specified condition.
     */
    public static Where of(QueryCondition condition) {
        return new Where(condition);
    }
}
