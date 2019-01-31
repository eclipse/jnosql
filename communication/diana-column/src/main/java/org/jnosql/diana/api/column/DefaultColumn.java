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

package org.jnosql.diana.api.column;

import org.jnosql.diana.api.TypeSupplier;
import org.jnosql.diana.api.Value;

import java.util.Objects;


final class DefaultColumn implements Column {

    private final String name;

    private final Value value;

    DefaultColumn(String name, Value value) {
        this.name = Objects.requireNonNull(name, "name is required");
        this.value = Objects.requireNonNull(value, "value is required");
    }


    public String getName() {
        return name;
    }

    public Value getValue() {
        return value;
    }

    @Override
    public <T> T get(Class<T> clazz) {
        return value.get(clazz);
    }

    @Override
    public <T> T get(TypeSupplier<T> typeSupplier) {
        return value.get(typeSupplier);
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
        return Objects.equals(name, that.getName()) &&
                Objects.equals(value, that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DefaultColumn{");
        sb.append("name='").append(name).append('\'');
        sb.append(", value=").append(value);
        sb.append('}');
        return sb.toString();
    }
}
