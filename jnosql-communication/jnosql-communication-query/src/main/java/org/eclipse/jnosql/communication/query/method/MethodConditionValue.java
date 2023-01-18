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
package org.eclipse.jnosql.communication.query.method;

import org.eclipse.jnosql.communication.query.ConditionQueryValue;
import org.eclipse.jnosql.communication.query.QueryCondition;

import java.util.List;
import java.util.Objects;

import static java.util.Collections.unmodifiableList;

final class MethodConditionValue implements ConditionQueryValue {

    private final List<QueryCondition> conditions;

    private MethodConditionValue(List<QueryCondition> conditions) {
        this.conditions = conditions;
    }

    @Override
    public List<QueryCondition> get() {
        return unmodifiableList(conditions);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MethodConditionValue)) {
            return false;
        }
        MethodConditionValue that = (MethodConditionValue) o;
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

    public static ConditionQueryValue of(List<QueryCondition> conditions) {
        return new MethodConditionValue(conditions);
    }
}
