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
package org.eclipse.jnosql.mapping.reflection;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ClassRefTypeSignature;
import io.github.classgraph.ClassTypeSignature;
import io.github.classgraph.ReferenceTypeSignature;
import io.github.classgraph.ScanResult;
import io.github.classgraph.TypeArgument;
import io.github.classgraph.TypeParameter;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.DataRepository;
import jakarta.data.repository.PageableRepository;
import jakarta.data.repository.Repository;
import jakarta.nosql.Entity;
import org.eclipse.jnosql.mapping.Embeddable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.util.Collections.unmodifiableSet;
import static java.util.stream.Collectors.toUnmodifiableSet;

/**
 * Scanner classes that will load entities with both Entity and Embeddable
 * annotations and repositories: interfaces that extend DataRepository
 * and has the Repository annotation.
 */
public enum ClassScanner {

    INSTANCE;

    private final Set<Class<?>> entities;
    private final Set<Class<?>> repositores;
    private final Set<Class<?>> embeddables;


    ClassScanner() {
        entities = new HashSet<>();
        embeddables = new HashSet<>();
        repositores = new HashSet<>();

        Logger logger = Logger.getLogger(ClassScanner.class.getName());
        logger.fine("Starting scan class to find entities, embeddable and repositories.");
        try (ScanResult result = new ClassGraph().enableAllInfo().scan()) {
            this.entities.addAll(result.getClassesWithAnnotation(Entity.class).loadClasses());
            embeddables.addAll(result.getClassesWithAnnotation(Embeddable.class).loadClasses());
            this.repositores.addAll(result.getClassesWithAnnotation(Repository.class)
                    .getInterfaces()
                    .loadClasses(DataRepository.class));
            this.repositores.removeIf(UnsupportedRepositoryFilter.INSTANCE);
        }
        logger.fine(String.format("Finished the class scan with entities %d, embeddables %d and repositories: %d"
                , entities.size(), embeddables.size(), repositores.size()));

    }

    /**
     * Returns the classes that that has the {@link Entity} annotation
     *
     * @return classes with {@link Entity} annotation
     */
    public Set<Class<?>> entities() {
        return unmodifiableSet(entities);
    }

    /**
     * Returns repositories: interfaces that extend DataRepository and has the Repository annotation.
     *
     * @return the repositories items
     */
    public Set<Class<?>> repositories() {
        return unmodifiableSet(repositores);
    }


    /**
     * Returns the classes that that has the {@link Embeddable} annotation
     *
     * @return embeddables items
     */
    public Set<Class<?>> embeddables() {
        return unmodifiableSet(embeddables);
    }

    /**
     * Returns repositories {@link Class#isAssignableFrom(Class)} the parameter
     *
     * @param filter the repository filter
     * @return the list
     */
    public Set<Class<?>> repositories(Class<? extends DataRepository> filter) {
        Objects.requireNonNull(filter, "filter is required");
        return repositores.stream().filter(filter::isAssignableFrom)
                .filter(c -> Arrays.asList(c.getInterfaces()).contains(filter))
                .collect(toUnmodifiableSet());
    }


    /**
     * Returns the repositories that extends directly from {@link PageableRepository} and {@link CrudRepository}
     *
     * @return the standard repositories
     */
    public Set<Class<?>> repositoriesStandard() {
        return repositores.stream()
                .filter(c -> {
                    List<Class<?>> interfaces = Arrays.asList(c.getInterfaces());
                    return interfaces.contains(CrudRepository.class) || interfaces.contains(PageableRepository.class);
                }).collect(Collectors.toUnmodifiableSet());
    }
}