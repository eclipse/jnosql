/*
 *
 *  Copyright (c) 2019 Otávio Santana and others
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
package org.eclipse.jnosql.communication.column;

import jakarta.nosql.Value;
import jakarta.nosql.column.Column;
import jakarta.nosql.column.Column.ColumnProvider;

import java.util.Objects;

/**
 * The default {@link ColumnProvider}
 */
public final class DefaultColumnProvider implements ColumnProvider {

    @Override
    public Column apply(String name, Object value) {
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(value, "value is required");
        return new DefaultColumn(name, getValue(value));
    }

    private Value getValue(Object value) {
        if (value instanceof Value) {
            return Value.class.cast(value);
        } else {
            return Value.of(value);
        }
    }
}
