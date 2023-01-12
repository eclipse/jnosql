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

import java.util.Arrays;

/**
 * A sequence of elements that can be either {@link NumberQueryValue} or {@link StringQueryValue}
 */
public final class ArrayQueryValue implements  QueryValue<QueryValue<?>[]>{

    private final QueryValue<?>[] values;

    private ArrayQueryValue(QueryValue<?>[] values) {
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
        if (!(o instanceof ArrayQueryValue)) {
            return false;
        }
        ArrayQueryValue that = (ArrayQueryValue) o;
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

    @Override
    public ValueType getType() {
        return ValueType.ARRAY;
    }

    static ArrayQueryValue of(QueryValue<?>[] values) {
        return new ArrayQueryValue(values);
    }
}
