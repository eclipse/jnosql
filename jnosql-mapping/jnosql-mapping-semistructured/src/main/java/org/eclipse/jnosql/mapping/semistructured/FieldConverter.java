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
package org.eclipse.jnosql.mapping.semistructured;

import org.eclipse.jnosql.communication.TypeReference;
import org.eclipse.jnosql.communication.Value;
import org.eclipse.jnosql.communication.semistructured.Element;
import jakarta.nosql.AttributeConverter;
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
        public <X, Y, T> void convert(T instance, List<Element> elements, Element element,
                                      FieldMetadata field, EntityConverter converter) {
            Object subEntity = converter.toEntity(field.type(), elements);
            EntityMetadata mapping = converter.entities().get(subEntity.getClass());
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
        public <X, Y, T> void convert(T instance, List<Element> elements, Element subElement, FieldMetadata field,
                                      EntityConverter converter) {

            if (Objects.nonNull(subElement)) {
                converterSubDocument(instance, subElement, field, converter);
            } else {
                field.write(instance, converter.toEntity(field.type(), elements));
            }
        }

        @SuppressWarnings({"rawtypes", "unchecked"})
        private <T> void converterSubDocument(T instance, Element subElement, FieldMetadata field,
                                              EntityConverter converter) {
            Object value = subElement.get();
            if (value instanceof Map map) {
                List<Element> embeddedColumns = new ArrayList<>();

                for (Map.Entry entry : (Set<Map.Entry>) map.entrySet()) {
                    embeddedColumns.add(Element.of(entry.getKey().toString(), entry.getValue()));
                }
                field.write(instance, converter.toEntity(field.type(), embeddedColumns));

            } else {
                field.write(instance, converter.toEntity(field.type(),
                        subElement.get(new TypeReference<List<Element>>() {
                        })));
            }
        }
    }, COLLECTION {
        @SuppressWarnings("unchecked")
        @Override
        public <X, Y, T> void convert(T instance, List<Element> columns, Element element, FieldMetadata field,
                                      EntityConverter converter) {

            if (Objects.nonNull(element)) {
                GenericFieldMetadata genericField = (GenericFieldMetadata) field;
                Collection elements = genericField.collectionInstance();
                List<List<Element>> embeddable = (List<List<Element>>) element.get();
                if(Objects.isNull(embeddable)) {
                    return;
                }
                for (List<Element> elementList : embeddable) {
                    Object item = converter.toEntity(genericField.elementType(), elementList);
                    elements.add(item);
                }
                field.write(instance, elements);
            }
        }
    }, DEFAULT{
        @SuppressWarnings("unchecked")
        @Override
        public <X, Y, T> void convert(T instance, List<Element> elements, Element element,
                                      FieldMetadata field, EntityConverter converter) {
            if (Objects.nonNull(element)) {
                Value value = element.value();
                Optional<Class<AttributeConverter<Object, Object>>> optionalConverter = field.converter();
                if (optionalConverter.isPresent()) {
                    AttributeConverter<X, Y> attributeConverter = converter.converters().get(field);
                    Y attr = (Y)(value.isInstanceOf(List.class) ? element : value.get());
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
        } else if (MappingType.ENTITY.equals(field.mappingType())|| MappingType.EMBEDDED_GROUP.equals(field.mappingType())) {
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

    abstract <X, Y, T> void convert(T instance, List<Element> elements, Element element, FieldMetadata field,
                                    EntityConverter converter);

    <X, Y, T> void convert(T instance, Element element, FieldMetadata field,
                           EntityConverter converter) {
        convert(instance, null, element, field, converter);
    }
}
