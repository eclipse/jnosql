/*
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
 */
package org.eclipse.jnosql.mapping.reflection;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Objects;

/**
 * This class has information about {@link java.lang.reflect.Constructor}
 */
public final class ConstructorMetadata {

    private final Constructor<?> constructor;
    private final List<ParameterMetaData> parameters;

    ConstructorMetadata(Constructor<?> constructor, List<ParameterMetaData> parameters) {
        this.constructor = constructor;
        this.parameters = parameters;
    }

    public List<ParameterMetaData> parameters() {
        return parameters;
    }

    public Constructor<?> constructor() {
        return constructor;
    }

    public boolean isDefault(){
        return parameters.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConstructorMetadata that = (ConstructorMetadata) o;
        return Objects.equals(constructor, that.constructor);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(constructor);
    }

    @Override
    public String toString() {
        return "ConstructorMetadata{" +
                "constructor=" + constructor +
                ", parameters=" + parameters +
                '}';
    }
}
