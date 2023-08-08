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
package org.eclipse.jnosql.mapping.metadata;


import jakarta.data.repository.DataRepository;

import java.util.Set;

/**
 * Scanner classes that will load entities with both {@link jakarta.nosql.Entity} and
 * {@link org.eclipse.jnosql.mapping.Embeddable}
 * annotations and repositories: interfaces that extend {@link jakarta.data.repository.DataRepository}
 * and has the {@link jakarta.data.repository.Repository} annotation.
 */
public interface ClassScanner {

    /**
     * Returns the classes that that has the {@link jakarta.nosql.Entity} annotation
     *
     * @return classes with {@link jakarta.nosql.Entity} annotation
     */
    Set<Class<?>> entities();

    /**
     * Returns repositories: interfaces that extend DataRepository and has the Repository annotation.
     *
     * @return the repositories items
     */
    Set<Class<?>> repositories();

    /**
     * Returns the classes that that has the {@link org.eclipse.jnosql.mapping.Embeddable} annotation
     *
     * @return embeddables items
     */
    Set<Class<?>> embeddables();

    /**
     * Returns repositories {@link Class#isAssignableFrom(Class)} the parameter
     *
     * @param filter the repository filter
     * @return the list
     */
    Set<Class<?>> repositories(Class<? extends DataRepository<?,?>> filter);

    /**
     * Returns the repositories that extends directly from {@link jakarta.data.repository.PageableRepository}
     * and {@link jakarta.data.repository.CrudRepository}
     *
     * @return the standard repositories
     */
    Set<Class<?>> repositoriesStandard();

}
