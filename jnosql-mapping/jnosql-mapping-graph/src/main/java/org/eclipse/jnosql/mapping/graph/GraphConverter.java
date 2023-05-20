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
package org.eclipse.jnosql.mapping.graph;

import jakarta.data.exceptions.EmptyResultException;
import jakarta.data.exceptions.MappingException;
import org.eclipse.jnosql.communication.Value;
import org.eclipse.jnosql.mapping.AttributeConverter;
import org.eclipse.jnosql.mapping.Converters;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Property;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.eclipse.jnosql.mapping.reflection.ConstructorBuilder;
import org.eclipse.jnosql.mapping.reflection.ConstructorMetadata;
import org.eclipse.jnosql.mapping.reflection.EntityMetadata;
import org.eclipse.jnosql.mapping.reflection.EntitiesMetadata;
import org.eclipse.jnosql.mapping.reflection.FieldMapping;
import org.eclipse.jnosql.mapping.reflection.InheritanceMetadata;

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

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static org.eclipse.jnosql.mapping.reflection.MappingType.EMBEDDED;


public abstract class GraphConverter {


    protected abstract EntitiesMetadata getEntities();

    protected abstract Converters getConverters();

    protected abstract GraphEventPersistManager getEventManager();

    protected abstract Graph getGraph();

    /**
     * Converts entity object to  TinkerPop Vertex
     *
     * @param entity the entity
     * @param <T>    the entity type
     * @return the ThinkerPop Vertex with the entity values
     * @throws NullPointerException when entity is null
     */
    public <T> Vertex toVertex(T entity) {
        requireNonNull(entity, "entity is required");

        EntityMetadata mapping = getEntities().get(entity.getClass());
        String label = mapping.name();

        List<FieldGraph> fields = mapping.fields().stream()
                .map(f -> to(f, entity))
                .filter(FieldGraph::isNotEmpty).toList();

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

        mapping.inheritance().ifPresent(i ->
                vertex.property(i.getDiscriminatorColumn(), i.getDiscriminatorValue()));

        return vertex;
    }


    /**
     * List the fields in the entity as property exclude fields annotated with {@link jakarta.nosql.Id}
     *
     * @param entity the entity
     * @param <T>    the entity type
     * @return the properties of an entity
     * @throws NullPointerException when entity is null
     */
    public <T> List<Property<?>> getProperties(T entity) {
        Objects.requireNonNull(entity, "entity is required");
        EntityMetadata mapping = getEntities().get(entity.getClass());
        List<FieldGraph> fields = mapping.fields().stream()
                .map(f -> to(f, entity))
                .filter(FieldGraph::isNotEmpty).toList();

        return fields.stream().filter(FieldGraph::isNotId)
                .flatMap(f -> f.toElements(this, getConverters()).stream())
                .collect(Collectors.toList());
    }

    /**
     * Converts vertex to an entity
     *
     * @param vertex the vertex
     * @param <T>    the entity type
     * @return a entity instance
     * @throws NullPointerException when vertex is null
     */
    public <T> T toEntity(Vertex vertex) {
        requireNonNull(vertex, "vertex is required");
        EntityMetadata mapping = getEntities().findByName(vertex.label());

        List<Property<?>> properties = vertex.keys()
                .stream()
                .map(k -> DefaultProperty.of(k, vertex.value(k))).collect(toList());

        T entity;
        if (mapping.isInheritance()) {
            entity = mapInheritanceEntity(vertex, properties, mapping.type());
        } else {
            entity = convert((Class<T>) mapping.type(), properties, vertex);
        }
        getEventManager().firePostEntity(entity);
        return entity;
    }

    /**
     * Converts vertex to an entity
     *
     * @param type   the entity class
     * @param vertex the vertex
     * @param <T>    the entity type
     * @return a entity instance
     * @throws NullPointerException when vertex or type is null
     */
    public <T> T toEntity(Class<T> type, Vertex vertex) {
        requireNonNull(type, "type is required");
        requireNonNull(vertex, "vertex is required");

        List<Property<?>> properties = vertex.keys().stream()
                .map(k -> DefaultProperty.of(k, vertex.value(k))).collect(toList());
        T entity = convert(type, properties, vertex);
        getEventManager().firePostEntity(entity);
        return entity;
    }

