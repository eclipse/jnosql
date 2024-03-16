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
import org.eclipse.jnosql.communication.semistructured.Element;
import org.eclipse.jnosql.mapping.metadata.ConstructorBuilder;
import org.eclipse.jnosql.mapping.metadata.EntitiesMetadata;
import org.eclipse.jnosql.mapping.metadata.GenericParameterMetaData;
import org.eclipse.jnosql.mapping.metadata.ParameterMetaData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

enum ParameterConverter {
    DEFAULT {
        @Override
        void convert(EntityConverter converter,
                     Element element,
                     ParameterMetaData metaData,
                     ConstructorBuilder builder) {

            metaData.converter().ifPresentOrElse(c -> {
                Object value = converter.converters().get(metaData).convertToEntityAttribute(element.get());
                builder.add(value);
            }, () -> builder.add(element.get(metaData.type())));

        }
    }, ENTITY {
        @Override
        void convert(EntityConverter converter, Element element, ParameterMetaData metaData,
                     ConstructorBuilder builder) {

            Object value = element.get();
            if (value instanceof Map<?, ?> map) {
                List<Element> elements = new ArrayList<>();

                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    elements.add(Element.of(entry.getKey().toString(), entry.getValue()));
                }

                Object entity = converter.toEntity(metaData.type(), elements);
                builder.add(entity);

            } else {
                List<Element> columns = element.get(new TypeReference<>() {});
                Object entity = converter.toEntity(metaData.type(), columns);
                builder.add(entity);
            }
        }
    }, COLLECTION {
        @SuppressWarnings("unchecked")
        @Override
        void convert(EntityConverter converter, Element element, ParameterMetaData metaData,
                     ConstructorBuilder builder) {

            GenericParameterMetaData genericParameter = (GenericParameterMetaData) metaData;
            Collection elements = genericParameter.collectionInstance();
            List<List<Element>> embeddable = (List<List<Element>>) element.get();
            for (List<Element> elementsList : embeddable) {
                Object item = converter.toEntity(genericParameter.elementType(), elementsList);
                elements.add(item);
            }
            builder.add(elements);

        }

    };

    abstract void convert(EntityConverter converter,
                          Element element, ParameterMetaData metaData,
                          ConstructorBuilder builder);

    static ParameterConverter of(ParameterMetaData parameter, EntitiesMetadata entities) {
        return switch (parameter.mappingType()) {
            case COLLECTION -> validateCollection(parameter, entities);
            case ENTITY, EMBEDDED_GROUP -> ENTITY;
            default -> DEFAULT;
        };
    }

    private static ParameterConverter validateCollection(ParameterMetaData parameter, EntitiesMetadata entities) {
        GenericParameterMetaData genericParameter = (GenericParameterMetaData) parameter;
        Class<?> type = genericParameter.elementType();
        if (entities.findByClassName(type.getName()).isPresent()) {
            return COLLECTION;
        }
        return DEFAULT;
    }
}
