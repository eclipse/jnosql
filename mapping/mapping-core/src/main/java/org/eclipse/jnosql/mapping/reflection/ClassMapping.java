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

import jakarta.nosql.mapping.Entity;
import jakarta.nosql.mapping.Id;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * This instance is the meta-info of a loaded class that used to be annotated with {@link Entity}.
 */
public interface ClassMapping {


    /**
     * @return the Entity name
     */
    String getName();

    /**
     * @return the fields name
     */
    List<String> getFieldsName();

    /**
     * @return The class
     */
    Class<?> getClassInstance();

    /**
     * @return The fields from this class
     */
    List<FieldMapping> getFields();


    /**
     * Creates a new instance from {@link InstanceSupplier}
     * @param <T> the instance type
     * @return a new instance of this class
     */
    <T> T newInstance();


    /**
     * Gets the native column name from the Java field name
     *
     * @param javaField the java field
     * @return the column name or column
     * @throws NullPointerException when javaField is null
     */
    String getColumnField(String javaField);

    /**
     * Gets the {@link FieldMapping} from the java field name
     *
     * @param javaField the java field
     * @return the field otherwise {@link Optional#empty()}
     * @throws NullPointerException when the javaField is null
     */
    Optional<FieldMapping> getFieldMapping(String javaField);

    /**
     * Returns a Fields grouped by the name
     *
     * @return a {@link FieldMapping} grouped by
     * {@link FieldMapping#getName()}
     * @see FieldMapping#getName()
     */
    Map<String, FieldMapping> getFieldsGroupByName();


    /**
     * Returns the field that has {@link Id} annotation
     *
     * @return the field with ID annotation
     */
    Optional<FieldMapping> getId();
}
