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

import java.util.Objects;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.function.Predicate;

/**
 *This instance represents the reader for entity.
 */
public interface PersistenceEntityAnnotation extends Predicate<Class<?>> {


    /**
     * Return the PersistenceEntityAnnotation from the type
     *
     * @param type the type
     * @return a ColumnExtension of {@link Optional#empty()}
     */
    String name(Class<?> type);

    /**
     * Return the IdExtension from the field
     *
     * @param type the entity class
     * @return a ColumnExtension of {@link Optional#empty()}
     */
    static Optional<PersistenceEntityAnnotation> of(Class<?> type) {
        Objects.requireNonNull(type, "type is required");
        for (PersistenceEntityAnnotation extension : ServiceLoader.load(PersistenceEntityAnnotation.class)) {
            if (extension.test(type)) {
                return Optional.of(extension);
            }
        }
        return Optional.empty();
    }
}
