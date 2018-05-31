/*
 *
 *  Copyright (c) 2017 Otávio Santana and others
 *   All rights reserved. This program and the accompanying materials
 *   are made available under the terms of the Eclipse Public License v1.0
 *   and Apache License v2.0 which accompanies this distribution.
 *   The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *   and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *
 *   You may elect to redistribute this code under either of these licenses.
 *
 *   Contributors:
 *
 *   Otavio Santana
 *
 */
package org.jnosql.diana.api.document.query;

import org.jnosql.query.JSONValue;
import org.jnosql.query.QueryException;
import org.jnosql.query.Value;
import org.jnosql.query.ValueType;

final class Values {

    private Values() {
    }

    static Object get(Value<?> value) {

        ValueType type = value.getType();
        switch (type) {
            case NUMBER:
            case STRING:
                return value.get();
            case CONDITION:
            case CONVERT:
            case PARAMETER:
            case ARRAY:
            case FUNCTION:
            case JSON:
            default:
                throw new QueryException("There is not suppor to the value: " + type);

        }
    }
}
