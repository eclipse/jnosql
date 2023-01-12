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

package org.eclipse.jnosql.communication.document;


import org.eclipse.jnosql.communication.Entry;
import org.eclipse.jnosql.communication.TypeSupplier;
import org.eclipse.jnosql.communication.Value;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * A Document is a tuple (pair) that consists of the name and its respective value.
 * A {@link DocumentEntity} has one or more Documents.
 */
public final class Document implements Entry {

    private final String name;

    private final Value value;

    Document(String name, Value value) {
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
     * @param type {@link Value#get(Class)}
     * @param <T>   {@link Value#get(Class)}
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
     * @param supplier {@link Value#get(TypeSupplier)}
     * @param <T>          {@link Value#get(TypeSupplier)}
     * @return {@link Value#get(TypeSupplier)}
     * @throws NullPointerException          see {@link Value#get(TypeSupplier)}
     * @throws UnsupportedOperationException see {@link Value#get(TypeSupplier)}
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
        if (!(o instanceof Document)) {
            return false;
        }
        Document that = (Document) o;
        return Objects.equals(name, that.name()) &&
                Objects.equals(value, that.value());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }

    @Override
    public String toString() {
        return "Document{" + "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }

    /**
     * Creates a document instance
     *
     * @param name  - document's name
     * @param value - document's value
     * @param <V>   the value type
     * @return a document instance
     * @throws NullPointerException when there is any null parameter
     * @see Documents
     */
    public static <V> Document of(String name, V value) {
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(value, "value is required");
        return new Document(name, getValue(value));
    }

    private static Value getValue(Object value) {
        if (value instanceof Value) {
            return Value.class.cast(value);
        } else {
            return Value.of(value);
        }
    }
}
