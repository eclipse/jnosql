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
package org.eclipse.jnosql.communication;

import jakarta.nosql.TypeReferenceReader;
import jakarta.nosql.TypeReferenceReaderDecorator;
import jakarta.nosql.TypeSupplier;
import jakarta.nosql.Value;
import jakarta.nosql.ValueReader;
import jakarta.nosql.ValueReaderDecorator;

import java.util.Objects;

/**
 * Defines the default implementation of {@link Value}
 */
final class DefaultValue  implements Value {

    private static final transient ValueReader SERVICE_PROVIDER = ValueReaderDecorator.getInstance();

    private static final transient TypeReferenceReader REFERENCE_READER = TypeReferenceReaderDecorator.getInstance();

    private final Object value;

    DefaultValue(Object value) {
        this.value = value;
    }


    @Override
    public Object get() {
        return value;
    }

    @Override
    public <T> T get(Class<T> clazz) {
        return SERVICE_PROVIDER.read(clazz, value);
    }

    @Override
    public <T> T get(TypeSupplier<T> typeReference) {
        if (REFERENCE_READER.test(Objects.requireNonNull(typeReference, "typeReference is required"))) {
            return REFERENCE_READER.convert(typeReference, value);
        }
        throw new UnsupportedOperationException("The type " + typeReference + " is not supported");
    }

    @Override
    public boolean isInstanceOf(Class<?> typeClass) {
        Objects.requireNonNull(typeClass, "typeClass is required");
        return typeClass.isInstance(value);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Value)) {
            return false;
        }
        Value that = (Value) o;
        return Objects.equals(value, that.get());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return "DefaultValue{" + "value=" + value +
                '}';
    }
}
