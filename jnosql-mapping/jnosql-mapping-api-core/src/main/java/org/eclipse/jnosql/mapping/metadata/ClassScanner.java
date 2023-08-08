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
 * This interface defines a scanner for classes that are annotated with both the {@link jakarta.nosql.Entity}
 * and {@link org.eclipse.jnosql.mapping.Embeddable} annotations, as well as repositories: interfaces that
 * extend {@link jakarta.data.repository.DataRepository} and are annotated with {@link jakarta.data.repository.Repository}.
 * The scanner facilitates the discovery of entities and repositories in the Eclipse JNoSQL context.
 */
public interface ClassScanner {

    /**
     * Returns a set of classes that are annotated with the {@link jakarta.nosql.Entity} annotation.
     *
     * @return A set of classes with the {@link jakarta.nosql.Entity} annotation.
     */
    Set<Class<?>> entities();

    /**
     * Returns a set of repository interfaces that extend {@link jakarta.data.repository.DataRepository}
     * and are annotated with {@link jakarta.data.repository.Repository}.
     *
     * @return A set of repository interfaces.
     */
    Set<Class<?>> repositories();

    /**
     * Returns a set of classes that are annotated with the {@link org.eclipse.jnosql.mapping.Embeddable} annotation.
     *
     * @return A set of classes with the {@link org.eclipse.jnosql.mapping.Embeddable} annotation.
     */
    Set<Class<?>> embeddables();

    /**
     * Returns a set of repository interfaces that are assignable from the given filter type.
     *
     * @param filter The repository filter.
     * @return A set of repository interfaces that match the filter criteria.
     */
    Set<Class<?>> repositories(Class<? extends DataRepository<?, ?>> filter);

    /**
     * Returns a set of repository interfaces that directly extend both
     * {@link jakarta.data.repository.PageableRepository} and {@link jakarta.data.repository.CrudRepository}.
     *
     * @return A set of standard repository interfaces.
     */
    Set<Class<?>> repositoriesStandard();

}