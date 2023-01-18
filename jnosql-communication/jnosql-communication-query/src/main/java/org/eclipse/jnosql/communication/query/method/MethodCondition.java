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

import org.eclipse.jnosql.communication.Condition;
import org.eclipse.jnosql.communication.query.QueryCondition;
import org.eclipse.jnosql.communication.query.QueryValue;

final class MethodCondition implements QueryCondition {

    private final String name;

    private final Condition condition;

    private final QueryValue<?> value;

    MethodCondition(String name, Condition condition) {
        this.name = name;
        this.condition = condition;
        this.value = new MethodParamQueryValue(name);
    }

    MethodCondition(String name, Condition condition, QueryValue<?> value) {
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
    public String toString() {
        return name + " " + condition + " " + value;
    }
}
