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
import org.eclipse.jnosql.communication.Value;
import org.eclipse.jnosql.communication.column.Column;
import org.eclipse.jnosql.mapping.AttributeConverter;
import org.eclipse.jnosql.mapping.metadata.EntityMetadata;
import org.eclipse.jnosql.mapping.metadata.FieldMetadata;
import org.eclipse.jnosql.mapping.metadata.GenericFieldMetadata;
import org.eclipse.jnosql.mapping.metadata.MappingType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

enum FieldConverter {
    EMBEDDED {
        @Override
        public <X, Y, T> void convert(T instance, List<Column> columns, Column column,
                                      FieldMetadata field, ColumnEntityConverter converter) {
            Object subEntity = converter.toEntity(field.type(), columns);
            EntityMetadata mapping = converter.getEntities().get(subEntity.getClass());
            boolean areAllFieldsNull = mapping.fields()
                    .stream()
                    .map(f -> f.read(subEntity))
                    .allMatch(Objects::isNull);
            if (!areAllFieldsNull) {
                field.write(instance, subEntity);
            }
        }
    }, ENTITY {
        @Override
        public <X, Y, T> void convert(T instance, List<Column> columns, Column subColumn, FieldMetadata field,
                                      ColumnEntityConverter converter) {

            if (Objects.nonNull(subColumn)) {
                converterSubDocument(instance, subColumn, field, converter);
            } else {
                field.write(instance, converter.toEntity(field.type(), columns));
            }
        }

        private <T> void converterSubDocument(T instance, Column subColumn, FieldMetadata field,
                                              ColumnEntityConverter converter) {
            Object value = subColumn.get();
            if (value instanceof Map map) {
                List<Column> embeddedColumns = new ArrayList<>();

                for (Map.Entry entry : (Set<Map.Entry>) map.entrySet()) {
                    embeddedColumns.add(Column.of(entry.getKey().toString(), entry.getValue()));
                }
                field.write(instance, converter.toEntity(field.type(), embeddedColumns));

            } else {
                field.write(instance, converter.toEntity(field.type(),
                        subColumn.get(new TypeReference<List<Column>>() {
                        })));
            }
        }
    }, COLLECTION {
        @Override
        public <X, Y, T> void convert(T instance, List<Column> columns, Column column, FieldMetadata field,
                                      ColumnEntityConverter converter) {

            if (Objects.nonNull(column)) {
                GenericFieldMetadata genericField = (GenericFieldMetadata) field;
                Collection elements = genericField.collectionInstance();
                List<List<Column>> embeddable = (List<List<Column>>) column.get();
                for (List<Column> columnList : embeddable) {
                    Object element = converter.toEntity(genericField.elementType(), columnList);
                    elements.add(element);
                }
                field.write(instance, elements);
            }
        }
    }, DEFAULT{
        @SuppressWarnings("unchecked")
        @Override
        public <X, Y, T> void convert(T instance, List<Column> columns, Column column,
                                      FieldMetadata field, ColumnEntityConverter converter) {
            if (Objects.nonNull(column)) {
                Value value = column.value();
                Optional<Class<? extends AttributeConverter<X, Y>>> optionalConverter = field.converter();
                if (optionalConverter.isPresent()) {
                    AttributeConverter<X, Y> attributeConverter = converter.getConverters().get(field);
                    Y attr = (Y)(value.isInstanceOf(List.class) ? column : value.get());
                    Object attributeConverted = attributeConverter.convertToEntityAttribute(attr);
                    field.write(instance, field.value(Value.of(attributeConverted)));
                } else {
                    field.write(instance, field.value(value));
                }
            }
        }
    };

    static FieldConverter get(FieldMetadata field) {
        if (MappingType.EMBEDDED.equals(field.mappingType())) {
            return EMBEDDED;
        } else if (MappingType.ENTITY.equals(field.mappingType())) {
            return ENTITY;
        } else if (isCollectionEmbeddable(field)) {
            return COLLECTION;
        } else {
            return DEFAULT;
        }
    }

    private static boolean isCollectionEmbeddable(FieldMetadata field) {
        return MappingType.COLLECTION.equals(field.mappingType()) && ((GenericFieldMetadata) field).isEmbeddable();
    }

    abstract <X, Y, T> void convert(T instance, List<Column> columns, Column column, FieldMetadata field,
                                    ColumnEntityConverter converter);

    <X, Y, T> void convert(T instance, Column column, FieldMetadata field,
                           ColumnEntityConverter converter) {
        convert(instance, null, column, field, converter);
    }
}
