/*
 *  Copyright (c) 2023 Contributors to the Eclipse Foundation
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


import org.eclipse.jnosql.query.grammar.QueryParser;

import java.util.Objects;

/**
 * Boolean is a data type that has one of two possible values (usually denoted true and false)
 * which is intended to represent the two truth values of logic and Boolean algebra.
 */
public final class BooleanQueryValue implements QueryValue<Boolean>{

    public static final BooleanQueryValue TRUE = new BooleanQueryValue(true);
    public static final BooleanQueryValue FALSE = new BooleanQueryValue(false);

    private final boolean value;

    private BooleanQueryValue(boolean value) {
        this.value = value;
    }

    @Override
    public ValueType type() {
        return ValueType.BOOLEAN;
    }

    @Override
    public Boolean get() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BooleanQueryValue that = (BooleanQueryValue) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "BooleanQueryValue{" +
                "value=" + value +
                '}';
    }

    public static QueryValue<Boolean> of(QueryParser.BoolContext context) {
        String text = context.BOOLEAN().getText();
        return Boolean.parseBoolean(text)? TRUE : FALSE;
    }

}
