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
package org.eclipse.jnosql.communication;


import java.util.Objects;

/**
 * Defines the default implementation of {@link Value}
 */
final class DefaultValue  implements Value {

    private static final ValueReader SERVICE_PROVIDER = ValueReaderDecorator.getInstance();

    private static final  TypeReferenceReader REFERENCE_READER = TypeReferenceReaderDecorator.getInstance();

    private final Object value;

    DefaultValue(Object value) {
        this.value = value;
    }


    @Override
    public Object get() {
        return value;
    }

    @Override
    public <T> T get(Class<T> type) {
        Objects.requireNonNull(type, "type is required");
        return SERVICE_PROVIDER.read(type, value);
    }

    @Override
    public <T> T get(TypeSupplier<T> supplier) {
        if (REFERENCE_READER.test(Objects.requireNonNull(supplier, "supplier is required"))) {
            return REFERENCE_READER.convert(supplier, value);
        }
        throw new UnsupportedOperationException("The type " + supplier + " is not supported");
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
        if (!(o instanceof Value that)) {
            return false;
        }
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
