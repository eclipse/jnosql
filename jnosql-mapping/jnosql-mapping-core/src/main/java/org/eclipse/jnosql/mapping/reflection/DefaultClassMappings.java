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
package org.eclipse.jnosql.mapping.reflection;


import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
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
class DefaultClassMappings implements ClassMappings {

    private Map<String, EntityMetadata> mappings;

    private Map<Class<?>, EntityMetadata> classes;

    private Map<String, EntityMetadata> findBySimpleName;

    private Map<String, EntityMetadata> findByClassName;


    @Inject
    private ClassConverter classConverter;

    @Inject
    private ClassMappingExtension extension;

    @PostConstruct
    public void init() {
        mappings = new ConcurrentHashMap<>();
        classes = new ConcurrentHashMap<>();
        findBySimpleName = new ConcurrentHashMap<>();
        findByClassName = new ConcurrentHashMap<>();

        classes.putAll(extension.getClasses());
        mappings.putAll(extension.getMappings());
        mappings.values().forEach(r -> {
            Class<?> entityClass = r.getClassInstance();
            findBySimpleName.put(entityClass.getSimpleName(), r);
            findByClassName.put(entityClass.getName(), r);
        });
    }

    void load(Class<?> classEntity) {
        EntityMetadata entityMetadata = classConverter.create(classEntity);
        if (entityMetadata.hasEntityName()) {
            mappings.put(classEntity.getName(), entityMetadata);
        }
        findBySimpleName.put(classEntity.getSimpleName(), entityMetadata);
        findByClassName.put(classEntity.getName(), entityMetadata);
    }

    @Override
    public EntityMetadata get(Class<?> classEntity) {
        EntityMetadata entityMetadata = classes.get(classEntity);
        if (entityMetadata == null) {
            entityMetadata = classConverter.create(classEntity);
            classes.put(classEntity, entityMetadata);
            return this.get(classEntity);
        }
        return entityMetadata;
    }

    @Override
    public Map<String, InheritanceClassMapping> findByParentGroupByDiscriminatorValue(Class<?> parent) {
        Objects.requireNonNull(parent, "parent is required");
        return this.classes.values().stream()
                .flatMap(c -> c.getInheritance().stream())
                .filter(p -> p.isParent(parent))
                .collect(Collectors.toMap(InheritanceClassMapping::getDiscriminatorValue, Function.identity()));
    }

    @Override
    public EntityMetadata findByName(String name) {
        return mappings.keySet().stream()
                .map(k -> mappings.get(k))
                .filter(r -> r.getName().equalsIgnoreCase(name)).findFirst()
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
        return "DefaultClassMappings{" + "mappings-size=" + mappings.size() +
                ", classes=" + classes +
                ", classConverter=" + classConverter +
                ", extension=" + extension +
                '}';
    }
}
