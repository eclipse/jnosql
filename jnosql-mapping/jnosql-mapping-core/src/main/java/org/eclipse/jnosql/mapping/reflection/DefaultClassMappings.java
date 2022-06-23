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

/**
 * The default implementation of {@link ClassMapping}.
 * It's storage the class information in a {@link ConcurrentHashMap}
 */
@ApplicationScoped
class DefaultClassMappings implements ClassMappings {

    private Map<String, ClassMapping> mappings;

    private Map<Class<?>, ClassMapping> classes;

    private Map<String, ClassMapping> findBySimpleName;

    private Map<String, ClassMapping> findByClassName;


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

    void load(Class classEntity) {
        ClassMapping classMapping = classConverter.create(classEntity);
        mappings.put(classEntity.getName(), classMapping);
        findBySimpleName.put(classEntity.getSimpleName(), classMapping);
        findByClassName.put(classEntity.getName(), classMapping);
    }

    @Override
    public ClassMapping get(Class classEntity) {
        ClassMapping classMapping = classes.get(classEntity);
        if (classMapping == null) {
            classMapping = classConverter.create(classEntity);
            classes.put(classEntity, classMapping);
            return this.get(classEntity);
        }
        return classMapping;
    }

    @Override
    public ClassMapping findByName(String name) {
        return mappings.keySet().stream()
                .map(k -> mappings.get(k))
                .filter(r -> r.getName().equalsIgnoreCase(name)).findFirst()
                .orElseThrow(() -> new ClassInformationNotFoundException("There is not entity found with the name: " + name));
    }

    @Override
    public Optional<ClassMapping> findBySimpleName(String name) {
        Objects.requireNonNull(name, "name is required");
        return Optional.ofNullable(findBySimpleName.get(name));
    }

    @Override
    public Optional<ClassMapping> findByClassName(String name) {
        Objects.requireNonNull(name, "name is required");
        return Optional.ofNullable(findByClassName.get(name));
    }

    @Override
    public String toString() {
        return  "DefaultClassMappings{" + "mappings-size=" + mappings.size() +
                ", classes=" + classes +
                ", classConverter=" + classConverter +
                ", extension=" + extension +
                '}';
    }
}
