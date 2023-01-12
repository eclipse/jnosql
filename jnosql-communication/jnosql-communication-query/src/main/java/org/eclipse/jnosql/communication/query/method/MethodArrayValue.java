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


import org.eclipse.jnosql.communication.query.ArrayQueryValue;
import org.eclipse.jnosql.communication.query.QueryValue;

import java.util.Arrays;

final class MethodArrayValue implements ArrayQueryValue {

    private final QueryValue<?>[] values;

    private MethodArrayValue(QueryValue<?>[] values) {
        this.values = values;
    }

    @Override
    public QueryValue<?>[] get() {
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

    static ArrayQueryValue of(String name) {
        return new MethodArrayValue(new QueryValue[] {new MethodParamQueryValue(name),
                new MethodParamQueryValue(name)});
    }
}
