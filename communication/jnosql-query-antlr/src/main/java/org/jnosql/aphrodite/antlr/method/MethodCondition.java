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

package org.jnosql.aphrodite.antlr.method;

import org.jnosql.query.Condition;
import org.jnosql.query.Operator;
import org.jnosql.query.Value;

final class MethodCondition implements Condition {

    private final String name;

    private final Operator operator;

    private final Value<?> value;

    MethodCondition(String name, Operator operator) {
        this.name = name;
        this.operator = operator;
        this.value = new MethodParamValue(name);
    }

    MethodCondition(String name, Operator operator, Value<?> value) {
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
    public Value<?> getValue() {
        return value;
    }

    @Override
    public String toString() {
        return name + " " + operator + " " + value;
    }
}
