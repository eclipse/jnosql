/*
 * Copyright (c) 2022 Contributors to the Eclipse Foundation
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License v. 2.0 are satisfied: GNU General Public License v2.0
 * w/Classpath exception which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */
package jakarta.nosql.tck.entities;


import java.math.BigDecimal;
import java.util.Objects;

public class Money {

    private final String currency;

    private final BigDecimal value;

    public Money(String currency, BigDecimal value) {
        this.currency = currency;
        this.value = value;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getValue() {
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
        Money money = (Money) o;
        return Objects.equals(currency, money.currency) &&
                Objects.equals(value.doubleValue(), money.value.doubleValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(currency, value.doubleValue());
    }

    @Override
    public String toString() {
        return currency + " " + value.toString();
    }

    public static Money parse(String dbData) {
        String[] values = dbData.split(" ");
        String currency = values[0];
        BigDecimal value = BigDecimal.valueOf(Double.valueOf(values[1]));
        return new Money(currency, value);
    }
}

