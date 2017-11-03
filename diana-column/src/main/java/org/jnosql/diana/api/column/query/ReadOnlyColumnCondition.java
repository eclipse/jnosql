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

import org.jnosql.diana.api.Condition;
import org.jnosql.diana.api.column.Column;
import org.jnosql.diana.api.column.ColumnCondition;

import java.util.Objects;

class ReadOnlyColumnCondition implements ColumnCondition {

    private final ColumnCondition condition;

    ReadOnlyColumnCondition(ColumnCondition condition) {
        this.condition = Objects.requireNonNull(condition, "condition is required");
    }

    @Override
    public Column getColumn() {
        return condition.getColumn();
    }

    @Override
    public Condition getCondition() {
        return condition.getCondition();
    }

    @Override
    public ColumnCondition and(ColumnCondition condition) throws NullPointerException {
        throw new IllegalStateException("You cannot change the status after build the query");

    }

    @Override
    public ColumnCondition negate() {
        throw new IllegalStateException("You cannot change the status after build the query");
    }

    @Override
    public ColumnCondition or(ColumnCondition condition) throws NullPointerException {
        throw new IllegalStateException("You cannot change the status after build the query");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !ColumnCondition.class.isAssignableFrom(o.getClass())) {
            return false;
        }
        ColumnCondition that = (ColumnCondition) o;
        return Objects.equals(condition, that);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(condition);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ReadOnlyColumnCondition{");
        sb.append("condition=").append(condition);
        sb.append('}');
        return sb.toString();
    }
}
