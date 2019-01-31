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

import org.jnosql.artemis.RepositoryAsync;
import org.jnosql.diana.api.document.DocumentCollectionManagerAsync;

/**
 * The producer of {@link RepositoryAsync}
 *
 */
public interface DocumentRepositoryAsyncProducer {


    /**
     * Produces a Repository class from repository class and {@link DocumentCollectionManagerAsync}
     * @param repositoryClass the repository class
     * @param manager the manager
     * @param <E> the entity of repository
     * @param <ID> the ID of the entity
     * @param <T> the repository type
     * @return a {@link RepositoryAsync} interface
     * @throws NullPointerException when there is null parameter
     */
    <E,ID, T extends RepositoryAsync<E,ID>> T get(Class<T> repositoryClass, DocumentCollectionManagerAsync manager);

    /**
     * Produces a Repository class from repository class and {@link DocumentTemplateAsync}
     * @param repositoryClass the repository class
     * @param template the template
     * @param <E> the entity of repository
     * @param <ID> the ID of the entity
     * @param <T> the repository type
     * @return a {@link RepositoryAsync} interface
     * @throws NullPointerException when there is null parameter
     */
    <E,ID, T extends RepositoryAsync<E,ID>> T get(Class<T> repositoryClass, DocumentTemplateAsync template);

}
