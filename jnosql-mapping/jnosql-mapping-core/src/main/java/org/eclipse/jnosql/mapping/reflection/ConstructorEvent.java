/*
 *  Copyright (c) 2022 Otávio Santana and others
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
 */
package org.eclipse.jnosql.mapping.reflection;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Objects;

/**
 * Defines an event to be fired when the engine creates an entity by a constructor.
 * This is useful for the bean validation proposals.
 */
public final class ConstructorEvent {

    private final Constructor<?> constructor;

    private final Object[] params;

    private ConstructorEvent(Constructor<?> constructor, Object[] params) {
        this.constructor = constructor;
        this.params = params;
    }

    /**
     * @return the constructor
     */
    public Constructor<?> getConstructor() {
        return constructor;
    }

    /**
     * @return the param to the respective constructor
     */
    public Object[] getParams() {
        return params;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConstructorEvent that = (ConstructorEvent) o;
        return Objects.equals(constructor, that.constructor)
                && Arrays.equals(params, that.params);
    }

    @Override
    public int hashCode() {
        return 31 * Objects.hashCode(constructor) + Arrays.hashCode(params);
    }

    @Override
    public String toString() {
        return "ConstructorEvent{" +
                "constructor=" + constructor +
                ", params=" + Arrays.toString(params) +
                '}';
    }

    public static ConstructorEvent of(Constructor<?> constructor, Object[] params) {
        Objects.requireNonNull(constructor, "constructor is required");
        Objects.requireNonNull(params, "params is required");
        return new ConstructorEvent(constructor, params);
    }
}
