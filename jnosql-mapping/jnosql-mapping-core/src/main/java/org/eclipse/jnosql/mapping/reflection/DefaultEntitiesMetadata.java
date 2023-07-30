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
package org.eclipse.jnosql.mapping.reflection;


import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.jnosql.mapping.metadata.EntitiesMetadata;
import org.eclipse.jnosql.mapping.metadata.EntityMetadata;
import org.eclipse.jnosql.mapping.metadata.InheritanceMetadata;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The default implementation of {@link EntityMetadata}.
 * It's storage the class information in a {@link ConcurrentHashMap}
 */
@ApplicationScoped
class DefaultEntitiesMetadata implements EntitiesMetadata {

    private Map<String, EntityMetadata> mappings;

    private Map<Class<?>, EntityMetadata> classes;

    private Map<String, EntityMetadata> findBySimpleName;

    private Map<String, EntityMetadata> findByClassName;


    @Inject
    private ClassConverter classConverter;

    @Inject
    private EntityMetadataExtension extension;

    @PostConstruct
    public void init() {
        mappings = new ConcurrentHashMap<>();
        classes = new ConcurrentHashMap<>();
        findBySimpleName = new ConcurrentHashMap<>();
        findByClassName = new ConcurrentHashMap<>();

        classes.putAll(extension.getClasses());
        mappings.putAll(extension.getMappings());
        mappings.values().forEach(r -> {
            Class<?> type = r.type();
            findBySimpleName.put(type.getSimpleName(), r);
            findByClassName.put(type.getName(), r);
        });
    }

    EntityMetadata load(Class<?> type) {
        EntityMetadata metadata = classConverter.create(type);
        if (metadata.hasEntityName()) {
            mappings.put(type.getName(), metadata);
        }
        this.findBySimpleName.put(type.getSimpleName(), metadata);
        this.findByClassName.put(type.getName(), metadata);
        this.classes.put(type, metadata);
        return metadata;
    }

    @Override
    public EntityMetadata get(Class<?> entity) {
        EntityMetadata metadata = classes.get(entity);
        if (metadata == null) {
           return load(entity);
        }
        return metadata;
    }

    @Override
    public Map<String, InheritanceMetadata> findByParentGroupByDiscriminatorValue(Class<?> parent) {
        Objects.requireNonNull(parent, "parent is required");
        return this.classes.values().stream()
                .flatMap(c -> c.inheritance().stream())
                .filter(p -> p.isParent(parent))
                .collect(Collectors.toMap(InheritanceMetadata::discriminatorValue, Function.identity()));
    }

    @Override
    public EntityMetadata findByName(String name) {
        return mappings.keySet().stream()
                .map(k -> mappings.get(k))
                .filter(r -> r.name().equalsIgnoreCase(name)).findFirst()
                .orElseThrow(() -> new ClassInformationNotFoundException("There is not entity found with the name: " + name));
    }

    @Override
    public Optional<EntityMetadata> findBySimpleName(String name) {
        Objects.requireNonNull(name, "name is required");
        return Optional.ofNullable(findBySimpleName.get(name));
    }

    @Override
    public Optional<EntityMetadata> findByClassName(String name) {
        Objects.requireNonNull(name, "name is required");
        return Optional.ofNullable(findByClassName.get(name));
    }

    @Override
    public String toString() {
        return "DefaultEntitiesMetadata{" + "mappings-size=" + mappings.size() +
                ", classes=" + classes +
                ", classConverter=" + classConverter +
                ", extension=" + extension +
                '}';
    }
}
