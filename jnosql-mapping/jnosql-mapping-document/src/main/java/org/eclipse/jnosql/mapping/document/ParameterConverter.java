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
package org.eclipse.jnosql.mapping.document;

import org.eclipse.jnosql.communication.TypeReference;
import org.eclipse.jnosql.communication.document.Document;
import org.eclipse.jnosql.mapping.metadata.ConstructorBuilder;
import org.eclipse.jnosql.mapping.reflection.GenericParameterMetaData;
import org.eclipse.jnosql.mapping.metadata.ParameterMetaData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

enum ParameterConverter {
    DEFAULT {
        @Override
        void convert(DocumentEntityConverter converter,
                     Document document,
                     ParameterMetaData metaData,
                     ConstructorBuilder builder) {

            metaData.converter().ifPresentOrElse(c -> {
                Object value = converter.getConverters().get(c).convertToEntityAttribute(document.get());
                builder.add(value);
            }, () -> builder.add(document.get(metaData.type())));

        }
    }, ENTITY {
        @Override
        void convert(DocumentEntityConverter converter, Document document, ParameterMetaData metaData,
                     ConstructorBuilder builder) {

            Object value = document.get();
            if (value instanceof Map<?, ?> map) {
                List<Document> documents = new ArrayList<>();

                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    documents.add(Document.of(entry.getKey().toString(), entry.getValue()));
                }

                Object entity = converter.toEntity(metaData.type(), documents);
                builder.add(entity);

            } else {
                List<Document> documents = document.get(new TypeReference<>() {
                });
                Object entity = converter.toEntity(metaData.type(), documents);
                builder.add(entity);
            }
        }
    }, COLLECTION {
        @Override
        void convert(DocumentEntityConverter converter, Document document, ParameterMetaData metaData,
                     ConstructorBuilder builder) {

            GenericParameterMetaData genericParameter = (GenericParameterMetaData) metaData;
            Collection elements = genericParameter.collectionInstance();
            List<List<Document>> embeddable = (List<List<Document>>) document.get();
            for (List<Document> columnList : embeddable) {
                Object element = converter.toEntity(genericParameter.getElementType(), columnList);
                elements.add(element);
            }
            builder.add(elements);

        }

    };

    abstract void convert(DocumentEntityConverter converter,
                          Document document, ParameterMetaData metaData,
                          ConstructorBuilder builder);

    static ParameterConverter of(ParameterMetaData parameter) {
        return switch (parameter.paramType()) {
            case COLLECTION -> COLLECTION;
            case ENTITY -> ENTITY;
            default -> DEFAULT;
        };
    }
}
