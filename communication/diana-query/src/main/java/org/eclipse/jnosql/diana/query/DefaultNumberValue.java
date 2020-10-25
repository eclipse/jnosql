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

import jakarta.nosql.query.NumberQueryValue;

import java.util.Objects;

class DefaultNumberQueryValue implements NumberQueryValue {

    private final Number number;

    DefaultNumberQueryValue(Number number) {
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
        if (!(o instanceof DefaultNumberQueryValue)) {
            return false;
        }
        DefaultNumberQueryValue that = (DefaultNumberQueryValue) o;
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

    public static NumberQueryValue of(QueryParser.NumberContext context) {
        String value = context.getText();
        if (value.contains(".")) {
            return new DefaultNumberQueryValue(Double.valueOf(value));
        }
        return new DefaultNumberQueryValue(Long.valueOf(value));
    }


}
