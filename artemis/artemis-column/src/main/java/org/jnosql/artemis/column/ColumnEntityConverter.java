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
package org.jnosql.artemis.column;

import org.jnosql.diana.column.ColumnEntity;


/**
 * This interface represents the converter between an entity and the {@link ColumnEntity}
 */
public interface ColumnEntityConverter {

    /**
     * Converts the instance entity to {@link ColumnEntity}
     *
     * @param entityInstance the instnace
     * @return a {@link ColumnEntity} instance
     * @throws NullPointerException when entityInstance is null
     */
    ColumnEntity toColumn(Object entityInstance);

    /**
     * Converts a {@link ColumnEntity} to entity
     *
     * @param entityClass the entity class
     * @param entity      the {@link ColumnEntity} to be converted
     * @param <T>         the entity type
     * @return the instance from {@link ColumnEntity}
     * @throws NullPointerException when either entityClass or entity are null
     */
    <T> T toEntity(Class<T> entityClass, ColumnEntity entity);

    /**
     * Converts a {@link ColumnEntity} to entity
     * Instead of creating a new object is uses the instance used in this parameters
     *
     * @param entityInstance the instance
     * @param entity      the {@link ColumnEntity} to be converted
     * @param <T>         the entity type
     * @return the same instance with values set from {@link ColumnEntity}
     * @throws NullPointerException when either entityInstance or entity are null
     */
    <T> T toEntity(T entityInstance, ColumnEntity entity);

    /**
     * Similar to {@link ColumnEntityConverter#toEntity(Class, ColumnEntity)}, but
     * search the instance type from {@link ColumnEntity#getName()}
     *
     * @param entity the {@link ColumnEntity} to be converted
     * @param <T>    the entity type
     * @return the instance from {@link ColumnEntity}
     * @throws NullPointerException when entity is null
     */
    <T> T toEntity(ColumnEntity entity);
}
