/*
 *  Copyright (c) 2018 Otávio Santana and others
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */
package org.eclipse.jnosql.diana.query;

import jakarta.nosql.query.Condition;
import jakarta.nosql.query.Operator;
import jakarta.nosql.query.QueryValue;

import java.util.Objects;

final class DefaultCondition implements Condition {

    private final String name;

    private final Operator operator;

    private final QueryValue<?> value;

    DefaultCondition(String name, Operator operator, QueryValue<?> value) {
        this.name = name;
        this.operator = operator;
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Operator getOperator() {
        return operator;
    }

    @Override
    public QueryValue<?> getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DefaultCondition)) {
            return false;
        }
        DefaultCondition that = (DefaultCondition) o;
        return Objects.equals(name, that.name) &&
                operator == that.operator &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, operator, value);
    }

    @Override
    public String toString() {
        return name + " " + operator + " " + value;
    }
}
