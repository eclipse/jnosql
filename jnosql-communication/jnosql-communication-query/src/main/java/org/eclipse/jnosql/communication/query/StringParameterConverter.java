/*
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *
 *  Maximillian Arruda
 */

package org.eclipse.jnosql.communication.query;


import org.eclipse.jnosql.communication.QueryException;
import org.eclipse.jnosql.query.grammar.QueryParser;

import java.util.Objects;

final class StringParameterConverter {

    private static final String MESSAGE = "There is an error when trying to convert the value";

    private StringParameterConverter() {
    }

    static QueryValue<String> get(QueryParser.String_parameterContext context) {

        if (Objects.nonNull(context.string())) {
            return StringQueryValue.of(context.string());
        }

        if (Objects.nonNull(context.parameter())) {
            return DefaultQueryValue.of(context.parameter());
        }

        throw new QueryException(MESSAGE);
    }

}
