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
package org.jnosql.artemis.document;

import org.jnosql.diana.api.document.DocumentEntity;

/**
 * This interface represents the converter between an entity and the {@link DocumentEntity}
 */
public interface DocumentEntityConverter {

    /**
     * Converts the instance entity to {@link DocumentEntity}
     *
     * @param entityInstance the instnace
     * @return a {@link DocumentEntity} instance
     * @throws NullPointerException when entityInstance is null
     */
    DocumentEntity toDocument(Object entityInstance);

    /**
     * Converts a {@link DocumentEntity} to entity
     *
     * @param entityClass the entity class
     * @param entity      the {@link DocumentEntity} to be converted
     * @param <T>         the entity type
     * @return the instance from {@link DocumentEntity}
     * @throws NullPointerException when either entityClass or entity are null
     */
    <T> T toEntity(Class<T> entityClass, DocumentEntity entity);

    /**
     * Converts a {@link DocumentEntity} to entity
     * Instead of creating a new object is uses the instance used in this parameters
     *
     * @param entityInstance the entity class
     * @param entity         the {@link DocumentEntity} to be converted
     * @param <T>            the entity type
     * @return the instance from {@link DocumentEntity}
     * @throws NullPointerException when either entityInstance or entity are null
     */
    <T> T toEntity(T entityInstance, DocumentEntity entity);

    /**
     * Similar to {@link DocumentEntityConverter#toEntity(Class, DocumentEntity)}, but
     * search the instance type from {@link DocumentEntity#getName()}
     *
     * @param entity the {@link DocumentEntity} to be converted
     * @param <T>    the entity type
     * @return the instance from {@link DocumentEntity}
     * @throws NullPointerException when entity is null
     */
    <T> T toEntity(DocumentEntity entity);
}
