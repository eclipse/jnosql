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

import java.util.Objects;

/**
 * The parameter is a dynamic value, which means, it does not define the query, it'll replace in the execution time.
 */
public final class ParamQueryValue implements QueryValue<String> {

    private final String value;

    ParamQueryValue(String value) {
        this.value = value;
    }

    @Override
    public String get() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ParamQueryValue)) {
            return false;
        }
        ParamQueryValue that = (ParamQueryValue) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return "@" + value;
    }

    @Override
    public ValueType getType() {
        return ValueType.PARAMETER;
    }

    public static ParamQueryValue of(QueryParser.ParameterContext parameter) {
        return new ParamQueryValue(parameter.getText().substring(1));
    }

}
