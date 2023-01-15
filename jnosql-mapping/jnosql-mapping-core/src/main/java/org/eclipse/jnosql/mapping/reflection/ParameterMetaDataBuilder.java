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

import jakarta.nosql.Column;
import jakarta.nosql.Id;
import org.eclipse.jnosql.mapping.Convert;

import java.lang.reflect.Parameter;
import java.util.Optional;

class ParameterMetaDataBuilder {

    private final Parameter parameter;


    private ParameterMetaDataBuilder(Parameter parameter) {
        this.parameter = parameter;
    }

    ParameterMetaData build() {
        Id id = parameter.getAnnotation(Id.class);
        Column column = parameter.getAnnotation(Column.class);
        Convert convert = parameter.getAnnotation(Convert.class);
        Class<?> type = parameter.getType();
        String name = Optional.ofNullable(id)
                .map(Id::value)
                .orElseGet(() -> column.value());
        MappingType mappingType = MappingType.of(parameter);
        switch (mappingType) {
            case COLLECTION:
            case MAP:
                return new GenericParameterMetaData(name, type,
                        id != null,
                        Optional.ofNullable(convert).map(Convert::value).orElse(null),
                        mappingType, parameter::getParameterizedType);
            default:
                return new DefaultParameterMetaData(name, type,
                        id != null,
                        Optional.ofNullable(convert).map(Convert::value).orElse(null),
                        mappingType);
        }

    }

    public static ParameterMetaData of(Parameter parameter) {
        ParameterMetaDataBuilder builder = new ParameterMetaDataBuilder(parameter);
        return builder.build();
    }
}
