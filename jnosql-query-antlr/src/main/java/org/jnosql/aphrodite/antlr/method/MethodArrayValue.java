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

import org.jnosql.query.ArrayValue;
import org.jnosql.query.Value;

import java.util.Arrays;

final class MethodArrayValue implements ArrayValue {

    private final Value<?>[] values;

    private MethodArrayValue(Value<?>[] values) {
        this.values = values;
    }

    @Override
    public Value<?>[] get() {
        return values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MethodArrayValue)) {
            return false;
        }
        MethodArrayValue that = (MethodArrayValue) o;
        return Arrays.equals(values, that.values);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(values);
    }

    @Override
    public String toString() {
        return Arrays.toString(values);
    }

    static ArrayValue of(String name) {
        return new MethodArrayValue(new Value[] {new MethodParamValue(name),
                new MethodParamValue(name)});
    }
}
