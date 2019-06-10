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
import org.jnosql.query.Where;

import java.util.Objects;

final class MethodWhere implements Where {

    private final Condition condition;

    public MethodWhere(Condition condition) {
        this.condition = condition;
    }

    @Override
    public Condition getCondition() {
        return condition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MethodWhere)) {
            return false;
        }
        MethodWhere that = (MethodWhere) o;
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
}
