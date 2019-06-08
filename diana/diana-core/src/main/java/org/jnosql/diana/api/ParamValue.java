/*
 *  Copyright (c) 2019 Otávio Santana and others
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */
package org.jnosql.diana.api;

import java.util.Objects;

/**
 * A Value that allows to set value instead of be immutable. This Value will use at the Dynamic query.
 */
final class ParamValue implements Value {

    private final String name;

    private Object value;

    ParamValue(String name) {
        this.name = name;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public Object get() {
        validValue();
        return value;
    }

    public String getName() {
        return name;
    }

    @Override
    public <T> T get(Class<T> clazz) {
        validValue();
        return Value.of(value).get(clazz);
    }

    @Override
    public <T> T get(TypeSupplier<T> typeSupplier) {
        validValue();
        return Value.of(value).get(typeSupplier);
    }

    @Override
    public boolean isInstanceOf(Class<?> typeClass) {
        Objects.requireNonNull(typeClass, "typeClass is required");

        if (value == null) {
            return true;
        }
        return typeClass.isInstance(value);
    }

    public boolean isEmpty() {
        return Objects.isNull(value);
    }

    private void validValue() {
        if (Objects.isNull(value)) {
            throw new QueryException(String.format("The parameter %s is not defined", name));
        }
    }

    @Override
    public String toString() {
        if (Objects.isNull(value)) {
            return name + "= ?";
        }
        return name + "= " + value;
    }
}
