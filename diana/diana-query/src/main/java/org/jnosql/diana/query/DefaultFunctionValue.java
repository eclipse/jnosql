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

package org.jnosql.diana.query;

import jakarta.nosql.QueryException;
import jakarta.nosql.query.Function;
import jakarta.nosql.query.FunctionQueryValue;
import jakarta.nosql.query.QueryValue;

import java.util.Objects;

final class DefaultFunctionValue implements FunctionQueryValue {

    private final Function function;

    private DefaultFunctionValue(Function function) {
        this.function = function;
    }

    @Override
    public Function get() {
        return function;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DefaultFunctionValue)) {
            return false;
        }
        DefaultFunctionValue that = (DefaultFunctionValue) o;
        return Objects.equals(function, that.function);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(function);
    }

    @Override
    public String toString() {
        return function.toString();
    }

    static FunctionQueryValue of(QueryParser.FunctionContext context) {
        if (Objects.nonNull(context.convert())) {
            return getConverter(context);
        }
        throw new UnsupportedOperationException("There is not support to this function yet");
    }

    private static FunctionQueryValue getConverter(QueryParser.FunctionContext context) {
        QueryParser.ConvertContext converter = context.convert();
        QueryValue<?> value = Elements.getElement(converter.element());
        String text = converter.name().getText();
        try {
            Object[] params = new Object[]{value, Class.forName(text)};
            Function function1 = DefaultFunction.of("convert", params);
            return new DefaultFunctionValue(function1);
        } catch (ClassNotFoundException e) {
            throw new QueryException("Class does not found the converter function argument: " + text, e);
        }
    }


}
