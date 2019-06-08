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

package org.jnosql.query;

import java.util.List;

/**
 * The Value type that has a list of values, it will be used when the condition is composed such as
 * and ({@link Operator#AND}), or ({@link Operator#OR}) and negation ({@link Operator#NOT}).
 */
public interface ConditionValue extends Value<List<Condition>> {

    @Override
    default ValueType getType() {
        return ValueType.CONDITION;
    }
}
