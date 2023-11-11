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


import org.eclipse.jnosql.query.grammar.QueryParser;

import java.util.Objects;

/**
 * A number is a mathematical object used to count, measure and also label where if it is a decimal, will become
 * {@link Double}, otherwise, {@link Long}
 */
public final class NumberQueryValue implements QueryValue<Number> {

    private final Number number;

    NumberQueryValue(Number number) {
        this.number = number;
    }

    @Override
    public Number get() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NumberQueryValue that)) {
            return false;
        }
        return Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(number);
    }

    @Override
    public String toString() {
        return number.toString();
    }

    @Override
    public ValueType type() {
        return ValueType.NUMBER;
    }

    static NumberQueryValue of(QueryParser.NumberContext context) {
        String value = context.getText();
        if (value.contains(".")) {
            return new NumberQueryValue(Double.valueOf(value));
        }
        return new NumberQueryValue(Long.valueOf(value));
    }



}
