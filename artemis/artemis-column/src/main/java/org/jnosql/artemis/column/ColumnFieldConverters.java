/*
 *  Copyright (c) 2017 Otávio Santana and others
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
package org.jnosql.artemis.column;

import jakarta.nosql.TypeReference;
import jakarta.nosql.Value;
import jakarta.nosql.column.Column;
import jakarta.nosql.mapping.AttributeConverter;
import jakarta.nosql.mapping.reflection.FieldMapping;
import org.jnosql.artemis.reflection.GenericFieldMapping;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import static jakarta.nosql.mapping.reflection.FieldType.COLLECTION;
import static jakarta.nosql.mapping.reflection.FieldType.EMBEDDED;
import static jakarta.nosql.mapping.reflection.FieldType.EMBEDDED_ENTITY;

class ColumnFieldConverters {

    static class ColumnFieldConverterFactory {

        private final EmbeddedFieldConverter embeddedFieldConverter = new EmbeddedFieldConverter();
        private final DefaultConverter defaultConverter = new DefaultConverter();
        private final CollectionEmbeddableConverter embeddableConverter = new CollectionEmbeddableConverter();
        private final SubEntityConverter subEntityConverter = new SubEntityConverter();

        ColumnFieldConverter get(FieldMapping field) {
            if (EMBEDDED.equals(field.getType())) {
                return embeddedFieldConverter;
            } else if (EMBEDDED_ENTITY.equals(field.getType())) {
                return subEntityConverter;
            } else if (isCollectionEmbeddable(field)) {
                return embeddableConverter;
            } else {
                return defaultConverter;
            }
        }

        private boolean isCollectionEmbeddable(FieldMapping field) {
            return COLLECTION.equals(field.getType()) && ((GenericFieldMapping) field).isEmbeddable();
        }
    }


    private static class SubEntityConverter implements ColumnFieldConverter {

        @Override
        public <T> void convert(T instance, List<Column> columns, Optional<Column> column, FieldMapping field,
                                AbstractColumnEntityConverter converter) {

            if (column.isPresent()) {
                Column subColumn = column.get();
                Object value = subColumn.get();
                if (value instanceof Map) {
                    Map map = (Map) value;
                    List<Column> embeddedColumns = new ArrayList<>();

                    for (Map.Entry entry : (Set<Map.Entry>) map.entrySet()) {
                        embeddedColumns.add(Column.of(entry.getKey().toString(), entry.getValue()));
                    }
                    field.write(instance, converter.toEntity(field.getNativeField().getType(), embeddedColumns));

                } else {
                    field.write(instance, converter.toEntity(field.getNativeField().getType(),
                            subColumn.get(new TypeReference<List<Column>>() {
                            })));
                }

            } else {
                field.write(instance, converter.toEntity(field.getNativeField().getType(), columns));
            }
        }
    }

    private static class EmbeddedFieldConverter implements ColumnFieldConverter {


        @Override
        public <T> void convert(T instance, List<Column> columns, Optional<Column> column,
                                FieldMapping field, AbstractColumnEntityConverter converter) {


            Field nativeField = field.getNativeField();
            Object subEntity = converter.toEntity(nativeField.getType(), columns);
            field.write(instance, subEntity);

        }
    }


    private static class DefaultConverter implements ColumnFieldConverter {


        @Override
        public <T> void convert(T instance, List<Column> columns, Optional<Column> column,
                                FieldMapping field, AbstractColumnEntityConverter converter) {
            Value value = column.get().getValue();
            Optional<Class<? extends AttributeConverter>> optionalConverter = field.getConverter();
            if (optionalConverter.isPresent()) {

                AttributeConverter attributeConverter = converter.getConverters().get(optionalConverter.get());
                Object attributeConverted = attributeConverter.convertToEntityAttribute(value.get());
                field.write(instance, field.getValue(Value.of(attributeConverted)));
            } else {
                field.write(instance, field.getValue(value));
            }
        }
    }


    private static class CollectionEmbeddableConverter implements ColumnFieldConverter {

        @Override
        public <T> void convert(T instance, List<Column> columns, Optional<Column> column, FieldMapping field,
                                AbstractColumnEntityConverter converter) {

            column.ifPresent(convertColumn(instance, field, converter));
        }

        private <T> Consumer<Column> convertColumn(T instance, FieldMapping field, AbstractColumnEntityConverter converter) {
            return column -> {
                GenericFieldMapping genericField = (GenericFieldMapping) field;
                Collection collection = genericField.getCollectionInstance();
                List<List<Column>> embeddable = (List<List<Column>>) column.get();
                for (List<Column> columnList : embeddable) {
                    Object element = converter.toEntity(genericField.getElementType(), columnList);
                    collection.add(element);
                }
                field.write(instance, collection);
            };
        }
    }
}
