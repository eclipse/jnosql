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
package org.eclipse.jnosql.mapping.column;

import org.eclipse.jnosql.communication.TypeReference;
import org.eclipse.jnosql.communication.column.Column;
import org.eclipse.jnosql.mapping.metadata.ConstructorBuilder;
import org.eclipse.jnosql.mapping.metadata.GenericParameterMetaData;
import org.eclipse.jnosql.mapping.metadata.ParameterMetaData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

enum ParameterConverter {
    DEFAULT {
        @Override
        void convert(ColumnEntityConverter converter,
                     Column column,
                     ParameterMetaData metaData,
                     ConstructorBuilder builder) {

            metaData.converter().ifPresentOrElse(c -> {
                Object value = converter.getConverters().get(c).convertToEntityAttribute(column.get());
                builder.add(value);
            }, () -> builder.add(column.get(metaData.type())));

        }
    }, ENTITY {
        @Override
        void convert(ColumnEntityConverter converter, Column column, ParameterMetaData metaData,
                     ConstructorBuilder builder) {

            Object value = column.get();
            if (value instanceof Map<?, ?> map) {
                List<Column> columns = new ArrayList<>();

                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    columns.add(Column.of(entry.getKey().toString(), entry.getValue()));
                }

                Object entity = converter.toEntity(metaData.type(), columns);
                builder.add(entity);

            } else {
                List<Column> columns = column.get(new TypeReference<>() {});
                Object entity = converter.toEntity(metaData.type(), columns);
                builder.add(entity);
            }
        }
    }, COLLECTION {
        @Override
        void convert(ColumnEntityConverter converter, Column column, ParameterMetaData metaData,
                     ConstructorBuilder builder) {

            GenericParameterMetaData genericParameter = (GenericParameterMetaData) metaData;
            Collection elements = genericParameter.collectionInstance();
            List<List<Column>> embeddable = (List<List<Column>>) column.get();
            for (List<Column> columnList : embeddable) {
                Object element = converter.toEntity(genericParameter.elementType(), columnList);
                elements.add(element);
            }
            builder.add(elements);

        }

    };

    abstract void convert(ColumnEntityConverter converter,
                          Column column, ParameterMetaData metaData,
                          ConstructorBuilder builder);

    static ParameterConverter of(ParameterMetaData parameter) {
        return switch (parameter.paramType()) {
            case COLLECTION -> COLLECTION;
            case ENTITY -> ENTITY;
            default -> DEFAULT;
        };
    }
}
