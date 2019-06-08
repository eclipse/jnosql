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

import java.util.function.Supplier;

/**
 * The value is the last element in a condition, and it defines what it 'll go to be used, with an operator, in a field target.
 *
 * @param <T> the type.
 */
public interface Value<T> extends Supplier<T> {


    /**
     * Returns a value type
     *
     * @return a value type
     */
    ValueType getType();
}
