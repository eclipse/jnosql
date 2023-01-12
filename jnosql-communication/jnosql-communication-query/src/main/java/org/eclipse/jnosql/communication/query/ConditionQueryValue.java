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


import java.util.List;
import java.util.Objects;

import static java.util.Collections.unmodifiableList;

/**
 * The QueryValue type that has a list of values, it will be used when the condition is composed such as
 * and ({@link org.eclipse.jnosql.communication.Condition#AND}), or ({@link org.eclipse.jnosql.communication.Condition#OR})
 * and negation ({@link org.eclipse.jnosql.communication.Condition#NOT}).
 */
public final class ConditionQueryValue implements QueryValue<List<QueryCondition>> {

    private final List<QueryCondition> conditions;

    private ConditionQueryValue(List<QueryCondition> conditions) {
        this.conditions = conditions;
    }

    @Override
    public List<QueryCondition> get() {
        return unmodifiableList(conditions);
    }

    @Override
    public ValueType getType() {
        return ValueType.CONDITION;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConditionQueryValue)) {
            return false;
        }
        ConditionQueryValue that = (ConditionQueryValue) o;
        return Objects.equals(conditions, that.conditions);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(conditions);
    }

    @Override
    public String toString() {
        return conditions.toString();
    }

    static ConditionQueryValue of(List<QueryCondition> conditions) {
        return new ConditionQueryValue(conditions);
    }


}
