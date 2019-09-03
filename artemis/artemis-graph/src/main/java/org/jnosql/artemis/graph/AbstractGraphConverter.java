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
package org.jnosql.artemis.graph;

import jakarta.nosql.Value;
import jakarta.nosql.mapping.AttributeConverter;
import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.EntityNotFoundException;
import jakarta.nosql.mapping.reflection.ClassMapping;
import jakarta.nosql.mapping.reflection.ClassMappings;
import jakarta.nosql.mapping.reflection.FieldMapping;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Property;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static jakarta.nosql.mapping.reflection.FieldType.EMBEDDED;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

abstract class AbstractGraphConverter implements GraphConverter {


    protected abstract ClassMappings getClassMappings();

    protected abstract Converters getConverters();

    protected abstract Graph getGraph();

    @Override
    public <T> Vertex toVertex(T entity) {
        requireNonNull(entity, "entity is required");

        ClassMapping mapping = getClassMappings().get(entity.getClass());
        String label = mapping.getName();

        List<FieldGraph> fields = mapping.getFields().stream()
                .map(f -> to(f, entity))
                .filter(FieldGraph::isNotEmpty).collect(toList());

        Optional<FieldGraph> id = fields.stream().filter(FieldGraph::isId).findFirst();
        final Function<Property, Vertex> findVertexOrCreateWithId = p -> {
            Iterator<Vertex> vertices = getGraph().vertices(p.value());
            return vertices.hasNext() ? vertices.next() :
                    getGraph().addVertex(org.apache.tinkerpop.gremlin.structure.T.label, label,
                            org.apache.tinkerpop.gremlin.structure.T.id, p.value());
        };

        Vertex vertex = id.map(i -> i.toElement(getConverters()))
                .map(findVertexOrCreateWithId)
                .orElseGet(() -> getGraph().addVertex(label));

        fields.stream().filter(FieldGraph::isNotId)
                .flatMap(f -> f.toElements(this, getConverters()).stream())
                .forEach(p -> vertex.property(p.key(), p.value()));

        return vertex;
    }

    @Override
    public <T> List<Property<?>> getProperties(T entity) {
        Objects.requireNonNull(entity, "entity is required");
        ClassMapping mapping = getClassMappings().get(entity.getClass());
        List<FieldGraph> fields = mapping.getFields().stream()
                .map(f -> to(f, entity))
                .filter(FieldGraph::isNotEmpty).collect(toList());

        return fields.stream().filter(FieldGraph::isNotId)
                .flatMap(f -> f.toElements(this, getConverters()).stream())
                .collect(Collectors.toList());
    }

    @Override
    public <T> T toEntity(Vertex vertex) {
        requireNonNull(vertex, "vertex is required");
        ClassMapping mapping = getClassMappings().findByName(vertex.label());

        List<Property> properties = vertex.keys().stream().map(k -> DefaultProperty.of(k, vertex.value(k))).collect(toList());
        T entity = toEntity((Class<T>) mapping.getClassInstance(), properties);
        feedId(vertex, entity);
        return entity;
    }

    @Override
    public <T> T toEntity(Class<T> entityClass, Vertex vertex) {
        requireNonNull(entityClass, "entityClass is required");
        requireNonNull(vertex, "vertex is required");

        List<Property> properties = vertex.keys().stream().map(k -> DefaultProperty.of(k, vertex.value(k))).collect(toList());
        T entity = toEntity(entityClass, properties);
        feedId(vertex, entity);
        return entity;
    }

    @Override
    public <T> T toEntity(T entityInstance, Vertex vertex) {
        requireNonNull(entityInstance, "entityInstance is required");
        requireNonNull(vertex, "vertex is required");

        List<Property> properties = vertex.keys().stream().map(k -> DefaultProperty.of(k, vertex.value(k))).collect(toList());

        ClassMapping mapping = getClassMappings().get(entityInstance.getClass());
        convertEntity(properties, mapping, entityInstance);
        feedId(vertex, entityInstance);
        return entityInstance;

    }

    @Override
    public EdgeEntity toEdgeEntity(Edge edge) {
        requireNonNull(edge, "vertex is required");
        Object out = toEntity(edge.outVertex());
        Object in = toEntity(edge.inVertex());
        return new DefaultEdgeEntity<>(edge, in, out);
    }

    @Override
    public Edge toEdge(EdgeEntity edge) {
        requireNonNull(edge, "vertex is required");
        Object id = edge.getId();
        Iterator<Edge> edges = getGraph().edges(id);
        if (edges.hasNext()) {
            return edges.next();
        }
        throw new EntityNotFoundException("Edge does not found in the database with id: " + id);
    }

    private <T> void feedId(Vertex vertex, T entity) {
        ClassMapping mapping = getClassMappings().get(entity.getClass());
        Optional<FieldMapping> id = mapping.getId();


        Object vertexId = vertex.id();
        if (Objects.nonNull(vertexId) && id.isPresent()) {
            FieldMapping fieldMapping = id.get();

            if (fieldMapping.getConverter().isPresent()) {
                AttributeConverter attributeConverter = getConverters().get(fieldMapping.getConverter().get());
                Object attributeConverted = attributeConverter.convertToEntityAttribute(vertexId);

                fieldMapping.write(entity, fieldMapping.getValue(Value.of(attributeConverted)));
            } else {
                fieldMapping.write(entity, fieldMapping.getValue(Value.of(vertexId)));
            }

        }
    }

    private <T> T toEntity(Class<T> entityClass, List<Property> properties) {
        ClassMapping mapping = getClassMappings().get(entityClass);
        T instance = mapping.newInstance();
        return convertEntity(properties, mapping, instance);
    }

    private <T> T convertEntity(List<Property> elements, ClassMapping mapping, T instance) {

        Map<String, FieldMapping> fieldsGroupByName = mapping.getFieldsGroupByName();
        List<String> names = elements.stream()
                .map(Property::key)
                .sorted()
                .collect(toList());
        Predicate<String> existField = k -> Collections.binarySearch(names, k) >= 0;

        fieldsGroupByName.keySet().stream()
                .filter(existField.or(k -> EMBEDDED.equals(fieldsGroupByName.get(k).getType())))
                .forEach(feedObject(instance, elements, fieldsGroupByName));

        return instance;
    }

    private <T> Consumer<String> feedObject(T instance, List<Property> elements,
                                            Map<String, FieldMapping> fieldsGroupByName) {
        return k -> {
            Optional<Property> element = elements
                    .stream()
                    .filter(c -> c.key().equals(k))
                    .findFirst();

            FieldMapping field = fieldsGroupByName.get(k);
            if (EMBEDDED.equals(field.getType())) {
                setEmbeddedField(instance, elements, field);
            } else {
                setSingleField(instance, element, field);
            }
        };
    }

    private <T> void setSingleField(T instance, Optional<Property> element, FieldMapping field) {
        Object value = element.get().value();
        Optional<Class<? extends AttributeConverter>> converter = field.getConverter();
        if (converter.isPresent()) {
            AttributeConverter attributeConverter = getConverters().get(converter.get());
            Object attributeConverted = attributeConverter.convertToEntityAttribute(value);
            field.write(instance, field.getValue(Value.of(attributeConverted)));
        } else {
            field.write(instance, field.getValue(Value.of(value)));
        }
    }

    private <T> void setEmbeddedField(T instance, List<Property> elements,
                                      FieldMapping field) {
        field.write(instance, toEntity(field.getNativeField().getType(), elements));
    }

    protected FieldGraph to(FieldMapping field, Object entityInstance) {
        Object value = field.read(entityInstance);
        return FieldGraph.of(value, field);
    }
}
