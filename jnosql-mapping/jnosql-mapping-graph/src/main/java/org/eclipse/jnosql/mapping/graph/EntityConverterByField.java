/*
 *  Copyright (c) 2023 Contributors to the Eclipse Foundation
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
package org.eclipse.jnosql.mapping.graph;

import org.apache.tinkerpop.gremlin.structure.Property;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.eclipse.jnosql.communication.Value;
import org.eclipse.jnosql.mapping.AttributeConverter;
import org.eclipse.jnosql.mapping.Converters;
import org.eclipse.jnosql.mapping.reflection.ConstructorMetadata;
import org.eclipse.jnosql.mapping.reflection.EntitiesMetadata;
import org.eclipse.jnosql.mapping.reflection.EntityMetadata;
import org.eclipse.jnosql.mapping.reflection.FieldMetadata;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.eclipse.jnosql.mapping.reflection.MappingType.EMBEDDED;

final class EntityConverterByField<T> implements Supplier<T> {
    private final List<Property<?>> elements;
    private final EntityMetadata mapping;
    private final T instance;
    private final Vertex vertex;

    private final Converters converters;

    private final EntitiesMetadata entities;


    private EntityConverterByField(List<Property<?>> elements, EntityMetadata mapping,
                                   T instance, Vertex vertex, Converters converters,
                                   EntitiesMetadata entities) {
        this.elements = elements;
        this.mapping = mapping;
        this.instance = instance;
        this.vertex = vertex;
        this.converters = converters;
        this.entities = entities;
    }

    @Override
    public T get() {
        Map<String, FieldMetadata> fieldsGroupByName = mapping.fieldsGroupByName();
        List<String> names = elements.stream()
                .map(Property::key)
                .sorted()
                .toList();
        Predicate<String> existField = k -> Collections.binarySearch(names, k) >= 0;

        fieldsGroupByName.keySet().stream()
                .filter(existField.or(k -> EMBEDDED.equals(fieldsGroupByName.get(k).type())))
                .forEach(feedObject(instance, elements, fieldsGroupByName, vertex));

        feedId(vertex, instance);
        return instance;
    }

    private Consumer<String> feedObject(T instance, List<Property<?>> elements,
                                            Map<String, FieldMetadata> fieldsGroupByName,
                                            Vertex vertex) {
        return k -> {
            Optional<Property<?>> element = elements
                    .stream()
                    .filter(c -> c.key().equals(k))
                    .findFirst();

            FieldMetadata field = fieldsGroupByName.get(k);
            if (EMBEDDED.equals(field.type())) {
                embeddedField(instance, elements, field, vertex);
            } else {
                element.ifPresent(e -> singleField(instance, e, field));
            }
        };
    }

    private void embeddedField(T instance, List<Property<?>> elements,
                               FieldMetadata field, Vertex vertex) {
        Class<T> type = (Class<T>) field.nativeField().getType();
        field.write(instance, convert(type, elements, vertex));
    }

    private <X, Y> void singleField(T instance, Property<?> element, FieldMetadata field) {
        Object value = element.value();
        Optional<Class<? extends AttributeConverter<X, Y>>> converter = field.converter();
        if (converter.isPresent()) {
            AttributeConverter<X, Y> attributeConverter = converters().get(converter.get());
            Object attributeConverted = attributeConverter.convertToEntityAttribute((Y) value);
            field.write(instance, field.value(Value.of(attributeConverted)));
        } else {
            field.write(instance, field.value(Value.of(value)));
        }
    }

    private T convert(Class<T> type, List<Property<?>> properties, Vertex vertex) {
        EntityMetadata mapping = entities().get(type);
        ConstructorMetadata constructor = mapping.constructor();
        if (constructor.isDefault()) {
            T instance = mapping.newInstance();
            T entity = convertEntity(properties, mapping, instance, vertex);
            feedId(vertex, entity);
            return entity;
        } else {
            EntityConverterByContructor<T> supplier = EntityConverterByContructor.of(mapping, vertex,
                    converters());
            return supplier.get();
        }
    }

    private void feedId(Vertex vertex, T entity) {
        EntityMetadata mapping = entities.get(entity.getClass());
        Optional<FieldMetadata> id = mapping.id();


        Object vertexId = vertex.id();
        if (Objects.nonNull(vertexId) && id.isPresent()) {
            FieldMetadata fieldMetadata = id.get();
            fieldMetadata.converter().ifPresentOrElse(c -> {
                AttributeConverter attributeConverter = converters.get(c);
                Object attributeConverted = attributeConverter.convertToEntityAttribute(vertexId);
                fieldMetadata.write(entity, fieldMetadata.value(Value.of(attributeConverted)));
            }, () -> fieldMetadata.write(entity, fieldMetadata.value(Value.of(vertexId))));
        }
    }

    private T convertEntity(List<Property<?>> elements, EntityMetadata mapping, T instance, Vertex vertex) {

        Map<String, FieldMetadata> fieldsGroupByName = mapping.fieldsGroupByName();
        List<String> names = elements.stream()
                .map(Property::key)
                .sorted()
                .toList();
        Predicate<String> existField = k -> Collections.binarySearch(names, k) >= 0;

        fieldsGroupByName.keySet().stream()
                .filter(existField.or(k -> EMBEDDED.equals(fieldsGroupByName.get(k).type())))
                .forEach(feedObject(instance, elements, fieldsGroupByName, vertex));

        return instance;
    }

    private Converters converters() {
        return converters;
    }

    private EntitiesMetadata entities() {
        return entities;
    }

    static <T> EntityConverterByField<T> of(List<Property<?>> elements, EntityMetadata mapping,
                                            T instance, Vertex vertex, Converters converters,
                                            EntitiesMetadata entities){
        return new EntityConverterByField<>(elements, mapping, instance, vertex, converters, entities);
    }

}
