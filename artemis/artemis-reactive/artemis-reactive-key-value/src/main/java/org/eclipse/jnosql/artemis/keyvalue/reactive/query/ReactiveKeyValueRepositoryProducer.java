/*
 *  Copyright (c) 2020 Otávio Santana and others
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */
package org.eclipse.jnosql.artemis.keyvalue.reactive.query;

import jakarta.nosql.mapping.Repository;
import jakarta.nosql.mapping.keyvalue.KeyValueTemplate;
import org.eclipse.jnosql.artemis.reactive.ReactiveRepository;

/**
 * The producer of {@link ReactiveRepository}
 *
 */
public interface ReactiveKeyValueRepositoryProducer {

    /**
     * Produces a Repository class from repository class and {@link KeyValueTemplate}
     * @param repositoryClass the repository class
     * @param template the template
     * @param <T> the entity of repository
     * @param <K> the K of the entity
     * @param <R> the repository type
     * @return a {@link Repository} interface
     * @throws NullPointerException when there is null parameter
     */
    <T, K, R extends ReactiveRepository<T, K>> R get(Class<R> repositoryClass, KeyValueTemplate template);
}
