/*
 *
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
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

import org.eclipse.jnosql.communication.Entry;
import org.eclipse.jnosql.communication.TypeSupplier;
import org.eclipse.jnosql.communication.Value;

import java.util.Objects;

/**
 * The default implementation of {@link Column}
 */
public final class Column implements Entry {

    private final String name;

    private final Value value;

    Column(String name, Value value) {
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

    /**
     * Alias to {@link Value#get(Class)}
     *
     * @param type the type class
     * @param <T>  the instance type
     * @return {@link Value#get(Class)}
     * @throws NullPointerException          see {@link Value#get(Class)}
     * @throws UnsupportedOperationException see {@link Value#get(Class)}
     */
    public <T> T get(Class<T> type) {
        Objects.requireNonNull(type, "type is required");
        return value.get(type);
    }

    /**
     * Alias to {@link Value#get(TypeSupplier)}
     *
     * @param supplier {@link Value#get(Class)}
     * @param <T>      {@link Value#get(Class)}
     * @return {@link Value#get(TypeSupplier)}
     * @throws NullPointerException          see {@link Value#get(Class)}
     * @throws UnsupportedOperationException see {@link Value#get(Class)}
     */
    public <T> T get(TypeSupplier<T> supplier) {
        Objects.requireNonNull(supplier, "supplier is required");
        return value.get(supplier);
    }

    /**
     * Alias to {@link Value#get()}
     *
     * @return {@link Value#get()}
     */
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
        return "DefaultColumn{" + "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }

    /**
     * Creates a column instance
     *
     * @param name  - column's name
     * @param value - column's value
     * @param <V>   the value type
     * @return a column instance
     * @throws NullPointerException when there is any null parameter
     * @see Columns
     */
    public static <V> Column of(String name, V value) {
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(value, "value is required");
        return new Column(name, getValue(value));
    }

    private static Value getValue(Object value) {
        if (value instanceof Value) {
            return Value.class.cast(value);
        } else {
            return Value.of(value);
        }
    }
}
