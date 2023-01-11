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

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;
import java.util.ServiceLoader;

/**
 * A specialization of a persistence field that indicates this field is an ID.
 */
public interface PersistenceIdAnnotation extends PersistenceColumnAnnotation {

    /**
     * Return the IdExtension from the field
     *
     * @param field the field
     * @return a ColumnExtension of {@link Optional#empty()}
     */
    static Optional<PersistenceIdAnnotation> of(Field field) {
        Objects.requireNonNull(field, "field is required");
        for (PersistenceIdAnnotation extension : ServiceLoader.load(PersistenceIdAnnotation.class)) {
            if (extension.test(field)) {
                return Optional.of(extension);
            }
        }
        return Optional.empty();
    }
}
