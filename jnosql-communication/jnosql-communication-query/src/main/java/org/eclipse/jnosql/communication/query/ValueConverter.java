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


import org.eclipse.jnosql.communication.QueryException;

import java.util.Objects;

final class ValueConverter {

    private static final String MESSAGE = "There is an error when trying to convert the value";

    private ValueConverter() {
    }

    static QueryValue<?> get(QueryParser.ValueContext context) {

        if (Objects.nonNull(context.number())) {
            return NumberQueryValue.of(context.number());
        }

        if (Objects.nonNull(context.string())) {
            return StringQueryValue.of(context.string());
        }

        if (Objects.nonNull(context.json())) {
            return JSONQueryValue.of(context.json());
        }

        if (Objects.nonNull(context.parameter())) {
            return ParamQueryValue.of(context.parameter());
        }

        if (Objects.nonNull(context.function())) {
            return FunctionQueryValue.of(context.function());
        }
        if (Objects.nonNull(context.array())) {
            QueryValue<?>[] elements = context.array().element().stream()
                    .map(Elements::getElement)
                    .toArray(QueryValue[]::new);
            return ArrayQueryValue.of(elements);
        }
        throw new QueryException(MESSAGE);
    }

}
