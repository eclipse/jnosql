/*
 *  Copyright (c) 2020 Otávio Santana and others
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


import java.util.Map;
import java.util.Optional;

/**
 * This class contains all the class in cached way to be used inside artemis.
 */
public interface ClassMappings {

    /**
     * Find a class in the cached way and return in a class,
     * if it's not found the class will be both, loaded and cached, when this method is called
     *
     * @param classEntity the class of entity
     * @return the {@link EntityMetadata}
     * @throws NullPointerException when class entity is null
     */
    EntityMetadata get(Class<?> classEntity);

    /**
     * Find the {@link InheritanceClassMapping} where the parameter is the parent parameter
     * and it returns a map group by the {@link jakarta.nosql.mapping.DiscriminatorValue}
     * @param parent the parent
     * @return a {@link Map}
     * @throws NullPointerException when parent is null
     */
    Map<String, InheritanceClassMapping> findByParentGroupByDiscriminatorValue(Class<?> parent);

    /**
     * Returns the {@link EntityMetadata} instance from {@link EntityMetadata#getName()} in ignore case
     *
     * @param name the name to select ah {@link EntityMetadata} instance
     * @return the {@link EntityMetadata} from name
     * @throws ClassInformationNotFoundException when the class is not loaded
     * @throws NullPointerException              when the name is null
     */
    EntityMetadata findByName(String name);

    /**
     * Returns the {@link EntityMetadata} instance from {@link Class#getSimpleName()}
     *
     * @param name the name of {@link Class#getSimpleName()} instance
     * @return the {@link EntityMetadata} from name otherwise {@link Optional#empty()}
     * @throws NullPointerException              when the name is null
     */
    Optional<EntityMetadata> findBySimpleName(String name);

    /**
     * Returns the {@link EntityMetadata} instance from {@link Class#getName()}
     *
     * @param name the name of {@link Class#getName()} instance
     * @return the {@link EntityMetadata} from name otherwise {@link Optional#empty()}
     * @throws NullPointerException              when the name is null
     */
    Optional<EntityMetadata> findByClassName(String name);

}
