/*
 *
 *  Copyright (c) 2023 Contributors to the Eclipse Foundation
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

import org.eclipse.jnosql.communication.TypeSupplier;
import org.eclipse.jnosql.communication.Value;

import java.util.Objects;

/**
 * Default implementation of {@link Column}
 */
final class DefaultColumn implements Column {

    private final String name;

    private final Value value;

    DefaultColumn(String name, Value value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Value value() {
        return value;
    }


    @Override
    public <T> T get(Class<T> type) {
        Objects.requireNonNull(type, "type is required");
        return value.get(type);
    }

    @Override
    public <T> T get(TypeSupplier<T> supplier) {
        Objects.requireNonNull(supplier, "supplier is required");
        return value.get(supplier);
    }

    @Override
    public Object get() {
        return value.get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Column)) {
            return false;
        }

        Column that = (Column) o;
        return Objects.equals(name, that.name()) &&
                Objects.equals(value, that.value());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }

    @Override
    public String toString() {
        return "Column{" + "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
