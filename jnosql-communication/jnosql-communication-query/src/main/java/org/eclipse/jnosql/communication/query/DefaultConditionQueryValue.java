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
 * Default implementation of {@link ConditionQueryValue}
 */
final class DefaultConditionQueryValue implements ConditionQueryValue {

    private final List<QueryCondition> conditions;

    private DefaultConditionQueryValue(List<QueryCondition> conditions) {
        this.conditions = conditions;
    }

    @Override
    public List<QueryCondition> get() {
        return unmodifiableList(conditions);
    }

    @Override
    public ValueType type() {
        return ValueType.CONDITION;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DefaultConditionQueryValue)) {
            return false;
        }
        DefaultConditionQueryValue that = (DefaultConditionQueryValue) o;
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
        return new DefaultConditionQueryValue(conditions);
    }


}
