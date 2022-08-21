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
package org.eclipse.jnosql.mapping.column;

import jakarta.nosql.TypeReference;
import jakarta.nosql.column.Column;
import jakarta.nosql.mapping.AttributeConverter;
import org.eclipse.jnosql.mapping.reflection.ParameterMetaData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

enum ParameterConverter {

    DEFAULT {
        @Override
        void convert(AbstractColumnEntityConverter converter,
                     Column column,
                     ParameterMetaData parameterMetaData,
                     ConstructorBuilder builder) {

            parameterMetaData.getConverter().ifPresentOrElse(c -> {
                Object value = converter.getConverters().get(c).convertToEntityAttribute(column.get());
                builder.add(value);
            }, () -> builder.add(column.get()));

        }
    }, ENTITY {
        @Override
        void convert(AbstractColumnEntityConverter converter, Column column, ParameterMetaData parameterMetaData,
                     ConstructorBuilder builder) {

            Object value = column.get();
            if (value instanceof Map) {
                Map<?, ?> map = (Map) value;
                List<Column> columns = new ArrayList<>();

                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    columns.add(Column.of(entry.getKey().toString(), entry.getValue()));
                }

                Object entity = converter.toEntity(parameterMetaData.getType(), columns);
                builder.add(entity);

            } else {
                List<Column> columns = column.get(new TypeReference<List<Column>>() {
                });
                Object entity = converter.toEntity(parameterMetaData.getType(), columns);
                builder.add(entity);
            }
        }
    };

    abstract void convert(AbstractColumnEntityConverter converter,
                          Column column, ParameterMetaData parameterMetaData,
                          ConstructorBuilder builder);

    static ParameterConverter of(ParameterMetaData parameter) {
        switch (parameter.getParamType()) {
            case ENTITY:
                return ENTITY;
            default:
                return DEFAULT;
        }
    }
}
