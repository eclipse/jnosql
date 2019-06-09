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
package org.jnosql.artemis.key;


import org.jnosql.diana.key.KeyValueEntity;

/**
 * This interface represents the converter between an entity and the {@link KeyValueEntity}
 */
public interface KeyValueEntityConverter {

    /**
     * Converts the instance entity to {@link KeyValueEntity}
     *
     * @param <T> the entity type
     * @param entityInstance the instnace
     * @return a {@link KeyValueEntity} instance
     * @throws org.jnosql.artemis.IdNotFoundException when the entityInstance hasn't a field with {@link org.jnosql.artemis.Id}
     * @throws NullPointerException when the entityInstance is null
     */
    <T> KeyValueEntity<T> toKeyValue(Object entityInstance);

    /**
     * Converts a {@link KeyValueEntity} to entity
     *
     * @param entityClass the entity class
     * @param entity      the {@link KeyValueEntity} to be converted
     * @param <T>         the entity type
     * @return the instance from {@link KeyValueEntity}
     * @throws org.jnosql.artemis.IdNotFoundException when the entityInstance hasn't a field with {@link org.jnosql.artemis.Id}
     * @throws NullPointerException when the entityInstance is null
     */
    <T> T toEntity(Class<T> entityClass, KeyValueEntity<?> entity);

}
