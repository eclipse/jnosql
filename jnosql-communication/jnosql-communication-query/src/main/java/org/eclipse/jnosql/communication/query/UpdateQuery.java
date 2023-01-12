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

import jakarta.nosql.query.Condition;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Updating an entity is done using an <b>UPDATE</b> statement.
 */
public final class UpdateQuery implements Query {

    private final String entity;

    private final List<QueryCondition> conditions;

    private final JSONQueryValue value;

    UpdateQuery(String entity, List<QueryCondition> conditions, JSONQueryValue value) {
        this.entity = entity;
        this.conditions = conditions;
        this.value = value;
    }

    /**
     * The entity name
     * @return the entity name
     */
    public String entity() {
        return entity;
    }

    /**
     * The list of changes as conditions. Each condition will use the equals operator, {@link org.eclipse.jnosql.communication.Condition#EQUALS},
     *  e.g., name = "any name"
     * @return the conditions
     */
    public List<QueryCondition> conditions() {
        return Collections.unmodifiableList(conditions);
    }

    /**
     * Returns the value to update when the query uses JSON value instead of Conditions.
     * In an insert, an operation is not able to use both: {@link UpdateQuery#conditions()} and
     * {@link UpdateQuery#value()}.
     * Therefore, execution will use just one operation type.
     * @return a {@link JSONQueryValue} or {@link Optional#empty()} when it uses {@link UpdateQuery#conditions()}
     */
    public Optional<JSONQueryValue> value() {
        return Optional.ofNullable(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UpdateQuery)) {
            return false;
        }
        UpdateQuery that = (UpdateQuery) o;
        return Objects.equals(entity, that.entity) &&
                Objects.equals(conditions, that.conditions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entity, conditions);
    }

    @Override
    public String toString() {
        if (conditions.isEmpty() && value != null) {
            return "update " + entity + ' ' + value;
        } else {
            return "update " + entity + " (" + conditions + ") ";
        }
    }
}
