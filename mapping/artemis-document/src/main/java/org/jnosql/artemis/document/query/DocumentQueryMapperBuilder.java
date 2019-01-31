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
package org.jnosql.artemis.document.query;


/**
 * The builder to either select and delete query using an object mapper API.
 */
public interface DocumentQueryMapperBuilder {


    /**
     * Returns a {@link DocumentMapperFrom} implementation that does the object mapper API.
     *
     * @param entityClass the entity class
     * @param <T>         the entity type
     * @return a {@link DocumentMapperFrom} instance
     * @throws NullPointerException when entityClass is null
     */
    <T> DocumentMapperFrom selectFrom(Class<T> entityClass);

    /**
     * Returns a {@link DocumentMapperDeleteFrom} implementation that does the object mapper API.
     *
     * @param entityClass the entity class
     * @param <T>         the entity type
     * @return a {@link DocumentMapperDeleteFrom} instance
     * @throws NullPointerException when entityClass is null
     */
    <T> DocumentMapperDeleteFrom deleteFrom(Class<T> entityClass);
}
