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
import java.lang.reflect.Parameter;
import java.util.Objects;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.function.Predicate;


/**
 * This instance represents a persistence field on the entity. It loads from SPI to allow extensions of annotations.
 */
public interface PersistenceColumnAnnotation extends Predicate<Field> {


    /**
     * Reads {@link Field} and return the name from the annotation
     *
     * @param field the field
     * @return the name from annotation or {@link Field#getName()}
     */
    String name(Field field);

    /**
     * Reads from constructor parameter and return the name
     *
     * @param parameter the parameter from constructor
     * @return the name from the annotation or {@link Parameter#getName()}
     */
    String name(Parameter parameter);

    /**
     * Return the ColumnExtension from the field
     * @param field the field
     * @return a ColumnExtension of {@link Optional#empty()}
     */
    static Optional<PersistenceColumnAnnotation> of(Field field) {
        Objects.requireNonNull(field, "field is required");
        for (PersistenceColumnAnnotation extension : ServiceLoader.load(PersistenceColumnAnnotation.class)) {
            if (extension.test(field)) {
                return Optional.of(extension);
            }
        }
        return Optional.empty();
    }
}
