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
 * Condition performs different computations or actions depending on whether a boolean query
 * condition evaluates to true or false.
 * The conditions are composed of three elements.
 * The condition's name
 * The Operator
 * The Value
 *
 * @see DefaultQueryCondition#name()
 * @see DefaultQueryCondition#condition()
 * @see DefaultQueryCondition#value()
 */
final class DefaultQueryCondition implements QueryCondition{

    private final String name;

    private final Condition condition;

    private final QueryValue<?> value;

    DefaultQueryCondition(String name, Condition condition, QueryValue<?> value) {
        this.name = name;
        this.condition = condition;
        this.value = value;
    }

    @Override
    public String name() {
        return name;
    }

   @Override
    public Condition condition() {
        return condition;
    }

   @Override
    public QueryValue<?> value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DefaultQueryCondition)) {
            return false;
        }
        DefaultQueryCondition that = (DefaultQueryCondition) o;
        return Objects.equals(name, that.name) &&
                condition == that.condition &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, condition, value);
    }

    @Override
    public String toString() {
        return name + " " + condition + " " + value;
    }
}