    /**
     * Converts vertex to an entity
     * Instead of creating a new object is uses the instance used in this parameters
     *
     * @param type   the entity class
     * @param vertex the vertex
     * @param <T>    the entity type
     * @return a entity instance
     * @throws NullPointerException when vertex or type is null
     */
    public <T> T toEntity(T type, Vertex vertex) {
        requireNonNull(type, "entityInstance is required");
        requireNonNull(vertex, "vertex is required");

        if (type.getClass().isRecord()) {
            return (T) toEntity(type.getClass(), vertex);
        }

        List<Property<?>> properties = vertex.keys().stream()
                .map(k -> DefaultProperty.of(k, vertex.value(k)))
                .collect(toList());

        EntityMetadata mapping = getEntities().get(type.getClass());
        EntityConverterByField<T> converter = EntityConverterByField.of(properties, mapping,
                type, vertex, getConverters(), getEntities());
        return converter.get();

    }

    /**
     * Converts {@link EdgeEntity} from {@link Edge} Thinkerpop
     *
     * @param edge the ThinkerPop edge
     * @return an EdgeEntity instance
     * @throws NullPointerException when Edge is null
     */
    public EdgeEntity toEdgeEntity(Edge edge) {
        requireNonNull(edge, "vertex is required");
        Object out = toEntity(edge.outVertex());
        Object in = toEntity(edge.inVertex());
        return new DefaultEdgeEntity<>(edge, in, out);
    }

    /**
     * Converts {@link Edge} from {@link EdgeEntity}
     *
     * @param edge the EdgeEntity instance
     * @return a Edge instance
     * @throws NullPointerException when edge entity is null
     */
    public Edge toEdge(EdgeEntity edge) {
        requireNonNull(edge, "vertex is required");
        Object id = edge.id();
        Iterator<Edge> edges = getGraph().edges(id);
        if (edges.hasNext()) {
            return edges.next();
        }
        throw new EmptyResultException("Edge does not found in the database with id: " + id);
    }

    private <T> T convert(Class<T> type, List<Property<?>> properties, Vertex vertex) {
        EntityMetadata mapping = getEntities().get(type);
        ConstructorMetadata constructor = mapping.constructor();
        if (constructor.isDefault()) {
            EntityConverterByField<T> converter = EntityConverterByField.of(properties, mapping,
                    mapping.newInstance(), vertex, getConverters(), getEntities());
            return converter.get();
        } else {
            EntityConverterByContructor<T> supplier = EntityConverterByContructor.of(mapping, vertex, getConverters());
            return supplier.get();
        }
    }


    protected FieldGraph to(FieldMapping field, Object entityInstance) {
        Object value = field.read(entityInstance);
        return FieldGraph.of(value, field);
    }

    private <T> T mapInheritanceEntity(Vertex vertex,
                                       List<Property<?>> properties, Class<?> type) {

        Map<String, InheritanceMetadata> group = getEntities()
                .findByParentGroupByDiscriminatorValue(type);

        if (group.isEmpty()) {
            throw new MappingException("There is no discriminator inheritance to the vertex "
                    + vertex.label());
        }
        String column = group.values()
                .stream()
                .findFirst()
                .map(InheritanceMetadata::getDiscriminatorColumn)
                .orElseThrow();


        String discriminator = properties.stream().filter(p -> p.key().equals(column))
                .map(Property::value).map(Object::toString)
                .findFirst()
                .orElseThrow(
                        () -> new MappingException("To inheritance there is the discriminator column missing" +
                                " on the Vertex, the document name: " + column));

        InheritanceMetadata inheritance = Optional.ofNullable(group.get(discriminator))
                .orElseThrow(() -> new MappingException("There is no inheritance map to the discriminator" +
                        " column value " + discriminator));

        EntityMetadata mapping = getEntities().get(inheritance.getEntity());
        return convert((Class<T>) mapping.type(), properties, vertex);
    }
}