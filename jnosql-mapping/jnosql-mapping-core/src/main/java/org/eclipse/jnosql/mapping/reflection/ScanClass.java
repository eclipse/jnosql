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
import io.github.classgraph.ScanResult;
import jakarta.data.repository.DataRepository;
import jakarta.data.repository.Repository;
import jakarta.nosql.Entity;
import org.eclipse.jnosql.mapping.Embeddable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Scanner classes that will load entities with both Entity and Embeddable
 * annotations and repositories: interfaces that extend DataRepository
 * and has the Repository annotation.
 */
public enum ScanClass {

    INSTANCE;

    private final Set<Class<?>> entities;
    private final Set<Class<?>> repositores;
    private final Set<Class<?>> embeddables;


    ScanClass() {
        entities = new HashSet<>();
        embeddables = new HashSet<>();
        repositores = new HashSet<>();

        Logger logger = Logger.getLogger(ScanClass.class.getName());
        logger.fine("Starting scan class to find entities, embeddable and repositories.");
        try (ScanResult result = new ClassGraph().enableAllInfo().scan()) {
            for (Class<?> entity : result.getClassesWithAnnotation(Entity.class).loadClasses()) {
                this.entities.add(entity);
            }
            for (Class<?> embeddable : result.getClassesWithAnnotation(Embeddable.class).loadClasses()) {
                embeddables.add(embeddable);
            }
            for (Class<?> repositories : result.getClassesWithAnnotation(Repository.class)
                    .getInterfaces().loadClasses(DataRepository.class)) {
                this.repositores.add(repositories);
            }
        }
        logger.fine(String.format("Finished the class scan with entities %i, embeddables %i and repositories: %i"
                , entities.size(), embeddables.size(), repositores.size()));

    }


    /**
     * Returns the classes that that has the {@link Entity} annotation
     * @return classes with {@link Entity} annotation
     */
    public Set<Class<?>> entities() {
        return Collections.unmodifiableSet(entities);
    }
    /**
     * Returns repositories: interfaces that extend DataRepository and has the Repository annotation.
     * @return the repositories items
     */
    public Set<Class<?>> reepositores() {
        return Collections.unmodifiableSet(repositores);
    }


    /**
     * Returns the classes that that has the {@link Embeddable} annotation
     * @return embeddables items
     */
    public Set<Class<?>> embeddables() {
        return Collections.unmodifiableSet(embeddables);
    }
}
